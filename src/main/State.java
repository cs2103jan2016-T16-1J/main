package main;

import java.util.ArrayList;
import java.util.Stack;
import constant.Constant;

import main.Event;
import main.GenericEvent.Status;

/**
 * State class- keeps all information on the state of the program
 * @@author Reem Razak
 */
public class State {
	
	public static final String MESSAGE_EVENT_NOT_FOUND = "The requested event was not found";
	public static final String MESSAGE_PARSE_ERROR = "Unable to process the requested event";
	public static final String MESSAGE_TOO_MANY_SELECTIONS = "More than one result was found. Please select the desired event's number";
	public static final String MESSAGE_NO_SELECTED_EVENT = "No event has been selected. Please select an event.";
	public static final String MESSAGE_INVALID_INDEX = "Please select a valid index";
	public static final String MESSAGE_ATTEMPTED_ADD_WITH_RESERVE = "Cannot create floating event using reserve. Please use the Add command";
	public static final String MESSAGE_ATTEMPTED_RESERVE_WITH_ADD = "Cannot create reserved event using add. Please use the Reserve command";
	public static final String MESSAGE_INVALID_RESERVED = "Cannot use Reserve for this type";
	public static final String MESSAGE_INVALID_CONFIRM = "Cannot use confirm an event that is not a reserved type";
	public static final String MESSAGE_WELCOME = "Welcome to Supahotfire's task manager";
	public static final String MESSAGE_COMPLETE_INVALID_EVENT_TYPE = "You cannot complete a reserved or floating event";
	public static final String MESSAGE_COMPLETE_INVALID_EVENT_STATUS = "You can only complete events with the status incomplete";

	public static final int NO_EVENTS_SELECTED = 0;
	public static final int ONE_EVENT_SELECTED = 1;
	public static final int MULTIPLE_EVENTS_SELECTED = 2;
	
	public ArrayList<Event> completedEvents;
	public ArrayList<Event> incompletedEvents;
	//Floating events
	public ArrayList<ReservedEvent> undeterminedEvents;
	//Events with multiple times
	public ArrayList<ReservedEvent> reservedEvents;
	
	
	/**Need to implements- selecting of individual types*/
	/*Type is generic for easy access in the UI*/
	public ArrayList<GenericEvent> undeterminedSelected;
	public ArrayList<GenericEvent> completedSelected;
	public ArrayList<GenericEvent> incompletedSelected;
	public GenericEvent filteredSelectedEvent;
	
	public ArrayList<GenericEvent> displayedEvents;
	public ArrayList<GenericEvent> selectedEvents;
	public Stack<State> eventHistory;
	
	public boolean hasErrorMessage;
	public GenericEvent selectedEvent;
	
	
	//indicates whether an event is selected and if more than one is selected
	public int selectionStatus;
	public int filterStatus;
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
		undeterminedSelected = new ArrayList<GenericEvent>();
		completedSelected = new ArrayList<GenericEvent>();
		incompletedSelected = new ArrayList<GenericEvent>();
		
		
		eventHistory = new Stack<State>();
		
		statusMessage = new String();
		setStatusMessage(MESSAGE_WELCOME);
		hasErrorMessage = false;
		
		tabStatus = Constant.TAB_INCOMPLETE;

	}
	
	/**
	 * Constructs a State which is a clone of the state parameter passed to the constructor
	 * @param anotherState
	 */
	public State(State anotherState){
		completedEvents = cloneEventArray(anotherState.getCompletedList());
		incompletedEvents = cloneEventArray(anotherState.getIncompletedList());
		//Floating events
		undeterminedEvents = cloneReservedEventArray(anotherState.getUndeterminedList());
		//Events with multiple times
		reservedEvents = cloneReservedEventArray(anotherState.getReservedList());
		
		
		/**Need to implements- selecting of individual types*/
		/*Type is generic for easy access in the UI*/
		undeterminedSelected = cloneGenericEventArray(anotherState.getUndeterminedSelectedList());
		completedSelected = cloneGenericEventArray(anotherState.getCompletedSelectedList());
		incompletedSelected = cloneGenericEventArray(anotherState.getIncompletedSelectedList());
		filteredSelectedEvent = anotherState.getSingleFilteredEvent();
		
		displayedEvents = cloneGenericEventArray(anotherState.getDisplayedEvents());
		selectedEvents = cloneGenericEventArray(anotherState.getAllSelectedEvents());
		eventHistory = anotherState.eventHistory;
		
		hasErrorMessage = anotherState.hasErrorMessage;
		selectedEvent = anotherState.getSingleSelectedEvent();
		
		
		//indicates whether an event is selected and if more than one is selected
	    selectionStatus = anotherState.selectionStatus;
		filterStatus = anotherState.filterStatus;
		tabStatus = anotherState.tabStatus;
		
		statusMessage = anotherState.statusMessage;
	}
	
	/**
	 * Creates an exact clone of the ReservedEvent arrayList provided. 
	 * @param arrayList
	 * @return cloned ReservedEvent arrayList
	 */
	public ArrayList<ReservedEvent> cloneReservedEventArray(ArrayList<ReservedEvent> arrayList){
		ArrayList<ReservedEvent> newArray = new ArrayList<ReservedEvent>();
		for(ReservedEvent e: arrayList){
				newArray.add(e.getClone());
		}		
		return newArray;		
	}
	
	/**
	 * Creates an exact clone of the Event arrayList provided. 
	 * @param arrayList
	 * @return cloned Event arrayList
	 */
	public ArrayList<Event> cloneEventArray(ArrayList<Event> arrayList){
		ArrayList<Event> newArray = new ArrayList<Event>();
		for(Event e: arrayList){
				newArray.add(e.getClone());
		}		
		return newArray;		
	}
		
	/**
	 * Creates an exact clone of the GenericEvent arrayList provided. 
	 * @param arrayList
	 * @return cloned GenericEvent arrayList
	 */
	public ArrayList<GenericEvent> cloneGenericEventArray(ArrayList<GenericEvent> arrayList){
		ArrayList<GenericEvent> newArray = new ArrayList<GenericEvent>();
		for(GenericEvent e: arrayList){
			if(e instanceof ReservedEvent){
				newArray.add( ((ReservedEvent)e).getClone());
			}
			if(e instanceof Event){
				newArray.add( ((Event)e).getClone());
			}
		}		
		return newArray;		
	}
	
	/**
	 * Gets the singular selected event
	 * @return Current selected event. Null will be returned if more than one or no events are selected.
	 */
	public GenericEvent getSingleSelectedEvent(){
		return selectedEvent;			
	}
	
	/**
	 * Gets list of all the selected events.
	 * @return Current list of selected events. The list will be empty if no events are selected
	 */
	public ArrayList<GenericEvent> getAllSelectedEvents(){
		return selectedEvents;
	}

	
	/**
	 * Gets the singular selected event filtered by the status tab
	 * @return Current filtered event. Null will be returned if more than one or no events are selected through the status tab filter.
	 */
	public GenericEvent getSingleFilteredEvent(){
		return filteredSelectedEvent;

	}
	
	/**
	 * Gets list of all the selected events filtered by the status tab
	 * @return Current list of filtered events. The list will be empty if no events are selected through the status tab filter
	 */
	public ArrayList<GenericEvent> getAllFilteredEvents(){
		filterListsByTab();
		switch (tabStatus){
			case COMPLETE:
				return completedSelected;
			case INCOMPLETE:
				return incompletedSelected;
			case UNDETERMINED:
				return undeterminedSelected;				
			default:
				return null;
		
		}
	}

	/**
	 * Updates the filter lists based on current selected Events
	 */
	public void filterListsByTab(){
		clearFilterArrays();
		
		for(GenericEvent e: selectedEvents){
			filterComplete(e);
			filterIncomplete(e);
			filterUndetermined(e);
		}
		
	}

	/**
	 * Adds an event to the completed filter list
	 * @param e the event to be added
	 */
	private void filterComplete(GenericEvent e) {
		if(e.getStatus().equals(Status.COMPLETE)){
			completedSelected.add(e);
		}
	}
	
	/**
	 * Adds an event to the incomplete filter list
	 * @param e the event to be added
	 */
	private void filterIncomplete(GenericEvent e) {
		if(e.getStatus().equals(Status.INCOMPLETE)){
			incompletedSelected.add(e);
		}
	}
	
	/**
	 * Adds an event to the undetermined filter list
	 * @param e the event to be added
	 */
	private void filterUndetermined(GenericEvent e){
		if(e.getStatus().equals(Status.UNDETERMINED)){
			undeterminedSelected.add(e);
		}
	}
	
	/**
	 * Clear all filter arrays
	 */
	private void clearFilterArrays(){
		completedSelected.clear();
		incompletedSelected.clear();
		undeterminedSelected.clear();
	}
	
	/**
	 * Clears the state selection
	 */
	public void clearSelections(){
		if(!selectedEvents.isEmpty()){
			selectedEvents.clear();
		}
		selectedEvent = null;
		filteredSelectedEvent = null;
		clearFilterArrays();
		setSelectionStatus(NO_EVENTS_SELECTED);
		setFilterStatus(NO_EVENTS_SELECTED);;

	}
	
	/**
	 * Gets the status of the selected tab
	 * @return
	 */
	public Status getSelectedTab(){
		return tabStatus;
	}
	
	/**
	 * Sets the status of the selected tab
	 * @param tabStatus the new tab status
	 */
	public void setSelectedTab(Status tabStatus){
		this.tabStatus = tabStatus;
	}
	
	/**
	 * Returns whether or not the tab is currently COMPLETE
	 * @return
	 */
	public boolean isCompletedSelected(){
		return tabStatus == Constant.TAB_COMPLETE;
	}
	
	/**
	 * Returns whether or not the tab is currently INCOMPLETE
	 * @return
	 */
	public boolean isIncompletedSelected(){
		return tabStatus == Constant.TAB_INCOMPLETE;
	}
	
	/**
	 * Returns whether or not the tab is currently UNDETERMINED
	 * @return
	 */
	public boolean isUndeterminedSelected(){
		return tabStatus == Constant.TAB_UNDETERMINED;
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
	
	public boolean hasEventFiltered () {
		return filterStatus != NO_EVENTS_SELECTED;
	}
	
	public boolean hasSingleEventFiltered () {
		return filterStatus == ONE_EVENT_SELECTED;
	}
	
	public boolean hasMultipleEventFiltered () {
		return filterStatus == MULTIPLE_EVENTS_SELECTED;
	}
	
	public void setOneSelectedEvent(GenericEvent event){
		clearSelections();
		addToSelectedEvents(event);
		selectedEvent = event;
		filteredSelectedEvent = event;
		setSelectionStatus(ONE_EVENT_SELECTED);
		setFilterStatus(ONE_EVENT_SELECTED);

	}
	
	public void addToSelectedEvents(GenericEvent event){
		selectedEvents.add(event);
	}
	
	public int getFilterStatus(){
		return filterStatus;
	}
	
	public void setFilterStatus(int filterStatus){
		if(filterStatus > 2){
			filterStatus = 2;
		}
		this.filterStatus = filterStatus;
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
	
	public void setErrorMessage(String statusMessage){
		this.hasErrorMessage = true;
		this.statusMessage = statusMessage;
	}
	
	public void clearStatusMessage(){
		this.statusMessage = null;
		hasErrorMessage = false;
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
		return reservedEvents;
	}
	
	public  ArrayList<ReservedEvent> getFloatingList(){
		return undeterminedEvents;
	}
	
	public  ArrayList<GenericEvent> getCompletedSelectedList(){
		return completedSelected;
	}
	
	public  ArrayList<GenericEvent> getIncompletedSelectedList(){
		return incompletedSelected;
	}
	
	public  ArrayList<GenericEvent> getUndeterminedSelectedList(){
		return undeterminedSelected;
	}
	
	public  ArrayList<GenericEvent> getDisplayedEvents(){
		return displayedEvents;
	}
	
	public void updateDisplayedEvents(){
		displayedEvents.clear();
		displayedEvents.addAll(completedEvents);
		displayedEvents.addAll(incompletedEvents);
		displayedEvents.addAll(reservedEvents);		
	}
 
	

}

