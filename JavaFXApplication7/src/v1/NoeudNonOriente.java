package v1;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Noeud non orienté
 * Pour rappel, un lien non orienté doit respecter les principes suivants :
 * - Il possède un nom unique, permetant de le distinguer de tout autre noeud
 * @author Mewen
 */
public class NoeudNonOriente extends Noeud {

    // Nom du noeud
    private String nom;

    // Position X,Y du noeud
    private double[] pos;

    // Radius du noeud
    private double radius;
    
    // Représentation graphique du noeud associé
    private Circle circle;
    
    /**
     * Crée un noeud
     * @param nom nom du noeud
     * @param pos position x,y du noeud
     * @param radius radius du noeud
     */
    public NoeudNonOriente(String nom, double[] pos, double radius) {
        this.nom = nom;
        this.pos = pos;
        this.radius = radius;
    }
    
    // Sert uniquement à des fins d'instanciation
    NoeudNonOriente() {}
    
    @Override
    public double[] getPositions() {
        return pos;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public Noeud creerNoeud(String nom, double[] pos, double radius) {
        return new NoeudNonOriente(nom, pos, radius);
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public void setNom(String value) {
        this.nom = value;
    }

    @Override
    public void setPositions(double[] positions) {
        this.pos = positions;
    }

    @Override
    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public void dessinerNoeud(AnchorPane zoneDessin) {
        this.circle = new Circle(pos[0], pos[1], radius);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);
        Label nomNoeud = new Label();
        nomNoeud.setText(this.nom);
        nomNoeud.setLayoutX(pos[0] - radius);
        nomNoeud.setLayoutY(pos[1] - (radius / 2.0));
        zoneDessin.getChildren().addAll(circle, nomNoeud);
    }
    
    @Override
    public String toString() {
        return "Nom : " +  nom + " positions : [" + pos[0] + " , " + pos[1] + "] radius : " + radius;
    }
    
    @Override
    public Circle getCircle() {
        return circle;
    }

}
