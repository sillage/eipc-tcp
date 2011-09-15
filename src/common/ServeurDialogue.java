package common;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import Interface.GUIManager;
import kernel.*;
/**
 * Class gérant la conversation entre le serveur et un client
 * @author Bertrand
 *
 */
class ServeurDialogue implements Runnable {
	// Socket client
	Socket sock;
	// Flux d'écriture depuis le client
	BufferedReader fromCLi;
	// Flux d'écriture vers le client
	PrintStream toCli;
	// Interaction GUI
	GUIManager gui = GUIManager.getInstance();
	Traitement traitement;
	
	/**
	 * Constructeur
	 * @param sock, socket client
	 */
	public ServeurDialogue(Socket sock) {
		traitement = Traitement.getInstance();
		this.sock = sock;
		try {
			this.fromCLi = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
			this.toCli = new PrintStream(this.sock.getOutputStream());
		} catch (IOException e) {
			try { sock.close(); } catch (IOException ee) {}
		}
		new Thread(this).start();
	}

	/**
	 * Lance la conversation
	 */
	public void run() {
		try {
			String line;
			String data;
				while (true) {
						if ((line = fromCLi.readLine()) != null) {
								String [] result = line.split(":@:");
								line = result[0];
								if (result.length > 1)
									data = result[1];
								else
									data ="";
								if (traitement.getStateAutomate() != 10)
									gui.getPanelConsole().insertLine("Received Message: " + line, "Normal EM");
								Segment seg = new Segment(line);
								//System.out.println("Serveur Reception");
								seg.display_text();
								long tmp = (seg.get_seq_number());
								gui.getPanelReceive().setSeqNumber(String.valueOf(tmp));
								tmp = seg.get_ack_number();
								gui.getPanelReceive().setAckNumber(String.valueOf(tmp));
								tmp = seg.get_offset();
								gui.getPanelReceive().setOffset(String.valueOf(tmp));
								tmp = seg.get_window();
								gui.getPanelReceive().setFenetre(String.valueOf(tmp));
								tmp = seg.get_checksum();
								gui.getPanelReceive().setChecksum(String.valueOf(tmp));
								tmp = seg.get_pointeur_urgent();
								gui.getPanelReceive().setPointUrg(String.valueOf(tmp));
								/*byte[] b = seg.get_data();
								 if (b != null)
								 {
								 String so = new String(b);
								 gui.getPanelReceive().setData(so);
								 }*/
								gui.getPanelReceive().setData(data);
								if (seg.get_URG())
									gui.getPanelReceive().setURG(true);
								else
									gui.getPanelReceive().setURG(false);
								if (seg.get_ACK())
									gui.getPanelReceive().setACK(true);
								else
									gui.getPanelReceive().setACK(false);
								if (seg.get_PSH())
									gui.getPanelReceive().setPSH(true);
								else
									gui.getPanelReceive().setPSH(false);
								if (seg.get_SYN())
									gui.getPanelReceive().setSYN(true);
								else
									gui.getPanelReceive().setSYN(false);
								if (seg.get_RST())
									gui.getPanelReceive().setRST(true);
								else
									gui.getPanelReceive().setRST(false);
								if (seg.get_FIN())
									gui.getPanelReceive().setFIN(true);
								else
									gui.getPanelReceive().setFIN(false);
								gui.getPanelReceive().segment = seg;
								traitement.makeTreatmentReceive(seg);
						}
				}
		} catch (IOException e) {
			gui.getPanelConsole().insertLine("Error: " + e.getMessage(), "Red");
		}
	}

	/**
	 * Traite les flux reçus
	 * @param recu, msg reçu 
	 */
	public void traitementReception(String recu){
		
	}

	/**
	 * Envoie un message au client
	 * @param msg, message à envoyer
	 */
	public void envoyer(String msg) {
		if (traitement.getStateAutomate() != 0 && traitement.getStateAutomate() != 10)
			gui.getPanelConsole().insertLine("Sending message:" + msg, "Normal EM");
			toCli.println(msg + ":@:" + gui.getPanelSend().getEnvoiData().getText());
	}

	/**
	 * Déconnecte le client
	 *
	 */
	public void stop() {
		try {
			gui.getPanelConsole().insertLine("Client closed", "White Bold");
			sock.close();
		} catch (IOException e) {
			gui.getPanelConsole().insertLine("Error closing client: " + e.getMessage(), "Red");
		}
	}
}

