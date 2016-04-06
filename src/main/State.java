package main;

import java.util.ArrayList;
import java.util.Stack;
import constant.Constant;

import main.Event;
import main.GenericEvent.Status;

public class State {

	public static final String MESSAGE_EVENT_NOT_FOUND = "The requested event was not found";
	public static final String MESSAGE_PARSE_ERROR = "Unable to parse the requestedd event";
	public static final String MESSAGE_TOO_MANY_SELECTIONS = "More than one result was found. Please select the desired event's number";
	public static final String MESSAGE_NO_SELECTED_EVENT = "No event has been selected. Please select an event.";
	public static final String MESSAGE_INVALID_INDEX = "Please select a valid index";
	public static final String MESSAGE_ATTEMPTED_ADD_WITH_RESERVE = "Cannot create floating event using reserve. Please use the Add command";
	public static final String MESSAGE_ATTEMPTED_RESERVE_WITH_ADD = "Cannot create reserved event using add. Please use the Reserve command";
	public static final String MESSAGE_INVALID_RESERVED = "Cannot use Reserve for this type";
	public static final String MESSAGE_WELCOME = "Welcome to Supahotfire's task manager";
	public static final int NO_EVENTS_SELECTED = 0;
	public static final int ONE_EVENT_SELECTED = 1;
	public static final int MULTIPLE_EVENTS_SELECTED = 2;
	public static ArrayList<Event> completedEvents;
	public static ArrayList<Event> incompletedEvents;
	//Floating events
	public static ArrayList<ReservedEvent> undeterminedEvents;
	//Events with multiple times
	public static ArrayList<ReservedEvent> reservedEvents;
	
	
	/**Need to implements- selecting of individual types*/
	public ArrayList<ReservedEvent> undeterminedSelected;
	public ArrayList<Event> completedSelected;
	public ArrayList<Event> incompletedSelected;

	
	
	public ArrayList<GenericEvent> displayedEvents;
	public ArrayList<GenericEvent> selectedEvents;
	Stack<GenericEvent> eventHistory;
	
	public GenericEvent selectedEvent;
	
	
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
		undeterminedEvents = new ArrayList<ReservedEvent>();
		reservedEvents = new ArrayList<ReservedEvent>(); 
		
		displayedEvents = new ArrayList<GenericEvent>();
		selectedEvents = new ArrayList<GenericEvent>();
		
		
		//Arrays for selected tabs of each kind of event
		undeterminedSelected = new ArrayList<ReservedEvent>();
		completedSelected = new ArrayList<Event>();
		incompletedSelected = new ArrayList<Event>();
		
		statusMessage = new String();
		setStatusMessage(MESSAGE_WELCOME);
		
		tabStatus = Constant.TAB_INCOMPLETE;

	}
	
	public GenericEvent getSingleSelectedEvent(){
		return selectedEvent;
	}
	
	public ArrayList<GenericEvent> getAllSelectedEvents(){
		return filterByTab();
	}
	
	public ArrayList<GenericEvent> filterByTab(){
		ArrayList<GenericEvent> filteredEvents = new ArrayList<GenericEvent>();
		
		for(GenericEvent e: selectedEvents){
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
	
	public void setSelectedTab(Status tabStatus){
		this.tabStatus = tabStatus;
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
	
	public void setOneSelectedEvent(GenericEvent event){
		clearSelections();
		addToSelectedEvents(event);
		selectedEvent = event;
		setSelectionStatus(State.ONE_EVENT_SELECTED);
	}
	
	public void addToSelectedEvents(GenericEvent event){
		selectedEvents.add(event);
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
		return statusMessage;
	}
	
	public ArrayList<GenericEvent> getAllEvents(){
		ArrayList<GenericEvent> allEvents = new ArrayList<GenericEvent>();
		allEvents.addAll(completedEvents);
		allEvents.addAll(incompletedEvents);
		allEvents.addAll(undeterminedEvents);
		allEvents.addAll(reservedEvents);
		
		return allEvents;
	}
	
	public  void addToCompletedList(Event event){
		completedEvents.add(event);
	}
	
	public  void addToIncompletedList(Event event){
		incompletedEvents.add(event);
	}
	
	public  void addToUndeterminedList(ReservedEvent event){
		undeterminedEvents.add(event);
	}
	
	public  void addToReservedList(ReservedEvent event){
		reservedEvents.add(event);
	}
	

	public  ArrayList<Event> getCompletedList(){
		return completedEvents;
	}
	
	public  ArrayList<Event> getIncompletedList(){
		return incompletedEvents;
	}
	
	public  ArrayList<ReservedEvent> getUndeterminedList(){
		return undeterminedEvents;
	}
	
	public  ArrayList<ReservedEvent> getReservedList(){
		return undeterminedEvents;
	}
	
	public  ArrayList<ReservedEvent> getFloatingList(){
		return undeterminedEvents;
	}
	
	public void updateDisplayedEvents(){
		displayedEvents.clear();
		displayedEvents.addAll(completedEvents);
		displayedEvents.addAll(incompletedEvents);
		displayedEvents.addAll(reservedEvents);		
	}
 
	

}

