import java.awt.image.BufferedImage;

public class Operateur {

    /**
     * Cree une image semblable a celle passee en parametre, mais vide
     * L'image retournee a les memes dimensions, le meme type (couleur ou noir et blanc), le meme espace
     * de representation des pixels (le même nombre de bits pour le codage). Aucun pixel n'est initialise,
     * l'image retournee est donc noire.
     *
     * @param img image modele
     * @return image semblable au modele selon les dimensions et le type
     */
    public static BufferedImage copie(BufferedImage img) {
        return new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
    }

    /**
     * Cree une image semblable a celle passee en parametre, mais vide et transposée
     * L'image retournee a les memes dimensions mais transposées (la largeur devient la hauteur et vice-versa)
     * le meme type (couleur ou noir et blanc), le meme espace de representation des pixels (le même nombre de
     * bits pour le codage). Aucun pixel n'est initialise, l'image retournee est donc noire.
     *
     * @param img image modele
     * @return image semblable au modele selon les dimensions et le type
     */
    public static BufferedImage copieTransposee(BufferedImage img) {
        return new BufferedImage(img.getHeight(), img.getWidth(), img.getType());
    }

    /**
     * Extrait la composante rouge d'une couleur
     *
     * @param color nombre representant une couleur issue d'une image RVB
     * @return valeur de la composante rouge
     */
    public static int rouge(int color) {
        return (color >> 16) & 0xFF;
    }

    /**
     * Extrait la composante verte d'une couleur
     *
     * @param color nombre representant une couleur issue d'une image RVB
     * @return valeur de la composante verte
     */
    public static int vert(int color) {
        return (color >> 8) & 0xFF;
    }

    /**
     * Extrait la composante bleue d'une couleur
     *
     * @param color nombre representant une couleur issue d'une image RVB
     * @return valeur de la composante bleue
     */
    public static int bleu(int color) {
        return (color & 0xFF);
    }

    /**
     * Compose un nombre representant une couleur
     *
     * @param r quantite entiere de rouge a prendre dans (0..255)
     * @param v quantite entiere de vert a prendre dans (0..255)
     * @param b quantite entiere de bleu a prendre dans (0..255)
     * @return nombre compose des 3 quantites
     */
    public static int couleur(int r, int v, int b) {
        return (r << 16 | v << 8 | b);
    }

    /**
     * Calcule la LUT (cf. question 4.1.1) identite
     * Apres son application l'image resultante a exactement la meme configuration
     *
     * @return tableau de 256 entiers modelisant la transformation
     */
    public static int[] calculeLUT_Identite() {
        int[] lut = new int[256];

        // LUT identité : y = x
        for (int i = 0; i < lut.length; i++)
            lut[i] = i;

        return lut;
    }


    // OPERATEURS A PROGRAMMER





    public static int[] histogramme(BufferedImage img) {
        int[] h = new int[256];

        // parcourt toute l'image, colonne par colonne
        for (int c = 0; c < img.getWidth(); c++)
            // et ligne par ligne au sein d'une meme colonne
            for (int l = 0; l < img.getHeight(); l++) {
                int col = rouge(img.getRGB(c, l));
                h[col]++;
            }

        return h;
    }

    public static int[] histogrammeCumule(BufferedImage img) {

        int[] h = histogramme(img);

        // cumul
        for (int i = 1; i < h.length; i++)
            h[i] += h[i - 1];

        return h;
    }


    public static BufferedImage rotationDroite(BufferedImage image) {

        BufferedImage result = copieTransposee(image);

        // parcourt toute l'image, colonne par colonne
        for (int c = 0; c < image.getWidth(); c++)
            // et ligne par ligne au sein d'une meme colonne
            for (int l = 0; l < image.getHeight(); l++) {

                result.setRGB(result.getWidth() - 1 - l, c, image.getRGB(c, l));
            }

        return result;
    }

    /**
     * Egalisation d'image
     * Le methode calcule une LUT d'apres l'histogramme cumule et l'applique sur l'image
     *
     * @param image image à égaliser
     * @return image traitee
     */
    public static BufferedImage egalisation(BufferedImage image) {

        int[] h = histogrammeCumule(image);
        double ratio = 255. / h[h.length - 1];

        // calcule la LUT
        for (int i = 0; i < h.length; i++) h[i] = (int) (h[i] * ratio);

        // applique sur l'image
        BufferedImage result = Operateur.appliqueLUT(image, h);

        return result;
    }


    /**
     * Calcule la LUT de requantification
     * Apres son application l'image resultante comporte moins de couleurs
     *
     * @param nbcol nombre de couleurs attendues dans l'image resultat
     * @return tableau de 256 entiers modelisant la transformation
     */
    public static int[] calculeLUTRequantification(int nbcol) {

        int[] lut = new int[256]; // LUT a renvoyer
        int plage = 256 / nbcol;  // largeur d'une plage d'entree
        int start = plage / 2;    // milieu d'une plage

        // la division entiere de l'index par la largeur d'une plage va permettre de calculer le rang d'un bloc de
        // quantification (les 256/nbcol premiers pixels dans le bloc 0, les 256/nbcol suivants dans le bloc 1 etc.
        // Il reste à multiplier ce rang par la largeur d'un plage afin d'avoir une couleur régulierement etagee
        // d'un bout a l'autre. On decale d'une demi plage afin de "centrer" la quantification.
        for (int i = 0; i < lut.length; i++) lut[i] = start + plage * (i / plage);

        return lut;
    }


    /**
     * Calcule la LUT pour assombrir avec y = x2
     *
     * @return tableau de 256 entiers modelisant la transformation
     */
    public static int[] calculeLUTassombrir() {

        int[] lut = new int[256]; // LUT a renvoyer
        double colmax = 255 * 255;

        for (int i = 0; i < lut.length; i++) lut[i] = (int) ((255 * i * i) / colmax);

        return lut;
    }


    public static int[] calculeLUTrecalage(int D, int A) {

        int[] lut = new int[256]; // LUT a renvoyer

        // supprime avant D
        for (int i = 0; i <= D; i++) lut[i] = 0;

        // supprime après A
        for (int i = A; i < lut.length; i++) lut[i] = 255;

        // distribution lineaire du reste
        // droite passe par (D,0) et (A,255) d'ou pente = 255/(A-D) et ordonnee a l'origine =
        double pente = 255.0 / (A - D);
        double ordoo = -D * pente;
        for (int i = D + 1; i < A; i++) lut[i] = (int) (pente * i + ordoo);

        return lut;
    }


    public static BufferedImage appliqueLUT(BufferedImage img, int[] lut) {

        BufferedImage result = copie(img);

        // parcourt toute l'image, colonne par colonne
        for (int c = 0; c < img.getWidth(); c++)
            // et ligne par ligne au sein d'une meme colonne
            for (int l = 0; l < img.getHeight(); l++) {

                // couleur d'origine
                int src = img.getRGB(c, l);
                // se base sur le rouge
                int rou = rouge(src);
                // couleur de remplacement d'apres la lut
                int dst = lut[rou];
                // reconstitue la couleur
                int col = couleur(dst, dst, dst);
                // remplace un pixel par son complement au blanc
                result.setRGB(c, l, col);
            }


        return result;
    }


    public static BufferedImage convolution(BufferedImage img, double[][] filtre) {

        BufferedImage result = copie(img);

        // parcourt toute l'image, colonne par colonne
        for (int c = 1; c < img.getWidth() - 1; c++)
            // et ligne par ligne au sein d'une meme colonne
            for (int l = 1; l < img.getHeight() - 1; l++) {

                // filtrage 3x3
                double sum = 0.0;
                for (int x = -1; x <= 1; x++)
                    for (int y = -1; y <= 1; y++) {
                        // couleur d'origine
                        int src = img.getRGB(c + x, l + y);
                        // se base sur le rouge
                        int rou = rouge(src);
                        // applique le filtre
                        sum += rou * filtre[x + 1][y + 1];
                    }

                // controle de la valeur finale
                if (sum < 0.) sum = 0.;
                if (sum > 255.) sum = 255.;
                // couleur de remplacement d'apres le filtrage
                int dst = (int) sum;
                // reconstitue la couleur
                int col = couleur(dst, dst, dst);
                // affecte dans l'image resultats
                result.setRGB(c, l, col);
            }

        return result;
    }


    public static BufferedImage symetrieH(BufferedImage img) {

        BufferedImage result = copie(img);

        // considere toutes les colonnes de la moitie gauche
        for (int c = 0; c < img.getWidth() / 2; c++)
            // recopie tous les pixels des colonnes en bouclant sur les lignes
            for (int l = 0; l < img.getHeight(); l++) {
                // valeurs origine
                int src = img.getRGB(c, l);
                int dst = img.getRGB(img.getWidth() - 1 - c, l);
                // affecte en inversant source et destination
                result.setRGB(c, l, dst);
                result.setRGB(img.getWidth() - 1 - c, l, src);
            }

        return result;
    }


    public static BufferedImage inverser(BufferedImage img) {

        BufferedImage result = copie(img);

        // cree un blanc de reference
        final int blanc = couleur(255, 255, 255);

        // parcourt toute l'image, colonne par colonne
        for (int c = 0; c < img.getWidth(); c++)
            // et ligne par ligne au sein d'une meme colonne
            for (int l = 0; l < img.getHeight(); l++)
                // remplace un pixel par son complement au blanc
                result.setRGB(c, l, blanc - img.getRGB(c, l));

        return result;
    }


    public static BufferedImage seuiller(BufferedImage img, int seuil) {

        BufferedImage result = copie(img);

        // parcourt toute l'image, colonne par colonne
        for (int c = 0; c < img.getWidth(); c++)
            // et ligne par ligne au sein d'une meme colonne
            for (int l = 0; l < img.getHeight(); l++) {
                // le seuillage se fait sur un plan donne. Ici on a considere que par defaut c'est le rouge.
                if (rouge(img.getRGB(c, l)) < seuil)
                    // si le rouge est inferieur au seuil, on remplace la couleur par noir
                    result.setRGB(c, l, 0);
                else
                    // sinon on affecte la couleur d'origine
                    result.setRGB(c, l, img.getRGB(c, l));
            }
        return result;
    }


    // ADAPTATION EN RVB

    public static int[][] histogrammeRVB(BufferedImage img) {
        int[][] h = new int[3][256];

        // parcourt toute l'image, colonne par colonne
        for (int c = 0; c < img.getWidth(); c++)
            // et ligne par ligne au sein d'une meme colonne
            for (int l = 0; l < img.getHeight(); l++) {
                h[0][rouge(img.getRGB(c, l))]++;
                h[1][ vert(img.getRGB(c, l))]++;
                h[2][ bleu(img.getRGB(c, l))]++;
            }

        return h;
    }

    public static int[][] histogrammeRVBCumule(BufferedImage img) {

        int[][] h = histogrammeRVB(img);

        // cumul
        for (int i = 1; i < h[0].length; i++) {
            h[0][i] += h[0][i - 1];
            h[1][i] += h[1][i - 1];
            h[2][i] += h[2][i - 1];
        }
        return h;
    }

    public static BufferedImage appliqueLUTRVB(BufferedImage img, int[][] lut) {

        BufferedImage result = copie(img);

        // parcourt toute l'image, colonne par colonne
        for (int c = 0; c < img.getWidth(); c++)
            // et ligne par ligne au sein d'une meme colonne
            for (int l = 0; l < img.getHeight(); l++) {

                // couleur d'origine
                int src = img.getRGB(c, l);
                // couleur de remplacement d'apres la lut
                int dstR = lut[0][rouge(src)];
                int dstV = lut[1][ vert(src)];
                int dstB = lut[2][ bleu(src)];
                // reconstitue la couleur
                int col = couleur(dstR, dstV, dstB);
                // remplace un pixel par son complement au blanc
                result.setRGB(c, l, col);
            }


        return result;
    }

    public static BufferedImage egalisationRVB(BufferedImage image) {

        int[][] h = histogrammeRVBCumule(image);
        HistogrammeViewer hv3 = new HistogrammeViewer("Histogramme cumule", h[0], h[1], h[2]);
        double ratio = 255. / h[0][h[0].length - 1];

        // calcule la LUT
        for (int channel=0; channel<3; channel++)
            for (int i = 0; i < h[channel].length; i++) h[channel][i] = (int) (h[channel][i] * ratio);

        // applique sur l'image
        BufferedImage result = Operateur.appliqueLUTRVB(image, h);

        return result;
    }

}
