package v1;

public class NoeudNonOriente extends Noeud {

    private String nom;

    private double[] pos;

    private double radius;
    
    public NoeudNonOriente(String nom, double[] pos, double radius) {
        this.nom = nom;
        this.pos = pos;
        this.radius = radius;
    }
    
    public NoeudNonOriente() {
        
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
    public void dessinerNoeud() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public String toString() {
        return "Nom : " +  nom + " positions : [" + pos[0] + " , " + pos[1] + "] radius : " + radius;
    }

}
