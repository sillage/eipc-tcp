package Interface;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import common.TCPManager;
import common.TcpClient;
import common.TcpServer;
import myLib.BoundedTextField;

/**
 * Classe pour la création du JDialog de l'ouverture de connexion
 * @author Sebastien Gislais
 *
 */
@SuppressWarnings("serial")
public class ConnectionDialog extends JDialog implements ActionListener, ItemListener {

    JPanel dialogPanel;
    Checkbox BoxClient, BoxServer;
    CheckboxGroup typeConnec;
    BoundedTextField IpDest, IpSrc, PortDest, PortSrc, Identifiant;
    JButton Bok, Bcancel;

    /**
     * Constructeur
     */
    public ConnectionDialog() {
        setTitle("Connexion");
        setSize(285, 250);
        setResizable(false);
        this.setLocationRelativeTo(null);
        setVisible(true);

        dialogPanel = new JPanel();
        dialogPanel.setBounds(0, 0, 300, 300);
        this.getContentPane().add(dialogPanel);

        // Type connexion
        JLabel TypeLabel = new JLabel("Type");
        TypeLabel.setBounds(10, 10, 80, 20);
        dialogPanel.add(TypeLabel);
        typeConnec = new CheckboxGroup();
        BoxClient = new Checkbox("Client", true, typeConnec);
        BoxClient.setBounds(120, 12, 70, 20);
        BoxClient.addItemListener(this);
        dialogPanel.add(BoxClient);
        BoxServer = new Checkbox("Serveur", false, typeConnec);
        BoxServer.setBounds(200, 12, 100, 20);
        BoxServer.addItemListener(this);
        dialogPanel.add(BoxServer);

        // Ip destination
        JLabel IpDestLabel = new JLabel("IP Destination");
        IpDestLabel.setBounds(10, 40, 80, 20);
        dialogPanel.add(IpDestLabel);
        IpDest = new BoundedTextField(null, 15);
        IpDest.setText("localhost");
        IpDest.setBounds(120, 40, 150, 20);
        dialogPanel.add(IpDest);

        // Ip source
        JLabel IpSrcLabel = new JLabel("IP Source");
        IpSrcLabel.setBounds(10, 70, 80, 20);
        dialogPanel.add(IpSrcLabel);
        IpSrc = new BoundedTextField(null, 15);
        IpSrc.setText("localhost");
        IpSrc.setBounds(120, 70, 150, 20);
        IpSrc.setEnabled(false);
        dialogPanel.add(IpSrc);

        // Port destination
        JLabel PortDestLabel = new JLabel("Port Destination");
        PortDestLabel.setBounds(10, 100, 100, 20);
        dialogPanel.add(PortDestLabel);
        PortDest = new BoundedTextField(null, 15);
        PortDest.setText("4242");
        PortDest.setBounds(120, 100, 150, 20);
        dialogPanel.add(PortDest);

        // Ip source
        JLabel PortSrcLabel = new JLabel("Port Source");
        PortSrcLabel.setBounds(10, 130, 80, 20);
        dialogPanel.add(PortSrcLabel);
        PortSrc = new BoundedTextField(null, 15);
        PortSrc.setText("4242");
        PortSrc.setBounds(120, 130, 150, 20);
        PortSrc.setEnabled(false);
        dialogPanel.add(PortSrc);

        // Identifiant
        JLabel IdentLabel = new JLabel("Identifiant");
        IdentLabel.setBounds(10, 160, 80, 20);
        dialogPanel.add(IdentLabel);
        Identifiant = new BoundedTextField(null, 15);
        Identifiant.setText("Gislais");
        Identifiant.setBounds(120, 160, 150, 20);
        dialogPanel.add(Identifiant);

        // bouton ok et cancel
        Bok = new JButton("Connexion");
        Bok.addActionListener(this);
        Bok.setBounds(25, 190, 110, 20);
        dialogPanel.add(Bok);
        Bcancel = new JButton("Annuler");
        Bcancel.addActionListener(this);
        Bcancel.setBounds(140, 190, 110, 20);
        dialogPanel.add(Bcancel);
    }

    /**
     * Gestion des actions liées aux boutons
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Bok) {
            TCPManager tcp = TCPManager.getInstance();
            GUIManager gui = GUIManager.getInstance();
            if (BoxClient.getState() == true) {
                tcp.setClient(new TcpClient(IpDest.getText(), Integer.valueOf(PortDest.getText()).intValue(), Identifiant.getText()));
                tcp.setisClient(true);
                gui.getPanelConnection().fill_infos("client", IpSrc.getText(), IpDest.getText(), PortSrc.getText(), PortDest.getText(), Identifiant.getText());
                Thread t = new Thread(tcp.getClient());
                t.start();
            } else {
                tcp.setServeur(new TcpServer(Integer.valueOf(PortSrc.getText()).intValue()));
                tcp.setisServer(true);
                gui.getPanelConnection().fill_infos("serveur", IpSrc.getText(), IpDest.getText(), PortSrc.getText(), PortDest.getText(), Identifiant.getText());
                Thread t = new Thread(tcp.getServeur());
                t.start();
            }
            dispose();
        }
        if (e.getSource() == Bcancel) {
            dispose();
        }
    }

    /**
     * Gestion des actions liées au changement de type
     */
    @Override
    public void itemStateChanged(ItemEvent item) {
        // action pour la ckeckbox step_by_step
        if (item.getSource() == BoxClient
                && item.getStateChange() == ItemEvent.SELECTED) {
            IpDest.setEnabled(true);
            IpSrc.setEnabled(false);
            PortDest.setEnabled(true);
            PortSrc.setEnabled(false);
            Identifiant.setEnabled(true);
        }
        if (item.getSource() == BoxServer
                && item.getStateChange() == ItemEvent.SELECTED) {
            IpDest.setEnabled(false);
            IpSrc.setEnabled(false);
            PortDest.setEnabled(false);
            PortSrc.setEnabled(true);
            Identifiant.setEnabled(true);
        }
    }
}
