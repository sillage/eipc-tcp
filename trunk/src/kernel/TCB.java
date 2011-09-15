package kernel;

import Interface.GUIManager;
import kernel.SegmentVector;
import common.Segment;


public class TCB {

	private long	_sndUNA			= -1; // "unacknowledge" - envoie sans accusé de reception
	private long	_sndNXT			= -1; // "next" - envoie du suivant
	private long	_sndUP			= 0; // "urgent pointer" - pointeur de donnees urgentes
	private long	_ISS			= -1; // "initial send sequence" - premier numero de sequence du message
	private int		_sndWND			= 0; // "window" - envoie de la fenetre
	private int		_sndWL1			= -1; // "window last 1" - dernier numero de sequence de segment envoye
	private int		_sndWL2			= -1; // "window last 2" - dernier accuse de reception

	/* Variable de sequence de reception */
	private long	_rcvNXT			= -1; // "next" - reception du suivant
	private long	_rcvLST			= -1; // 
	private long	_IRS			= -1; // "initial receive sequence" - premier numero de sequence en reception
	private int		_rcvWND			= 45; // "window" - reception de la fenetre
	private int		_rcvUP			= -1; // "urgent ponter" - pointeur de donnees urgentes
	
	/* Variable du segment courant */
	private boolean				_URG			= false;
	private boolean				_ACK			= false;
	private boolean				_PSH			= false;
	private boolean				_RST			= false;
	private boolean				_SYN			= false;
	private boolean				_FIN			= false;
	
	private String _data;
	public int portClient;
	public int portServer;
	public int type;
	private SegmentVector		_rsdVector;
	
	
	/**
	 * Segment courant
	 */
	private Segment Segment_courant;
	
	/**
	 * Le buffer utilisé pour la réception
	 */
	private byte[]	_rcvBuffer;
	
	/**
	 * Le buffer utilisé pour l'emission
	 */
	private byte[] _sndBuffer;
	
	/**
	 * Constante definissant la longueur du buffer
	 */
	private int	buffer_lg;

	

	
	public TCB() {
		_ISS = Math.round(Math.random()* Math.pow(2, 32)) & 0x00000000FFFFFFFF;
		_sndNXT = _ISS;
		_rcvNXT = 0;	
		buffer_lg = 1000;
		portClient = 0;
		portServer = 0;
	}
	
	public void initSegmentVector() {
		_rsdVector = new SegmentVector(this);
	}
	
	public synchronized void addSegment(Segment seg) {
		_rsdVector.addSegment(seg);
	}
	
	public synchronized void delSegment(Segment seg) {
		_rsdVector.deleteSegment(seg);
	}
	
	public synchronized void delAllSegment() {
		_rsdVector.deleteAllSegment();
	}
	
	public synchronized boolean emptyStack() {
		return _rsdVector.emptyStack();
	}
	
	public synchronized Segment send(boolean urg, boolean ack, boolean psh, 
			boolean rst, boolean syn, boolean fin, String data, long seq_, long ack_) {
		_URG = urg;
		_ACK = ack;
		_PSH = psh;
		_RST = rst;
		_SYN = syn;
		_FIN = fin;
		_data = data;
		
		Segment seg;
		seg = new Segment();
		seg.set_ACK(_ACK);
		seg.set_URG(_URG);
		seg.set_PSH(_PSH);
		seg.set_RST(_RST);
		seg.set_SYN(_SYN);
		seg.set_FIN(_FIN);
		seg.set_data(_data.getBytes());
		seg.set_ack(ack_);
		seg.set_sequence(seq_);
		seg.set_offset(5);
		
		if (this.type == 0) {
			seg.set_port_dest(portClient);
			seg.set_portsource(portServer);
		}
		else {
			seg.set_port_dest(portServer);
			seg.set_portsource(portClient);
		}
		seg.set_window(calcWindow(this, algorithme_windows.simple));
		seg.set_checksum(seg.get_checksum(seg));
		//send(seg);
		return seg;
	}

	public synchronized Segment send(boolean urg, boolean ack, boolean psh, 
			boolean rst, boolean syn, boolean fin, String data,
			long seq_,
			long ack_,
			long off,
			long win,
			long ptr) {
		_URG = urg;
		_ACK = ack;
		_PSH = psh;
		_RST = rst;
		_SYN = syn;
		_FIN = fin;
		_data = data;
		
		Segment seg;
		seg = new Segment();
		seg.set_ACK(_ACK);
		seg.set_URG(_URG);
		seg.set_PSH(_PSH);
		seg.set_RST(_RST);
		seg.set_SYN(_SYN);
		seg.set_FIN(_FIN);
		seg.set_data(_data.getBytes());
		seg.set_ack(ack_);
		seg.set_sequence(seq_);
		seg.set_offset(off);
		
		if (this.type == 0) {
			seg.set_port_dest(portClient);
			seg.set_portsource(portServer);
		}
		else {
			seg.set_port_dest(portServer);
			seg.set_portsource(portClient);
		}
		seg.set_window(win);
		seg.set_pointeur_urgent(ptr);
		return seg;
	}
	
	public long get_sndUNA() {
		return _sndUNA;
	}


	public void set_sndUNA(long _snduna) {
		_sndUNA = _snduna;
	}


	public long get_sndNXT() {
		return _sndNXT;
	}


	public void set_sndNXT(long _sndnxt) {
		_sndNXT = _sndnxt;
	}


	public long get_sndUP() {
		return _sndUP;
	}


	public void set_sndUP(long _sndup) {
		_sndUP = _sndup;
	}


	public long get_ISS() {
		return _ISS;
	}


	public void set_ISS(long _iss) {
		_ISS = _iss;
	}


	public int get_sndWND() {
		return _sndWND;
	}


	public void set_sndWND(int _sndwnd) {
		_sndWND = _sndwnd;
	}


	public int get_sndWL1() {
		return _sndWL1;
	}


	public void set_sndWL1(int _sndwl1) {
		_sndWL1 = _sndwl1;
	}


	public int get_sndWL2() {
		return _sndWL2;
	}


	public void set_sndWL2(int _sndwl2) {
		_sndWL2 = _sndwl2;
	}


	public long get_rcvNXT() {
		return _rcvNXT;
	}


	public void set_rcvNXT(long _rcvnxt) {
		_rcvNXT = _rcvnxt;
	}


	public long get_rcvLST() {
		return _rcvLST;
	}


	public void set_rcvLST(long _rcvlst) {
		_rcvLST = _rcvlst;
	}


	public long get_IRS() {
		return _IRS;
	}


	public void set_IRS(long _irs) {
		_IRS = _irs;
	}


	public int get_rcvWND() {
		return _rcvWND;
	}


	public void set_rcvWND(int _rcvwnd) {
		_rcvWND = _rcvwnd;
	}


	public int get_rcvUP() {
		return _rcvUP;
	}


	public void set_rcvUP(int _rcvup) {
		_rcvUP = _rcvup;
	}


	public boolean is_URG() {
		return _URG;
	}


	public void set_URG(boolean _urg) {
		_URG = _urg;
	}


	public boolean is_ACK() {
		return _ACK;
	}


	public void set_ACK(boolean _ack) {
		_ACK = _ack;
	}


	public boolean is_PSH() {
		return _PSH;
	}


	public void set_PSH(boolean _psh) {
		_PSH = _psh;
	}


	public boolean is_RST() {
		return _RST;
	}


	public void set_RST(boolean _rst) {
		_RST = _rst;
	}


	public boolean is_SYN() {
		return _SYN;
	}


	public void set_SYN(boolean _syn) {
		_SYN = _syn;
	}


	public boolean is_FIN() {
		return _FIN;
	}


	public void set_FIN(boolean _fin) {
		_FIN = _fin;
	}

	/**
	 * Effacer le buffer de reception
	 */
	/*
	private void Vider_rcvBuffer() {
		int i;
		
		for (i = 0; i < buffer_lg; i++)
		{
			_rcvBuffer[i] = 0x00;
		}	
	}
	*/
	
	/**
	 * Effacer le buffer d'emission
	 */
	/*
	private void Vider_sndBuffer() {
		int i;
		
		for (i = 0; i < buffer_lg; i++)
		{
			_sndBuffer[i] = 0x00;
		}
	}*/
	
	/**
	 * Initialisation du buffer de réception
	 */
	/*
	private void Initialisation_rcvBuffer() {
		_rcvBuffer = new byte[buffer_lg];
		Vider_rcvBuffer();
	}
	*/
	
	/**
	 * Initialisation du buffer d'emission
	 */
	/*
	private void Initialisation_sndBuffer() {
		_sndBuffer = new byte[buffer_lg];
		Vider_sndBuffer();
	}
	*/
	
	/**
	 * Calcule l'espace libre du buffer de réception en octet
	 * @return l'espace libre du buffer de réception
	 */
	public long Combien_Espace_Libre_Rcv() {
		return (getBuffer_lg() - get_IRS()) * get_rcvWND();
	}
	
	/**
	 * Calcule l'espace libre du buffer d'emission en octet
	 * @return l'espace libre du buffer d'emission
	 */
	public long Combien_Espace_Libre_Snd() {
		return (getBuffer_lg() - get_ISS()) * get_sndWND();
	}
	
	/**
	 * Indique si le buffer de réception est plein
	 * @return un booleen
	 */
	public boolean isBufferRcvPlein() {
		return Combien_Espace_Libre_Rcv() == 0;
	}
	
	/**
	 * Indique si le buffer d'emission est plein
	 * @return un booleen
	 */
	public boolean isBufferSndPlein() {
		return Combien_Espace_Libre_Snd() == 0;
	}
	
	/**
	 * Procedure qui remplit le buffer de réception des données reçues dans les segments
	 */
	public void Remplir_Buffer_Rcv(Segment seg) {
		int i;
		int j;
		int debut = (int) (get_rcvNXT() - get_IRS()) * get_rcvWND();
		
		j = 0;
		for (i = debut; i < (debut + get_rcvWND()); i++) {
			_rcvBuffer[i] = seg.get_data()[j];
			j++;
		}
	}
	
	/**
	 * Procedure qui remplit le buffer d'emission des données reçues dans les segments
	 */
	public void Remplir_Buffer_Snd(Segment seg) {
		int i;
		int j;
		int debut = (int)(get_sndNXT() - get_ISS()) * get_sndWND();
		
		j = 0;
		for (i = debut; i < (debut + get_sndWND()); i++) {
			_sndBuffer[i] = seg.get_data()[j];
			j++;
		}
	}


	public Segment getSegment_courant() {
		return Segment_courant;
	}


	public void setSegment_courant(Segment segment_courant) {
		Segment_courant = segment_courant;
	}


	public byte[] get_rcvBuffer() {
		return _rcvBuffer;
	}


	public void set_rcvBuffer(byte[] buffer) {
		_rcvBuffer = buffer;
	}


	public byte[] get_sndBuffer() {
		return _sndBuffer;
	}


	public void set_sndBuffer(byte[] buffer) {
		_sndBuffer = buffer;
	}


	public int getBuffer_lg() {
		return buffer_lg;
	}


	public void setBuffer_lg(int buffer_lg) {
		this.buffer_lg = buffer_lg;
	}
	
	
/**
 * Met à jour les flags du segment en paramètre avant de passer par l'automate 
 * @param SegIn
 * @return 
 */	
	public Segment updateSegment(Segment SegIn) {

		//retourne un segment updaté (SegOut par exemple) 
		return SegIn;
	}
	
	public enum algorithme_windows {
		simple, nagle
	}
	
	/**
	 * Calcul la fenêtre
	 * @params t represente le TCB courant et a le choix de l'agorithme de gestion de la fenêtre
	 * 
	 * @return la fenêtre
	 */
	public long calcWindow(TCB t, algorithme_windows a) {
		long res = 0;
		switch (a) {
		case simple:
			if (true) // FIXME
			{
				res = t.Combien_Espace_Libre_Rcv();
			}
			else {
				res = t.Combien_Espace_Libre_Snd();
			}
			break;
		case nagle:
			break;
		default:
			System.err.println("Unknown algorithme_windows");
		}
		return res;
	}
}
