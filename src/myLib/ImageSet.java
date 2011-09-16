package myLib;

import javax.swing.ImageIcon;

/**
 * Classe qui charge une image dans un ImageIcon
 * @author Rémi TANIWAKI
 *
 */
public class ImageSet {

    private ImageIcon _image;

    /**
     * Constructeur
     */
    public ImageSet() {
    }

    /**
     * Constructeur avec fichier
     * @param path Chemin du fichier image
     * @param description Description de l'image
     */
    public ImageSet(String path, String description) {
        _image = createImageIcon(path, description);
    }

    /**
     * ImageIcon getter
     * @return l'attribut _image de la classe de type ImageIcon
     */
    public ImageIcon getImage() {
        return _image;
    }

    /**
     * ImageIcon setter
     * @param i une varible de type ImageIcon à intégrer dans l'objet
     */
    public void setImage(ImageIcon i) {
        _image = i;
    }

    /**
     * Retourne un ImageIcon, ou null si chemin invalide.
     * @param path chemin de l'image
     * @param description une description de l'image si besoin
     * @return l'image sous le type ImageIcon
     */
    protected ImageIcon createImageIcon(String path,
            String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
