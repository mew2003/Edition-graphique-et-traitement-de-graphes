/*
 * Factory de graphe probabiliste
 */
package app;

/**
 * Factory de graphe probabiliste
 */
public class FactoryGrapheProbabiliste extends FactoryGraphe {

	@Override
	public Graphe creerGraphe() {
		return new GrapheProbabiliste();
	}

}
