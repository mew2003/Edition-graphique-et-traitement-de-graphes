import java.util.HashMap;
import java.util.Set;
import javafxapplication7.source.Noeud;
import javafxapplication7.source.NoeudRectangle;
import javafxapplication7.source.NoeudRond;

public class FactoryGraphes {
    private HashMap<String, Noeud> noeudsTypes;
    private static FactoryGraphes instance = new FactoryGraphes();

    public static FactoryGraphes getInstance() {
        return instance;
    }
    private FactoryGraphes() {
        noeudsTypes = new HashMap();
        noeudsTypes.put("Noeud Rectangulaire", new NoeudRectangle());
        noeudsTypes.put("Noeud circulaire", new NoeudRond());
    }

    public Set<String> getRegistredTypes() {
        return noeudsTypes.keySet();
    }

    public Noeud creerNoeud(String type, Enveloppe enveloppe) {
        if (noeudsTypes.get(type) != null) {
            return noeudsTypes.get(type).creerNoeud(enveloppe);
        } else {
            return null;
        }
    }
    
}
