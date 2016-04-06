package command;

import java.io.IOException;
import java.util.ArrayList;

import json.JSONException;
import main.GenericEvent;
import main.GenericEvent.Category;
import main.ReservedEvent;
import main.State;

/**
 * Add class must be instantiated with an event to add
 * @author Reem
 *
 */
public class Reserve implements Command{

	GenericEvent modifiedEvent;
	State completeState;

	/**
	 * Add class constructor
	 * @param modifiedEvent the event that will be added
	 */
	public Reserve(GenericEvent modifiedEvent){
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
			case UNDETERMINED:
				addToUndeterminedList();
				break;
			default:
				completeState.setStatusMessage(State.MESSAGE_INVALID_RESERVED);
				completeState.hasErrorMessage = true;

				break;
							
			}
		
		if(!completeState.hasErrorMessage){
			completeState.setOneSelectedEvent(modifiedEvent);
			
			completeState.updateDisplayedEvents();
		}
		
		return completeState;
	}


	private boolean isNewEventValid(){
		if(null == modifiedEvent){
			completeState.setStatusMessage(State.MESSAGE_PARSE_ERROR);
			return false;
		}
		
		return true;
	}
	
	public void addToUndeterminedList(){

		if(modifiedEvent.getCategory() == Category.FLOATING){
			
			completeState.setStatusMessage(State.MESSAGE_ATTEMPTED_ADD_WITH_RESERVE);
			completeState.hasErrorMessage = true;

		} else{
			completeState.addToReservedList((ReservedEvent)modifiedEvent);
		}

	}


}
