package command;

import main.Event;
import main.State;

/**
 * Delete class must be instantiated with an Event object to delete
 * @author Reem
 *
 */
public class Delete implements Command{
	Event modifiedEvent;
	State completeState;

	/**
	 * Delete class constructor. 
	 * @param modifiedEvent the event that will be deleted
	 */
	public Delete(Event modifiedEvent){
		this.modifiedEvent = modifiedEvent;
	}
	
	/**
	 * Inherited from the Command interface
	 * execute will delete the event from its corresponding list based on status
	 * If two identical instances of the class are found in the list, only one is deleted
	 * @param completeState the state of all the tasks in the program
	 */
	public State execute(State completeState){
		
		this.completeState = completeState;
		
		switch(modifiedEvent.getStatus()){
		case COMPLETE:
			deleteFromCompleteList();
			break;
		case INCOMPLETE:
			deleteFromIncompleteList();
			break;
		case FLOATING:
			deleteFromFloatingList();
			break;
				
		}
		return completeState;
	}
	
	/**
	 * deletes the given task from the completed list in State
	 */
	public void deleteFromCompleteList(){
		completeState.completedTasks.remove(modifiedEvent);
	}
	
	/**
	 * deletes the given task from the incomplete list in State
	 */
	public void deleteFromIncompleteList(){
		completeState.incompletedTasks.remove(modifiedEvent);
	}
	
	/**
	 * deletes the given task from the floating list in STate
	 */
	public void deleteFromFloatingList(){
		completeState.incompletedTasks.remove(modifiedEvent);
	}
}
