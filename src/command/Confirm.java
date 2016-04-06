package command;

import java.io.IOException;
import java.util.Date;

import json.JSONException;
import main.Event;
import main.GenericEvent;
import main.ReservedEvent;
import main.State;
import main.GenericEvent.Category;
import main.GenericEvent.Status;

public class Confirm implements Command{
	GenericEvent originalEvent;
	Event newEvent;
	Date startTime;
	Date endTime;
	String stringStartTime;
	String stringEndTime;
	State completeState;

	/**
	 * Edit Event constructor
	 * @param originalEvent the original event located in completeState
	 * @param editedEvent new information in the form of an Event object that will replace the original event
	 */

	public Confirm(Date startTime, Date endTime, String stringStartTime, String stringEndTime){
		this.startTime = startTime;
		this.endTime = endTime;
		this.stringStartTime = stringStartTime;
		this.stringEndTime = stringEndTime;
	}
	
	private boolean checkForSelectedEvent(){
		if(completeState.hasSingleEventSelected()){
			originalEvent = completeState.getSingleSelectedEvent();
		}
		
		return true;
	}
	
	private boolean checkEventType(){
		if(!(originalEvent instanceof ReservedEvent)){
			completeState.setErrorMessage(State.MESSAGE_INVALID_CONFIRM);
			return false;
		}
		return true;
	}
	
	/*	public Event(String name, String location, String description, Category category, Date startTime, Date endTime,
			String stringStartTime,String stringEndTime, Status status){*/
	private void confirmTimes(){
		newEvent = new Event(originalEvent.getName(), originalEvent.getLocation(), originalEvent.getDescription(), originalEvent.getCategory(), startTime, endTime, stringStartTime, stringEndTime, Status.INCOMPLETE);
		switchObjectTypes();
	}
	
	public void switchObjectTypes(){
		
		Command delete = new Delete(null);
		Command add = new Add(newEvent);
		
		try {
			completeState = delete.execute(completeState);
			completeState = add.execute(completeState);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public State execute(State completeState){
		this.completeState = completeState;
		
		if(!checkForSelectedEvent()){
			completeState.setStatusMessage(State.MESSAGE_EVENT_NOT_FOUND);
			return completeState;
		}
		
		if(!checkEventType()){
			return completeState;
		}
		
		confirmTimes();
		
		
		return completeState;
	}
		



}
