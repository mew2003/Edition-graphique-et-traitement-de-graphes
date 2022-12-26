/*
 * Classe manager des factory de graphe 
 */
package app;

import java.util.HashMap;
import java.util.Set;

/**
 * Manager des factories de graphes, permet l'instantiation et la création
 * de toutes les factories de graphes disponibles.
 * @author Mewen
 */
public class FactoryGrapheManager {
    
    // Liste des factories de graphe
    private HashMap<String, FactoryGraphe> factories;

    private static final FactoryGrapheManager instance = new FactoryGrapheManager();

    // Instantiation de toutes les factories de graphe
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
     * Renvoie une instantiation de la classe FactoryGrapheManager
     * @return l'instanciation
     */
    public static FactoryGrapheManager getInstance() {
        return instance;
    }
    
    /**
     * Permet de créer un factory parmi la liste de celle implémenté
     * @param type chaîne de caractères correspondant au type de factory souhaité
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
