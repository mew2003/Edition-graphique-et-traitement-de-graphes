package app;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.shape.QuadCurve;
import static tools.drawingPositions.*;

import java.io.Serializable;

public class LienOriente extends Lien implements Serializable{
	
    // Noeud que relie le lien
    private Noeud[] noeuds;
    
    // Représentation graphique du lien
    private transient Line arrow1, arrow2;
        
    private transient Arc arc = null;
    
    private transient QuadCurve quadCurve;
    
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

	public Shape[] getArc() {
		Shape[] shape = {arc, arrow1, arrow2};
		return shape;
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
    		double[] pointCentral = {(linePos[0] + linePos[2])/2, (linePos[1] + linePos[3])/2};
            // (Y2 - Y1)/5     (X1 - X2)/5 © Mewen
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
    public void effacer(AnchorPane zoneDessin) {
    	if (quadCurve != null) {
    		zoneDessin.getChildren().remove(quadCurve);
    	} else {
    		zoneDessin.getChildren().remove(arc);
    	}
    	zoneDessin.getChildren().remove(arrow1);
    	zoneDessin.getChildren().remove(arrow2);
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

	public Shape[] getQuadCurved() {
		Shape[] lines = {quadCurve, arrow1, arrow2};
		return lines;
	}

	@Override
	public LienOriente clone() {
		LienOriente lienOR = new LienOriente(noeuds, 0);
		lienOR.arc = this.arc;
		lienOR.arrow1 = this.arrow1;
		lienOR.arrow2 = this.arrow2;
		lienOR.noeuds = this.noeuds;
		lienOR.nom = this.nom;
		lienOR.quadCurve = this.quadCurve;
		return lienOR;
	}

}
