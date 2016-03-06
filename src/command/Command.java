package command;

import state.State;
import main.Event;

public interface Command {
	public State execute(State completeState);
}
