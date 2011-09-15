package common;

public class TCPManager {
	private boolean isServer = false;
	private boolean isClient = false;
	private static TCPManager instance;
	private TcpClient client;
	private TcpServer serveur;
	
	/**
	 * Constructeur par défaut
	 */
	private TCPManager() {
	}

	/**
	 * Créé l'instance TCPManager si elle n'existe pas déja 
	 * ou retourne un pointeur sur l'instance si elle existe.
	 * @return l'intance TCPManager
	 */
	public static TCPManager getInstance() {
        if (null == instance) {
            instance = new TCPManager();
        }
        return instance;
    }
	
	public TcpClient getClient() {
		return client;
	}

	public void setClient(TcpClient client) {
		this.client = client;
	}

	public TcpServer getServeur() {
		return serveur;
	}

	public void setServeur(TcpServer serveur) {
		this.serveur = serveur;
	}

	public boolean getisServer() {
		return isServer;
	}

	public void setisServer(boolean isServer) {
		this.isServer = isServer;
	}

	public boolean getisClient() {
		return isClient;
	}

	public void setisClient(boolean isClient) {
		this.isClient = isClient;
	}
}
