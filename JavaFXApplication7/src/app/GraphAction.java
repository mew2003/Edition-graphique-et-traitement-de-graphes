package app;

public class GraphAction extends Action {
    private Graphe previousState;
    private Graphe nextState;

    public GraphAction(Graphe previousState, Graphe nextState) {
      this.previousState = previousState;
      this.nextState = nextState;
    }

    @Override
    public void redoAction() {
    	nextState.setEtat(nextState);
    }

    @Override
    public void undoAction() {
      previousState.setEtat(previousState);
    }

	@Override
	public Graphe getPreviousState() {
		return previousState;
	}

	@Override
	public Graphe getNextState() {
		return nextState;
	}
}
