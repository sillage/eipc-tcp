package common;

import java.io.PrintStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import Interface.GUIManager;
import kernel.Automaton;
import kernel.Traitement;

/** 
 * Client TCP
 * @author Sebastien Gislais
 *
 */
public class TcpClient implements Runnable {
    // Nom du client

    private String name;
    // Adresse IP du serveur
    private String ipServ;
    // Port d'écoute du serveur
    private int portServ;
    // Socket entre le serveur et le client
    private static Socket sock;
    // Flux d'écriture depuis le serveur
    private BufferedReader fromServ;
    // Flux d'écriture vers le serveur 
    private PrintStream toServ;
    // Interaction GUI
    private GUIManager gui;
    public Traitement traitement;

    /**
     * Constructeur
     * @param hostname, adresse IP du serveur à contacter
     * @param port, port d'écoute du serveur à contacter
     * @param name, nom du client
     */
    public TcpClient(String hostname, int port, String name) {
        this.name = name;
        this.ipServ = hostname;
        this.portServ = port;
        this.gui = GUIManager.getInstance();
        this.traitement = Traitement.getInstance();
    }

    /**
     * Lance le Client (Thread)
     */
    @Override
    public void run() {
        try {

            connexion();
            String line;
            String data;
            while (true) {
                if ((line = fromServ.readLine()) != null) {
                    String[] result = line.split(":@:");
                    line = result[0];
                    if (result.length > 1) {
                        data = result[1];
                    } else {
                        data = "";
                    }
                    if (traitement.getStateAutomate() != 10) {
                        gui.getPanelConsole().insertLine("Message received: " + line, "Normal EM");
                    }
                    Segment seg = new Segment(line);
                    //System.out.println("Client Reception");
                    seg.display_text();
                    gui.getPanelReceive().setSeqNumber(String.valueOf(seg.get_seq_number()));
                    gui.getPanelReceive().setAckNumber(String.valueOf(seg.get_ack_number()));
                    gui.getPanelReceive().setOffset(String.valueOf(seg.get_offset()));
                    gui.getPanelReceive().setFenetre(String.valueOf(seg.get_window()));
                    gui.getPanelReceive().setChecksum(String.valueOf(seg.get_checksum()));
                    gui.getPanelReceive().setPointUrg(String.valueOf(seg.get_pointeur_urgent()));
                    /*byte[] b = seg.get_data();
                    if (b != null)
                    {
                    String so = new String(b);
                    gui.getPanelReceive().setData(so);
                    }*/
                    gui.getPanelReceive().setData(data);
                    gui.getPanelReceive().setURG(seg.get_URG());
                    gui.getPanelReceive().setACK(seg.get_ACK());
                    gui.getPanelReceive().setPSH(seg.get_PSH());
                    gui.getPanelReceive().setSYN(seg.get_SYN());
                    gui.getPanelReceive().setRST(seg.get_RST());
                    gui.getPanelReceive().setFIN(seg.get_FIN());
                    gui.getPanelReceive().segment = seg;
                    traitement.makeTreatmentReceive(seg);
                }
            }
        } catch (Exception e) {
            gui.getPanelConsole().insertLine("Error: " + e.getMessage(), "Red");
        }
    }

    /**
     * Envoie un message au serveur
     * @param msg, message à envoyer
     */
    public void sendMsg(String msg) {
        try {
            if (traitement.getStateAutomate() != 0 && traitement.getStateAutomate() != 10) {
                gui.getPanelConsole().insertLine("Message sent: " + msg, "Normal EM");
            }
            this.toServ.println(msg + ":@:" + gui.getPanelSend().getEnvoiData().getText());
        } catch (Exception e) {
            gui.getPanelConsole().insertLine("Error: " + e.getMessage(), "Red");
        }
    }

    /**
     * Connexion du client au serveur
     *
     */
    public void connexion() {
        try {
            sock = new Socket(ipServ, portServ);
            this.fromServ = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            this.toServ = new PrintStream(new DataOutputStream(sock.getOutputStream()));
            traitement.giveInfo(sock, portServ, ipServ);
            traitement.makeTreatmentOpen(Automaton.CLIENT); // TITI
        } catch (Exception e) {
            gui.getPanelConsole().insertLine("Connexion Error: " + e.getMessage(), "Red");
        }
    }

    /**
     * Déconnexion du client
     *
     */
    public void disconnect() {
        try {
            sock.close();
            gui.getPanelConsole().insertLine("Connexion Closed", "Green");
        } catch (Exception e) {
            gui.getPanelConsole().insertLine("Deconnexion Error: " + e.getMessage(), "Red");
        }
    }

    /**
     * Gettor name
     * @return le nom du client 
     */
    public String getName() {
        return this.name;
    }
}
