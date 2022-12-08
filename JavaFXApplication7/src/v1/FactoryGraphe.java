package v1;

import java.util.Set;

public abstract class FactoryGraphe {
    
    public abstract Noeud creerNoeud(String type, String nom, double[] pos, double radius);

    public abstract Lien creerLien(String type, Noeud[] noeuds);
    
    public abstract Set<String> getTypesNoeuds();
    
    public abstract Set<String> getTypesLiens();

}
