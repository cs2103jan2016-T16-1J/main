package command;

import java.io.IOException;

import json.JSONException;
import main.State;

public class ChangeTab implements Command{
	int newTab;
	State completeState;
	
	public ChangeTab(int newTab){
		this.newTab = newTab;
	}
	
	public State execute(State completeState) throws IOException, JSONException{
		
		return completeState;
	}

}
