package v1;

import java.util.HashMap;

public class FactoryGrapheManager {
    
    private HashMap<String, FactoryGraphe> factories;

    private static FactoryGrapheManager instance = new FactoryGrapheManager();

    private FactoryGrapheManager() {
        factories = new HashMap<>();
        factories.put("GrapheNonOriente", new FactoryGrapheNonOriente());
    }

    public static FactoryGrapheManager getInstance() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return instance;
    }
    
    public FactoryGraphe creerFactory(String type) {
        if (factories.get(type) != null) {
            return factories.get(type);
        } else {
            return null;
        }
    }

}
