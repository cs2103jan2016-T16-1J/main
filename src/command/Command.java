package command;

import main.State;
import main.Event;

/**
 * Command interface- commands are defined by the action that the user provides
 * @author Reem
 *
 */
public interface Command {
	
	/**
	 * execute must be inherited by each command. This method invokes the command action
	 * @param completeState the state of all tasks in the program
	 * @return the state of all tasks in the program
	 */
	public State execute(State completeState);	
}
