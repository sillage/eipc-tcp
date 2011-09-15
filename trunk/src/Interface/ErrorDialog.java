/**
 * Creation de fenêtre de Warning
 */
package Interface;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import myLib.BoundedTextField;

/** 
 * @author Murphy
 *
 */
@SuppressWarnings("serial")
public class ErrorDialog extends JDialog implements ActionListener {
	JPanel dialogPanel;
	JLabel warningMsg;
	JButton Bok;
	
	
	/**
	 * Constructeur
	 * @param bufferenvoi taille du buffer d'envoi
	 * @param bufferrecep taille du buffer de reception
	 */
	public ErrorDialog(String msg) {
		int lg = msg.length() * 6;
		
		setTitle("Avertissement !");
		setSize(lg + 10,75);
		setResizable(false);
		this.setLocationRelativeTo(null);
		setVisible(true);

		dialogPanel = new JPanel();
		dialogPanel.setBounds(0,0, lg + 25, 75);
		this.getContentPane().add(dialogPanel);
        
        warningMsg = new JLabel(msg);
        warningMsg.setBounds(5, 5, lg, 20);
        dialogPanel.add(warningMsg);
        
        // bouton ok
        Bok = new JButton("OK");
        Bok.addActionListener(this);
        Bok.setBounds(lg /2 - 25, 25, 110, 20);
        dialogPanel.add(Bok);
	}
	
	/**
	 * Gestion des actions liées aux boutons
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == Bok) {
			dispose();
			}
		else {
			System.out.println("Error");	
			}
	}
}
