package Interface;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import myLib.ImageSet;
import kernel.Traitement;

/**
 * Classe définissant le panneau automate
 * @author Sebastien Gislais
 *
 */
@SuppressWarnings("serial")
public class PanelAutomate extends JPanel implements ActionListener, ItemListener {

    ImageSet panel_background = new ImageSet("../Interface/Img/automatepanelbg.jpg", null);
    Map<String, Map<String, ImageIcon>> map_textures = new HashMap<String, Map<String, ImageIcon>>();
    Map<String, JLabel> map_jlabels = new HashMap<String, JLabel>();
    JLabel labclosed1, lablisten, labsynrcvd, labsynsent;
    JComboBox listStates = null;
    JButton /*Bgotostate,*/ Bnextstep;
    Checkbox Boxstepbystep, Boxautoack;
    GUIManager gui_manager = GUIManager.getInstance();
    Traitement traitement = Traitement.getInstance();
    String[] automate_states = new String[]{"", "CLOSED_INIT", "LISTEN", "SYN_RCVD", "SYN_SENT", "ESTAB",
        "FIN_WAIT_1", "CLOSE_WAIT", "FIN_WAIT_2", "CLOSING",
        "LAST_ACK", "TIME_WAIT", "CLOSED"};

    /**
     * Constructeur du panneau automate
     */
    public PanelAutomate() {
        setLayout(null);
        setPreferredSize(new Dimension(345, 559));
        setBounds(320, 12, 345, 559);

        // chargement de l'automate
        loadMapTextures();
        loadMapJLabels();
        draw_automate();

        // check box ack-auto
        Boxautoack = new Checkbox("ACK Automatique", true);
        Boxautoack.setBounds(8, 505, 130, 20);
        Boxautoack.setBackground(new Color(59, 59, 59));
        Boxautoack.setForeground(Color.white);
        Boxautoack.addItemListener(this);
        add(Boxautoack);

        // check box step-by-step
        Boxstepbystep = new Checkbox("Mode \"Step-by-Step\"", false);
        Boxstepbystep.setBounds(8, 530, 130, 20);
        Boxstepbystep.setBackground(new Color(59, 59, 59));
        Boxstepbystep.setForeground(Color.white);
        Boxstepbystep.addItemListener(this);
        add(Boxstepbystep);

        // combo box des états
		/*listStates = new JComboBox(automate_states);
        listStates.setBounds(8, 530, 100, 20);
        listStates.setFont(new Font("Arial", 0, 11));
        add(listStates);*/

        // bouton de switching
        /*Bgotostate = new JButton("Switch");
        Bgotostate.addActionListener(this);
        add(Bgotostate);
        Bgotostate.setBounds(110, 530, 80, 20);*/

        // bouton de next step
        Bnextstep = new JButton("Next Step");
        Bnextstep.addActionListener(this);
        Bnextstep.setEnabled(false);
        add(Bnextstep);
        Bnextstep.setBounds(247, 530, 90, 20);
    }

    /**
     * Charge une texture depuis un fichier dans une map pour créer un couple (texture off, texture on)
     * @param text1 chemin du fichier texture "off"
     * @param text2 chemin du fichier texture "on"
     * @return un couple (texture off, texture on)
     */
    public Map<String, ImageIcon> loadTexture(String text1, String text2) {
        Map<String, ImageIcon> map = new HashMap<String, ImageIcon>();
        ImageSet text_off = new ImageSet(text1, null);
        ImageSet text_on = new ImageSet(text2, null);

        map.put("OFF", text_off.getImage());
        map.put("ON", text_on.getImage());
        return map;
    }

    /**
     * Charge les textures de l'automate
     */
    public void loadMapTextures() {
        map_textures.put("CLOSED_INIT", loadTexture("../Interface/Img/autom/closed1.jpg", "../Interface/Img/autom/closed1-o.jpg"));
        map_textures.put("LISTEN", loadTexture("../Interface/Img/autom/listen.jpg", "../Interface/Img/autom/listen-o.jpg"));
        map_textures.put("SYN_RCVD", loadTexture("../Interface/Img/autom/synrcvd.jpg", "../Interface/Img/autom/synrcvd-o.jpg"));
        map_textures.put("SYN_SENT", loadTexture("../Interface/Img/autom/synsent.jpg", "../Interface/Img/autom/synsent-o.jpg"));
        map_textures.put("ESTAB", loadTexture("../Interface/Img/autom/estab.jpg", "../Interface/Img/autom/estab-o.jpg"));
        map_textures.put("FIN_WAIT_1", loadTexture("../Interface/Img/autom/fin-wait-1.jpg", "../Interface/Img/autom/fin-wait-1-o.jpg"));
        map_textures.put("CLOSE_WAIT", loadTexture("../Interface/Img/autom/close-wait.jpg", "../Interface/Img/autom/close-wait-o.jpg"));
        map_textures.put("FIN_WAIT_2", loadTexture("../Interface/Img/autom/fin-wait-2.jpg", "../Interface/Img/autom/fin-wait-2-o.jpg"));
        map_textures.put("CLOSING", loadTexture("../Interface/Img/autom/closing.jpg", "../Interface/Img/autom/closing-o.jpg"));
        map_textures.put("LAST_ACK", loadTexture("../Interface/Img/autom/last-ack.jpg", "../Interface/Img/autom/last-ack-o.jpg"));
        map_textures.put("TIME_WAIT", loadTexture("../Interface/Img/autom/time-wait.jpg", "../Interface/Img/autom/time-wait-o.jpg"));
        map_textures.put("CLOSED", loadTexture("../Interface/Img/autom/closed.jpg", "../Interface/Img/autom/closed-o.jpg"));
    }

    /**
     * Charge la map (table de hash) des JLabels
     * Structure : "STATE_NAME" => JLabel
     */
    public void loadMapJLabels() {
        JLabel closed_initLabel = new JLabel((map_textures.get("CLOSED_INIT")).get("OFF"));
        JLabel listenLabel = new JLabel((map_textures.get("LISTEN")).get("OFF"));
        JLabel syn_rcvdLabel = new JLabel((map_textures.get("SYN_RCVD")).get("OFF"));
        JLabel syn_sentLabel = new JLabel((map_textures.get("SYN_SENT")).get("OFF"));
        JLabel estabLabel = new JLabel((map_textures.get("ESTAB")).get("OFF"));
        JLabel fin_wait_1Label = new JLabel((map_textures.get("FIN_WAIT_1")).get("OFF"));
        JLabel close_waitLabel = new JLabel((map_textures.get("CLOSE_WAIT")).get("OFF"));
        JLabel fin_wait_2Label = new JLabel((map_textures.get("FIN_WAIT_2")).get("OFF"));
        JLabel closingLabel = new JLabel((map_textures.get("CLOSING")).get("OFF"));
        JLabel last_ackLabel = new JLabel((map_textures.get("LAST_ACK")).get("OFF"));
        JLabel time_waitLabel = new JLabel((map_textures.get("TIME_WAIT")).get("OFF"));
        JLabel closedLabel = new JLabel((map_textures.get("CLOSED")).get("OFF"));

        map_jlabels.put("CLOSED_INIT", closed_initLabel);
        map_jlabels.put("LISTEN", listenLabel);
        map_jlabels.put("SYN_RCVD", syn_rcvdLabel);
        map_jlabels.put("SYN_SENT", syn_sentLabel);
        map_jlabels.put("ESTAB", estabLabel);
        map_jlabels.put("FIN_WAIT_1", fin_wait_1Label);
        map_jlabels.put("CLOSE_WAIT", close_waitLabel);
        map_jlabels.put("FIN_WAIT_2", fin_wait_2Label);
        map_jlabels.put("CLOSING", closingLabel);
        map_jlabels.put("LAST_ACK", last_ackLabel);
        map_jlabels.put("TIME_WAIT", time_waitLabel);
        map_jlabels.put("CLOSED", closedLabel);
    }

    /**
     * Charge les JLabels (images) et les positiones dans le JPanel
     */
    public void draw_automate() {
        // CLOSED INIT STATE
        map_jlabels.get("CLOSED_INIT").setBounds(120, 100, 213, 56);
        add(map_jlabels.get("CLOSED_INIT"));

        // LISTEN
        map_jlabels.get("LISTEN").setBounds(120, 156, 213, 56);
        add(map_jlabels.get("LISTEN"));

        // SYN RCVD
        map_jlabels.get("SYN_RCVD").setBounds(10, 212, 160, 47);
        add(map_jlabels.get("SYN_RCVD"));

        // SYN SENT
        map_jlabels.get("SYN_SENT").setBounds(170, 212, 163, 47);
        add(map_jlabels.get("SYN_SENT"));

        // ESTAB
        map_jlabels.get("ESTAB").setBounds(10, 259, 214, 50);
        add(map_jlabels.get("ESTAB"));

        // FIN WAIT 1
        map_jlabels.get("FIN_WAIT_1").setBounds(10, 309, 168, 51);
        add(map_jlabels.get("FIN_WAIT_1"));

        // CLOSE WAIT
        map_jlabels.get("CLOSE_WAIT").setBounds(178, 309, 154, 51);
        add(map_jlabels.get("CLOSE_WAIT"));

        // FIN WAIT 2
        map_jlabels.get("FIN_WAIT_2").setBounds(10, 360, 105, 60);
        add(map_jlabels.get("FIN_WAIT_2"));

        // CLOSING WAIT
        map_jlabels.get("CLOSING").setBounds(115, 360, 109, 60);
        add(map_jlabels.get("CLOSING"));

        // LAST ACK
        map_jlabels.get("LAST_ACK").setBounds(224, 360, 108, 60);
        add(map_jlabels.get("LAST_ACK"));

        // TIME WAIT
        map_jlabels.get("TIME_WAIT").setBounds(55, 420, 169, 59);
        add(map_jlabels.get("TIME_WAIT"));

        // CLOSED
        map_jlabels.get("CLOSED").setBounds(224, 420, 108, 59);
        add(map_jlabels.get("CLOSED"));
    }

    /**
     * Met à jour un état dans l'automate
     * Les états : 
     * CLOSED_INIT,
     * LISTEN,
     * SYN_RCVD,
     * SYN_SENT,
     * ESTAB,
     * FIN_WAIT_1,
     * CLOSE_WAIT,
     * FIN_WAIT_2,
     * CLOSING,
     * LAST_ACK,
     * TIME_WAIT,
     * CLOSED
     * 
     * @param state_active Etat à activer
     */
    public void update_states(String state_active) {
        String key;
        for (Iterator<String> i = map_jlabels.keySet().iterator(); i.hasNext();) {
            key = i.next();
            if (key.equals(state_active)) {
                map_jlabels.get(state_active).setIcon((map_textures.get(state_active)).get("ON"));
                gui_manager.getPanelConsole().insertLine("State switched to : " + state_active, "Normal");
            } else {
                map_jlabels.get(key).setIcon((map_textures.get(key)).get("OFF"));

            }
        }
    }

    /**
     * Override de la méthode paintComponent pour l'update des images
     */
    @Override
    public void paintComponent(Graphics g) {
        Image bg;
        super.paintComponent(g);
        if (panel_background.getImage() == null) {
            return;
        }
        bg = panel_background.getImage().getImage();
        g.drawImage(bg, 0, 0, 345, 559, this);
    }

    /**
     * Action button manager
     */
    @Override
    public void actionPerformed(ActionEvent action) {
        /*if (action.getSource() == Bgotostate) {
        if (! (listStates.getSelectedItem().toString().equals(""))) {
        update_states(listStates.getSelectedItem().toString());
        }
        }*/
        if (action.getSource() == Bnextstep) {
            // HERE§
            traitement.goGoSend(1);
        }
    }

    /**
     * Action item manager
     */
    @Override
    public void itemStateChanged(ItemEvent item) {
        // action pour la ckeckbox step_by_step
        if (item.getSource() == Boxstepbystep) {
            if (item.getStateChange() == ItemEvent.SELECTED) {
                Bnextstep.setEnabled(true);
                traitement.setStepByStep(true);
                gui_manager.getPanelConsole().insertLine("Step-by-Step Mode : ENABLED", "Normal");
                // ajouter ici le mode_step_by_step = 1;
            } else {
                Bnextstep.setEnabled(false);
                traitement.setStepByStep(false);
                gui_manager.getPanelConsole().insertLine("Step-by-Step Mode : DISABLED", "Normal");
                // ajouter ici le mode_step_by_step = 0;
            }
        }
        if (item.getSource() == Boxautoack) {
            if (item.getStateChange() == ItemEvent.SELECTED) {
                traitement.setAutoAck(true);
                gui_manager.getPanelConsole().insertLine("ACK Auto Mode : ENABLED", "Normal");
            } else {
                traitement.setAutoAck(false);
                gui_manager.getPanelConsole().insertLine("ACK Auto Mode : DISABLED", "Normal");
            }
        }
    }
}
