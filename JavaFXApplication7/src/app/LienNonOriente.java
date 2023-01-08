/**
 * Représentation d'un lien non orienté
 */
package app;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import static tools.drawingPositions.*;

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
        double[] linePos = lineDrawingPositions(noeuds);
        line.setStartX(linePos[0]);
        line.setStartY(linePos[1]);
        line.setEndX(linePos[2]);
        line.setEndY(linePos[3]);
    }
    
    @Override
    public void actualiser() {
        double[] linePos = lineDrawingPositions(noeuds);
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
		double[] linePos = lineDrawingPositions(noeuds);
        this.line = new Line(linePos[0], linePos[1], linePos[2], linePos[3]);
        line.setFill(Color.TRANSPARENT);
        line.setStroke(Color.BLACK);
        zoneDessin.getChildren().addAll(line);
    }

    @Override
    public void effacer(AnchorPane zoneDessin) {
    	zoneDessin.getChildren().remove(line);
    }
    
    /**
     * Renvoie le nom du lien
     * @return le nom
     */
	public String getNom() {
		return nom;
	}

	public Line getLine() {
		return line;
	}

	@Override
	public LienNonOriente clone() {
		LienNonOriente lienNOR = new LienNonOriente(noeuds, 0);
		lienNOR.line = this.line;
		lienNOR.noeuds = this.noeuds;
		lienNOR.nom = this.nom;
		return lienNOR;
	}
}
