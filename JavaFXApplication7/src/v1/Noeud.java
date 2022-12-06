package v1;

public abstract class Noeud {

    private String nom;

    private double[] pos;

    private double radius;

    public String getNom() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.nom;
    }

    public void setNom(String value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.nom = value;
    }
 
    public List<Double> getPositions() {
    }

    public void setPositions(List<Double> positions) {
    }

    public double getRadius() {
    }

    public void setRadius(double radius) {
    }

    public void dessinerNoeud() {
    }

    public Noeud(String nom, List<Double> pos, double radius);

}
