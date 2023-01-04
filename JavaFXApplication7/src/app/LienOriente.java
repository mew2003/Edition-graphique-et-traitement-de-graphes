package app;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import static tools.drawingPositions.*;

public class LienOriente extends Lien {
	
    // Noeud que relie le lien
    private Noeud[] noeuds;
    
    // Représentation graphique du lien
    private Line arrow1, arrow2;
    
    private Line line = null;
    
    private Arc arc = null;
    
    private String nom;
    
    // Nom par défaut d'un lien
    private final String DEFAULT_NAME = "default";
    
    private final double BOUCLE_ANGLE = -14.49;
    
    private final double BOUCLE_SIZE = 208.98;
    
    /**
     * Crée un lien reliant 2 noeuds
     * @param noeuds les noeuds à relié
     * @param nbLien permet le nom unique du lien
     */
    public LienOriente(Noeud[] noeuds, int nbLien) {
        this.nom = DEFAULT_NAME + nbLien;
        this.noeuds = noeuds;
    }

	@Override
	public Noeud[] getNoeuds() {
		return noeuds;
	}

	public Arc getArc() {
		return arc;
	}

	@Override
	public void setNoeuds(Noeud[] value, AnchorPane zoneDessin) {
		this.noeuds = value;
		if (arc != null) {
			zoneDessin.getChildren().remove(arc);
		} 
		if (line != null) {
			zoneDessin.getChildren().remove(line);
		}
		zoneDessin.getChildren().remove(arrow1);
		zoneDessin.getChildren().remove(arrow2);
		dessiner(zoneDessin);
	}
	
    @Override
    public String toString() {
        return "Lien : [" + noeuds[0].getNom() + ", " + noeuds[1].getNom() + "]";
    }

    @Override
    public void dessiner(AnchorPane zoneDessin) {
    	double[] linePos, arrowPos;
    	if (noeuds[0] == noeuds[1]) {
    		double[] depart = departArc(noeuds);
    		linePos = new double[]{depart[0], depart[1] - noeuds[0].getRadius() / 2.0, depart[0], depart[1]};
    		double arcRadius = noeuds[0].getRadius() / 2.0;
    		arc = new Arc(noeuds[0].getPositions()[0], noeuds[0].getPositions()[1] - noeuds[0].getRadius(), arcRadius, arcRadius, BOUCLE_ANGLE, BOUCLE_SIZE);
            arc.setFill(Color.TRANSPARENT);
    		arc.setStroke(Color.BLACK);
    		zoneDessin.getChildren().addAll(arc);
    	} else {
    		linePos = lineDrawingPositions(noeuds);
            this.line = new Line(linePos[0], linePos[1], linePos[2], linePos[3]);
            line.setFill(Color.TRANSPARENT);
            line.setStroke(Color.BLACK);
            zoneDessin.getChildren().addAll(line);
    	}
    	arrowPos = arrowPositions(linePos);
    	this.arrow1 = new Line(linePos[2], linePos[3], arrowPos[0], arrowPos[1]);
        this.arrow2 = new Line(linePos[2], linePos[3], arrowPos[2], arrowPos[3]);
    	arrow1.setFill(Color.TRANSPARENT);
        arrow1.setStroke(Color.BLACK);
        arrow2.setFill(Color.TRANSPARENT);
        arrow2.setStroke(Color.BLACK);
    	zoneDessin.getChildren().addAll(arrow1, arrow2);
    }

	@Override
    public void actualiser() {
		double[] linePos, arrowPos;
		if (noeuds[0] == noeuds[1]) {
			double[] pointC = {noeuds[0].getPositions()[0], noeuds[0].getPositions()[1] - noeuds[0].getRadius()};
    		double arcRadius = noeuds[0].getRadius() / 2.0;
    		
    		arc.setCenterX(pointC[0]);
    		arc.setCenterY(pointC[1]);
    		arc.setRadiusX(arcRadius);
    		arc.setRadiusY(arcRadius);
    		
    		double[] depart = departArc(noeuds);
    		linePos = new double[]{depart[0], depart[1] - noeuds[0].getRadius() / 2.0, depart[0], depart[1]};
    		arrowPos = arrowPositions(linePos);
		} else {
			linePos = lineDrawingPositions(noeuds);
			arrowPos = arrowPositions(linePos);
	        line.setStartX(linePos[0]);
	        line.setStartY(linePos[1]);
	        line.setEndX(linePos[2]);
	        line.setEndY(linePos[3]);
		}
		arrow1.setStartX(linePos[2]);
		arrow1.setStartY(linePos[3]);
		arrow1.setEndX(arrowPos[0]);
		arrow1.setEndY(arrowPos[1]);
		arrow2.setStartX(linePos[2]);
		arrow2.setStartY(linePos[3]);
		arrow2.setEndX(arrowPos[2]);
		arrow2.setEndY(arrowPos[3]);
    }

	public Line getLine() {
		return line;
	}

}
