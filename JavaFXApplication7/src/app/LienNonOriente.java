/**
 * Représentation d'un lien non orienté
 */
package app;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Lien non orienté
 * Pour rappel, un lien non orienté doit respecter les principes suivants :
 * - Il est représenter par un ligne allant d'un noeud à un autre
 * - Il ne peut pas partir d'un noeud pour aller vers ce même noeud (boucle)
 * - Il ne peut pas peut partir du même noeud et aller vers un autre même noeud qu'un autre lien
 * @author Mewen
 */
public class LienNonOriente extends Lien {

    // Noeud que relie le lien
    private Noeud[] noeuds;
    
    // Représentation graphique du lien
    private Line line;
    
    private String nom;
    
    // Nom par défaut d'un lien
    private final String DEFAULT_NAME = "default";

    /**
     * Crée un lien reliant 2 noeuds
     * @param noeuds les noeuds à relié
     * @param nbLien permet le nom unique du lien
     */
    public LienNonOriente(Noeud[] noeuds, int nbLien) {
    	if (noeuds[0] == noeuds[1]) throw new IllegalArgumentException("Impossible de créer une boucle "
                                                                      + "pour un lien simple non orienté");
        this.nom = DEFAULT_NAME + nbLien;
        this.noeuds = noeuds;
    }
    
    @Override
    public Noeud[] getNoeuds() {
        return noeuds;
    }

    @Override
    public void setNoeuds(Noeud[] value, AnchorPane zoneDessin) {
        this.noeuds = value;
        double[] linePos = lineDrawingPositions();
        line.setStartX(linePos[0]);
        line.setStartY(linePos[1]);
        line.setEndX(linePos[2]);
        line.setEndY(linePos[3]);
    }
    
    @Override
    public void actualiser() {
        double[] linePos = lineDrawingPositions();
        line.setStartX(linePos[0]);
        line.setStartY(linePos[1]);
        line.setEndX(linePos[2]);
        line.setEndY(linePos[3]);
    }

    @Override
    public String toString() {
        return "Lien : [" + noeuds[0].getNom() + ", " + noeuds[1].getNom() + "]";
    }

    @Override
    public void dessiner(AnchorPane zoneDessin) {
		double[] linePos = lineDrawingPositions();
        this.line = new Line(linePos[0], linePos[1], linePos[2], linePos[3]);
        line.setFill(Color.TRANSPARENT);
        line.setStroke(Color.BLACK);
        zoneDessin.getChildren().addAll(line);
    }
    
    /**
     * Donne les positions X/Y du départ et de la fin du lien par rapport au noeuds qu'il relie
     * @return les positions
     */
    @Override
    public double[] lineDrawingPositions() {
        double[] posNoeud1 = noeuds[0].getPositions();
        double[] posNoeud2 = noeuds[1].getPositions();
        double L = Math.sqrt(Math.pow(posNoeud2[0] - posNoeud1[0],2) + Math.pow(posNoeud2[1] - posNoeud1[1],2));
        double[] vecteurAAPrime = {(posNoeud2[0]-posNoeud1[0]) * noeuds[0].getRadius() / L ,(posNoeud2[1] - posNoeud1[1]) * noeuds[0].getRadius() / L};
        double[] vecteurAAPrime2 = {(posNoeud2[0]-posNoeud1[0]) * noeuds[1].getRadius() / L ,(posNoeud2[1] - posNoeud1[1]) * noeuds[1].getRadius() / L};
        double[] result = {posNoeud1[0] + vecteurAAPrime[0], posNoeud1[1] + vecteurAAPrime[1], posNoeud2[0] - vecteurAAPrime2[0], posNoeud2[1] - vecteurAAPrime2[1]};
        return result;
    }

    /**
     * Renvoie le nom du lien
     * @return le nom
     */
	public String getNom() {
		return nom;
	}

	@Override
	public Line getLine() {
		return line;
	}
}
