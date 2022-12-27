/**
 * Représentation d'un noeud non orienté
 */
package app;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Noeud non orienté
 * Pour rappel, un lien non orienté doit respecter les principes suivants :
 * - Il possède un nom unique, permettant de le distinguer de tout autre noeud
 * @author Mewen
 */
public class NoeudXOROriente extends Noeud {

    // Nom du noeud
    private String nom;

    // Position X,Y du noeud
    private double[] pos;

    // Radius du noeud
    private double radius;
    
    // Représentation graphique du noeud
    private Circle circle;
    
    // Représentation graphique du nom du noeud
    private Label label;
    
    // Nom par défaut du noeud
    private final String DEFAULT_NAME = "default";
    
    // Radius par défaut du noeud
    private final double DEFAULT_RADIUS = 25;
    
    /**
     * Crée un noeud
     * @param pos positions X/Y du noeud
     * @param nbNoeud permet le nom unique du noeud
     */
    public NoeudXOROriente(double[] pos, int nbNoeud) {
    	//TODO: Possibilité de créer un noeud portant le même nom qu'un autre noeud précédemment crée.
        this.nom = DEFAULT_NAME + nbNoeud;
        this.pos = pos;
        this.radius = DEFAULT_RADIUS;
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
        label.setLayoutX(pos[0] - radius);
        label.setLayoutY(pos[1] - (radius / 2.0));
    }

    @Override
    public void setRadius(double radius) {
        this.radius = radius;
        circle.setRadius(radius);
    }
    
    @Override
    public String toString() {
        return "Noeud : " +  nom;
    }
    
    @Override
    public void dessiner(AnchorPane zoneDessin) {
        this.circle = new Circle(pos[0], pos[1], radius);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);
        label = new Label();
        label.setText(this.nom);
        label.setLayoutX(pos[0] - radius);
        label.setLayoutY(pos[1] - (radius / 2.0));
        zoneDessin.getChildren().addAll(circle, label);
    }

}
