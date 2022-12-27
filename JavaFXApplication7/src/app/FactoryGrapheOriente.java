package app;

public class FactoryGrapheOriente extends FactoryGraphe {
	
	public FactoryGrapheOriente() {}

	@Override
	public Graphe creerGraphe() {
		return new GrapheOriente();
	}

}
