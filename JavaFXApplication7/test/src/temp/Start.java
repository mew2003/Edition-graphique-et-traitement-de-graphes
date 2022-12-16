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
        double[] pos = {0.0, 0.0};
        graphe.creerNoeud("noeud1", pos, 0);
        graphe.creerNoeud("noeud2", pos, 0);
        graphe.creerLien("noeud1", "noeud2");
        System.out.println(graphe);
    }

}
