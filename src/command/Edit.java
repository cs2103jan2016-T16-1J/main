package command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import constant.Constant;
import json.JSONException;
import main.Event;
import main.GenericEvent;
import main.ReservedEvent;
import main.State;
import main.TimePair;
import main.GenericEvent.Category;
import main.GenericEvent.Status;


/**
 * Modifies the selected event with new parameters
 * Edit class must be instantiated with the new parameters
 * @@author Reem Razak
 */
public class Edit implements Command{
	GenericEvent originalEvent;
	GenericEvent selectedParameters;
	State completeState;

	/**
	 * Edit Event constructor
	 * @param originalEvent the original event located in completeState
	 * @param editedEvent new information in the form of an Event object that will replace the original event
	 */

	public Edit(GenericEvent event){
		this.selectedParameters = event;
	}
	
	private boolean checkForSelectedEvent(){
		if(completeState.hasSingleEventSelected()){
			originalEvent = completeState.getSingleSelectedEvent();
			return true;
		}
		
		return false;
	}

	/**
	 * Inherited from the Command interface
	 * execute will edit the event from its corresponding list based on status
	 * @param completeState the state of all the tasks in the program
	 */
	public State execute(State completeState){
		this.completeState = completeState;
		
		if(!checkForSelectedEvent()){
			completeState.setStatusMessage(State.MESSAGE_EVENT_NOT_FOUND);
			return completeState;
		}
		

		if(checkObjectTypes()){
			updateGenericEvent();
		}
		
		completeState.updateDisplayedEvents();

		return completeState;
	}
	
	public boolean checkObjectTypes(){
		//if the objects are not equal
		if(!selectedParameters.getClass().equals(originalEvent.getClass())){
			switchObjectTypes();
			return false;
		}
		
		return true;
	}
	
	public void switchObjectTypes(){
		Command delete;
		Command add;
		if(selectedParameters instanceof ReservedEvent){
			ReservedEvent newEvent = switchToReservedParameters();

			delete = new Delete(null);
			add = new Add(newEvent);
		}
		else{
			Event newEvent = switchToEventParameters();		
			delete = new Delete(null);
			add = new Add(newEvent);
		}
		
		try {
			completeState = delete.execute(completeState);
			completeState = add.execute(completeState);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ReservedEvent switchToReservedParameters(){
		ReservedEvent newEvent = new ReservedEvent();
		newEvent.setName(checkNameParameter(originalEvent.getName()));
		newEvent.setLocation(checkLocationParameter(originalEvent.getLocation()));
		newEvent.setDescription(checkDescriptionParameter(originalEvent.getDescription()));
		newEvent.setCategory(checkCategoryParameter(originalEvent.getCategory()));
		newEvent.setStatus(Status.UNDETERMINED);
		
		newEvent.setReservedTimes(((ReservedEvent)originalEvent).getReservedTimes());

		
		return newEvent;
	}
	
	public Event switchToEventParameters(){
		Event newEvent = new Event();
		newEvent.setName(checkNameParameter(originalEvent.getName()));
		newEvent.setLocation(checkLocationParameter(originalEvent.getLocation()));
		newEvent.setDescription(checkDescriptionParameter(originalEvent.getDescription()));
		newEvent.setCategory(checkCategoryParameter(originalEvent.getCategory()));
		newEvent.setStatus(Status.INCOMPLETE);
		
		newEvent.setStartTime(checkStartTimeParameter(Constant.MIN_DATE));
		
		newEvent.setEndTime(checkEndTimeParameter(Constant.MAX_DATE));

		return newEvent;
	}
	
	public String checkNameParameter(String original){
		if(!selectedParameters.getName().equals(Constant.EMPTY_STRING)){
			return selectedParameters.getName();
		}		
		return original;
	}

	public String checkLocationParameter(String original){
		if(!selectedParameters.getLocation().equals(Constant.EMPTY_STRING)){
			return selectedParameters.getLocation();
		}
		return original;
	}
	
	public String checkDescriptionParameter(String original){
		if(!selectedParameters.getDescription().equals(Constant.EMPTY_STRING)){
			return selectedParameters.getDescription();
		}		
		return original;
	}
	
	public Category checkCategoryParameter(Category original){
		if(!selectedParameters.getCategory().equals(Category.FLOATING)){
			return selectedParameters.getCategory();
		}		
		return original;
	}
	
	public Status checkStatusParameter(Status original){
		if(!selectedParameters.getStatus().equals(Status.INCOMPLETE)){
			return selectedParameters.getStatus();
		}
		return original;
	}

	public Date checkStartTimeParameter(Date original){
		if(!((Event)selectedParameters).getStartTime().equals(Constant.MIN_DATE)){
			return ((Event)selectedParameters).getStartTime();
		}
		return original;
	}
	
	public Date checkEndTimeParameter(Date original){
		if(!((Event)selectedParameters).getEndTime().equals(Constant.MAX_DATE)){
			return ((Event)selectedParameters).getEndTime();
		}
		return original;
	}
	
	public ArrayList<TimePair> checkReservedTimesParameter(ArrayList<TimePair> original){
		if(!((ReservedEvent)selectedParameters).getReservedTimes().isEmpty()){
			return ((ReservedEvent)selectedParameters).getReservedTimes();
		}
		return original;
	}
	
	public void updateGenericEvent(){
		originalEvent.setName(checkNameParameter(originalEvent.getName()));
		originalEvent.setLocation(checkLocationParameter(originalEvent.getLocation()));
		originalEvent.setDescription(checkDescriptionParameter(originalEvent.getDescription()));
		originalEvent.setCategory(checkCategoryParameter(originalEvent.getCategory()));
		originalEvent.setStatus(checkStatusParameter(originalEvent.getStatus()));
		/*if(!selectedParameters.getSelection().isEmpty()){
			originalEvent.setSelection(selectedParameters.getSelection());
		}*/
		
		if(originalEvent instanceof Event){
			updateEvent();
		}
		if(originalEvent instanceof ReservedEvent){
			updateReservedEvent();
		}
		
		completeState.setOneSelectedEvent(originalEvent);

	}
	
	public void updateEvent(){
		((Event)originalEvent).setStartTime(checkStartTimeParameter(((Event)originalEvent).getStartTime()));
			
		((Event)originalEvent).setEndTime(checkEndTimeParameter(((Event)originalEvent).getEndTime()));
		
	}
	
	public void updateReservedEvent(){
		((ReservedEvent)originalEvent).setReservedTimes(((ReservedEvent)originalEvent).getReservedTimes());
		
		
	}
	
	
}
