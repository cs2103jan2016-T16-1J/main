package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;
import constant.Constant;

import json.JSONException;
import main.Event;
import main.GenericEvent.Status;
import storage.Storage;;

public class State {

	public static final String MESSAGE_EVENT_NOT_FOUND = "The requested event was not found";
	public static final String MESSAGE_PARSE_ERROR = "Unable to parse the requestedd event";
	public static final String MESSAGE_TOO_MANY_SELECTIONS = "More than one result was found. Please select the desired event's number";
	public static final String MESSAGE_NO_SELECTED_EVENT = "No event has been selected. Please select an event.";
	public static final int NO_EVENTS_SELECTED = 0;
	public static final int ONE_EVENT_SELECTED = 1;
	public static final int MULTIPLE_EVENTS_SELECTED = 2;
	public static ArrayList<Event> completedEvents;
	public static ArrayList<Event> incompletedEvents;
	public static ArrayList<Event> floatingEvents;
	
	public ArrayList<Event> displayedEvents;
	public ArrayList<Event> selectedEvents;
	Stack<Event> eventHistory;
	
	public Event selectedEvent;
	
	
	//indicates whether an event is selected and if more than one is selected
	public int selectionStatus;
	public Status tabStatus;
	
	public String statusMessage;

	/**
	 * CompleteState constructor initializes all Event Lists
	 */
	public State(){
		completedEvents = new ArrayList<Event>();
		incompletedEvents = new ArrayList<Event>();
		floatingEvents = new ArrayList<Event>();
		displayedEvents = new ArrayList<Event>();
		selectedEvents = new ArrayList<Event>();
		
		statusMessage = new String();
		
		tabStatus = Constant.TAB_INCOMPLETE;

	}
	
	public Event getSingleSelectedEvent(){
		return selectedEvent;
	}
	
	public ArrayList<Event> getAllSelectedEvents(){
		return filterByTab();
	}
	
	public ArrayList<Event> filterByTab(){
		ArrayList<Event> filteredEvents = new ArrayList<Event>();
		
		for(Event e: selectedEvents){
			if(e.getStatus().equals(getSelectedTab())){
				filteredEvents.add(e);
			}
		}
		
		return filteredEvents;
	}
	
	public void clearSelections(){
		if(!selectedEvents.isEmpty()){
			selectedEvents.clear();
		}
		selectedEvent = null;
		setSelectionStatus(NO_EVENTS_SELECTED);;
	}
	
	public boolean isUndeterminedSelected(){
		return tabStatus == Constant.TAB_UNDETERMINED;
	}
	
	public Status getSelectedTab(){
		return tabStatus;
	}
	
	public boolean isCompletedSelected(){
		return tabStatus == Constant.TAB_COMPLETE;
	}
	
	public boolean isIncompletedSelected(){
		return tabStatus == Constant.TAB_INCOMPLETE;
	}
	
	public boolean hasEventSelected () {
		return selectionStatus != NO_EVENTS_SELECTED;
	}
	
	public boolean hasSingleEventSelected () {
		return selectionStatus == ONE_EVENT_SELECTED;
	}
	
	public boolean hasMultipleEventSelected () {
		return selectionStatus == MULTIPLE_EVENTS_SELECTED;
	}
	
	public int getSelectionStatus(){
		return selectionStatus;
	}
	
	public void setSelectionStatus(int selectionStatus){
		this.selectionStatus = selectionStatus;
	}
	
	public void setStatusMessage(String statusMessage){
		this.statusMessage = statusMessage;
	}
	
	public String getStatusMessage(){
		return this.statusMessage;
	}
	
	public ArrayList<Event> getAllEvents(){
		ArrayList<Event> allEvents = new ArrayList<Event>();
		allEvents.addAll(this.completedEvents);
		allEvents.addAll(this.incompletedEvents);
		allEvents.addAll(this.floatingEvents);	
		
		return allEvents;
	}
	
	public  void addToCompletedList(Event event){
		completedEvents.add(event);
	}
	
	public  void addToIncompletedList(Event event){
		incompletedEvents.add(event);
	}
	
	public  void addToFloatingList(Event event){
		floatingEvents.add(event);
	}
	
	public void sortDisplayedEvents() {
		Collections.sort(this.displayedEvents, new CustomEndTimeComparator());
	}
	
	private class CustomEndTimeComparator implements Comparator<Event> {
	    @Override
	    public int compare(Event o1, Event o2) {
	        return o1.getEndTime().compareTo(o2.getEndTime());
	    }
	}

}

