package state;

import java.util.ArrayList;

import main.Event;;

public class State {

	public ArrayList<Event> completedTasks;
	public ArrayList<Event> incompletedTasks;
	public ArrayList<Event> floatingTasks;
	
	public ArrayList<Event> displayedTasks;
	
	public Event selectedTask;
}
