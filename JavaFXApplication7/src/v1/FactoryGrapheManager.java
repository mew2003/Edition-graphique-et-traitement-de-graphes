package v1;

import java.util.ArrayList;
import java.util.List;

public class FactoryGrapheManager {
    
    private List<FactoryGraphe> factories = new ArrayList<FactoryGraphe> ();

    private List<FactoryGrapheManager> instance = new ArrayList<FactoryGrapheManager> ();

    private FactoryGrapheManager() {
    }

    public static FactoryGrapheManager getInstance() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.instance;
    }

}
