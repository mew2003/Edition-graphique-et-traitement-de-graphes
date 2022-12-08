package v1;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class NoeudNonOriente extends Noeud {

    private String nom;

    private double[] pos;

    private double radius;
    
    private Circle circle;
    
    public NoeudNonOriente(String nom, double[] pos, double radius) {
        this.nom = nom;
        this.pos = pos;
        this.radius = radius;
    }
    
    public NoeudNonOriente() {}
    
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
        zoneDessin.getChildren().addAll(circle);
    }
    
    @Override
    public String toString() {
        return "Nom : " +  nom + " positions : [" + pos[0] + " , " + pos[1] + "] radius : " + radius;
    }
    
    public Circle getCircle() {
        return circle;
    }

}
