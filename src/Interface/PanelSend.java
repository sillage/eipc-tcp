package Interface;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import common.TCPManager;

import myLib.BoundedTextField;

import kernel.Traitement;
import kernel.TCB.algorithme_windows;

import java.awt.event.ActionListener;
import java.nio.charset.CharsetEncoder;

/**
 * Classe pour le panneau d'émission de l'interface de l'application.
 * 
 * @author Rémi TANIWAKI
 * 
 */
@SuppressWarnings("serial")
public class PanelSend extends JPanel implements ActionListener {
	JButton EnvoiBtnAccept, RemplirBtnAccept;
	JCheckBox EnvoiURG, EnvoiACK, EnvoiPSH, EnvoiSYN, EnvoiRST, EnvoiFIN;
	JLabel EnvoiTitle, EnvoiLabNum, EnvoiLabAqu, EnvoiLabOff, EnvoiLabFen, EnvoiLabChk, EnvoiLabPtr, EnvoiLabData;
	BoundedTextField EnvoiNum, EnvoiAqu, EnvoiOff, EnvoiFen, EnvoiChk, EnvoiPtr, EnvoiData;
	Traitement traitement = Traitement.getInstance();
	
	/**
	 * Constructeur
	 */ 	
	public PanelSend() {
		setLayout(null);
		setPreferredSize(new Dimension(300, 300));
		setBounds(5, 5, 305, 300);
	    setBorder(new TitledBorder("Datagramme en émission"));
	    
	    	// Numéro de séquence
	        EnvoiLabNum = new JLabel("Numéro de séquence (32bits)");
	        EnvoiLabNum.setFont(new Font("arial", 0, 12));
	        EnvoiNum = new BoundedTextField("", 32);
	        EnvoiLabNum.setBounds(10, 15, 180, 20);
	        EnvoiNum.setBounds(10, 35, 285, 20);
	        EnvoiNum.setText("");
	        add(EnvoiLabNum);
	        add(EnvoiNum);
	        
	        // Numéro d'acquitement
	        EnvoiLabAqu = new JLabel("Numéro d'acquittement (32bits)");
	        EnvoiLabAqu.setFont(new Font("arial", 0, 12));
	        EnvoiAqu = new BoundedTextField("", 32);
	        EnvoiLabAqu.setBounds(10, 55, 180, 20);
	        EnvoiAqu.setBounds(10, 75, 285, 20);
	        add(EnvoiLabAqu);
	        add(EnvoiAqu);
	        
	        EnvoiLabOff = new JLabel("Offset (4bits)");
	        EnvoiLabOff.setFont(new Font("arial", 0, 12));
	        EnvoiOff = new BoundedTextField("", 4);
	        EnvoiLabOff.setBounds(10, 95, 125, 20);
	        EnvoiOff.setBounds(10, 115, 125, 20);
	        add(EnvoiLabOff);
	        add(EnvoiOff);
	        
	        // URG
	        EnvoiURG = new JCheckBox("URG");
	        EnvoiURG.setFont(new Font("arial", 0, 12));
	        EnvoiURG.setBounds(138, 103, 52, 20);
	        add(EnvoiURG);
	    
	        // ACK
	        EnvoiACK = new JCheckBox("ACK");
	        EnvoiACK.setFont(new Font("arial", 0, 12));
	        EnvoiACK.setBounds(193, 103, 52, 20);
	        add(EnvoiACK);
	        
	        // PSH
	        EnvoiPSH = new JCheckBox("PSH");
	        EnvoiPSH.setFont(new Font("arial", 0, 12));
	        EnvoiPSH.setBounds(248, 103, 52, 20);
	        add(EnvoiPSH);
	       
	        // RST
	        EnvoiRST = new JCheckBox("RST");
	        EnvoiRST.setFont(new Font("arial", 0, 12));
	        EnvoiRST.setBounds(138, 123, 52, 20);
	        add(EnvoiRST);

	        // SYN
	        EnvoiSYN = new JCheckBox("SYN");
	        EnvoiSYN.setFont(new Font("arial", 0, 12));
	        EnvoiSYN.setBounds(193, 123, 52, 20);
	        add(EnvoiSYN);

	        // FIN
	        EnvoiFIN = new JCheckBox("FIN");
	        EnvoiFIN.setFont(new Font("arial", 0, 12));
	        EnvoiFIN.setBounds(248, 123, 52, 20);
	        add(EnvoiFIN);

	        // Fenêtre
	        EnvoiLabFen = new JLabel("Fenêtre (16bits)");
	        EnvoiLabFen.setFont(new Font("arial", 0, 12));
	        EnvoiFen = new BoundedTextField("", 16);
	        EnvoiLabFen.setBounds(10, 135, 285, 20);
	        EnvoiFen.setBounds(10, 155, 285, 20);
	        add(EnvoiLabFen);
	        add(EnvoiFen);
	        
	        // Checksum
	        EnvoiLabChk = new JLabel("Checksum (16bits)");
	        EnvoiLabChk.setFont(new Font("arial", 0, 12));
	        EnvoiChk = new BoundedTextField("", 16);
	        EnvoiLabChk.setBounds(10, 175, 140, 20);
	        EnvoiChk.setBounds(10, 195, 140, 20);
	        add(EnvoiLabChk);
	        add(EnvoiChk);
	        
	        EnvoiLabPtr = new JLabel("Pointeur urgent (16bits)");
	        EnvoiLabPtr.setFont(new Font("arial", 0, 12));
	        EnvoiPtr = new BoundedTextField("", 16);
	        add(EnvoiLabPtr);
	        add(EnvoiPtr);
	        EnvoiLabPtr.setBounds(155, 175, 140, 20);
	        EnvoiPtr.setBounds(155, 195, 140, 20);
	        
	        // Data
	        EnvoiLabData = new JLabel("Données");
	        EnvoiLabData.setFont(new Font("arial", 0, 12));
	        EnvoiData = new BoundedTextField("", 100);
	        EnvoiLabData.setBounds(10, 215, 285, 20);
	        EnvoiData.setBounds(10, 235, 285, 20);
	        add(EnvoiLabData);
	        add(EnvoiData);
	        
	        EnvoiBtnAccept = new JButton("Accepter");
	        EnvoiBtnAccept.addActionListener(this);
	        add(EnvoiBtnAccept);
	        EnvoiBtnAccept.setBounds(10, 265, 100, 20);
	        
	        //Remplir
	        RemplirBtnAccept = new JButton("Remplir");
	        RemplirBtnAccept.addActionListener(this);
	        add(RemplirBtnAccept);
	        RemplirBtnAccept.setBounds(195, 265, 100, 20);
	}

	public void actionPerformed(ActionEvent e) {
		/*if (traitement == null) {
			traitement = Traitement.getInstance();
		}*/
		if (traitement.getState() == 11 || traitement.getState() == 0) {
			GUIManager gui = GUIManager.getInstance();
			gui.getPanelConsole().insertLine("Can't send in this state", "Red");
		}
		else
		if (e.getSource() == RemplirBtnAccept && traitement != null) {
			if (EnvoiNum.getText().equals("")) {
				EnvoiNum.setText(String.valueOf(traitement.getTCB().get_sndNXT()));
			}
			if (EnvoiAqu.getText().equals("")) {
				EnvoiAqu.setText(String.valueOf(traitement.getTCB().get_rcvNXT()));
			}
			if (EnvoiOff.getText().equals("")) {
				EnvoiOff.setText("5");
			}
			if (EnvoiFen.getText().equals("")) {
				EnvoiFen.setText(String.valueOf(traitement.getTCB().calcWindow(traitement.getTCB(), algorithme_windows.simple)));
			}
			if (EnvoiPtr.getText().equals("")) {
				EnvoiPtr.setText("0"); // A vérifier
			}
			if (EnvoiChk.getText().equals("")) {
				common.Segment seg = new common.Segment();
				seg = traitement.getTCB().send(EnvoiURG.isSelected(),
							EnvoiACK.isSelected(),
							EnvoiPSH.isSelected(),
							EnvoiRST.isSelected(),
							EnvoiSYN.isSelected(),
							EnvoiFIN.isSelected(),
							EnvoiData.getText(),
							Long.valueOf(EnvoiNum.getText()).longValue(), //SEQ
							Long.valueOf(EnvoiAqu.getText()).longValue(), //ACK
							Long.valueOf(EnvoiOff.getText()).longValue(),
							Long.valueOf(EnvoiFen.getText()).longValue(),
							Long.valueOf(EnvoiPtr.getText()).longValue()); //PTR
				EnvoiChk.setText(String.valueOf(seg.get_checksum(seg)));
			}
		}
		if (e.getSource() == EnvoiBtnAccept) {
			TCPManager tcp = TCPManager.getInstance();
			byte b[];
			long l = 0;
			common.Segment seg = new common.Segment();
			if (EnvoiNum.getText().equals(""))
			{
			 l = 0;
			}
			else
			{
				try {
					l = Long.valueOf(EnvoiNum.getText()).longValue();
				}
				catch (Exception ex) {
					l = 0;
				}
			}
			System.out.println(l);
			seg.set_sequence(l);
			if (EnvoiAqu.getText().equals(""))
			{
			 l = 0;
			}
			else
			{
				try {
					l = Long.valueOf(EnvoiAqu.getText()).longValue();
				}
				catch (Exception ex) {
					l = 0;
				}} 
			 seg.set_ack(l);
			 if (EnvoiOff.getText().equals(""))
				{
				 l = 0;
				}
				else
				{
					try {
						l = Long.valueOf(EnvoiOff.getText()).longValue();
						System.out.println(">>>>> Long -- -- " + l);
					}
					catch (Exception ex) {
						System.out.println("exception !!!\n\n\n");
						l = 0;
					}
				}
			seg.set_offset(l);
			seg.set_URG(EnvoiURG.isSelected());
			seg.set_ACK(EnvoiACK.isSelected());
			seg.set_PSH(EnvoiPSH.isSelected());
			seg.set_RST(EnvoiRST.isSelected());
			seg.set_SYN(EnvoiSYN.isSelected());
			seg.set_FIN(EnvoiFIN.isSelected());
			if (EnvoiFen.getText().equals(""))
			{
				l = 0;
			}
			else
			{
				try {
					l = Long.valueOf(EnvoiFen.getText()).longValue();
				}
				catch (Exception ex) {
					l = 0;
				}	} 
			 seg.set_window(l);
			 if (EnvoiChk.getText().equals(""))
				{
					l = 0;
				}
				else
				{
					try {
						l = Long.valueOf(EnvoiChk.getText()).longValue();
					}
					catch (Exception ex) {
						l = 0;
					}
				}
			 seg.set_checksum(l);
			 if (EnvoiPtr.getText().equals(""))
				{
				 l = 0;
				}
				else
				{
					try {
						l = Long.valueOf(EnvoiPtr.getText()).longValue();
					}
					catch (Exception ex) {
						l = 0;
					}}
			seg.set_pointeur_urgent(l);
			b = EnvoiData.getText().getBytes();
			System.out.println("Data receive : " + EnvoiData.getText());
			seg.set_data(b);
			if (seg.get_seq_number() != 0) {
				seg.set_sequence(seg.get_seq_number()+ seg.get_data().length + 1);
				traitement.getTCB().set_sndNXT(traitement.getTCB().get_sndNXT() + seg.get_data().length + 1);
			}

			//System.out.println("Dump -- ---" + seg.dump());
			// Envoie de texte
			if (tcp.getisClient()) {
				//System.out.println("Client Send :");
				tcp.getClient().traitement.makeTreatmentSend(seg);
				//tcp.getClient().sendMsg(seg.dump());
			}
			if (tcp.getisServer()) {
				//System.out.println("Serveur Send :");
				//seg.display_text();
				tcp.getServeur().traitement.makeTreatmentSend(seg);
				//tcp.getServeur().sendMsg(seg.dump());
			}
			EnvoiACK.setSelected(false);
			EnvoiPSH.setSelected(false);
			EnvoiRST.setSelected(false);
			EnvoiSYN.setSelected(false);
			EnvoiFIN.setSelected(false);
			EnvoiData.setText("");
			EnvoiNum.setText(""); //SEQ
			EnvoiAqu.setText(""); //ACK
			EnvoiOff.setText(""); //Offset
			EnvoiFen.setText(""); //Window
			EnvoiPtr.setText(""); //PTR
			EnvoiChk.setText("");
		}
}

	public BoundedTextField getEnvoiData() {
		return EnvoiData;
	}


}
