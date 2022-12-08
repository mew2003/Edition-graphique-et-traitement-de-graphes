package v1;

public abstract class Noeud {

    public abstract String getNom();

    public abstract void setNom(String value);
 
    public abstract double[] getPositions();

    public abstract void setPositions(double[] positions);

    public abstract double getRadius();

    public abstract void setRadius(double radius);

    public abstract void dessinerNoeud();

    public abstract Noeud creerNoeud(String nom, double[] pos, double radius);

}
