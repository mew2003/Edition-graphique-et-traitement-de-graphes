/*
 * Factory de graphe non oriente
 */
package app;

/**
 * Factory de graphe non oriente
 * @author Mewen
 */
public class FactoryGrapheNonOriente extends FactoryGraphe {

    public FactoryGrapheNonOriente() {}
    
    @Override
    public Graphe creerGraphe() {
        return new GrapheNonOriente();
    }

}
