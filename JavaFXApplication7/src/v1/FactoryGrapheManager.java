package v1;

import java.util.HashMap;
import java.util.Set;

/**
 * Manager des factories de graphes, permet l'instanciation et la création
 * de tout les graphes saisies dans la factories.
 * @author Mewen
 */
public class FactoryGrapheManager {
    
    // Liste des factory de graphe
    private HashMap<String, FactoryGraphe> factories;

    private static final FactoryGrapheManager instance = new FactoryGrapheManager();

    // Instanciation de toutes les factories de graphe
    private FactoryGrapheManager() {
        factories = new HashMap<>();
        factories.put("GrapheNonOriente", new FactoryGrapheNonOriente());
    }
    
    /**
     * Permet d'obtenir la liste de toute les factories
     * @return la liste des factories
     */
    public Set<String> getRegiteredFactories() {
        return factories.keySet();
    }

    /**
     * Renvoie une instanciation de la classe FactoryGrapheManager
     * @return l'instanciation
     */
    public static FactoryGrapheManager getInstance() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return instance;
    }
    
    /**
     * Permet de créer un factory parmis la liste de celle implémenté
     * @param type chaîne de caractères correspondant au type de graphe souhaité
     * @return la factory choisis
     */
    public FactoryGraphe creerFactory(String type) {
        if (factories.get(type) != null) {
            return factories.get(type);
        } else {
            return null;
        }
    }

}
