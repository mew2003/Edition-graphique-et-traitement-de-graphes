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
        System.err.println(f == null);
        Graphe graphe = f.creerGraphe();
        double[] pos = {0.0, 0.0};
        graphe.creerNoeud("test", pos, 20);
    }

}
