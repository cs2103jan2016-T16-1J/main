package command;

import java.io.IOException;
import java.util.Date;

import constant.Constant;

import java.util.ArrayList;

import json.JSONException;
import main.Event;
import main.Event.Status;
import main.State;

public class Select implements Command{

	Event selectedParameters;
	State completeState;
	
	public Select(Event selectedParameters){
		this.selectedParameters = selectedParameters;

	}
	
	@Override
	public State execute(State completeState) throws IOException, JSONException {
		// TODO Auto-generated method stub
		this.completeState = completeState;
		
		completeState.clearSelections();
		
		ArrayList<Event> allEvents = completeState.getAllEvents();
		///for each event in allEvents check if it matches selectedParameters
		//if the event does, clone it and add it to completeState.selectedEvents
		getMatchingEvents(allEvents);
		checkSelectionStatus();
		//new event list = eventlist.clone
		//for each event in 
		return null;
	}

	
	private void checkSelectionStatus(){
		
		if(completeState.selectedEvents.isEmpty() == true){
			completeState.setSelectionStatus(completeState.NO_EVENTS_SELECTED);;
		}
		else if(completeState.selectedEvents.size() > 1){
			completeState.setSelectionStatus(completeState.MULTIPLE_EVENTS_SELECTED);
		}
		else{
			completeState.setSelectionStatus(completeState.ONE_EVENT_SELECTED);
			completeState.selectedEvent = completeState.selectedEvents.get(0);
		}
	}
	
	//getMatchingEvents
	

	private void getMatchingEvents(ArrayList<Event> allEvents){
	
		for(Event e: allEvents){
			if(isMatchingEvent(e)){
				completeState.selectedEvents.add(e);
				
			}
		}	
	}
	
	private boolean isMatchingEvent(Event currentEvent){
		boolean isMatch = true;
		
		
		isMatch = isStringMatching(currentEvent.getName(), selectedParameters.getName()) &&
				isStringMatching(currentEvent.getLocation(), selectedParameters.getLocation()) &&
				isStringMatching(currentEvent.getDescription(), selectedParameters.getDescription())
				&&
				//isStringMatching(currentEvent.getCategory(), selectedParameters.getCategory()) &&
				isTimeMatching(currentEvent.getStartTime(), currentEvent.getEndTime(), selectedParameters.getStartTime(), selectedParameters.getEndTime())
				//&& isStatusMatching(currentEvent.getStatus(), selectedParameters.getStatus())
				;
		
		
		
		return isMatch;
		
	}
	
	private boolean isStatusMatching(Status eventStatus, Status paramStatus){
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
	
	
	
	//isEventmatching
	@Override
	public void updatedDisplayedEvents() {
		// TODO Auto-generated method stub
		
	}

}
