package command;

import java.io.IOException;

import json.JSONException;
import main.Event;
import main.GenericEvent;
import main.GenericEvent.Status;
import main.State;

/**
 * Complete changes an incompleted event to a completed event
 * @@author Reem Razak
 */
public class Complete implements Command{

	GenericEvent modifiedEvent;
	State completeState;

	public Complete(){}
	

	/**
	 * Inherited from the Command interface
	 * execute will edit the event from its corresponding list based on status
	 * @param completeState the state of all the tasks in the program
	 */
	public State execute(State completeState){
		this.completeState = completeState;
		
		if((!checkSelectedEvent()) || (!checkEventType())){
			return completeState;
		}
		
		modifiedEvent.setStatus(Status.COMPLETE);
		
		
		return completeState;
		
	}

	public boolean checkEventType(){
		if(!(modifiedEvent instanceof Event)){
			completeState.setErrorMessage(State.MESSAGE_COMPLETE_INVALID_EVENT_TYPE);
			return false;
		}
		if(modifiedEvent.getStatus() != Status.INCOMPLETE){
			completeState.setErrorMessage(State.MESSAGE_COMPLETE_INVALID_EVENT_STATUS);
			return false;
		}
		
		return true;
	}
	
	public boolean checkSelectedEvent(){
		
		if(!completeState.hasSingleEventSelected()){
			completeState.setErrorMessage(State.MESSAGE_EVENT_NOT_FOUND);
			return false;
		}
		
		modifiedEvent = completeState.getSingleSelectedEvent();

		
		return true;
		
	}
}
