package temp;

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
    public String toString() {
        return "Nom : " +  nom + " positions : [" + pos[0] + " , " + pos[1] + "] radius : " + radius;
    }
    
    public NoeudNonOriente(String nom, double[] pos, double radius) {
        this.nom = nom;
        this.pos = pos;
        this.radius = radius;
    }

}
