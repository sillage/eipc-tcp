package Interface;

/**
 * Classe en design pattern Singleton pour la gestion des JPanels 
 * utilisée pour le dialogue entre les JPanels.
 * @author Rémi TANIWAKI
 *
 */
public class GUIManager {
	private static GUIManager instance;
	TopMenu top_menu;
	PanelSend panel_send;
	PanelConnection panel_connection;
	PanelAutomate panel_automate;
	PanelReceive panel_receive;
	PanelConsole panel_console;

	/**
	 * Créé l'instance GUIManager si elle n'existe pas déja 
	 * ou retourne un pointeur sur l'instance si elle existe.
	 * @return l'intance GUIManager
	 */
	public static GUIManager getInstance() {
        if (null == instance) { // Premier appel
            instance = new GUIManager();
        }
        return instance;
    }
	
	/**
	 * Set le TopMenu dans l'instance singleton
	 * @param topmenu l'objet menu
	 */
	public void setTopMenu(TopMenu topmenu) {
		top_menu = topmenu;
	}
	
	/**
	 * Retourne l'objet menu de l'instance
	 * @return l'objet menu type TopMenu
	 */
	public TopMenu getTopMenu() {
		return top_menu;
	}
	
	/**
	 * Set le PanelSend dans l'instance singleton
	 * @param panelsend l'objet PanelSend
	 */
	public void setPanelSend(PanelSend panelsend) {
		panel_send = panelsend;
	}
	
	/**
	 * Retourne le PanelSend de l'instance singleton
	 * @return l'objet PanelSend
	 */
	public PanelSend getPanelSend() {
		return panel_send;
	}

	/**
	 * Set le PanelReceive dans l'instance singleton
	 * @param panelreceive l'objet PanelReceive
	 */
	public void setPanelReceive(PanelReceive panelreceive) {
		panel_receive = panelreceive;
	}
	
	/**
	 * Retourne le PanelReceive de l'instance singleton
	 * @return l'objet PanelReceive
	 */
	public PanelReceive getPanelReceive() {
		return panel_receive;
	}
	
	/**
	 * Set le PanelAutomate dans l'instance singleton
	 * @param panelautomate l'objet PanelAutomate
	 */
	public void setPanelAutomate(PanelAutomate panelautomate) {
		panel_automate = panelautomate;
	}
	
	/**
	 * Retourne le PanelAutomate de l'instance singleton
	 * @return l'objet PanelAutomate
	 */
	public PanelAutomate getPanelAutomate() {
		return panel_automate;
	}

	/**
	 * Set le PanelConsole dans l'instance singleton
	 * @param panelconsole l'objet PanelConsole
	 */
	public void setPanelConsole(PanelConsole panelconsole) {
		panel_console = panelconsole;
	}
	
	/**
	 * Retourne le PanelConsole de l'instance singleton
	 * @return l'objet PanelConsole
	 */
	public PanelConsole getPanelConsole() {
		return panel_console;
	}
	
	/**
	 * Set le PanelConnection dans l'instance singleton
	 * @param panelConnection l'objet PanelConnection
	 */
	public void setPanelConnection(PanelConnection panelConnection) {
		panel_connection = panelConnection;
	}
	
	/**
	 * Retourne le PanelConnection de l'instance singleton
	 * @return l'objet PanelConnection
	 */
	public PanelConnection getPanelConnection() {
		return panel_connection;
	}
	
	/**
	 * Constructeur par défaut
	 */
	private GUIManager() {
	}
}
