package common;

import java.net.ServerSocket;
import java.util.ArrayList;
import Interface.GUIManager;
import kernel.Automaton;
import kernel.Traitement;

/**
 * Class Serveur TCP
 * @author Sebastien Gislais
 *
 */
public class TcpServer implements Runnable {
    // Port d'écoute du Serveur

    public int port;
    // Tableau des clients connectés
    private static ArrayList<ServeurDialogue> clients;
    // Tableau
    private static ServerSocket sockServ;
    private static int running = 0;
    // Interaction GUI
    private GUIManager gui;
    public Traitement traitement;

    /**
     * Constructeur
     * @param port, port d'écoute
     */
    public TcpServer(int port) {
        this.port = port;
        this.gui = GUIManager.getInstance();
        this.traitement = Traitement.getInstance(); //TITI
    }

    /**
     * Lance le Serveur (Thread)
     */
    @Override
    public void run() {
        if (port < 1024 || 65535 < port) {
            gui.getPanelConsole().insertLine("Error: port number must be between 1024 and 65535", "Red");
        }
        try {
            running = 1;
            sockServ = new ServerSocket(port);
        } catch (Exception e) {
            gui.getPanelConsole().insertLine("Error creating the socket", "Red");
        }
        traitement.makeTreatmentOpen(Automaton.SERVER);

        clients = new ArrayList<ServeurDialogue>();
        try {
            while (running == 1) {
                clients.add(new ServeurDialogue(sockServ.accept()));
                traitement.giveInfo(sockServ, port, "0");
                //gui.getPanelConsole().insertLine("Client Connected", "Normal");
            }
        } catch (Exception e) {
            gui.getPanelConsole().insertLine("Client Creation Error:" + e.getMessage(), "RED");
            System.exit(1);
        }
    }

    /**
     * Envoie un Msg à tous les clients connectés
     * @param msg, msg à envoyer
     */
    public void sendMsg(String msg) {
        for (ServeurDialogue client : clients) {
            if (client != null) {
                client.envoyer(msg);
            }
        }
    }

    /**
     * Ferme un client
     * @param serveurDialogue
     */
    private static void retirerClient(ServeurDialogue serveurDialogue) {
        if (clients.contains(serveurDialogue)) {
            clients.get(clients.indexOf(serveurDialogue)).stop();
            clients.remove(serveurDialogue);
        }
    }

    public void retirerAllClient() {
        for (ServeurDialogue client : clients) {
            retirerClient(client);
        }
    }

    public void close() {
        running = 0;
        retirerAllClient();
        try {
            sockServ.close();
        } catch (Exception e) {
            gui.getPanelConsole().insertLine("Closing Server Error", "Red");
        }
        //gui.getPanelConsole().insertLine("Server Closed", "Green");
    }
}
