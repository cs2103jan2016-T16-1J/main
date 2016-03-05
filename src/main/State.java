package main;

import java.util.ArrayList;
import java.util.Stack;

public class State {
	ArrayList<Event> completed = new ArrayList<Event>();
	ArrayList<Event> incomplete = new ArrayList<Event>();
	ArrayList<Event> floating = new ArrayList<Event>();
	ArrayList<Event> displayedEvents = new ArrayList<Event>();
	ArrayList<Event> selectedEvents = new ArrayList<Event>();
	Stack<Event> eventHistory;
	
}
