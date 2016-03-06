package main;

import java.util.ArrayList;
import java.util.Stack;

import main.Event;;

public class State {

	public static final String MESSAGE_EVENT_NOT_FOUND = "The request event was not found";
	public ArrayList<Event> completedTasks;
	public ArrayList<Event> incompletedTasks;
	public ArrayList<Event> floatingTasks;
	
	public ArrayList<Event> displayedTasks;
	Stack<Event> eventHistory;
	
	public Event selectedTask;
	
	public String statusMessage;

	/**
	 * CompleteState constructor initializes all Event Lists
	 */
	public State(){
		completedTasks = new ArrayList<Event>();
		incompletedTasks = new ArrayList<Event>();
		floatingTasks = new ArrayList<Event>();
		displayedTasks = new ArrayList<Event>();
		statusMessage = new String();

	}
	
	public void setStatusMessage(String statusMessage){
		this.statusMessage = statusMessage;
	}
	
	public String getStatusMessage(){
		return this.statusMessage;
	}
}
