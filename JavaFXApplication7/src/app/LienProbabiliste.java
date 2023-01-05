package app;

import javafx.scene.control.Label;
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
    
    private Label label;
    
    // Nom par défaut d'un lien
    private final String DEFAULT_NAME = "default";
    
    private final double BOUCLE_ANGLE = -14.49;
    
    private final double BOUCLE_SIZE = 208.98;
    
    //valeur d'un lien
    private double valeur;
    
    private final double DEFAULT_VALUE = 0.0;

	public LienProbabiliste(Noeud[] noeuds, int nbLien) {
		this.nom = DEFAULT_NAME + nbLien;
        this.noeuds = noeuds;
        this.valeur = DEFAULT_VALUE;
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
		if (quadCurve != null) {
			zoneDessin.getChildren().remove(quadCurve);
		}
		zoneDessin.getChildren().remove(label);
		zoneDessin.getChildren().remove(arrow1);
		zoneDessin.getChildren().remove(arrow2);
		dessiner(zoneDessin);
	}

	@Override
	public void dessiner(AnchorPane zoneDessin) {
		double[] linePos, arrowPos;
    	if (noeuds[0] == noeuds[1]) {
    		double[] depart = departArc(noeuds);
    		linePos = new double[]{depart[0], depart[1] - noeuds[0].getRadius() / 2.0, depart[0], depart[1]};
    		double arcRadius = noeuds[0].getRadius() / 2.0;
    		arc = new Arc(noeuds[0].getPositions()[0], noeuds[0].getPositions()[1] - noeuds[0].getRadius(), arcRadius, arcRadius, BOUCLE_ANGLE, BOUCLE_SIZE);
    		label = new Label("" + this.valeur);
    		label.setLayoutX(noeuds[0].getPositions()[0]-7.5);
            label.setLayoutY(arc.getCenterY()-arc.getRadiusY()*2.5);
            arc.setFill(Color.TRANSPARENT);
    		arc.setStroke(Color.BLACK);
    		zoneDessin.getChildren().addAll(arc, label);
    	} else {
    		linePos = lineDrawingPositions(noeuds);
            double[] pointCentral = {(linePos[0] + linePos[2])/2, (linePos[1] + linePos[3])/2};
            // (Y2 - Y1)/5     (X1 - X2)/5 © Mewen
            double[] vecteur = {(linePos[3] - linePos[1])/5, (linePos[0] - linePos[2])/5};
            double[] pointC = {pointCentral[0] + vecteur[0], pointCentral[1] + vecteur[1]};
            quadCurve = new QuadCurve(
            		linePos[0], linePos[1],
                    pointCentral[0] + vecteur[0], pointCentral[1] + vecteur[1],
                	linePos[2], linePos[3]);
            label = new Label("" + this.valeur);
    		label.setLayoutX(pointCentral[0] + vecteur[0]-7.5);
            label.setLayoutY(pointCentral[1] + vecteur[1]);
            quadCurve.setFill(Color.TRANSPARENT);
            quadCurve.setStroke(Color.BLACK);
    		zoneDessin.getChildren().addAll(quadCurve, label);
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
		double[] linePos, arrowPos;
    	if (noeuds[0] == noeuds[1]) {
    		double[] depart = departArc(noeuds);
    		linePos = new double[]{depart[0], depart[1] - noeuds[0].getRadius() / 2.0, depart[0], depart[1]};
    		double arcRadius = noeuds[0].getRadius() / 2.0;
    		arc.setCenterX(noeuds[0].getPositions()[0]);
    		arc.setCenterY(noeuds[0].getPositions()[1] - noeuds[0].getRadius());
    		arc.setRadiusX(arcRadius);
    		arc.setRadiusY(arcRadius);
    		label.setText("" + valeur);
    		label.setLayoutX(noeuds[0].getPositions()[0]-7.5);
            label.setLayoutY(arc.getCenterY()-arc.getRadiusY()*2.5);
    	} else {
    		linePos = lineDrawingPositions(noeuds);
            double[] pointCentral = {(linePos[0] + linePos[2])/2, (linePos[1] + linePos[3])/2};
            // (Y2 - Y1)/5     (X1 - X2)/5 © Mewen
            double[] vecteur = {(linePos[3] - linePos[1])/5, (linePos[0] - linePos[2])/5};
            double[] pointC = {pointCentral[0] + vecteur[0], pointCentral[1] + vecteur[1]};
            quadCurve.setStartX(linePos[0]);
            quadCurve.setStartY(linePos[1]);
            quadCurve.setEndX(linePos[2]);
            quadCurve.setEndY(linePos[3]);
            quadCurve.setControlX(pointCentral[0] + vecteur[0]);
            quadCurve.setControlY(pointCentral[1] + vecteur[1]);
            label.setText("" + valeur);
    		label.setLayoutX(pointCentral[0] + vecteur[0]-7.5);
            label.setLayoutY(pointCentral[1] + vecteur[1]);
    		linePos[0] = pointC[0];
    		linePos[1] = pointC[1];
    	}
    	arrowPos = arrowPositions(linePos);
    	arrow1.setStartX(linePos[2]);
		arrow1.setStartY(linePos[3]);
		arrow1.setEndX(arrowPos[0]);
		arrow1.setEndY(arrowPos[1]);
		arrow2.setStartX(linePos[2]);
		arrow2.setStartY(linePos[3]);
		arrow2.setEndX(arrowPos[2]);
		arrow2.setEndY(arrowPos[3]);
	}
	
	@Override
    public String toString() {
        return "Lien : [" + noeuds[0].getNom() + ", " + noeuds[1].getNom() + "]";
    }

	public QuadCurve getQuadCurved() {
		return quadCurve;
	}

	@Override
	public void effacer(AnchorPane zoneDessin) {
		if (quadCurve != null) {
    		zoneDessin.getChildren().remove(quadCurve);
    	} else {
    		zoneDessin.getChildren().remove(arc);
    	}
    	zoneDessin.getChildren().remove(arrow1);
    	zoneDessin.getChildren().remove(arrow2);
    	zoneDessin.getChildren().remove(label);
		
	}
	
	public double getValue() {
		return valeur;
	}
	
	public void setValue(double newValue) {
		if (0 > newValue || newValue > 1) {
			throw new IllegalArgumentException("Un lien probabiliste ne peut pas " 
		                                       + "avoir pour valeur un nombre inférieur" 
					                           + " à 0 ou supérieur à 1.");
		}
		this.valeur = newValue;
	}

}
