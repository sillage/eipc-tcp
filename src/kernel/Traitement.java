package kernel;

import common.*;
import java.net.Socket;
import java.net.ServerSocket;

/**
 * Classe de traitement et de calcul du segment
 * 
 * @author Titi
 *
 */
public class Traitement {
	private static Traitement instance;
	public Automaton automate;
	private int state; //1 = Emission - 2 = Reception 
	
	/**
	 * Enumeration des algorithmes de contrôle de flux
	 */
	
	public void setStepByStep(boolean boo) {
		automate.stepbystep = boo;
	}
	
	public boolean getStepByStep() {
		return automate.stepbystep;
	}
	
	public void setAutoAck(boolean boo) {
		automate.autoAck = boo;
	}
	
	public boolean getAutoAck() {
		return automate.autoAck;
	}
	
	public int getStateAutomate() {
		return automate.getState();
	}
	
	public void giveInfo(Socket sock, int portServ, String ip) {
		automate.setSock(sock);
		automate.setPort(portServ);
		automate.setIp(ip);
	}
	
	public void giveInfo(ServerSocket sock, int portServ, String ip) {
		//automate.setServerSock(sock);
		automate.setPort(portServ);
		automate.setIp(ip);
	}
	
	public void goGoSend(int source) {
		automate.goGoSend(1);
	}
	
	/**
	 * Créé l'instance Traitement si elle n'existe pas déja 
	 * ou retourne un pointeur sur l'instance si elle existe.
	 * @return l'intance Traitement
	 */
	public static Traitement getInstance() {
        if (null == instance) { // Premier appel
            instance = new Traitement();
        }
        return instance;
    }
	
	/**
	 * Constructeur par défaut
	 */
	private Traitement() {
		automate = new Automaton();
		this.setStepByStep(false);
	}	
	
	public TCB getTCB() {
		return automate.getTcb();
	}

	public int getState() {
		return state;
	}
	
	/**
	 * Appel tous les traitements pour l'ouverture
	 *  
	 */
	public void makeTreatmentOpen(int type) {
		automate.evaluateStateOpen(type);
		//segment.set_checksum(calcCRC(segment));
		//segment.set_window(calcWindow(automate.getTcb(), algorithme_windows.simple));
	}
	
	public void makeTreatmentClose() {
		automate.evaluateStateClose();
	}
	
	/**
	 * Appel tous les traitements pour l'envoi
	 *  
	 * @param segment = Segment à traiter
	 * @return = Le segment bien rempli
	 */
	public Segment makeTreatmentSend(Segment segment) {
		automate.evaluateStateSend(segment);
		//segment.set_checksum(calcCRC(segment));
		state = 1;
		return segment;
	}
	
	/**
	 * Appel tout les traitements pour la réception
	 * 
	 * @param segment = Segment à traiter
	 */
	public void makeTreatmentReceive(Segment segment) {
		automate.EvaluateStateRcv(segment);
		state = 2;
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
	public void update_states(String state) {
		
		if ("CLOSED".equals(state))
			automate.set_state(0);
		if ("LISTEN".equals(state))
			automate.set_state(1);
		if ("SYN_RCVD".equals(state))
			automate.set_state(2);
		if ("SYN_SENT".equals(state))
			automate.set_state(3);
		if ("ESTAB".equals(state))
			automate.set_state(4);
		if ("FIN_WAIT_1".equals(state))
			automate.set_state(5);
		if ("CLOSE_WAIT".equals(state))
			automate.set_state(6);
		if ("FIN_WAIT_2".equals(state))
			automate.set_state(7);
		if ("CLOSING".equals(state))
			automate.set_state(8);
		if ("LAST_ACK".equals(state))
			automate.set_state(9);
		if ("TIME_WAIT".equals(state))
			automate.set_state(10);
		if ("CLOSED_INIT".equals(state))
			automate.set_state(11);
	} 
	
}
