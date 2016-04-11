package command;

import java.io.IOException;

import json.JSONException;
import main.State;

/**
 * Undos an action
 * Undo replaces the current state with the previous state.
 * @@author Reem Razak
 */
public class Undo implements Command{
	State completeState;

	@Override
	public State execute(State completeState) throws IOException, JSONException {
		// TODO Auto-generated method stub
		
		this.completeState = completeState;
		
		if(completeState.eventHistory.isEmpty() || completeState.eventHistory.size() < 2){
			return completeState;
		}
		
		completeState.eventHistory.pop();
		
		return completeState.eventHistory.pop();
	}

}
