package Interface;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import common.TCPManager;

import kernel.Traitement;

@SuppressWarnings("serial")
public class PanelConnection extends JPanel implements ActionListener {

    JButton DecoBtn;
    Clock clock;
    Traitement traitement = Traitement.getInstance();
    JLabel TypeLabel2, IpSrcLabel2, IpDestLabel2, PortLocalLabel2, PortDestLabel2,
            IdentLabel2;

    /**
     * Constructeur
     */
    public PanelConnection() {
        setLayout(null);
        setBounds(5, 390, 305, 210);
        setBorder(new TitledBorder("Informations connexion"));


        // ajout clock pour test
        clock = new Clock();
        clock.setBounds(120, 140, 200, 20);
        clock.setFocusable(false);
        clock.setEditable(false);
        clock.setBorder(null);
        add(clock);

        JLabel TypeLabel = new JLabel("Type: ");
        TypeLabel.setBounds(10, 20, 40, 20);
        add(TypeLabel);

        TypeLabel2 = new JLabel("Non défini");
        TypeLabel2.setFont(new Font("arial", 0, 12));
        TypeLabel2.setBounds(45, 20, 200, 20);
        add(TypeLabel2);

        JLabel IpSrcLabel = new JLabel("IP Source: ");
        IpSrcLabel.setBounds(10, 40, 70, 20);
        add(IpSrcLabel);

        IpSrcLabel2 = new JLabel("Non défini");
        IpSrcLabel2.setFont(new Font("arial", 0, 12));
        IpSrcLabel2.setBounds(72, 40, 200, 20);
        add(IpSrcLabel2);

        JLabel IpDestLabel = new JLabel("IP Destination: ");
        IpDestLabel.setBounds(10, 60, 90, 20);
        add(IpDestLabel);

        IpDestLabel2 = new JLabel("Non défini");
        IpDestLabel2.setFont(new Font("arial", 0, 12));
        IpDestLabel2.setBounds(95, 60, 200, 20);
        add(IpDestLabel2);

        JLabel PortLocalLabel = new JLabel("Port Local: ");
        PortLocalLabel.setBounds(10, 80, 70, 20);
        add(PortLocalLabel);

        PortLocalLabel2 = new JLabel("Non défini");
        PortLocalLabel2.setFont(new Font("arial", 0, 12));
        PortLocalLabel2.setBounds(75, 80, 200, 20);
        add(PortLocalLabel2);

        JLabel PortDestLabel = new JLabel("Port Destination: ");
        PortDestLabel.setBounds(10, 100, 100, 20);
        add(PortDestLabel);

        PortDestLabel2 = new JLabel("Non défini");
        PortDestLabel2.setFont(new Font("arial", 0, 12));
        PortDestLabel2.setBounds(108, 100, 200, 20);
        add(PortDestLabel2);

        JLabel IdentLabel = new JLabel("Identifiant: ");
        IdentLabel.setBounds(10, 120, 70, 20);
        add(IdentLabel);

        IdentLabel2 = new JLabel("Non défini");
        IdentLabel2.setFont(new Font("arial", 0, 12));
        IdentLabel2.setBounds(75, 120, 200, 20);
        add(IdentLabel2);

        JLabel ClockLabel = new JLabel("Durée de session: ");
        ClockLabel.setBounds(10, 140, 120, 20);
        add(ClockLabel);

        //Remplir
        DecoBtn = new JButton("Déconnecter");
        DecoBtn.addActionListener(this);
        add(DecoBtn);
        DecoBtn.setBounds(10, 180, 120, 20);
    }

    public void fill_infos(String type,
            String ipsrc,
            String ipdest,
            String portlocal,
            String portdest,
            String ident) {
        TypeLabel2.setText(type);
        IpSrcLabel2.setText(ipsrc);
        IpDestLabel2.setText(ipdest);
        PortLocalLabel2.setText(portlocal);
        PortDestLabel2.setText(portdest);
        IdentLabel2.setText(ident);
        clock.startClock();
    }

    public void erase_infos(String type,
                        String ipsrc,
            String ipdest,
            String portlocal,
            String portdest,
            String ident) {
        TypeLabel2.setText(type);
        IpSrcLabel2.setText(ipsrc);
        IpDestLabel2.setText(ipdest);
        PortLocalLabel2.setText(portlocal);
        PortDestLabel2.setText(portdest);
        IdentLabel2.setText(ident);
        clock.endClock();
    }
    
    @Override
    public void actionPerformed(ActionEvent action) {
        // TODO Auto-generated method stub
        GUIManager gui;
        gui = GUIManager.getInstance();
        TCPManager tcp = TCPManager.getInstance();

        if (action.getSource() == DecoBtn) {
            //if (traitement.getStateAutomate() == 4) {
            if (tcp.getisClient()) {        
                 gui.getPanelConnection().erase_infos("Non défini", "Non défini", "Non défini", "Non défini", "Non défini","Non défini");
                tcp.getClient().traitement.makeTreatmentClose();
            }
            if (tcp.getisServer()) {
                 gui.getPanelConnection().erase_infos("Non défini", "Non défini", "Non défini", "Non défini", "Non défini","Non défini");
                tcp.getServeur().traitement.makeTreatmentClose();
            }
            //}
        }
    }
}
