package command;

import java.io.IOException;

import json.JSONException;
import main.GenericEvent.Status;
import main.State;

public class ChangeTab implements Command{
	Status newTab;
	State completeState;
	
	public ChangeTab(Status newTab){
		this.newTab = newTab;
	}
	
	public State execute(State completeState) throws IOException, JSONException{
		
		return completeState;
	}

}
