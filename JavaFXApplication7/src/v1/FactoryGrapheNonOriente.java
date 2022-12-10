package v1;

import java.util.HashMap;
import java.util.Set;

/**
 * Factory de graphe non oriente
 * Pour rappel, un graphe non orienté doit respecter les principes suivants :
 * - Il peut y avoir N nombre de noeuds
 * - Il peut y avoir entre 0 et L = N(N+1)/2 nombre de liens
 * - Il ne peut pas y avoir de boucle (un lien qui part d'un noeud et revient à ce même noeud)
 * - Un noeud ne peut pas avoir plus de N-1 lien
 * - Deux liens ne peuvent partir du même noeud et aller vers un autre même noeud
 * @author Mewen
 */
public class FactoryGrapheNonOriente extends FactoryGraphe {
    
    private static final FactoryGrapheNonOriente instance = new FactoryGrapheNonOriente();
    
    // liste des noeuds / liens pouvant être instancié
    private HashMap<String, Noeud> typesNoeuds;
    private HashMap<String, Lien> typesLiens;

    /**
     * Renvoie une instanciation de la classe FactoryGrapheNonOriente
     * @return l'instanciation
     */
    public static FactoryGrapheNonOriente getInstance() {
        return instance;
    }
    
    // Instanciation de toutes les types de noeuds et de liens
    FactoryGrapheNonOriente() {
        typesNoeuds = new HashMap<>();
        typesLiens = new HashMap<>();
        typesNoeuds.put("NoeudNonOriente", new NoeudNonOriente());
        typesLiens.put("LienNonOriente", new LienNonOriente());
    }
    
    @Override
    public Set<String> getTypesNoeuds() {
        return typesNoeuds.keySet();
    }
    
    @Override
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
