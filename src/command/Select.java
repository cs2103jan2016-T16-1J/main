package command;

import java.io.IOException;

import json.JSONException;
import main.Event;
import main.State;

public class Select implements Command{

	Event selectedParameters;
	
	public Select(Event selectedParameters){
		this.selectedParameters = selectedParameters;
	}
	
	@Override
	public State execute(State completeState) throws IOException, JSONException {
		// TODO Auto-generated method stub
		
		//new event list = eventlist.clone
		//for each event in 
		return null;
	}

	@Override
	public void updatedDisplayedEvents() {
		// TODO Auto-generated method stub
		
	}

}
