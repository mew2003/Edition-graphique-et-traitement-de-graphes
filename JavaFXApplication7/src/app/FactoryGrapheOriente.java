/*
 * Factory de graphe orienté
 */
package app;

/**
 * Factory de graphe orienté
 */
public class FactoryGrapheOriente extends FactoryGraphe {
	
	@Override
	public Graphe creerGraphe() {
		return new GrapheOriente();
	}

}
