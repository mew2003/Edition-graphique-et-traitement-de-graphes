package v1;

import java.util.HashMap;
import java.util.Set;

public class FactoryGrapheNonOriente extends FactoryGraphe {
    
    private static FactoryGrapheNonOriente instance = new FactoryGrapheNonOriente();
    
    private HashMap<String, Noeud> typesNoeuds;
    private HashMap<String, Lien> typesLiens;

    public static FactoryGrapheNonOriente getInstance() {
        return instance;
    }
    
    FactoryGrapheNonOriente() {
        typesNoeuds = new HashMap<>();
        typesLiens = new HashMap<>();
        typesNoeuds.put("NoeudNonOriente", new NoeudNonOriente());
        typesLiens.put("LienNonOriente", new LienNonOriente());
    }
    
    public Set<String> getTypesNoeuds() {
        return typesNoeuds.keySet();
    }
    
    public Set<String> getTypesLiens() {
        return typesLiens.keySet();
    }

    @Override
    public Noeud creerNoeud(String type, String nom, double[] pos, double radius) {
        if (typesNoeuds.get(type) != null) {
            return typesNoeuds.get(type).creerNoeud(nom, pos, radius);
        } else {
            return null;
        }
    }

    @Override
    public Lien creerLien(String type, Noeud[] noeuds) {
        if (typesLiens.get(type) != null) {
            return typesLiens.get(type).creerLien(noeuds);
        } else {
            return null;
        }
    }
    
    @Override
    public String toString() {
        return "NoeudNonOriente";
    }

}
