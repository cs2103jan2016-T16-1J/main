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
		
		completeState.completedEvents  = 		completeState.completedEvents;
		completeState.displayedEvents = 		completeState.displayedEvents;
		completeState.incompletedEvents = completeState.incompletedEvents;
		
		completeState.setSelectedTab(newTab);
		return completeState;
	}
}
