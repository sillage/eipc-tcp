package kernel;

import Interface.GUIManager;

import common.Segment;
import common.TCPManager;
import java.net.Socket;
//import java.net.ServerSocket;

/**
 * L'automate met a jour les differents champs du TCB en fonction des trames recus
 * Il analyse lui meme les segments décodés et décide de l'état suivant.
 * 
 * @author Sebastien Gislais
 *
 */
public class Automaton {

    public static final int CLOSED = 0;
    public static final int LISTEN = 1;
    public static final int SYN_RCVD = 2;
    public static final int SYN_SENT = 3;
    public static final int ESTAB = 4;
    public static final int FIN_WAIT_1 = 5;
    public static final int CLOSE_WAIT = 6;
    public static final int FIN_WAIT_2 = 7;
    public static final int CLOSING = 8;
    public static final int LAST_ACK = 9;
    public static final int TIME_WAIT = 10;
    public static final int CLOSED_INIT = 11;
    public static final int CLIENT = 101;
    public static final int SERVER = 102;
    private int type = SERVER;
    private int state = CLOSED;
    //socketClient classe de creation de suppression de Socket.
    //private TcpClient	sockCl 	= null;
    private TCB tcb = null;
    private Socket sock_;
    //private ServerSocket	serverSock_;
    private int portServ;
    private String ip_;
    private Segment segToSend;
    private String stringToShow;
    private int stateToSet;
    private boolean send;
    public boolean stepbystep;
    public boolean autoAck;
    public boolean hidden;
    private GUIManager gui = GUIManager.getInstance();
    TCPManager tcp = TCPManager.getInstance();
    private static final String StatesStr[] = {"CLOSED", "LISTEN", "SYN_RCVD", "SYN_SENT", "ESTAB", "FIN_WAIT_1", "CLOSE_WAIT", "FIN_WAIT_2", "CLOSING", "LAST_ACK", "TIME_WAIT", "CLOSED_INIT"};

    /**
     * Constructeur
     * @param TcpClient sockCl 
     */
    public Automaton() {
        tcb = new TCB();
        state = CLOSED_INIT;
        //set_state(CLOSED_INIT);
        send = false;
        autoAck = true;
        stringToShow = "";
        hidden = false;
    }

    public void setSock(Socket sock) {
        sock_ = sock;
    }

    //public void setServerSock(ServerSocket serverSock) {
    //	serverSock_ = serverSock;
    //}
    public void setPort(int port) {
        portServ = port;
    }

    public int getPortServ() {
        return portServ;
    }

    public int getPortClient() {
        return sock_.getLocalPort();
    }

    public int getState() {
        return state;
    }

    public void setIp(String ip) {
        this.ip_ = ip;
    }

    public String getIp() {
        return ip_;
    }

    /**
     * @return Renvoi le TCB
     */
    public TCB getTcb() {
        return tcb;
    }

    public void goGoSend(int source) {
        if ((this.stepbystep) && (source == 1)) {
            if (!"".equals(stringToShow)) {
                gui.getPanelConsole().insertLine(stringToShow, "Green");
                stringToShow = "";
            }
            if (send) {
                send(segToSend);
                send = false;
            }
            set_state(stateToSet);
        } else {
            if (!this.stepbystep && source == 0) {

                send(segToSend);
                send = false;
                if (!"".equals(stringToShow)) {
                    gui.getPanelConsole().insertLine(stringToShow, "Green");
                    stringToShow = "";
                }
            }
        }
    }

    public synchronized void send(Segment segment) {
        if (tcp.getisClient()) {
            tcp.getClient().sendMsg(segment.dump());
        }
        if (tcp.getisServer()) {
            tcp.getServeur().sendMsg(segment.dump());
        }
        if (!segment.get_ACK()) {
            tcb.addSegment(segment);
        }
        //this.tcb.set_sndNXT(this.tcb.get_sndNXT() + segment.get_data().length);
    }

    public void evaluateStateClose() {
        switch (state) {
            case (CLOSED_INIT):
                gui.getPanelConsole().insertLine("Error: No connection", "Red");
                break;
            case (CLOSED):
                gui.getPanelConsole().insertLine("Error: No connection", "Red");
                break;
            case (LISTEN):
                gui.getPanelConsole().insertLine("Error: Server is shuting down", "Red");
                if (tcp.getServeur() != null) {
                    tcp.getServeur().close();
                }
                //Effacer le TCB
                if (this.stepbystep) {
                    this.stateToSet = CLOSED;
                } else {
                    set_state(CLOSED);
                }
                break;
            case (SYN_SENT):
                //Effacer le TCB
                gui.getPanelConsole().insertLine("Error: Server is shuting down", "Red");
                tcb.delAllSegment();
                if (this.stepbystep) {
                    this.stateToSet = CLOSED;
                } else {
                    set_state(CLOSED);
                }
                break;
            case (SYN_RCVD):
                segToSend = tcb.send(false, false, false, false, false, true, "", tcb.get_sndNXT(), tcb.get_rcvNXT());
                send = true;
                goGoSend(0);
                if (this.stepbystep) {
                    this.stateToSet = FIN_WAIT_1; // FIXME: Problème sur le correspondant (il passe en Establish)
                } else {
                    set_state(FIN_WAIT_1);
                }
                break;
            case (ESTAB):
                segToSend = tcb.send(false, false, false, false, false, true, "", tcb.get_sndNXT(), tcb.get_rcvNXT());
                send = true;
                goGoSend(0);
                if (this.stepbystep) {
                    this.stateToSet = FIN_WAIT_1; // FIXME: Problème sur le correspondant (il passe en Establish)
                } else {
                    set_state(FIN_WAIT_1);
                }
                break;
            case (CLOSE_WAIT):
                segToSend = tcb.send(false, false, false, false, false, true, "", tcb.get_sndNXT(), tcb.get_rcvNXT());
                send = true;
                goGoSend(0);
                if (this.stepbystep) {
                    this.stateToSet = CLOSING; // FIXME: Problème sur le correspondant (il passe en Establish)
                } else {
                    set_state(CLOSING);
                }
                break;
            default:
                gui.getPanelConsole().insertLine("Error: Server is shuting down", "Red");
                break;
        }
    }

    public void evaluateStateOpen(int type_) {
        type = type_;
        switch (state) {
            case (CLOSED_INIT): {
                set_state(CLOSED_INIT);
                if (type == SERVER) {
                    tcb.initSegmentVector();
                    tcb.portServer = this.getPortServ();
                    if (this.stepbystep) {
                        this.stateToSet = LISTEN;
                        stringToShow = "Server Created";
                    } else {
                        set_state(LISTEN);
                        gui.getPanelConsole().insertLine("Server Created", "Green");
                    }
                }
                if (type == CLIENT) {
                    tcb.initSegmentVector();
                    tcb.set_ISS(Math.round(Math.random() * Math.pow(2, 6)) & 0x00000000FFFFFFFF); //Initialise le numéro de séquence (random)
                    tcb.portServer = this.getPortServ();
                    tcb.portClient = this.getPortClient();
                    tcb.type = 1;

                    segToSend = tcb.send(false, false, false, false, true, false, "", tcb.get_ISS(), 0);
                    send = true;
                    goGoSend(0);

                    tcb.set_sndUNA(tcb.get_ISS());
                    tcb.set_sndNXT(calcSeq(tcb.get_ISS()));
                    if (this.stepbystep) {
                        this.stateToSet = SYN_SENT;
                    } else {
                        set_state(SYN_SENT);
                    }
                }
            }
            break;
            case (CLOSED): {
                if (type == SERVER) {
                    set_state(LISTEN);
                }
                if (type == CLIENT) {
                    tcb.set_ISS(Math.round(Math.random() * Math.pow(2, 32)) & 0x00000000FFFFFFFF); //Initialise le numéro de séquence (random)

                    segToSend = tcb.send(false, false, false, false, true, false, "", tcb.get_ISS(), 0);
                    send = true;
                    goGoSend(0);

                    tcb.set_sndUNA(tcb.get_ISS());
                    tcb.set_sndNXT(calcSeq(tcb.get_ISS()));
                    if (this.stepbystep) {
                        this.stateToSet = SYN_SENT;
                    } else {
                        set_state(SYN_SENT);
                    }
                }
            }
            break;
            default:
                System.err.println("Error: La connexion existe déjà");
                break;
        }
    }

    /**
     * évalue l'état de l'automate lors de l'arrivée d'un segment
     * TODO mettre à jour les variables d'emissions et reception
     * 
     */
    public void EvaluateStateRcv(Segment seg) {
        //send (urg,ack,psh,rst,syn,fin)
        if (seg.get_ACK()) {
            tcb.delSegment(seg);
        }
        switch (state) {
            case (CLOSED):
                //close the socketClient
                //sockCl.close();				
                break;
            case (LISTEN):
                if (seg.get_SYN()) {
                    //p24 RFC
                    //gui.getPanelConsole().insertLine("ACK (=seg recu): " + seg.get_seq_number(), "Green");
                    tcb.portClient = seg.get_port_source();
                    tcb.type = 0;
                    tcb.set_rcvNXT(calcSeq(seg.get_seq_number()));
                    tcb.set_IRS(seg.get_seq_number());
                    tcb.set_ISS(Math.round(Math.random() * Math.pow(2, 6)) & 0x00000000FFFFFFFF); //Initialise le numéro de séquence (random)
                    segToSend = tcb.send(false, true, false, false, true, false, "", tcb.get_ISS(), tcb.get_rcvNXT());
                    send = true;
                    goGoSend(0);

                    tcb.set_sndNXT(calcSeq(tcb.get_ISS()));
                    tcb.set_sndUNA(tcb.get_ISS());
                    if (this.stepbystep) {
                        this.stateToSet = SYN_RCVD;
                    } else {
                        set_state(SYN_RCVD);
                    }
                }
                if (seg.get_ACK()) {
                    gui.getPanelConsole().insertLine("Error: Segment contains ACK flag", "Red");
                }
                break;
            case (SYN_SENT):
                //p.25 RFC

                if (seg.get_ACK()) {
                    // Cas d'Error sur l'ACK
                    //gui.getPanelConsole().insertLine("SNDNXT:" + tcb.get_sndNXT() + ": ACK: "+ seg.get_ack_number(), "Green");
                    if ((seg.get_ack_number() <= tcb.get_ISS()) || (seg.get_ack_number() > tcb.get_sndNXT())) {
                        segToSend = tcb.send(false, false, false, true, false, false, "", seg.get_ack_number(), 0);
                        send = true;
                        goGoSend(0);
                        gui.getPanelConsole().insertLine("Acquittement négatif", "Red");
                    }
                    // Acquittement positif
                    if (tcb.get_sndUNA() <= seg.get_ack_number() && seg.get_ack_number() <= tcb.get_sndNXT()) {
                        //Acceptation de l'acquittement
                        // Next: SYN
                        if (seg.get_SYN()) {
                            tcb.set_rcvNXT(calcSeq(seg.get_seq_number()));
                            tcb.set_IRS(seg.get_seq_number());
                            if (seg.get_ACK()) {
                                tcb.set_sndUNA(seg.get_ack_number());
                                //FIXME supprimer les segments qui ont ete acquité de la pile de retransmission
                                if (tcb.get_sndUNA() > tcb.get_ISS()) {
                                    if (this.stepbystep) {
                                        this.stateToSet = ESTAB;
                                    } else {
                                        set_state(ESTAB);
                                    }
                                    //CLIENT
                                    stringToShow = "Connected to server " + getIp() + ":" + this.getPortServ() + " on port:" + this.getPortClient();
                                    segToSend = tcb.send(false, true, false, false, false, false, "", tcb.get_sndNXT(), tcb.get_rcvNXT());
                                    send = true;
                                    goGoSend(0);
                                } else {
                                    set_state(SYN_RCVD);
                                    segToSend = tcb.send(false, true, false, false, true, false, "", tcb.get_ISS(), tcb.get_rcvNXT());
                                    send = true;
                                    goGoSend(0);
                                }
                            }
                        }
                    }
                }
                break;
            case (SYN_RCVD):
                // TODO test snd.una <= seg.ack <= snd.nxt
                if (seg.get_ACK()) {
                    tcb.delSegment(seg);
                    if (this.stepbystep) {
                        this.stateToSet = ESTAB;
                        stringToShow = "Connected to client";
                    } else {
                        set_state(ESTAB);
                        gui.getPanelConsole().insertLine("Connected to client", "Green");
                    }
                }
                if (seg.get_seq_number() >= tcb.get_rcvNXT()) {
                    //Numéro acceptable
                    if (seg.get_RST()) {
                        //Abandonner la session - Idem pour états FIN-WAIT-1/FIN-WAIT-2/CLOSE-WAIT
                        if (tcp.getisServer()) {
                            tcp.getServeur().retirerAllClient();
                            if (this.stepbystep) {
                                this.stateToSet = LISTEN;
                            } else {
                                set_state(LISTEN);
                            }
                            stringToShow = "Going back to LISTEN state";
                        } else {
                            gui.getPanelConsole().insertLine("Error: Connection rejected", "Red");
                            tcb.delAllSegment();
                            if (this.stepbystep) {
                                this.stateToSet = CLOSED;
                            } else {
                                set_state(CLOSED);
                            }
                        }
                    }
                }
                break;
            case (ESTAB): //RECEPTION DE DONNEES DE L'APPLICATION
                //Traitement du numéro de séquence (FENETRE NON GEREE) /!\
                //gui.getPanelConsole().insertLine("RCVNXT:" + tcb.get_rcvNXT() + ": SEQ: "+ seg.get_seq_number(), "Green");
                if (seg.get_FIN()) {
                    if (this.stepbystep) {
                        this.stateToSet = CLOSE_WAIT;
                    } else {
                        set_state(CLOSE_WAIT);
                    }
                    // Récupération des données
                    tcb.set_rcvNXT(seg.get_seq_number());//NEXT.
                    //tcb.set_sndNXT(this.calcSeq(seg.get_ack_number())); //NEXT.
                    if (this.autoAck) {
                        segToSend = tcb.send(false, true, false, false, false, false, "", tcb.get_sndNXT(), tcb.get_rcvNXT());
                        send = true;
                        this.goGoSend(0);
                    }
                } else if (seg.get_seq_number() >= tcb.get_rcvNXT()) {// && seg.get_seq_number() < (tcb.get_rcvNXT()+tcb.get_rcvWND())) {
                    //Numéro acceptable
                    if (seg.get_RST()) {
                        //Abandonner la session - Idem pour états FIN-WAIT-1/FIN-WAIT-2/CLOSE-WAIT
                        if (this.stepbystep) {
                            this.stateToSet = CLOSED;
                        } else {
                            set_state(CLOSED);
                        }
                        gui.getPanelConsole().insertLine("Error: Session is shuting down", "Red");
                        tcb.delAllSegment();
                    } else {
                        if (seg.get_SYN()) {
                            //FIXME Window...
                        }
                        if (seg.get_ACK()) {
                            if (tcb.get_sndUNA() < seg.get_ack_number() && seg.get_ack_number() <= tcb.get_sndNXT()) {
                                tcb.set_sndUNA(seg.get_ack_number());
                                tcb.delSegment(seg);
                                tcb.set_rcvNXT(seg.get_seq_number());
                                tcb.set_sndNXT(seg.get_ack_number());//NEXT. (pas modif)
                            }
                        } else {
                                // Récupération des données
                                tcb.set_rcvNXT(seg.get_seq_number());//NEXT.
                                //tcb.set_sndNXT(this.calcSeq(seg.get_ack_number())); //NEXT.
                                if (this.autoAck) {
                                    segToSend = tcb.send(false, true, false, false, false, false, "", tcb.get_sndNXT(), tcb.get_rcvNXT());
                                    send = true;
                                    this.goGoSend(0);
                                }
                        }
                    }
                } else {
                    if (seg.get_ACK()) {
                        gui.getPanelConsole().insertLine("Segment refused (wrong sequence number)", "Red");
                    } else {
                        tcb.set_rcvNXT(seg.get_seq_number());
                        //tcb.set_sndNXT(this.calcSeq(seg.get_ack_number())); Peut etre a remettre
                        if (this.autoAck) {
                            segToSend = tcb.send(false, true, false, false, false, false, "", tcb.get_sndNXT(), tcb.get_rcvNXT());
                            send = true;
                            this.goGoSend(0);
                        }
                        gui.getPanelConsole().insertLine("Segment refused (wrong sequence number)", "Red");
                        //gui.getPanelConsole().insertLine("SEQ: "+seg.get_seq_number()+": RCVNXT: "+tcb.get_rcvNXT(), "Red");
                    }
                }
                break;
            case (FIN_WAIT_1):
                //if (seg.get_seq_number() >= tcb.get_rcvNXT()) {// && seg.get_seq_number() < (tcb.get_rcvNXT()+tcb.get_rcvWND())) {
                //Numéro acceptable
                if (seg.get_RST()) {
                    //Abandonner la session - Idem pour états FIN-WAIT-1/FIN-WAIT-2/CLOSE-WAIT
                    if (this.stepbystep) {
                        this.stateToSet = CLOSED;
                    } else {
                        set_state(CLOSED);
                    }
                    gui.getPanelConsole().insertLine("Error: Session is shuting down", "Red");
                    tcb.delAllSegment();
                }
                if (seg.get_ACK()) {
                    if (tcb.get_sndUNA() < seg.get_ack_number() && seg.get_ack_number() <= tcb.get_sndNXT()) {
                        tcb.set_sndUNA(seg.get_ack_number());
                        tcb.delSegment(seg);
                        tcb.set_rcvNXT(seg.get_seq_number());
                        tcb.set_sndNXT(seg.get_ack_number());//NEXT. (pas modif)
                    }
                } else {
                    // Récupération des données
                    tcb.set_rcvNXT(seg.get_seq_number());//NEXT.
                    //tcb.set_sndNXT(this.calcSeq(seg.get_ack_number())); //NEXT.
                    if (this.autoAck) {
                        segToSend = tcb.send(false, true, false, false, false, false, "", tcb.get_sndNXT(), tcb.get_rcvNXT());
                        send = true;
                        this.goGoSend(0);
                    }
                }
                if (seg.get_ACK()) {
                    segToSend = tcb.send(false, true, false, false, false, false, "", tcb.get_sndNXT(), tcb.get_rcvNXT());
                    send = true;
                    this.goGoSend(0);
                    hidden = true;
                    if (this.stepbystep) {
                        this.stateToSet = FIN_WAIT_2;
                    } else {
                        set_state(FIN_WAIT_2);
                    }
                } else {
                    if (tcb.emptyStack()) {
                        if (this.stepbystep) {
                            this.stateToSet = TIME_WAIT;
                        } else {
                            set_state(TIME_WAIT);
                        }
                    } else {
                        if (this.stepbystep) {
                            this.stateToSet = CLOSING;
                        } else {
                            set_state(CLOSING);
                        }
                    }
                }
                //}
                break;
            /*				if (seg.get_ACK() && seg.get_FIN())
            {
            //tcb.send(false, true, false, false, false, false, "");
            set_state(TIME_WAIT);
            set_state(CLOSED);
            }
            else if (seg.get_ACK()) {
            set_state(FIN_WAIT_2);
            }
            else if (seg.get_FIN()) {
            //tcb.send(false, true, false, false, false, false, "");
            set_state(CLOSING);
            }
            break;*/
            case (CLOSE_WAIT):
                if (seg.get_seq_number() >= tcb.get_rcvNXT()) {// && seg.get_seq_number() < (tcb.get_rcvNXT()+tcb.get_rcvWND())) {
                    //Numéro acceptable
                    if (seg.get_RST()) {
                        //Abandonner la session - Idem pour états FIN-WAIT-1/FIN-WAIT-2/CLOSE-WAIT
                        if (this.stepbystep) {
                            this.stateToSet = CLOSED;
                        } else {
                            set_state(CLOSED);
                        }
                        gui.getPanelConsole().insertLine("Error: Session is shuting down", "Red");
                        tcb.delAllSegment();
                    }
                }
                segToSend = tcb.send(false, false, false, false, false, true, "", tcb.get_sndNXT(), tcb.get_rcvNXT());
                send = true;
                this.goGoSend(0);
                if (this.stepbystep) {
                    this.stateToSet = LAST_ACK;
                } else {
                    set_state(LAST_ACK);
                }
                hidden = false;
                break;
            case (FIN_WAIT_2):
                hidden = false;
                if (seg.get_seq_number() >= tcb.get_rcvNXT()) {// && seg.get_seq_number() < (tcb.get_rcvNXT()+tcb.get_rcvWND())) {
                    //Numéro acceptable
                    if (seg.get_RST()) {
                        //Abandonner la session - Idem pour états FIN-WAIT-1/FIN-WAIT-2/CLOSE-WAIT
                        if (this.stepbystep) {
                            this.stateToSet = CLOSED;
                        } else {
                            set_state(CLOSED);
                        }
                        gui.getPanelConsole().insertLine("Error: Session is shuting down", "Red");
                        tcb.delAllSegment();
                    }
                }
                if (this.stepbystep) {
                    this.stateToSet = TIME_WAIT;
                } else {
                    set_state(TIME_WAIT);
                }
                if (seg.get_FIN()) {
                    if (this.autoAck) {
                        segToSend = tcb.send(false, true, false, false, false, false, "", tcb.get_sndNXT(), tcb.get_rcvNXT());
                        send = true;
                        this.goGoSend(0);
                    }
                }

                break;
            /*
            if (seg.get_FIN()) {
            //tcb.send(false, true, false, false, false, false, "");
            set_state(TIME_WAIT);
            set_state(CLOSED);
            }
            break;*/
            case (CLOSING):
                if (seg.get_seq_number() >= tcb.get_rcvNXT()) {// && seg.get_seq_number() < (tcb.get_rcvNXT()+tcb.get_rcvWND())) {
                    //Numéro acceptable
                    if (seg.get_RST()) {
                        //Abandonner la session - Idem pour états FIN-WAIT-1/FIN-WAIT-2/CLOSE-WAIT
                        if (this.stepbystep) {
                            this.stateToSet = CLOSED;
                        } else {
                            set_state(CLOSED);
                        }
                        gui.getPanelConsole().insertLine("Error: Session is shuting down", "Red");
                        tcb.delAllSegment();
                    }
                }
                if (tcb.emptyStack()) {
                    if (this.stepbystep) {
                        this.stateToSet = TIME_WAIT;
                    } else {
                        set_state(TIME_WAIT);
                    }
                }
                if (seg.get_ACK()) {
                    if (this.autoAck) {
                        segToSend = tcb.send(false, true, false, false, false, false, "", tcb.get_sndNXT(), tcb.get_rcvNXT());
                        send = true;
                        this.goGoSend(0);
                    }
                }
                if (seg.get_FIN()) {
                    if (this.autoAck) {
                        segToSend = tcb.send(false, true, false, false, false, false, "", tcb.get_sndNXT(), tcb.get_rcvNXT());
                        send = true;
                        this.goGoSend(0);
                    }
                }
                break;
            /*if (seg.get_ACK()) {
            set_state(TIME_WAIT);
            set_state(CLOSED);
            }
            break;*/
            case (LAST_ACK):
                if (seg.get_seq_number() >= tcb.get_rcvNXT()) {// && seg.get_seq_number() < (tcb.get_rcvNXT()+tcb.get_rcvWND())) {
                    //Numéro acceptable
                    if (seg.get_RST()) {
                        //Abandonner la session - Idem pour états FIN-WAIT-1/FIN-WAIT-2/CLOSE-WAIT
                        if (this.stepbystep) {
                            this.stateToSet = CLOSED;
                        } else {
                            set_state(CLOSED);
                        }
                        gui.getPanelConsole().insertLine("Error: Session is shuting down", "Red");
                        tcb.delAllSegment();
                    }
                } /*if (seg.get_ACK())*/ {
                if (this.stepbystep) {
                    this.stateToSet = CLOSED;
                } else {
                    set_state(CLOSED);
                }
                hidden = true;
                segToSend = tcb.send(false, false, false, false, false, false, "", tcb.get_sndNXT(), tcb.get_rcvNXT());
                send = true;
                this.goGoSend(0);
            }

            break;
            case (TIME_WAIT):
                hidden = true;
                if (seg.get_seq_number() >= tcb.get_rcvNXT()) {// && seg.get_seq_number() < (tcb.get_rcvNXT()+tcb.get_rcvWND())) {
                    //Numéro acceptable
                    if (seg.get_RST()) {
                        //Abandonner la session - Idem pour états FIN-WAIT-1/FIN-WAIT-2/CLOSE-WAIT
                        if (this.stepbystep) {
                            this.stateToSet = CLOSED;
                        } else {
                            set_state(CLOSED);
                        }
                        gui.getPanelConsole().insertLine("Error: Session is shuting down", "Red");
                        tcb.delAllSegment();
                    }
                }
                if (seg.get_FIN()) {
                    if (this.autoAck) {
                        segToSend = tcb.send(false, true, false, false, false, false, "", tcb.get_sndNXT(), tcb.get_rcvNXT());
                        send = true;
                        this.goGoSend(0);
                    }
                }
                if (this.stepbystep) {
                    this.stateToSet = CLOSED;
                } else {
                    set_state(CLOSED);
                }

                break;
            /*
            set_state(CLOSED);
            break;*/
            default:
                System.err.println("Unknown state");
                break;
        }

    }

    /**
     * Cette fonction évalue l'état de l'automate lors de l'envoie d'un segment
     * @param seg : Segment à envoyer
     */
    public void evaluateStateSend(Segment seg) {
        switch (state) {
            case CLOSED:
                if (seg.get_SYN()) {
                    if (this.stepbystep) {
                        this.stateToSet = SYN_SENT;
                    } else {
                        set_state(SYN_SENT);
                    }
                    tcb.set_sndUNA(tcb.get_ISS());
                    tcb.set_sndNXT(tcb.get_ISS() + 1);
                } else {
                    gui.getPanelConsole().insertLine("Error: No connection", "Red");
                }
                break;
            case LISTEN:
                /*if (seg.get_SYN() && seg.get_ACK()) {
                set_state(SYN_RCVD);
                }
                else if (seg.get_SYN())  {
                set_state(SYN_SENT);
                }	*/
                break;
            case SYN_RCVD:
                gui.getPanelConsole().insertLine("Error: Insuffisant resources", "Red");
                break;
            case SYN_SENT:
                gui.getPanelConsole().insertLine("Error: Insuffisant resources", "Red");
                break;
            case ESTAB: //CAS D'ENVOI MANUEL DE SEGMENT DANS L'APPLICATION /!\	
                //                                                /___\
                if (seg.get_ack_number() == 0) {
                    seg.set_ack(tcb.get_rcvNXT());
                }
                if (seg.get_seq_number() == 0) {
                    seg.set_sequence(tcb.get_sndNXT() + seg.get_data().length + 1); //NEXT.
                    tcb.set_sndNXT(tcb.get_sndNXT() + seg.get_data().length + 1);
                }
                if (seg.get_checksum() == 0) {
                    seg.set_checksum(seg.get_checksum(seg));
                }
                this.send(seg);
                tcb.set_sndUP(0);
                break;
            case FIN_WAIT_1:
                gui.getPanelConsole().insertLine("Error: Connection is closing", "Red");
                break;
            case CLOSE_WAIT:
                if (seg.get_ack_number() == 0) {
                    seg.set_ack(tcb.get_rcvNXT());
                }
                if (seg.get_seq_number() == 0) {
                    seg.set_sequence(tcb.get_sndNXT() + seg.get_data().length + 1); //NEXT.
                    tcb.set_sndNXT(tcb.get_sndNXT() + seg.get_data().length + 1);
                }
                if (seg.get_checksum() == 0) {
                    seg.set_checksum(seg.get_checksum(seg));
                }
                this.send(seg);
                tcb.set_sndUP(0);
                break;
            case FIN_WAIT_2:
                gui.getPanelConsole().insertLine("Error: Connection is closing", "Red");
                break;
            case CLOSING:
                if (seg.get_ack_number() == 0) {
                    seg.set_ack(tcb.get_rcvNXT());
                }
                if (seg.get_seq_number() == 0) {
                    seg.set_sequence(tcb.get_sndNXT() + seg.get_data().length + 1); //NEXT.
                    tcb.set_sndNXT(tcb.get_sndNXT() + seg.get_data().length + 1);
                }
                if (seg.get_checksum() == 0) {
                    seg.set_checksum(seg.get_checksum(seg));
                }
                this.send(seg);
                tcb.set_sndUP(0);
                break;
            case LAST_ACK:
                //gui.getPanelConsole().insertLine("Error: Connection is closing", "Red");
                if (seg.get_ack_number() == 0) {
                    seg.set_ack(tcb.get_rcvNXT());
                }
                if (seg.get_seq_number() == 0) {
                    seg.set_sequence(tcb.get_sndNXT() + seg.get_data().length + 1); //NEXT.
                    tcb.set_sndNXT(tcb.get_sndNXT() + seg.get_data().length + 1);
                }
                if (seg.get_checksum() == 0) {
                    seg.set_checksum(seg.get_checksum(seg));
                }
                this.send(seg);
                tcb.set_sndUP(0);
                break;
            case TIME_WAIT:
                if (seg.get_ack_number() == 0) {
                    seg.set_ack(tcb.get_rcvNXT());
                }
                if (seg.get_seq_number() == 0) {
                    seg.set_sequence(tcb.get_sndNXT() + seg.get_data().length + 1); //NEXT.
                    tcb.set_sndNXT(tcb.get_sndNXT() + seg.get_data().length + 1);
                }
                if (seg.get_checksum() == 0) {
                    seg.set_checksum(seg.get_checksum(seg));
                }
                this.send(seg);
                tcb.set_sndUP(0);
                break;
            default:
                System.err.println("Unknown state");
                break;
        }
    }

    /**
     * @return le numéro de séquence du prochain segment
     */
    private long calcSeq(long oldSeq) {
        long max = 4294967296L;

        oldSeq++;
        if (oldSeq > max) {
            oldSeq = 0;
        }
        return oldSeq;
    }

    /**
     * Calcul le numéro d'acquittement par rapport au segment reçu
     * @param segment = Segment reçu
     * @return
     */
    public long calcAck(Segment segment) {
        long ack;
        long max = 4294967296L;

        ack = segment.get_seq_number();
        ack += segment.get_data().length;
        if (ack > max) {
            ack = ack - max;
        }
        return ack;
    }

    public void set_state(int currentState) {
        int i = 0;
        String stateStr = null;
        for (i = 0; i <= 11; ++i) {
            if (currentState == i) {
                stateStr = StatesStr[i];
                break;
            }
        }
        GUIManager.getInstance().getPanelAutomate().update_states(stateStr);
        state = currentState;
        //communication avec l'interface
    }
}
