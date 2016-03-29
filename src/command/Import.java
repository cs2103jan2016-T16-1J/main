package command;

import java.io.IOException;

import json.JSONException;
import main.Event;
import main.State;

public class Import implements Command{

	Event modifiedEvent;

	public Import(Event modifiedEvent){
		this.modifiedEvent = modifiedEvent;
	}
	@Override
	public State execute(State completeState) throws IOException, JSONException {
		// TODO Auto-generated method stub
		return null;
	}

}
