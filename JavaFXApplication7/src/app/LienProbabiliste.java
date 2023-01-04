package app;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.QuadCurve;

import static tools.drawingPositions.*;

public class LienProbabiliste extends Lien {
	
	// Noeud que relie le lien
    private Noeud[] noeuds;
    
    // Représentation graphique du lien
    private Line arrow1, arrow2;
    
    private Arc arc = null;
    
    private QuadCurve quadCurve;
    
    private String nom;
    
    // Nom par défaut d'un lien
    private final String DEFAULT_NAME = "default";
    
    private final double BOUCLE_ANGLE = -14.49;
    
    private final double BOUCLE_SIZE = 208.98;

	public LienProbabiliste(Noeud[] noeuds, int nbLien) {
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
		// TODO Auto-generated method stub

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
            double[] pointCentral = {(linePos[0] + linePos[2])/2, (linePos[1] + linePos[3])/2};
            // (Y2 - Y1)/5     (X1 - X2)/5
            double[] vecteur = {(linePos[3] - linePos[1])/5, (linePos[0] - linePos[2])/5};
            double[] pointC = {pointCentral[0] + vecteur[0], pointCentral[1] + vecteur[1]};
            quadCurve = new QuadCurve(
            		linePos[0], linePos[1],
                    pointCentral[0] + vecteur[0], pointCentral[1] + vecteur[1],
                	linePos[2], linePos[3]);
            quadCurve.setFill(Color.TRANSPARENT);
            quadCurve.setStroke(Color.BLACK);
    		zoneDessin.getChildren().addAll(quadCurve);
    		linePos[0] = pointC[0];
    		linePos[1] = pointC[1];
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
		// TODO Auto-generated method stub

	}
	
	@Override
    public String toString() {
        return "Lien : [" + noeuds[0].getNom() + ", " + noeuds[1].getNom() + "]";
    }

}
