package app;

public class FactoryGrapheOrientePondere extends FactoryGraphe {

	@Override
	public Graphe creerGraphe() {
		return new GrapheOrientePondere();
	}

}
