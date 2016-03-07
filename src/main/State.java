package main;

import java.util.ArrayList;
import java.util.Stack;

import main.Event;;

public class State {

	public static final String MESSAGE_EVENT_NOT_FOUND = "The request event was not found";
	public static ArrayList<Event> completedEvents;
	public static ArrayList<Event> incompletedEvents;
	public static ArrayList<Event> floatingEvents;
	
	public ArrayList<Event> displayedEvents;
	Stack<Event> eventHistory;
	
	public Event selectedEvent;
	
	public String statusMessage;

	/**
	 * CompleteState constructor initializes all Event Lists
	 */
	public State(){
		completedEvents = new ArrayList<Event>();
		incompletedEvents = new ArrayList<Event>();
		floatingEvents = new ArrayList<Event>();
		displayedEvents = new ArrayList<Event>();
		statusMessage = new String();

	}
	
	public void setStatusMessage(String statusMessage){
		this.statusMessage = statusMessage;
	}
	
	public String getStatusMessage(){
		return this.statusMessage;
	}
	
	public static void addToCompletedList(Event event){
		completedEvents.add(event);
	}
	
	public static void addToIncompletedList(Event event){
		incompletedEvents.add(event);
	}
	
	public static void addToFloatingList(Event event){
		floatingEvents.add(event);
	}
	
	
}
