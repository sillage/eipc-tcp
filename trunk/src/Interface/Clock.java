package Interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;

/**
 * Classe servant � afficher un chronom�tre
 * @author R�mi TANIWAKI
 *
 */
@SuppressWarnings("serial")
class Clock extends JTextField {
    javax.swing.Timer m_t;
    int count;

	/**
	 * Constructeur
	 */
    public Clock() {
 
    	count = 0;
        setColumns(8);
        setHorizontalAlignment(JTextField.LEFT);
        setText("Aucune session");
        m_t = new javax.swing.Timer(1000, new ClockTickAction());
    }
    
    public void startClock() {
    	m_t.start();
    }

    private class ClockTickAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	Integer sec, min, hr, reste; 

            sec = count % 60; 
            reste = (count - sec) / 60; 
            min = reste%60; 
            hr = (reste - min) / 60; 
            ++count; 
            setText(hr + " h "+ min + " m " + sec + " s"); 
        }
    }
}