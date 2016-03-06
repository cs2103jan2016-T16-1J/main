package command;

import state.CompleteState;
import main.Event;

public interface Command {
	public CompleteState execute(CompleteState completeState);
}
