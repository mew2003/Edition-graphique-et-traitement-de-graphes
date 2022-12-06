package v1;

import java.util.HashMap;
import java.util.Set;

public class FactoryGrapheNonOriente extends FactoryGraphe {
    
    private static FactoryGrapheNonOriente instance = new FactoryGrapheNonOriente();
    
    private HashMap<String, Noeud> typesNoeuds;

    public static FactoryGrapheNonOriente getInstance() {
        return instance;
    }
    
    FactoryGrapheNonOriente() {
        typesNoeuds = new HashMap<>();
        typesNoeuds.put("NoeudNonOriente", new NoeudNonOriente());
    }
    
    public Set<String> getTypesNoeuds() {
        return typesNoeuds.keySet();
    }

    public Noeud creerNoeud(String type, String nom, double[] pos, double radius) {
        if (typesNoeuds.get(type) != null) {
            return typesNoeuds.get(type).creerNoeud(nom, pos, radius);
        } else {
            return null;
        }
    }

    @Override
    public Lien creerLien(Noeud[] noeuds) {
    }
    
    @Override
    public String toString() {
        return "NoeudNonOriente";
    }

}
