package temp;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Lien non orienté
 * Pour rappel, un lien non orienté doit respecter les principes suivants :
 * - Il est représenter par un ligne allant d'un noeud à un autre
 * - Un lien ne peut pas partir d'un noeud pour aller vers ce même noeud
 * @author Mewen
 */
public class LienNonOriente extends Lien {

    // Noeud que relie le lien
    private Noeud[] noeuds;
    
    private Line line;

    /**
     * Crée un lien reliant 2 noeuds
     * @param noeuds les noeuds à relié
     */
    public LienNonOriente(Noeud[] noeuds) {
        this.noeuds = noeuds;
    }
    
    @Override
    public Noeud[] getNoeuds() {
        return noeuds;
    }

    @Override
    public void setNoeuds(Noeud[] value) {
        this.noeuds = value;
    }

    @Override
    public String toString() {
        return "Lien : {" + noeuds[0] + " | " + noeuds[1] + "}";
    }

    @Override
    public void dessiner(AnchorPane zoneDessin) {
        double[] posNoeud1 = noeuds[0].getPositions();
        double[] posNoeud2 = noeuds[1].getPositions();
        double L = Math.sqrt(Math.pow(posNoeud2[0] - posNoeud1[0],2) + Math.pow(posNoeud2[1] - posNoeud1[1],2));
        double[] vecteurAAPrime = {(posNoeud2[0]-posNoeud1[0]) * noeuds[0].getRadius() / L ,(posNoeud2[1] - posNoeud1[1]) * noeuds[0].getRadius() / L};
        this.line = new Line(posNoeud1[0] + vecteurAAPrime[0], posNoeud1[1] + vecteurAAPrime[1], posNoeud2[0] - vecteurAAPrime[0], posNoeud2[1] - vecteurAAPrime[1]);
        line.setFill(Color.TRANSPARENT);
        line.setStroke(Color.BLACK);
        zoneDessin.getChildren().addAll(line);
    }
}
