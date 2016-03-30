package command;

import java.io.IOException;

import json.JSONException;
import main.Event;
import main.State;
import storage.Storage;

public class Export implements Command {
	State completeState;
	
	public Export(Event event) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public State execute(State completeState) throws IOException, JSONException {
		this.completeState = completeState;
		
		Storage storage = new Storage();
		//storage.changeDirectory(completeState.gnewDirectory);
		
		return null;
	}

}
