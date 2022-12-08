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
        double[] posTest = {0.0, 0.0};
        Noeud noeud1 = f.creerNoeud("NoeudNonOriente", "salut", posTest, 5);
        System.out.println(noeud1.toString());
        Noeud noeud2 = f.creerNoeud("NoeudNonOriente", "Jean-michel", posTest, 3);
        Noeud[] lesNoeuds = {noeud1, noeud2};
        Noeud[] lesNoeudsInv = {noeud2, noeud1};
        Noeud[] lesMemeNoeuds = {noeud1, noeud1};
        Lien lien1 = f.creerLien("LienNonOriente", lesNoeuds);
        Lien lien1Inv = f.creerLien("LienNonOriente", lesNoeudsInv);
        Lien boucle = f.creerLien("LienNonOriente", lesMemeNoeuds);
        System.out.println(lien1.toString());
        System.out.println(lien1Inv.toString());
        System.out.println(boucle.toString());
    }

}
