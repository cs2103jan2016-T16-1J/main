package command;

import java.io.IOException;

import json.JSONException;
import main.Event;
import main.State;
import storage.Storage;

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
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public State execute(State completeState) throws IOException, JSONException{
		
		this.completeState = completeState;
		
		switch (modifiedEvent.getStatus()){
		case COMPLETE:
			addToCompleteList();
			Storage.addToStorage(modifiedEvent);
			break;
		case INCOMPLETE:
			addToIncompleteList();
			Storage.addToStorage(modifiedEvent);
			break;
		case FLOATING:
			addToFloatingList();
			Storage.addToStorage(modifiedEvent);
			break;
		}

		updatedDisplayedEvents();
		
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
