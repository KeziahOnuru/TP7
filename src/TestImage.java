import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestImage {

    public static void main(String[] args) throws IOException {

        String path = "1.jpg";

        // chargement en memoire de l'image
        BufferedImage image = ImageIO.read(new File(path));

        // affichage
        ImageViewer visu = new ImageViewer(path, image);

        // 4.1 symetrie l'image
        BufferedImage im1 = Operateur.symetrieH(image);
        ImageViewer iw1 = new ImageViewer("4.1 symetrie horizontale", im1);

        // 4.2 rotation droite
        BufferedImage im2 = Operateur.rotationDroite(image);
        ImageViewer iw2 = new ImageViewer("4.2 rotation 90 degres", im2);

        // 4.3 inverser l'image
        BufferedImage im3 = Operateur.inverser(image);
        ImageViewer iw3 = new ImageViewer("4.3 inverse", Operateur.inverser(image));

        // 4.4 seuiller l'image
        BufferedImage im4 = Operateur.seuiller(image, 120);
        ImageViewer iw4 = new ImageViewer("4.4 seuillage (seuil=120)", im4);

        // 4.5.1 LUT identite
        int[] lut1 = Operateur.calculeLUT_Identite();
        BufferedImage im5 = Operateur.appliqueLUT(image, lut1);
        ImageViewer iw5 = new ImageViewer("LUT Identite", im5);

        // 4.5.2 LUT requantification
        int[] lut2 = Operateur.calculeLUTRequantification(16);
        BufferedImage im6 = Operateur.appliqueLUT(image, lut2);
        ImageViewer iw6 = new ImageViewer("LUT requanticiation 16 couleurs", im6);

        // 4.5.3 LUT assombrir
        int[] lut3 = Operateur.calculeLUTassombrir();
        BufferedImage im7 = Operateur.appliqueLUT(image, lut3);
        ImageViewer iw7 = new ImageViewer("LUT assombrir", im7);

        // 4.5.4 LUT recalage
        int[] lut4 = Operateur.calculeLUTrecalage(50, 200);
        BufferedImage im8 = Operateur.appliqueLUT(image, lut4);
        ImageViewer iw8 = new ImageViewer("LUT recalage 50-200", im8);

        // 4.5.5.a - verif histogramme

        // calcul local de l'histogramme
        int[] h1 = Operateur.histogramme(image);
        HistogrammeViewer hv1 = new HistogrammeViewer("Histogramme (program.)", h1, h1, h1);

        // verification avec histogramme fourni
        HistogrammeViewer hv2 = new HistogrammeViewer("Histogramme (fourni)", image);

        // 4.5.5.b histogramme cumule
        int[] h2 = Operateur.histogrammeCumule(image);
        HistogrammeViewer hv3 = new HistogrammeViewer("Histogramme cumule", h2, h2, h2);

        // 4.5.5.c egalisation
        String path2 = "3.jpg";
        BufferedImage imag2 = ImageIO.read(new File(path2));
        ImageViewer visu2 = new ImageViewer(path2, imag2);
        BufferedImage im9 = Operateur.egalisation(imag2);
        ImageViewer iw9 = new ImageViewer("egalisation", im9);

        // 4.6 Convolution

        // 4.6.a convolution moyenneur
        double[][] n1 = {{1./9, 1./9, 1./9}, {1./9, 1./9, 1./9}, {1./9, 1./9, 1./9}};
        BufferedImage im10 = Operateur.convolution(image, n1);
        ImageViewer iw10 = new ImageViewer("convolution : moyenneur", im10);

        // 4.6.b convolution passe-haut
        double[][] n2 = {{0, -1, 0}, {-1, 5, -1}, {0, -1, 0}};
        BufferedImage im11 = Operateur.convolution(image, n2);
        ImageViewer iw11 = new ImageViewer("convolution : passe-haut", im11);

        // 4.6.C convolution laplacien
        double[][] n3 = {{-1, -1, -1}, {-1, 8, -1}, {-1, -1, -1}};
        BufferedImage im12 = Operateur.convolution(image, n3);
        ImageViewer iw12 = new ImageViewer("convolution : laplacien", Operateur.convolution(image, n3));

        // bonus : test egalisation couleur
        String path3 = "4.jpg";
        BufferedImage imag3 = ImageIO.read(new File(path3));
        ImageViewer visu3 = new ImageViewer(path3, imag3);
        BufferedImage im13 = Operateur.egalisationRVB(imag3);
        ImageViewer iw13 = new ImageViewer("egalisation", im13);

    }

}
