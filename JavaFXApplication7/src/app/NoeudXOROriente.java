/**
 * Représentation d'un noeud
 */
package app;

import java.io.Serializable;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Pour rappel, un noeud doit respecter les principes suivants :
 * - Il possède un nom unique, permettant de le distinguer de tout autre noeud
 * @author Mewen
 */
public class NoeudXOROriente extends Noeud implements Serializable {

	// Nom du noeud
    private String nom;

    // Position X,Y du noeud
    private double[] pos;

	// Radius du noeud
    private double radius;
    
    // Représentation graphique du noeud
    private transient Circle circle;
    
    // Représentation graphique du nom du noeud
    private transient Label label;
    
    // Nom par défaut du noeud
    private final String DEFAULT_NAME = "S";
    
    // Radius par défaut du noeud
    private final double DEFAULT_RADIUS = 25;
    
    @Override
    public String getDEFAULT_NAME() {
		return DEFAULT_NAME;
	}

	/**
     * Crée un noeud
     * @param pos positions X/Y du noeud
     * @param nbNoeud permet le nom unique du noeud
     */
    public NoeudXOROriente(double[] pos, int nbNoeud) {
        this.nom = DEFAULT_NAME + nbNoeud;
        this.pos = pos;
        this.radius = DEFAULT_RADIUS;
    }
    
    @Override
    public Circle getCircle() {
		return circle;
	}

	@Override
    public double[] getPositions() {
        return pos;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
	public void setNom(String value) {
        this.nom = value;
        label.setText(nom);
    }

    @Override
    public void setPositions(double[] positions) {
        this.pos = positions;
        circle.setCenterX(pos[0]);
        circle.setCenterY(pos[1]);
        actualiser();
    }

    @Override
    public void setRadius(double radius) {
        this.radius = radius;
        circle.setRadius(radius);
        actualiser();
    }
    
    @Override
    public String toString() {
        return "Nœud : " +  nom;
    }
    
    @Override
    public void dessiner(AnchorPane zoneDessin) {
        this.circle = new Circle(pos[0], pos[1], radius);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);
        label = new Label();
        label.setText(this.nom);
        label.setLayoutX(pos[0] - radius);
        label.setLayoutY(pos[1] - 8);
        zoneDessin.getChildren().addAll(circle, label);
    }
    
    @Override
    public void effacer(AnchorPane zoneDessin) {
    	zoneDessin.getChildren().removeAll(circle, label);
    }
    
    /**
     * Actualisation de l'affichage graphique du noeud
     */
    public void actualiser() {
    	label.setLayoutX(pos[0] - radius);
        label.setLayoutY(pos[1] - (label.getHeight() / 2));
    }

}
