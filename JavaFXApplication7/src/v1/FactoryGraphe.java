package v1;

public abstract class FactoryGraphe {
    
    public abstract Noeud creerNoeud(String type, String nom, double[] pos, double radius);

    public abstract Lien creerLien(Noeud[] noeuds);

}
