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

/**
 * Confirms a time for a reserved event
 * Confirm class must be instantiated with either the index of the event time pair tht will be confirmed, or the startTime and endtime that the user has provided
 * @@author Reem Razak
 */
public class Confirm implements Command{

	GenericEvent originalEvent;
	Event newEvent;
	Date startTime;
	Date endTime;
	String stringStartTime;
	String stringEndTime;
	State completeState;
	int index;

	/**
	 * Edit Event constructor
	 * @param originalEvent the original event located in completeState
	 * @param editedEvent new information in the form of an Event object that will replace the original event
	 */
	
	public Confirm(int index){
		this.index = index - 1;
	}

	public Confirm(Date startTime, Date endTime, String stringStartTime, String stringEndTime){
		this.startTime = startTime;
		this.endTime = endTime;
		this.stringStartTime = stringStartTime;
		this.stringEndTime = stringEndTime;
		this.index = -1;
	}
	
	private boolean checkForSelectedEvent(){
		if(completeState.hasSingleEventFiltered()){
			originalEvent = completeState.getSingleFilteredEvent();
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
	
	private boolean getTimesOfSelectedIndex(){
		int numberOfReservedTimes = ((ReservedEvent)originalEvent).getReservedTimes().size();
		
		if((index < 0) || (index > numberOfReservedTimes)){
			completeState.setErrorMessage(State.MESSAGE_INVALID_INDEX);
			return false;
		}
		
		startTime = ((ReservedEvent)originalEvent).getReservedTimes().get(index).getStartTime();
		endTime = ((ReservedEvent)originalEvent).getReservedTimes().get(index).getEndTime();
		stringStartTime = startTime.toString();
		stringEndTime = endTime.toString();

		
		return true;
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
		
		if(index != -1){
			getTimesOfSelectedIndex();
		}
		
		if(!completeState.hasErrorMessage){
			confirmTimes();
		}
		
		
		return completeState;
	}
		
}
