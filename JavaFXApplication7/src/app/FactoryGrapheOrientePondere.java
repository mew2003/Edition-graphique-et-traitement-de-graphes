/*
 * Factory de graphe orienté pondéré
 */
package app;

/**
 * Factory de graphe orienté pondéré
 */
public class FactoryGrapheOrientePondere extends FactoryGraphe {

	@Override
	public Graphe creerGraphe() {
		return new GrapheOrientePondere();
	}

}
