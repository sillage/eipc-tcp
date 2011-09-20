package Interface;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import common.TCPManager;
import kernel.TCB;
import myLib.BoundedTextField;
import Interface.ErrorDialog;

/**
 * Classe pour la fenetre de paramètrage (option)
 * @author Sebastien Gislais
 *
 */
@SuppressWarnings("serial")
public class ParametersDialog extends JDialog implements ActionListener {

    JPanel dialogPanel;
    BoundedTextField bufferEnvoi, bufferRecep;
    JButton Bok, Bcancel;
    TCPManager tcp = TCPManager.getInstance();

    /**
     * Constructeur
     * @param bufferenvoi taille du buffer d'envoi
     * @param bufferrecep taille du buffer de reception
     */
    public ParametersDialog(int bufferenvoi, int bufferrecep) {
        setTitle("Paramètres de connexion");
        setSize(260, 150);
        setResizable(false);
        this.setLocationRelativeTo(null);
        setVisible(true);

        dialogPanel = new JPanel();
        dialogPanel.setBounds(0, 0, 285, 150);
        this.getContentPane().add(dialogPanel);

        // Les Buffers
        JLabel BuffersLab = new JLabel("Tailles des Buffers");
        BuffersLab.setBounds(10, 5, 150, 20);
        dialogPanel.add(BuffersLab);
        JLabel BufferEnvoiLab = new JLabel("Buffer Emission");
        BufferEnvoiLab.setBounds(20, 28, 130, 20);
        BufferEnvoiLab.setFont(new Font("arial", 0, 12));
        dialogPanel.add(BufferEnvoiLab);
        JLabel BufferRecepLab = new JLabel("Buffer Réception");
        BufferRecepLab.setBounds(20, 53, 130, 20);
        BufferRecepLab.setFont(new Font("arial", 0, 12));
        dialogPanel.add(BufferRecepLab);

        bufferEnvoi = new BoundedTextField("", 20);
        bufferEnvoi.setBounds(115, 30, 80, 20);
        bufferEnvoi.setText(String.valueOf(bufferenvoi));
        dialogPanel.add(bufferEnvoi);
        bufferRecep = new BoundedTextField("", 20);
        bufferRecep.setBounds(115, 55, 80, 20);
        bufferRecep.setText(String.valueOf(bufferrecep));
        dialogPanel.add(bufferRecep);

        JLabel octetLab1 = new JLabel("octets");
        octetLab1.setBounds(200, 28, 100, 20);
        octetLab1.setFont(new Font("arial", 0, 12));
        dialogPanel.add(octetLab1);
        JLabel octetLab2 = new JLabel("octets");
        octetLab2.setBounds(200, 53, 100, 20);
        octetLab2.setFont(new Font("arial", 0, 12));
        dialogPanel.add(octetLab2);

        // bouton ok et cancel
        Bok = new JButton("OK");
        Bok.addActionListener(this);
        Bok.setBounds(15, 90, 110, 20);
        dialogPanel.add(Bok);
        Bcancel = new JButton("Annuler");
        Bcancel.addActionListener(this);
        Bcancel.setBounds(130, 90, 110, 20);
        dialogPanel.add(Bcancel);
    }

    /**
     * Filtre les champs concernant les paramètres des buffers
     * @param s la String à filtrer
     * @return un boolean qui indique si les données sont correctes
     */
    private boolean filtre_param(String s) {
        if (s.matches("[0-9]+")) {
            {
                if (Integer.valueOf(s) <= Short.MAX_VALUE) {
                    return true;
                } else {
                    new ErrorDialog("L'entier que vous avez entré dans la zone de saisie est supérieur à 2^16 - 1 !");
                    return false;
                }
            }
        } else {
            new ErrorDialog("Vous n'avez pas entré un entier positif et inferieur à 2^16 - 1");
            return false;
        }
    }

    /**
     * Gestion des actions liées aux boutons
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Bok) {
            // insert code action here for "OK"
            if (filtre_param(this.bufferEnvoi.getText()) & filtre_param(this.bufferRecep.getText())) {
                if (tcp.getisClient()) {
                    tcp.getClient().traitement.getTCB().setBuffer_lg(Integer.valueOf(bufferRecep.getText()));
                }
                if (tcp.getisServer()) {
                    tcp.getServeur().traitement.getTCB().setBuffer_lg(Integer.valueOf(bufferEnvoi.getText()));
                }
                dispose();
            }
        }
        if (e.getSource() == Bcancel) {
            dispose();
        }
    }
}
