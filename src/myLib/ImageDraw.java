package myLib;


import javax.swing.JLabel;
import myLib.ImageSet;

		
/**
 * Classe qui cree un label pour dessiner une image.
 * @author Rémi TANIWAKI
 *
 */
public class ImageDraw {
	private int _x, _y, _h, _w;
	private JLabel _picture;
	
	/**
	 * Constructeur 
	 * @param filepath chemin de l'image
	 * @param x position x dans la JFrame
	 * @param y position y dans la JFrame
	 * @param h hauteur du JLabel
	 * @param w largeur du JLabel
	 */
	public ImageDraw(String filepath, int x, int y, int w, int h) {
		ImageSet pic = new ImageSet(filepath, null);
		
		_x = x;
		_y = y;
		_h = h;
		_w = w;
		_picture = new JLabel(pic.getImage());
		_picture.setBounds(_x, _y, _w, _h);
	}
	
	/**
	 * Getter de l'image (JLabel)
	 * @return le JLabel
	 */
	public JLabel getPicture() {
		return _picture;
	}
}
