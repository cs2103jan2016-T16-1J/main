package command;

import java.io.IOException;

import json.JSONException;
import main.Event;
import main.State;
import storage.Storage;

public class ChangeDir implements Command {

	Event modifiedEvent;
	State completeState;
	
	public ChangeDir(Event modifiedEvent){
		this.modifiedEvent = modifiedEvent;
	}

	@Override
	public State execute(State completeState) throws IOException, JSONException {
		this.completeState = completeState;
		
		Storage storage = new Storage();
		storage.changeDirectory(modifiedEvent.getName());
		
		return completeState;
	}

}
