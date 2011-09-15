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
	 * Enumeration des algorithmes de contr�le de flux
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
	 * Cr�� l'instance Traitement si elle n'existe pas d�ja 
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
	 * Constructeur par d�faut
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
	 * @param segment = Segment � traiter
	 * @return = Le segment bien rempli
	 */
	public Segment makeTreatmentSend(Segment segment) {
		automate.evaluateStateSend(segment);
		//segment.set_checksum(calcCRC(segment));
		state = 1;
		return segment;
	}
	
	/**
	 * Appel tout les traitements pour la r�ception
	 * 
	 * @param segment = Segment � traiter
	 */
	public void makeTreatmentReceive(Segment segment) {
		automate.EvaluateStateRcv(segment);
		state = 2;
	}
	
	/**
	 * Met � jour un �tat dans l'automate
	 * Les �tats : 
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
	 * @param state_active Etat � activer
	 */
	public void update_states(String state) {
		
		if (state == "CLOSED")
			automate.set_state(0);
		if (state == "LISTEN")
			automate.set_state(1);
		if (state == "SYN_RCVD")
			automate.set_state(2);
		if (state == "SYN_SENT")
			automate.set_state(3);
		if (state == "ESTAB")
			automate.set_state(4);
		if (state == "FIN_WAIT_1")
			automate.set_state(5);
		if (state == "CLOSE_WAIT")
			automate.set_state(6);
		if (state == "FIN_WAIT_2")
			automate.set_state(7);
		if (state == "CLOSING")
			automate.set_state(8);
		if (state == "LAST_ACK")
			automate.set_state(9);
		if (state == "TIME_WAIT")
			automate.set_state(10);
		if (state == "CLOSED_INIT")
			automate.set_state(11);
	} 
	
}
