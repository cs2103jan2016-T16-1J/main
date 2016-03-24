package command;

import json.JSONException;
import main.Event;
import main.State;
import storage.Storage;

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
	 * @throws JSONException 
	 */
	public State execute(State completeState) throws JSONException{
		boolean successfullyDeleted = false;
		this.completeState = completeState;
		
		switch(modifiedEvent.getStatus()){
		case COMPLETE:
			successfullyDeleted = deleteFromCompleteList();
			break;
		case INCOMPLETE:
			successfullyDeleted = deleteFromIncompleteList();
			break;
		case FLOATING:
			successfullyDeleted = deleteFromFloatingList();
			break;
		}
		
		if(!successfullyDeleted){
			completeState.setStatusMessage(State.MESSAGE_EVENT_NOT_FOUND);
		}
		
		updatedDisplayedEvents();

		return completeState;
	}
	
	/**
	 * deletes the given task from the completed list in State
	 * @return whether or not the event was found and deleted
	 */
	public boolean deleteFromCompleteList(){
		return completeState.completedEvents.remove(modifiedEvent);
	}
	
	/**
	 * deletes the given task from the incomplete list in State
	 * @return whether or not the event was found and deleted
	 */
	public boolean deleteFromIncompleteList(){
		return completeState.incompletedEvents.remove(modifiedEvent);
	}
	
	/**
	 * deletes the given task from the floating list in State
	 * @return whether or not the event was found and deleted
	 */
	public boolean deleteFromFloatingList(){
		return completeState.incompletedEvents.remove(modifiedEvent);
	}
	
	
	/**
	 * updates the displayedEvents with new information
	 */
	public void updatedDisplayedEvents(){
		completeState.displayedEvents.clear();
		completeState.displayedEvents.addAll(completeState.completedEvents);
		completeState.displayedEvents.addAll(completeState.incompletedEvents);
		completeState.displayedEvents.addAll(completeState.floatingEvents);		
	}
}
