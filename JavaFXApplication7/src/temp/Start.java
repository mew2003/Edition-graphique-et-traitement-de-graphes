package temp;

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
        Noeud noeud1 = graphe.creerNoeud(pos);
        Noeud noeud2 = graphe.creerNoeud(pos);
        graphe.creerLien(noeud1, noeud2);
        System.out.println(graphe);
    }

}
