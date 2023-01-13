/**
 * Représentation d'un lien orienté pondéré
 */
package app;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;

import static tools.drawingPositions.*;

import java.io.Serializable;

/**
 * Lien orienté
 * Pour rappel, un lien orienté doit respecter les principes suivants :
 * - Il est représenter par une flèche allant d'un noeud à un autre
 * - Il ne peut pas partir du même noeud et aller vers un autre même noeud qu'un autre lien
 * - Chaque lien peut comporter une valeur compris entre -∞ et +∞
 * @author Mewen
 */
public class LienOrientePondere extends Lien implements Serializable {

	// Noeuds que relient le lien
    private Noeud[] noeuds;
    
    // Représentation graphique du lien sous forme de flèche
    private transient Line arrow1, arrow2;
    private transient QuadCurve quadCurve = null;
    
    // Représentation graphique du lien sous forme de boucle
    private transient Arc arc = null;
    
    // Représentation graphique de la valeur du lien
    private transient Label label;
    
    // Permet la bonne création d'un boucle
    private final double BOUCLE_ANGLE = -14.49;
    private final double BOUCLE_SIZE = 208.98;
    
    // Valeur d'un lien
    private double valeur;
    
    // Valeur par défaut d'un lien
    private final double DEFAULT_VALUE = 0.0;

    /**
     * Crée un lien reliant 2 noeuds
     * @param noeuds les noeuds à relié
     */
	public LienOrientePondere(Noeud[] noeuds) {
        this.noeuds = noeuds;
        this.valeur = DEFAULT_VALUE;
	}
	
	@Override
	public Noeud[] getNoeuds() {
		return noeuds;
	}
	
	/**
	 * Renvoie la représentation graphique du lien sous forme de boucle
	 * @return la boucle
	 */
	public Shape[] getArc() {
		return new Shape[] {arc, arrow1, arrow2};
	}

	@Override
	public void setNoeuds(Noeud[] value, AnchorPane zoneDessin) {
		this.noeuds = value;
		if (arc != null) {
			zoneDessin.getChildren().remove(arc);
		} else {
			zoneDessin.getChildren().remove(quadCurve);
		}
		zoneDessin.getChildren().removeAll(label, arrow1, arrow2);
		dessiner(zoneDessin);
	}
	
	@Override
    public void dessiner(AnchorPane zoneDessin) {
    	// Arrays stockant les positions X/Y départ/arrivé des objects graphiques
    	double[] linePos, arrowPos;
    	if (noeuds[0] == noeuds[1]) {
    		// Cas d'une boucle
    		
    		// Point de départ de l'arc (En partant du haut du cercle ~ -30° à gauche)
    		double[] depart = departArc(noeuds);
    		double arcRadius = noeuds[0].getRadius() / 2.0;
    		linePos = new double[]{depart[0], depart[1] - arcRadius, depart[0], depart[1]};

    		// Représentation graphique de la boucle
    		arc = new Arc(noeuds[0].getPositions()[0], noeuds[0].getPositions()[1] - noeuds[0].getRadius(), arcRadius, arcRadius, BOUCLE_ANGLE, BOUCLE_SIZE);
    		arc.setFill(Color.TRANSPARENT);
    		arc.setStroke(Color.BLACK);
    		
    		// Représentation graphique de la valeur du lien
    		label = new Label("" + this.valeur);
    		label.setLayoutX(noeuds[0].getPositions()[0] - 7.5);
            label.setLayoutY(arc.getCenterY() - arc.getRadiusY()*2.5);
    		
    		zoneDessin.getChildren().addAll(arc, label);
    	} else {
    		// Cas d'une flèche
    		
    		//Permet de récupérer les points de départ et d'arriver d'un noeud à un autre
    		linePos = lineDrawingPositions(noeuds);
    		
    		// Calcul du point de contrôle de la ligne
    		double[] pointCentral = {(linePos[0] + linePos[2])/2, (linePos[1] + linePos[3])/2};
            double[] vecteur = {(linePos[3] - linePos[1])/5, (linePos[0] - linePos[2])/5};
            double[] pointC = {pointCentral[0] + vecteur[0], pointCentral[1] + vecteur[1]};
            
            // Représentation graphique de la ligne
            quadCurve = new QuadCurve(
            		    linePos[0], linePos[1], pointC[0], pointC[1], linePos[2], linePos[3]);
            quadCurve.setFill(Color.TRANSPARENT);
            quadCurve.setStroke(Color.BLACK);
            
            // Représentation graphique de la valeur du lien
    		label = new Label("" + this.valeur);
    		label.setLayoutX(pointCentral[0] + vecteur[0] - 7.5);
            label.setLayoutY(pointCentral[1] + vecteur[1]);
    		
            zoneDessin.getChildren().addAll(quadCurve, label);
            linePos[0] = pointC[0];
    		linePos[1] = pointC[1];
    	}
    	// Récupération des positions de l'embout de la flèche
    	arrowPos = arrowPositions(linePos);
    	
    	// Représentation graphe de l'embout de la flèche
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
		// Arrays stockant les positions X/Y départ/arrivé des objects graphiques
		double[] linePos, arrowPos;
    	if (noeuds[0] == noeuds[1]) {
    		// Cas d'une boucle
    		
    		// Point de départ de l'arc (En partant du haut du cercle ~ -30° à gauche)
    		double[] depart = departArc(noeuds);
    		double arcRadius = noeuds[0].getRadius() / 2.0;
    		linePos = new double[]{depart[0], depart[1] - arcRadius, depart[0], depart[1]};

    		// Représentation graphique de la boucle
    		arc.setCenterX(noeuds[0].getPositions()[0]);
    		arc.setCenterY(noeuds[0].getPositions()[1] - noeuds[0].getRadius());
    		arc.setRadiusX(arcRadius);
    		arc.setRadiusY(arcRadius);
    		
    		// Représentation graphique de la valeur du lien
    		label.setText("" + valeur);
    		label.setLayoutX(noeuds[0].getPositions()[0]-7.5);
            label.setLayoutY(arc.getCenterY()-arc.getRadiusY()*2.5);
    	} else {
    		// Cas d'une flèche
    		
    		//Permet de récupérer les points de départ et d'arriver d'un noeud à un autre
    		linePos = lineDrawingPositions(noeuds);
    		
    		// Calcul du point de contrôle de la ligne
    		double[] pointCentral = {(linePos[0] + linePos[2])/2, (linePos[1] + linePos[3])/2};
            double[] vecteur = {(linePos[3] - linePos[1])/5, (linePos[0] - linePos[2])/5};
            double[] pointC = {pointCentral[0] + vecteur[0], pointCentral[1] + vecteur[1]};
            
            // Représentation graphique de la ligne
            quadCurve.setStartX(linePos[0]);
            quadCurve.setStartY(linePos[1]);
            quadCurve.setEndX(linePos[2]);
            quadCurve.setEndY(linePos[3]);
            quadCurve.setControlX(pointCentral[0] + vecteur[0]);
            quadCurve.setControlY(pointCentral[1] + vecteur[1]);
            
            // Représentation graphique de la valeur du lien
            label.setText("" + valeur);
    		label.setLayoutX(pointCentral[0] + vecteur[0]-7.5);
            label.setLayoutY(pointCentral[1] + vecteur[1]);
            
    		linePos[0] = pointC[0];
    		linePos[1] = pointC[1];
    	}
    	// Récupération des positions de l'embout de la flèche
    	arrowPos = arrowPositions(linePos);
    	
    	// Représentation graphe de l'embout de la flèche
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

	/**
	 * Renvoie la représentation graphique du lien sous forme de ligne
	 * @return la ligne
	 */
	public Shape[] getQuadCurved() {
		return new Shape[] {quadCurve, arrow1, arrow2};
	}

	@Override
	public void effacer(AnchorPane zoneDessin) {
		if (quadCurve != null) {
    		zoneDessin.getChildren().remove(quadCurve);
    	} else {
    		zoneDessin.getChildren().remove(arc);
    	}
    	zoneDessin.getChildren().removeAll(arrow1, arrow2, label);
	}
	
	/**
	 * Renvoie la valeur du lien
	 * @return la valeur
	 */
	public double getValue() {
		return valeur;
	}
	
	/**
	 * Remplace l'ancienne valeur d'un lien par la nouvelle saisie en argument
	 * @param newValue nouvelle valeur du lien
	 */
	public void setValue(double newValue) {
		this.valeur = newValue;
	}

}
