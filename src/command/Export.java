package command;

import java.io.IOException;

import json.JSONException;
import main.Event;
import main.State;
import storage.Storage;

public class Export implements Command {

	Event modifiedEvent;
	State completeState;
	
	public Export(Event modifiedEvent){
		this.modifiedEvent = modifiedEvent;
	}

	@Override
	public State execute(State completeState) throws IOException, JSONException {
		this.completeState = completeState;
		
		Storage storage = new Storage();
		storage.exportDirectory(modifiedEvent.getName());
		
		return null;
	}

}
