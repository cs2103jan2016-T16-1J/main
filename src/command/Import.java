package command;

import java.io.IOException;

import json.JSONException;
import main.Event;
import main.State;
import storage.Storage;

public class Import implements Command {

	Event modifiedEvent;
	State completeState;
	
	public Import(Event modifiedEvent){
		this.modifiedEvent = modifiedEvent;
	}

	@Override
	public State execute(State completeState) throws IOException, JSONException {
		//this.completeState = completeState;
		State newState = new State();
		
		Storage storage = new Storage();
		storage.importDir(modifiedEvent.getName());
		newState = storage.readStorage(modifiedEvent.getName());
		
		return newState;
	}

}
