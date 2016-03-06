package command;

import main.Event;
import main.State;

/**
 * Add class must be instantiated with an event to add
 * @author Reem
 *
 */
public class Add implements Command{

	Event modifiedEvent;
	State completeState;

	/**
	 * Add class constructor
	 * @param modifiedEvent the event that will be added
	 */
	public Add(Event modifiedEvent){
		this.modifiedEvent = modifiedEvent;
	}

	/**
	 * Inherited from the Command interface
	 * execute will add the event to its corresponding list based on status
	 * @param completeState the state of all the tasks in the program
	 */
	public State execute(State completeState){
		
		this.completeState = completeState;
		
		switch (modifiedEvent.getStatus()){
		case COMPLETE:
			addToCompleteList();
			break;
		case INCOMPLETE:
			addToIncompleteList();
			break;
		case FLOATING:
			addToFloatingList();
			break;
		}
		
		return completeState;
	}

	/**
	 * adds the given task to the completed list in State
	 */
	public void addToCompleteList(){
		completeState.completedEvents.add(modifiedEvent);
	}

	/**
	 * adds the given task to the incomplete list in State
	 */
	public void addToIncompleteList(){
		completeState.incompletedEvents.add(modifiedEvent);
	}

	/**
	 * adds the given task to the floating list in State
	 */
	public void addToFloatingList(){
		completeState.floatingEvents.add(modifiedEvent);
		
	}
}
