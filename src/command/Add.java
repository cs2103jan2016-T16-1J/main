package command;

import java.io.IOException;

import json.JSONException;
import main.Event;
import main.GenericEvent;
import main.GenericEvent.Category;
import main.ReservedEvent;
import main.State;

/**
 * Creates a new event
 * Add class must be instantiated with an event to add
 * @@author Reem Razak
 */
public class Add implements Command{

	GenericEvent modifiedEvent;
	State completeState;

	/**
	 * Add class constructor
	 * @param modifiedEvent the event that will be added
	 */
	public Add(GenericEvent modifiedEvent){
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
		
		if(!isNewEventValid()){
			return completeState;
		}
		
		switch (modifiedEvent.getStatus()){
		case COMPLETE:
			addToCompleteList();
			break;
		case INCOMPLETE:
			addToIncompleteList();
			break;			
		case UNDETERMINED:
			addToUndeterminedList();
			break;
		}
		
		/*to select the previously added or reserved event*/
		if(!completeState.hasErrorMessage){
			completeState.setOneSelectedEvent(modifiedEvent);
			
			updatedDisplayedEvents();
		}
		
		return completeState;
	}


	private boolean isNewEventValid(){
		if(null == modifiedEvent){
			completeState.setStatusMessage(State.MESSAGE_PARSE_ERROR);
			completeState.hasErrorMessage = true;
			return false;
		}
		
		return true;
	}
	/**
	 * adds the given task to the completed list in State
	 */
	public void addToCompleteList(){
		completeState.addToCompletedList((Event)modifiedEvent);
	}

	/**
	 * adds the given task to the incomplete list in State
	 */
	public void addToIncompleteList(){
		completeState.addToIncompletedList((Event)modifiedEvent);
	}

	public void addToUndeterminedList(){
		
		if(modifiedEvent.getCategory() == Category.FLOATING){
			
			completeState.addToUndeterminedList((ReservedEvent) modifiedEvent);
		} else{
			
			completeState.setStatusMessage(State.MESSAGE_ATTEMPTED_RESERVE_WITH_ADD);
			completeState.hasErrorMessage = true;

		}
	}
	
	/**
	 * updates the displayedEvents with new information
	 */
	public void updatedDisplayedEvents(){
		completeState.displayedEvents.clear();
		completeState.displayedEvents.addAll(completeState.getCompletedList());
		completeState.displayedEvents.addAll(completeState.getIncompletedList());
		completeState.displayedEvents.addAll(completeState.getReservedList());
		completeState.displayedEvents.addAll(completeState.getUndeterminedList());
	}

}
