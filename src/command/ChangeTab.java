package command;

import java.io.IOException;
import java.util.ArrayList;

import json.JSONException;
import main.GenericEvent.Status;
import main.GenericEvent;
import main.State;

public class ChangeTab implements Command{
	Status newTab;
	State completeState;
	
	public ChangeTab(Status newTab){
		this.newTab = newTab;
	}
	
	public State execute(State completeState) throws IOException, JSONException{
		this.completeState = completeState;
		
		completeState.setSelectedTab(newTab);
		filterSingleSelectedEventByTab();
		completeState.filterListsByTab();
		return completeState;
	}
	
	public boolean filterSingleSelectedEventByTab(){
		if(!completeState.hasSingleEventSelected()){
			return false;
		}
		completeState.filteredSelectedEvent = completeState.selectedEvent;
		
		if(completeState.filteredSelectedEvent.getStatus().equals(completeState.getSelectedTab())){
			completeState.setFilterStatus(State.ONE_EVENT_SELECTED);

			return true;
		}
		
		completeState.setFilterStatus(State.NO_EVENTS_SELECTED);
		completeState.filteredSelectedEvent = null;

		return false;
	}	
	
}
