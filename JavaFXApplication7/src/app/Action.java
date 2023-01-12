package app;

public abstract class Action {

    public abstract Graphe getPreviousState();
    public abstract Graphe getNextState();
	/* Rétablit une action */
	public abstract void redoAction();
	/* Annule une action */
    public abstract void undoAction();
}
