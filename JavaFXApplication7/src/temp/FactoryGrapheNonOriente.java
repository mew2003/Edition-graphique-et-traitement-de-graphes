package temp;

import java.util.HashMap;
import java.util.Set;

/**
 * Factory de graphe non oriente
 * Pour rappel, un graphe non orienté doit respecter les principes suivants :
 * - Il peut y avoir N nombre de noeuds
 * - Il peut y avoir entre 0 et L = N(N+1)/2 nombre de liens
 * - Il ne peut pas y avoir de boucle (un lien qui part d'un noeud et revient à ce même noeud)
 * - Un noeud ne peut pas avoir plus de N-1 lien
 * - Deux liens ne peuvent partir du même noeud et aller vers un autre même noeud
 * @author Mewen
 */
public class FactoryGrapheNonOriente extends FactoryGraphe {

    public FactoryGrapheNonOriente() {
    }
    
    @Override
    public Graphe creerGraphe() {
        return new GrapheNonOriente();
    }

}
