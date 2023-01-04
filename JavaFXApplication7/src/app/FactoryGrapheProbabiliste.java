package app;

public class FactoryGrapheProbabiliste extends FactoryGraphe {

	@Override
	public Graphe creerGraphe() {
		return new GrapheProbabiliste();
	}

}
