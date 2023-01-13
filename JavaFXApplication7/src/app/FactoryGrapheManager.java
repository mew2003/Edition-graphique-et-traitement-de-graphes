/*
 * Classe manager des factories de graphe 
 */
package app;

import java.util.HashMap;
import java.util.Set;

/**
 * Manager des factories de graphes, permet l'instanciation et la création
 * de toutes les factories de graphes disponibles.
 * @author Mewen
 */
public class FactoryGrapheManager {
    
    // Liste des factories de graphe
    private HashMap<String, FactoryGraphe> factories;

    // l'instance du graphe
    private static final FactoryGrapheManager instance = new FactoryGrapheManager();

    // Instantiation de toutes les factories de graphe
    private FactoryGrapheManager() {
        factories = new HashMap<>();
        factories.put("GrapheNonOriente", new FactoryGrapheNonOriente());
        factories.put("GrapheOriente", new FactoryGrapheOriente());
        factories.put("GrapheOrientePondere", new FactoryGrapheOrientePondere());
        factories.put("GrapheProbabiliste", new FactoryGrapheProbabiliste());
    }
    
    /**
     * Permet d'obtenir la liste de toutes les factories
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
        return instance;
    }
    
    /**
     * Permet de créer une factory parmi la liste de celles implémentées
     * @param type  chaîne de caractères correspondant au type de factory souhaité
     * @return la factory choisie / null si elle n'existe pas
     */
    public FactoryGraphe creerFactory(String type) {
        if (factories.get(type) != null) {
            return factories.get(type);
        }
        return null;
    }

}
