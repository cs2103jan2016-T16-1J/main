package command;

import java.io.IOException;
import java.util.Date;
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
		completeState.selectedEvents.clear();
		completeState.selectedEvent = null;

	}
	
	@Override
	public State execute(State completeState) throws IOException, JSONException {
		// TODO Auto-generated method stub
		
		this.completeState = completeState;
		ArrayList<Event> allEvents = completeState.getAllEvents();
		///for each event in allEvents check if it matches selectedParameters
		//if the event does, clone it and add it completeState.to selectedEvents
		getMatchingEvents(allEvents);
		
		
		
		
		//new event list = eventlist.clone
		//for each event in 
		return null;
	}

	//getMatchingEvents
	
	public void getMatchingEvents(ArrayList<Event> allEvents){
		
		
		for(Event e: allEvents){
			if(isMatchingEvent(e)){
				completeState.selectedEvents.add(e);
				
			}
		}
		
		
	}
	
	public boolean isMatchingEvent(Event currentEvent){
		boolean isMatch = true;
		
		
		isMatch = isStringMatching(currentEvent.getName(), selectedParameters.getName()) &&
				isStringMatching(currentEvent.getLocation(), selectedParameters.getLocation()) &&
				isStringMatching(currentEvent.getDescription(), selectedParameters.getDescription()) &&
				isStringMatching(currentEvent.getCategory(), selectedParameters.getCategory()) &&
				isTimeMatching(currentEvent.getStartTime(), currentEvent.getEndTime(), selectedParameters.getStartTime(), selectedParameters.getEndTime()) &&
				isStatusMatching(currentEvent.getStatus(), selectedParameters.getStatus());
		
		
		
		return isMatch;
		
	}
	
	public boolean isStatusMatching(Status eventStatus, Status paramStatus){
		return eventStatus == paramStatus;
		
	}
	
	public boolean isStringMatching(String eventString, String paramString){
		boolean isMatch = true;
		
		if(paramString == null){
			return isMatch;
		}
		
		isMatch = eventString.toLowerCase().contains(paramString.toLowerCase());
		
		return isMatch;
	}
	
	public boolean isTimeMatching(Date eventStart, Date eventEnd, Date paramStart, Date paramEnd){
		boolean isMatch;
		
		/****CHECK IF DATA PARAMETERS HAVE BEEN GIVEN*****/
		
		isMatch = isStartTimeWithinRange(eventStart, paramStart, paramEnd) && isEndTimeWithinRange(eventEnd, paramStart, paramEnd);
		
		
		return isMatch;
	}
	
	public boolean isStartTimeWithinRange(Date eventStart, Date paramStart, Date paramEnd){
		
		return ((eventStart.getTime() >= paramStart.getTime()) && (eventStart.getTime() <= paramEnd.getTime()));
	}
	
	public boolean isEndTimeWithinRange(Date eventEnd, Date paramStart, Date paramEnd){
		
		return ((eventEnd.getTime() >= paramStart.getTime()) && (eventEnd.getTime() <= paramEnd.getTime()));
	}
	
	
	
	//isEventmatching
	@Override
	public void updatedDisplayedEvents() {
		// TODO Auto-generated method stub
		
	}

}
