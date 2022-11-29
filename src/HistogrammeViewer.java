import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Fenetre pour affichage graphique d'un histogramme d'image couleur
 */
public class HistogrammeViewer {


    JFrame frame;           // fenetre
    BufferedImage image;    // image a afficher dans la fenetre
    String name;            // nom de la fenetre dans la barre de titre

    Histogramme histo;

    /**
     * Initialisation de la fenetre
     *
     * @param name nom de la fenetre qui sera affiche dans la barre de titre
     */
    private void init(String name) {

        this.frame = new JFrame(name);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setContentPane(histo);
        this.frame.setSize(257, 256);
        this.frame.setLocation(100, 100);
        this.frame.setVisible(true);
        this.frame.repaint();
    }

    /**
     * Affichage d'un histogramme calcule sur l'image passee en parametre
     * @param name nom de la fenetre qui sera affiche dans la barre de titre
     * @param img image sur laquelle calculer l'histogramme
     * @see #init(String)
     */
    public HistogrammeViewer(String name, BufferedImage img) {

        this.histo = new Histogramme(img);
        init(name);
    }

    /**
     * Affichage d'un histogramme calcule d'après les tableaux representant les histogrammes passes en parametres
     * @param name nom de la fenetre qui sera affiche dans la barre de titre
     * @param hr tableau 1D d'entiers representant l'histogramme pour le rouge
     * @param hg tableau 1D d'entiers representant l'histogramme pour le vert
     * @param hb tableau 1D d'entiers representant l'histogramme pour le bleu
     * @see #init(String)
     */
    public HistogrammeViewer(String name, int[] hr, int[] hg, int hb[]) {

        this.histo = new Histogramme(hr, hg, hb);
        init(name);
    }


    // classe locale pour representer l'histogramme a afficher
    private class Histogramme extends JPanel {
        public final int RED = 0;
        public final int GREEN = 1;
        public final int BLUE = 2;
        public final int OFFSET = 20;
        private BufferedImage image;
        private int SIZE = 256;
        private int NUMBER_OF_COLOURS = 3;
        private int[][] colourBins;
        private int maxY;

        public Histogramme(BufferedImage img) {

            this.image = img;

            colourBins = new int[NUMBER_OF_COLOURS][];

            for (int i = 0; i < NUMBER_OF_COLOURS; i++) {
                colourBins[i] = new int[SIZE];
            }

            // Reset all the bins
            for (int i = 0; i < NUMBER_OF_COLOURS; i++) {
                for (int j = 0; j < SIZE; j++) {
                    colourBins[i][j] = 0;
                }
            }

            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    Color c = new Color(image.getRGB(x, y));

                    colourBins[RED][c.getRed()]++;
                    colourBins[GREEN][c.getGreen()]++;
                    colourBins[BLUE][c.getBlue()]++;
                }
            }

            maxY = 0;

            for (int i = 0; i < NUMBER_OF_COLOURS; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (maxY < colourBins[i][j]) {
                        maxY = colourBins[i][j];
                    }
                }
            }


        }


        public Histogramme(int[] hred, int[] hgreen, int[] hblue) {

            this.image = null;

            if (SIZE == hred.length && SIZE == hgreen.length && SIZE == hblue.length) {


                colourBins = new int[NUMBER_OF_COLOURS][];

                for (int i = 0; i < NUMBER_OF_COLOURS; i++) {
                    colourBins[i] = new int[SIZE];
                }

                System.arraycopy(hred, 0, colourBins[RED], 0, SIZE);
                System.arraycopy(hgreen, 0, colourBins[GREEN], 0, SIZE);
                System.arraycopy(hblue, 0, colourBins[BLUE], 0, SIZE);

                maxY = 0;

                for (int i = 0; i < NUMBER_OF_COLOURS; i++) {
                    for (int j = 0; j < SIZE; j++) {
                        if (maxY < colourBins[i][j]) {
                            maxY = colourBins[i][j];
                        }
                    }
                }
            }


        }


        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;

            g2.setColor(Color.white);
            g2.fillRect(0, 0, getWidth(), getHeight());

            int xInterval = (int) ((double) getWidth() / ((double) SIZE + 1));

            // barre de luminance
            g2.setStroke(new BasicStroke(1));
            for (int j = 0; j < SIZE - 1; j++) {
                g2.setColor(new Color(j<<16|j<<8|j));
                g2.fillRect(j * xInterval, getHeight() -  OFFSET, (j + 1) * xInterval, getHeight());
            }

            g2.setColor(Color.black);
            g2.setStroke(new BasicStroke(2));

            for (int i = 0; i < NUMBER_OF_COLOURS; i++) {

                // Set the graph
                if (i == RED) {
                    g2.setColor(Color.red);
                } else if (i == GREEN) {
                    g2.setColor(Color.GREEN);
                } else if (i == BLUE) {
                    g2.setColor(Color.blue);
                }

                // draw the graph for the specific colour.
                for (int j = 0; j < SIZE - 1; j++) {
                    int value = (int) (((double) colourBins[i][j] / (double) maxY) * (getHeight()-OFFSET));
                    int value2 = (int) (((double) colourBins[i][j + 1] / (double) maxY) * (getHeight()-OFFSET));
                    // point de la courbe
                    g2.drawLine(j * xInterval, getHeight() - (value + OFFSET), (j + 1) * xInterval, getHeight() - (value2 + OFFSET));
                }
            }

        }


    }


}
