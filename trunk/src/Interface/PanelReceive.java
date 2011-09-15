package Interface;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import myLib.BoundedTextField;
import java.awt.event.ActionListener;

/**
 * Classe pour le panneau de réception de l'interface de l'application.
 * @author Rémi TANIWAKI
 *
 */
@SuppressWarnings("serial")
public class PanelReceive extends JPanel implements ActionListener {
	JButton CheckBtn;
	JCheckBox RecepURG, RecepACK, RecepPSH, RecepSYN, RecepRST, RecepFIN;
	JLabel RecepTitle, RecepLabNum, RecepLabAqu, RecepLabOff, RecepLabFen, RecepLabChk, RecepLabPtr, RecepLabData;
	BoundedTextField RecepNum, RecepAqu, RecepOff, RecepFen, RecepChk, RecepPtr, RecepData;
	public common.Segment segment;  
	
	/**
	 * Constructeur
	 */ 	
	public PanelReceive() {
		setLayout(null);
		setPreferredSize(new Dimension(300, 300));
		setBounds(675, 5, 305, 300);
	    setBorder(new TitledBorder("Datagramme en Réception"));
	    
    	// Numéro de séquence
        RecepLabNum = new JLabel("Numéro de séquence (32bits)");
        RecepLabNum.setFont(new Font("arial", 0, 12));
        RecepNum = new BoundedTextField("", 32);
        RecepLabNum.setBounds(10, 15, 180, 20);
        RecepNum.setBounds(10, 35, 285, 20);
        add(RecepLabNum);
        add(RecepNum);
        
        // Numéro d'acquitement
        RecepLabAqu = new JLabel("Numéro d'acquittement (32bits)");
        RecepLabAqu.setFont(new Font("arial", 0, 12));
        RecepAqu = new BoundedTextField("", 32);
        RecepLabAqu.setBounds(10, 55, 180, 20);
        RecepAqu.setBounds(10, 75, 285, 20);
        add(RecepLabAqu);
        add(RecepAqu);
        
        RecepLabOff = new JLabel("Offset (4bits)");
        RecepLabOff.setFont(new Font("arial", 0, 12));
        RecepOff = new BoundedTextField("", 4);
        RecepLabOff.setBounds(10, 95, 125, 20);
        RecepOff.setBounds(10, 115, 125, 20);
        add(RecepLabOff);
        add(RecepOff);
        //RecepOff.addActionListener(this);
        
        // URG
        RecepURG = new JCheckBox("URG");
        RecepURG.setFont(new Font("arial", 0, 12));
        RecepURG.setBounds(138, 103, 52, 20);
        add(RecepURG);
        //RecepURG.addActionListener(this);
    
        // ACK
        RecepACK = new JCheckBox("ACK");
        RecepACK.setFont(new Font("arial", 0, 12));
        RecepACK.setBounds(193, 103, 52, 20);
        add(RecepACK);
        //RecepACK.addActionListener(this);
        
        // PSH
        RecepPSH = new JCheckBox("PSH");
        RecepPSH.setFont(new Font("arial", 0, 12));
        RecepPSH.setBounds(248, 103, 52, 20);
        add(RecepPSH);
        //RecepPSH.addActionListener(this);
       
        // RST
        RecepRST = new JCheckBox("RST");
        RecepRST.setFont(new Font("arial", 0, 12));
        RecepRST.setBounds(138, 123, 52, 20);
        add(RecepRST);
        //RecepRST.addActionListener(this);

        // SYN
        RecepSYN = new JCheckBox("SYN");
        RecepSYN.setFont(new Font("arial", 0, 12));
        RecepSYN.setBounds(193, 123, 52, 20);
        add(RecepSYN);
        //RecepSYN.addActionListener(this);

        // FIN
        RecepFIN = new JCheckBox("FIN");
        RecepFIN.setFont(new Font("arial", 0, 12));
        RecepFIN.setBounds(248, 123, 52, 20);
        add(RecepFIN);
        //RecepFIN.addActionListener(this);

        // Fenêtre
        RecepLabFen = new JLabel("Fenêtre (16bits)");
        RecepLabFen.setFont(new Font("arial", 0, 12));
        RecepFen = new BoundedTextField("", 16);
        RecepLabFen.setBounds(10, 135, 285, 20);
        RecepFen.setBounds(10, 155, 285, 20);
        add(RecepLabFen);
        add(RecepFen);
        //RecepFen.addActionListener(this);
        
        // Checksum
        RecepLabChk = new JLabel("Checksum (16bits)");
        RecepLabChk.setFont(new Font("arial", 0, 12));
        RecepChk = new BoundedTextField("", 16);
        RecepLabChk.setBounds(10, 175, 140, 20);
        RecepChk.setBounds(10, 195, 140, 20);
        add(RecepLabChk);
        add(RecepChk);
        //RecepChk.addActionListener(this);
        
        RecepLabPtr = new JLabel("Pointeur urgent (16bits)");
        RecepLabPtr.setFont(new Font("arial", 0, 12));
        RecepPtr = new BoundedTextField("", 16);
        add(RecepLabPtr);
        add(RecepPtr);
        RecepLabPtr.setBounds(155, 175, 140, 20);
        RecepPtr.setBounds(155, 195, 140, 20);
        //RecepPtr.addActionListener(this);
        
        // Data
        RecepLabData = new JLabel("Données");
        RecepLabData.setFont(new Font("arial", 0, 12));
        RecepData = new BoundedTextField("", 100);
        RecepLabData.setBounds(10, 215, 285, 20);
        RecepData.setBounds(10, 235, 285, 20);
        add(RecepLabData);
        add(RecepData);
        //RecepData.addActionListener(this);
	        
	    CheckBtn = new JButton("Check it !");
	    CheckBtn.addActionListener(this);
	    add(CheckBtn);
	    CheckBtn.setBounds(10, 265, 100, 20);
	}

	public void actionPerformed(ActionEvent action) {
		// TODO Auto-generated method stub
		GUIManager gui;
		gui = GUIManager.getInstance();
		
		if (action.getSource() == CheckBtn) {
			// HERE§
			if (segment != null) {
				if (segment.get_checksum() == segment.get_checksum(segment)) {
					gui.getPanelConsole().insertLine("CRC is correct", "Green");
				}
				else
					gui.getPanelConsole().insertLine("CRC is incorrect: Written="+this.RecepChk.getText()+" Expected="+segment.get_checksum(segment), "Red");
			}
		}
	}
	
	public void setSeqNumber (String s) {
		RecepNum.setText(s);
	}
	
	public void setAckNumber (String s) {
		RecepAqu.setText(s);
	}
	
	public void setOffset (String s) {
		RecepOff.setText(s);
	}
	
	public void setFenetre (String s) {
		RecepFen.setText(s);
	}
	
	public void setChecksum (String s){
		RecepChk.setText(s);
	}
	
	public void setData (String s) {
		RecepData.setText(s);
	}
	
	public void setPointUrg (String s) {
		RecepPtr.setText(s);
	}
	
	public void setURG (boolean b) {
		RecepURG.setSelected(b);
	}
	
	public void setACK (boolean b) {
		RecepACK.setSelected(b);
	}
	
	public void setPSH (boolean b) {
		RecepPSH.setSelected(b);
	}
	
	public void setSYN (boolean b) {
		RecepSYN.setSelected(b);
	}
	
	public void setRST (boolean b) {
		RecepRST.setSelected(b);
	}
	
	public void setFIN (boolean b) {
		RecepFIN.setSelected(b);
	}

	public BoundedTextField getRecepData() {
		return RecepData;
	}
}
