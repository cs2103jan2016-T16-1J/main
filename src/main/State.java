package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import json.JSONException;
import main.Event;
import storage.Storage;;

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
	
	public  void addToCompletedList(Event event){
		completedEvents.add(event);
	}
	
	public  void addToIncompletedList(Event event){
		incompletedEvents.add(event);
	}
	
	public  void addToFloatingList(Event event){
		floatingEvents.add(event);
	}
	
}
