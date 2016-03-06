package main;

import java.util.ArrayList;
import java.util.Stack;

import main.Event;;

public class State {

	public ArrayList<Event> completedTasks;
	public ArrayList<Event> incompletedTasks;
	public ArrayList<Event> floatingTasks;
	
	public ArrayList<Event> displayedTasks;
	Stack<Event> eventHistory;

	
	public Event selectedTask;

	/**
	 * CompleteState constructor initializes all Event Lists
	 */
	public State(){
		completedTasks = new ArrayList<Event>();
		incompletedTasks = new ArrayList<Event>();
		floatingTasks = new ArrayList<Event>();
		displayedTasks = new ArrayList<Event>();

	}
}
