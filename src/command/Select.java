package command;

import java.io.IOException;
import java.util.Date;

import constant.Constant;

import java.util.ArrayList;

import json.JSONException;
import main.Event;
import main.GenericEvent;
import main.GenericEvent.Category;
import main.GenericEvent.Status;
import main.State;

public class Select implements Command{

	Event selectedParameters;
	State completeState;
	
	boolean selectByIndex;
	int index;
	
	public Select(Event selectedParameters){
		this.selectedParameters = selectedParameters;
		selectByIndex = false;

	}
	
	public Select(int index){
		this.index = index -1;
		selectByIndex = true;
	}
	
	@Override
	public State execute(State completeState) throws IOException, JSONException {
		// TODO Auto-generated method stub
		this.completeState = completeState;
				
		ArrayList<GenericEvent> allEvents = completeState.getAllEvents();
		///for each event in allEvents check if it matches selectedParameters
		//if the event does, clone it and add it to completeState.selectedEvents
		if(selectByIndex){
			selectViaIndex();
		}
		else{
			getMatchingEvents(allEvents);
			checkSelectionStatus();
		}
		return completeState;
	}

	private boolean selectViaIndex(){
		//if there are no events selected or if there is already only one event selected, don't do anything
		if(!completeState.hasEventSelected() || completeState.hasSingleEventSelected()){
			return false;
		}

		//if an invalid index is provided
		if(index > completeState.selectedEvents.size()){
			completeState.setStatusMessage(State.MESSAGE_INVALID_INDEX);
			completeState.hasErrorMessage = true;
			return false;
		}
		
		completeState.setOneSelectedEvent(completeState.selectedEvents.get(index));
		
		completeState.selectedEvents.clear();
		completeState.selectedEvents.add(completeState.selectedEvent);
		completeState.setSelectionStatus(State.ONE_EVENT_SELECTED);
		
		return true;
	}
	
	private void checkSelectionStatus(){
		
		if(completeState.selectedEvents.isEmpty() == true){
			completeState.setSelectionStatus(State.NO_EVENTS_SELECTED);
			completeState.setFilterStatus(State.NO_EVENTS_SELECTED);
		}
		else if(completeState.selectedEvents.size() > 1){
			completeState.setSelectionStatus(State.MULTIPLE_EVENTS_SELECTED);
			completeState.setFilterStatus(State.MULTIPLE_EVENTS_SELECTED);
		}
		else{
			completeState.setOneSelectedEvent(completeState.selectedEvents.get(0));
		}
	}	

	private void getMatchingEvents(ArrayList<GenericEvent> allEvents){
		completeState.clearSelections();
	
		for(GenericEvent e: allEvents){
			if(isMatchingEvent(e)){
				completeState.selectedEvents.add(e);
				
			}
		}	
	}
	
	private boolean isMatchingEvent(GenericEvent currentEvent){
		boolean isMatch = true;
				
		isMatch = isStringMatching(currentEvent.getName(), selectedParameters.getName()) &&
				isStringMatching(currentEvent.getLocation(), selectedParameters.getLocation()) &&
				isStringMatching(currentEvent.getDescription(), selectedParameters.getDescription())
				//&&
				//isCategoryMatching(currentEvent.getCategory(), selectedParameters.getCategory()) &&
				//isTimeMatching(currentEvent.getStartTime(), currentEvent.getEndTime(), selectedParameters.getStartTime(), selectedParameters.getEndTime())
				//&& isStatusMatching(currentEvent.getStatus(), selectedParameters.getStatus())
				;
						
		return isMatch;
		
	}
	
	private boolean isCategoryMatching(GenericEvent.Category eventCategory, GenericEvent.Category paramCategory){
		if(paramCategory.equals(Constant.CATEGORY_NULL)){
			return true;
		}
		return eventCategory == paramCategory;
		
	}
	
	private boolean isStatusMatching(GenericEvent.Status eventStatus, GenericEvent.Status paramStatus){
		if(paramStatus.equals(Constant.STATUS_NULL)){
			return true;
		}
		return eventStatus == paramStatus;
		
	}
	
	private boolean isStringMatching(String eventString, String paramString){
		boolean isMatch = true;
		
		if(paramString.equals(Constant.EMPTY_STRING)){
			return isMatch;
		}
		
		isMatch = eventString.toLowerCase().contains(paramString.toLowerCase());
		
		return isMatch;
	}
	
	private boolean isTimeMatching(Date eventStart, Date eventEnd, Date paramStart, Date paramEnd){
		boolean isStartMatch;
		boolean isEndMatch;
		
		isStartMatch = isStartTimeWithinRange(eventStart, paramStart, paramEnd);
		if(paramStart.equals(Constant.MIN_DATE)){
			isStartMatch = true;
		}
		
		isEndMatch = isEndTimeWithinRange(eventEnd, paramStart, paramEnd);
		
		if(paramStart.equals(Constant.MAX_DATE)){
			isEndMatch = true;
		}
		
		
		return isStartMatch && isEndMatch;
	}
	
	public boolean isStartTimeWithinRange(Date eventStart, Date paramStart, Date paramEnd){
		
		return ((eventStart.getTime() >= paramStart.getTime()) && (eventStart.getTime() <= paramEnd.getTime()));
	}
	
	private boolean isEndTimeWithinRange(Date eventEnd, Date paramStart, Date paramEnd){
		
		return ((eventEnd.getTime() >= paramStart.getTime()) && (eventEnd.getTime() <= paramEnd.getTime()));
	}
	


}