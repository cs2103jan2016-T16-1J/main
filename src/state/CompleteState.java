package state;

import java.util.ArrayList;

import main.Event;;

public class CompleteState {

	public ArrayList<Event> completedTasks;
	public ArrayList<Event> incompletedTasks;
	public ArrayList<Event> floatingTasks;
	
	public ArrayList<Event> displayedTasks;
	
	public Event selectedTask;

	/**
	 * CompleteState constructor initializes all Event Lists
	 */
	public CompleteState(){
		completedTasks = new ArrayList<Event>();
		incompletedTasks = new ArrayList<Event>();
		floatingTasks = new ArrayList<Event>();
		displayedTasks = new ArrayList<Event>();

	}
}
