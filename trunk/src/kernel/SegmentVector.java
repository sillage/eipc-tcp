package kernel;

import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import common.Segment;
import common.TCPManager;
import Interface.GUIManager;

public class SegmentVector extends Thread {
    //Traitement	traitement = Traitement.getInstance();

    GUIManager gui = GUIManager.getInstance();
    TCPManager tcp = TCPManager.getInstance();
    long _srtt = 60000; // Smoothed Round Trip Time (initialiser à 1 min normalement...)
    private long _rto = 30000; //Retransmission Time Out (initialisé à 10secondes ici), Attention toujours passer par les methodes Get
    private Vector<Pair> _rsdStack = new Vector<Pair>();
    private TCB _tcb;

    public SegmentVector(TCB tcb) {
        this._tcb = tcb;
        this.start();
    }

    public synchronized boolean emptyStack() {
        return _rsdStack.isEmpty();
    }

    public synchronized boolean addSegment(Segment segment) {
        int i = 0;
        Traitement t = Traitement.getInstance();

        if (t.getStateAutomate() != 0 /*&& t.getStateAutomate() != 11*/
                && t.getStateAutomate() != 9 && t.getStateAutomate() != 10) {
            for (Pair p : _rsdStack) {
                if (p.getSegment().get_seq_number() == segment.get_seq_number()) {
                    return false;
                }
            }
            _rsdStack.addElement(new Pair(segment));
            //On décrémente la taille de la fenêtre de réception de ce k'on a envoyé
            _tcb.set_sndWND(_tcb.get_sndWND() - segment.get_data().length);
            // System.out.println("Nouvelle taille de la stack : " + _rsdStack.size());
            this.printAddededSegment(segment);
            return true;
        }
        return false;
    }

    /**
     * Retire du vecteur les segments acquités :
     * Un segment est effacé si la somme de son numéro de séquence (1er octet de donnée) et sa longueur
     * est inférieure au numéro de séquence du dernier accusé de réception 
     * @param TCP_segement segment : le segment recu contenant un accusé de reception
     * @return
     */
    public synchronized boolean deleteSegment(Segment segment) {
        long ack = segment.get_ack_number();
        long seq_nb = 0;
        long len = 0;
        int i = _rsdStack.size() - 1;

        for (; i >= 0; i--) {
            seq_nb = ((Pair) (_rsdStack.get(i))).getSegment().get_seq_number();
            len = ((Pair) (_rsdStack.get(i))).getSegment().get_data().length;
            //gui.getPanelConsole().insertLine("Delete- seq:"+seq_nb+":len:"+len+":ack:"+ack, "Green");
            //cas ou c un du milieu
            if (seq_nb + len <= ack + len) {
                gui.getPanelConsole().insertLine("Segment acquitted, deleted from stack", "Green");
                removeTCPSegment(i);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean deleteAllSegment() {
        int i = _rsdStack.size() - 1;

        gui.getPanelConsole().insertLine("Deleting stack...", "Red");
        for (; i >= 0; i--) {
            gui.getPanelConsole().insertLine("- Segment deleted", "Red");
            removeTCPSegment(i);
            return true;
        }
        return false;
    }

    private void removeTCPSegment(int i) {
        int j = i;

        for (; j > 0; j--) {
            //printDeletedSegment(((Pair)_rsdStack.get(j)).getSegment());
            _rsdStack.removeElementAt(j);
        }
        this.calRto(((Pair) _rsdStack.get(j)).getCreationDate().getTime());
        //printDeletedSegment(((Pair)_rsdStack.get(j)).getSegment());
        _rsdStack.removeElementAt(j);
    }

    // calcul dynamique du temps de retransmission selon l'algorithme proposé par la RFC 793 de TCP
    private void calRto(long date) {
        Date now = new Date();
        long rtt = 0; //Round Trip Time
        Double c = null;

        rtt = now.getTime() - date;
        c = new Double((0.85 * _srtt) + ((1 - 0.85) * rtt));
        _srtt = c.longValue();
        c = new Double(_srtt * 1.7);
        this.setRto(Math.min(120000, Math.max(10000, c.longValue())));
    }

    public synchronized int rsdStackSize() {
        return _rsdStack.size();
    }

// 	 returns a copy of _rsdStack not a reference on it
    public synchronized Vector rsdStackGet() {
        return (Vector) _rsdStack.clone();
    }

    private synchronized void setRto(long rto) {
        //Date d = new Date(rto);

        _rto = rto;
        /*if (this._printRto) {
        System.out.println("SegmentVector.java, new value of rto :" + 
        d.getMinutes() + "min " + d.getSeconds() + "s");
        }*/
        //_tcb.getSoClt().writeInterfaceConsole("New value for RTO :" + d.getMinutes() + "min " + d.getSeconds() + "s", SocketClient.BLACK);
    }

    //private synchronized long getRto() {
    //	return _rto;
    //}
    private void printAddededSegment(Segment segment) {
        if (gui != null && gui.getPanelConsole() != null) {
            gui.getPanelConsole().insertLine("Added Segment to the stack", "Green");
        }
    }

    private void printRetransmitedSegment(Segment segment) {
        if (gui != null && gui.getPanelConsole() != null) {
            gui.getPanelConsole().insertLine("Segment retransmited", "Red");
        }
    }

    // cette fonction re-emet les paquets qui sont restés trop longtemps dans la pile de retransmission
    // Elle parcours le vecteur de paquet emis pour re-emttre ceux dont l'accusé de reception
    // n'a pas été recu depuis un temps stocké dans _rto
    // TODO ajuster le temps pendant lequel on attend avant de re-emettre 
    private synchronized void reSendSegment() throws IOException, InterruptedException {
        Date now = new Date();
        int i = 0;

        for (Pair p : _rsdStack) {
            if (now.getTime() - p.getCreationDate().getTime() > this._rto) {
                p.setCreationDate(now);
                // Réenvoi du segment (copié collé)
                if (tcp.getisClient()) {
                    tcp.getClient().sendMsg(p.getSegment().dump());
                }
                if (tcp.getisServer()) {
                    tcp.getServeur().sendMsg(p.getSegment().dump());
                }
                // Fin réenvoi du segment
                this.printRetransmitedSegment(p.getSegment());
            }
        }
    }

    @Override
    public void run() {
        Traitement t = Traitement.getInstance();

        while (tcp.getServeur() != null || tcp.getClient() != null) {
            try {
                if (t.getStateAutomate() != 0 && t.getStateAutomate() != 11) {
                    reSendSegment();
                }
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
