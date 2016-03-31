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
		if(newTab == Status.COMPLETE){
			completeState.isCompletedSelected();
		} else if(newTab == Status.INCOMPLETE){
			completeState.isIncompletedSelected();
		} else if(newTab == Status.UNDETERMINED){
			completeState.isUndeterminedSelected();
		}
		return completeState;
	}

}
