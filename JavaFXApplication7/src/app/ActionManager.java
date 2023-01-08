package app;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ActionManager {
    private List<Action> actions;
    private int index;

    public ActionManager() {
        actions = new ArrayList<>();
        index = 0;
    }

    public void executeAction(Action action) {
        while (index < actions.size()) {
            actions.remove(index);
        }
        actions.add(action);
        index++;
        action.redoAction();
    }

    public Graphe undoAction() {
        if (index > 0) {
            index--;
            Action action = actions.get(index);
            action.undoAction();
            return action.getPreviousState();
        }
        return null;
    }

    public Graphe redoAction() {
        if (index < actions.size()) {
            Action action = actions.get(index);
            index++;
            action.redoAction();
            return action.getNextState();
        }
        return null;
    }
    
    public List<Action> getAction() {
    	return this.actions;
    }
    
    @Override
    public String toString() {
    	return(actions.toString());
    }
}
