package command;

import java.io.IOException;

import json.JSONException;
import main.State;

public class Undo implements Command{
	//@@author Reem

	State completeState;

	@Override
	public State execute(State completeState) throws IOException, JSONException {
		// TODO Auto-generated method stub
		
		this.completeState = completeState;
		
		if(completeState.eventHistory.isEmpty()){
			return completeState;
		}
		
		
		return completeState.eventHistory.pop();
	}

}
