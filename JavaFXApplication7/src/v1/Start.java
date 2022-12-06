package v1;

/**
 *
 * @author mewen.derruau
 */
public class Start {
    
    public static void main(String[] args) {
        FactoryGrapheManager factory = FactoryGrapheManager.getInstance();
        FactoryGraphe f = factory.creerFactory("GrapheNonOriente");
        System.out.println(f.toString());
    }
    
}
