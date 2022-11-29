import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Fenetre pour affichage graphique d'une image couleur RVB
 */
public class ImageViewer {

    JFrame frame;           // fenetre
    BufferedImage image;    // image a afficher dans la fenetre

    /**
     * Constructeur qui cree la fenetre et affiche l'image
     *
     * @param name nom a afficher dans la barre de titre de la fenetre
     * @param img  image a afficher dans cette fenetre
     */
    public ImageViewer(String name, BufferedImage img) {

        // image a afficher
        this.image = img;

        // creation de la zone d'affichage graphique situee dans la fenetre
        VisuImage contentPane = new VisuImage(this.image);

        // parametrage de la fenetre d'affichage
        this.frame = new JFrame(name);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setContentPane(contentPane);
        this.frame.setSize(this.image.getWidth(), this.image.getHeight());
        this.frame.setLocation(100, 100);
        this.frame.setVisible(true);
    }


    // classe locale pour afficher l'image
    public class VisuImage extends JPanel {

        BufferedImage image; // image a afficher

        /**
         * Constructeur qui initialise l'objet en memorisant l'image a afficher
         *
         * @param image image a afficher
         */
        public VisuImage(BufferedImage image) {
            this.image = image;
        }

        /**
         * Methode qui dessine l'image lorsque c'est necessaire
         *
         * @param g contexte graphique dans lequel dessiner l'image
         */
        @Override
        protected void paintComponent(Graphics g) {
            int x = (getWidth() - image.getWidth()) / 2;
            int y = (getHeight() - image.getHeight()) / 2;
            g.drawImage(image, x, y, this);
        }
    }
}
