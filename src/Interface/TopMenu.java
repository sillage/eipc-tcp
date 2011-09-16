package Interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import common.TCPManager;

/**
 * Classe définissant le top menu de l'application
 * @author Rémi TANIWAKI
 *
 */
public class TopMenu implements ActionListener {
	private JMenuBar menuBar = null ; 
	private JMenu connexion = null ;
	private JMenu parametre = null ;
	private JMenuItem newconnec = null ;
	private JMenuItem paramsub = null ;

	/**
	 * Constructeur
	 */
	public TopMenu() {
		menuBar = new JMenuBar() ;
		connexion = new JMenu("Connexion") ;
		newconnec = new JMenuItem("Nouvelle Connexion") ;
		newconnec.addActionListener(this) ;
		connexion.add(newconnec) ;
		menuBar.add(connexion) ;

		parametre = new JMenu("Options");
		paramsub = new JMenuItem("Paramètres");
		paramsub.addActionListener(this);
		parametre.add(paramsub);
		menuBar.add(parametre);
	}
	
	/**
	 * Getter pour l'objet JMenuBar
	 * @return un objet de type JMenuBar
	 */
	public JMenuBar getJMenuBar() {
		return menuBar;
	}
	
    @Override
	public void actionPerformed(ActionEvent arg0) {
		TCPManager tcp = TCPManager.getInstance();
		int buff_env = 1000;
		int buff_rec = 1000;
		
		if (arg0.getSource() == newconnec) {
			new ConnectionDialog();
		}
		if (arg0.getSource() == paramsub) {
			// valeur a afficher
			// buff_env = tcp.getServeur().traitement.getTCB().getBuffer_lg();
			// buff_rec = tcp.getClient().traitement.getTCB().getBuffer_lg();
			new ParametersDialog(buff_env, buff_rec);
		}
	}
}
