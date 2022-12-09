package v1;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class LienNonOriente extends Lien {

    private Noeud[] noeuds;
    
    private Line line;

    public LienNonOriente(Noeud[] noeuds) {
        this.noeuds = noeuds;
    }
    
    public LienNonOriente() {}
    
    @Override
    public Noeud[] getNoeuds() {
        return noeuds;
    }

    @Override
    public void setNoeuds(Noeud[] value) {
        this.noeuds = value;
    }

    @Override
    public void dessinerLien(AnchorPane zoneDessin) {
        double[] posNoeud1 = noeuds[0].getPositions();
        double[] posNoeud2 = noeuds[1].getPositions();
        this.line = new Line(posNoeud1[0], posNoeud1[1], posNoeud2[0], posNoeud2[1]);
        line.setFill(Color.TRANSPARENT);
        line.setStroke(Color.BLACK);
        zoneDessin.getChildren().addAll(line);
    }

    @Override
    public Lien creerLien(Noeud[] noeuds) {
        return new LienNonOriente(noeuds);
    }

    @Override
    public String toString() {
        return "Lien : {" + noeuds[0] + " | " + noeuds[1] + "}";
    }
    
    public Line getLine() {
        return line;
    }
}
