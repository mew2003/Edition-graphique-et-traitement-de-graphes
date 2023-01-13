/**
 * Représentation d'un lien non orienté
 */
package app;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import static tools.drawingPositions.*;

import java.io.Serializable;

/**
 * Lien non orienté
 * Pour rappel, un lien non orienté doit respecter les principes suivants :
 * - Il est représenter par un ligne allant d'un noeud à un autre
 * - Il ne peut pas partir d'un noeud pour aller vers ce même noeud (boucle)
 * - Il ne peut pas partir du même noeud et aller vers un autre même noeud qu'un autre lien
 * @author Mewen
 */
public class LienNonOriente extends Lien implements Serializable {

	// Noeuds que relient le lien
    private Noeud[] noeuds;
    
    // Représentation graphique du lien
    private transient Line line;

    /**
     * Crée un lien reliant 2 noeuds
     * @param noeuds les noeuds à relié
     * @throws IllegalArgumentException Si les deux noeuds en arguments sont identiques
     */
    public LienNonOriente(Noeud[] noeuds) {
    	if (noeuds[0] == noeuds[1]) throw new IllegalArgumentException("Impossible de créer une boucle "
                                                                      + "pour un lien simple non orienté");
        this.noeuds = noeuds;
    }
    
    @Override
    public Noeud[] getNoeuds() {
        return noeuds;
    }

    @Override
    public void setNoeuds(Noeud[] value, AnchorPane zoneDessin) {
        this.noeuds = value;
        actualiser();
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
     * Renvoie l'élément graphique correspondant au lien
     * @return la ligne graphique
     */
	public Line getLine() {
		return line;
	}
}
