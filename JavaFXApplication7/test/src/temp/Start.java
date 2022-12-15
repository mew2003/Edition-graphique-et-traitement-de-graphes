package temp;

import temp.*;

/**
 *
 * @author mewen.derruau
 */
public class Start {
    
    public static void main(String[] args) {
        FactoryGrapheManager manager = FactoryGrapheManager.getInstance();
        FactoryGraphe f = manager.creerFactory("GrapheNonOriente");
        Graphe graphe = f.creerGraphe();
        System.out.println(graphe.toString());
    }

}
