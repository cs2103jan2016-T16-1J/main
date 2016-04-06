package command;

import java.io.IOException;

import json.JSONException;
import main.GenericEvent;
import main.State;

public class Complete {
	GenericEvent modifiedEvent;
	State completeState;

	/**
	 * Add class constructor
	 * @param modifiedEvent the event that will be added
	 */
	public Complete(GenericEvent modifiedEvent){
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
		

		
		
		return completeState;
		
	}

}
