package Interface;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import myLib.ImageDraw;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe principale pour l'interface de l'application.
 * @author Sebastien Gislais 
 */
@SuppressWarnings("serial")
public class Interface extends JFrame implements ActionListener {

    private TopMenu top_menu;
    private PanelSend panel_send;
    private PanelConnection panel_connection;
    private PanelAutomate panel_automate;
    private PanelReceive panel_receive;
    private PanelConsole panel_console;
    private ImageDraw panel_banner;
    private ImageDraw foot_banner;
    private GUIManager gui_manager; //singleton

    /**
     * Constructeur.
     */
    public Interface() {
        super();
        CreateAndShowGUI();
    }

    /**
     * Création de la fenêtre d'affichage.
     */
    private void CreateAndShowGUI() {
        // propriétés fenêtre principale
        this.setTitle("Projet TCP - TeamCP");
        this.setSize(new Dimension(995, 660));
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        //this.setMinimumSize(new Dimension(995, 605));

        // pas de layout prédéfini
        this.getContentPane().setLayout(null);

        // ajout du top menu
        top_menu = new TopMenu();
        this.setJMenuBar(top_menu.getJMenuBar());

        // ajout panneau send
        panel_send = new PanelSend();
        this.getContentPane().add(panel_send);

        // ajout panneau connection
        panel_connection = new PanelConnection();
        this.getContentPane().add(panel_connection);

        // ajout panneau automate
        panel_automate = new PanelAutomate();
        this.getContentPane().add(panel_automate);

        // ajout panneau receive
        panel_receive = new PanelReceive();
        this.getContentPane().add(panel_receive);

        // ajout panneau receive
        panel_console = new PanelConsole();
        this.getContentPane().add(panel_console);

        // ajout panneau_banner
        //panel_banner = new ImageDraw("Interface/Img/bantcp.jpg", 7, 308, 301, 80);
        //this.getContentPane().add(panel_banner.getPicture());

        // ajout panneau_banner
       //foot_banner = new ImageDraw("/Interface/Img/footban.jpg", 320, 577, 345, 21);
        //this.getContentPane().add(foot_banner.getPicture());

        // création du singleton pour manager les panels entres eux
        gui_manager = GUIManager.getInstance();
        gui_manager.setPanelConsole(panel_console);
        gui_manager.setPanelSend(panel_send);
        gui_manager.setPanelReceive(panel_receive);
        gui_manager.setPanelAutomate(panel_automate);
        gui_manager.setPanelConnection(panel_connection);
        gui_manager.setTopMenu(top_menu);
    }

    /**
     * Fonction main() pour le projet.
     * @param args
     */
    public static void main(String[] args) {
        Splash s = new Splash(2000);
        s.dispose(0);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                Interface gui = new Interface();
                gui.setVisible(true);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
    }
}
