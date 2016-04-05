package command;

import java.util.ArrayList;
import java.util.Date;

import constant.Constant;
import json.JSONException;
import main.Event;
import main.State;
import main.GenericEvent;
import main.GenericEvent.Category;
import main.GenericEvent.Status;
import main.ReservedEvent;
import storage.Storage;

/**
 * Delete class must be instantiated with an Event object to delete
 * @author Reem
 *
 */
public class Delete implements Command{
	GenericEvent selectedParameters;
	State completeState;

	/**
	 * Delete class constructor. 
	 * @param modifiedEvent the event that will be deleted
	 */
	public Delete(GenericEvent selectedParameters){
		this.selectedParameters = selectedParameters;
	}
	
	/**
	 * Inherited from the Command interface
	 * execute will delete the event from its corresponding list based on status
	 * If two identical instances of the class are found in the list, only one is deleted
	 * @param completeState the state of all the tasks in the program
	 * @throws JSONException 
	 */
	public State execute(State completeState) throws JSONException{
		
		this.completeState = completeState;
		
		if(!hasDeleteParameters()){
			updatedDisplayedEvents();
			return completeState;
		}
		
		ArrayList<GenericEvent> allEvents = completeState.getAllEvents();
		ArrayList<GenericEvent> eventsToDelete = getMatchingEvents(allEvents);

		
		if(!hasMatchingEvents(eventsToDelete)){
			return completeState;
		}
		
		deleteFromSelectedEvents(eventsToDelete);
		
		deleteMatchedEvents(eventsToDelete);
		
		updatedDisplayedEvents();

		return completeState;
	}
	
	private boolean hasDeleteParameters(){
		if(null == selectedParameters){
			deleteStateSelectedEvent();
			return false;
			
		}
		
		return true;
	}
	
	private void deleteFromSelectedEvents(ArrayList<GenericEvent> eventsToDelete){
		if((completeState.hasSingleEventSelected()) && (isMatchingEvent(completeState.getSingleSelectedEvent()))){
			completeState.selectedEvent = null;
			completeState.selectedEvents.clear();
		}
		if(completeState.hasMultipleEventSelected()){
			for(GenericEvent e : completeState.getAllSelectedEvents()){
				if(isMatchingEvent(e)){
					completeState.selectedEvents.remove(e);
				}
			}
		}
	}

	
	private void deleteStateSelectedEvent(){
		if(completeState.hasSingleEventSelected()){
			deleteFromCorrespondingArray(completeState.getSingleSelectedEvent());
		}
		else if(completeState.hasMultipleEventSelected()){
			for(GenericEvent e : completeState.getAllSelectedEvents()){
				deleteFromCorrespondingArray(e);
			}
		}		
		else{
			completeState.setStatusMessage(State.MESSAGE_NO_SELECTED_EVENT);
		}

		completeState.clearSelections();

	}
	
	private void deleteMatchedEvents(ArrayList<GenericEvent> eventsToDelete){
		for(GenericEvent e: eventsToDelete ){
			deleteFromCorrespondingArray(e);
		}
	}
	
	/**
	 * Deletes the event from the corresponding array, which is based on the event's status
	 * @param e
	 */
	private void deleteFromCorrespondingArray(GenericEvent e){
		switch (e.getStatus()){
		case COMPLETE:
			removeFromCompleteList((Event)e);
			break;
		case INCOMPLETE:
			removeFromIncompleteList((Event)e);
			break;
		case UNDETERMINED:
			//removeFromFloatingList(e);
			break;
			
		}
	}
	
	private void removeFromCompleteList(Event e){
		completeState.completedEvents.remove(e);
	}
	
	private void removeFromIncompleteList(Event e){
		completeState.incompletedEvents.remove(e);
	}
	
	private void removeFromFloatingList(ReservedEvent e){
	//	completeState.floatingEvents.remove(e);
	}
	
	private boolean hasMatchingEvents(ArrayList<GenericEvent> eventsToDelete){
		if(eventsToDelete.isEmpty()){
			completeState.setStatusMessage(State.MESSAGE_EVENT_NOT_FOUND);
			return false;
		}
		
		return true;
	}
	
	private ArrayList<GenericEvent> getMatchingEvents(ArrayList<GenericEvent> allEvents){
		ArrayList<GenericEvent> matchingEvents = new ArrayList<GenericEvent>();
		
		for(GenericEvent e: allEvents){
			if(isMatchingEvent(e)){
				matchingEvents.add(e);				
			}
		}
		
		return matchingEvents;
	}
	
	
	private boolean isMatchingEvent(GenericEvent currentEvent){
		boolean isMatch = true;
				
		isMatch = isStringMatching(currentEvent.getName(), selectedParameters.getName()) &&
				isStringMatching(currentEvent.getLocation(), selectedParameters.getLocation()) &&
				isStringMatching(currentEvent.getDescription(), selectedParameters.getDescription())
				//&& isCategoryMatching(currentEvent.getCategory(), selectedParameters.getCategory())
				//&& isTimeMatching(currentEvent.getStartTime(), currentEvent.getEndTime(), selectedParameters.getStartTime(), selectedParameters.getEndTime())
				//&& isStatusMatching(currentEvent.getStatus(), selectedParameters.getStatus())
				;
		
		
		
		return isMatch;
		
	}
	
	private boolean isStatusMatching(GenericEvent.Status eventStatus, GenericEvent.Status paramStatus){
		if(paramStatus == Constant.STATUS_NULL){
			return true;
		}
		return eventStatus == paramStatus;
		
	}
	
	private boolean isCategoryMatching(GenericEvent.Category eventCategory, GenericEvent.Category paramCategory){
		if(paramCategory == Constant.CATEGORY_NULL){
			return true;
		}
		return eventCategory == paramCategory;
	}
	
	private boolean isStringMatching(String eventString, String paramString){
		boolean isMatch = true;
		
		if(paramString.equals(Constant.EMPTY_STRING)){
			return isMatch;
		}
		
		isMatch = eventString.toLowerCase().contains(paramString.toLowerCase());
		
		return isMatch;
	}
	
	private boolean isTimeMatching(Date eventStart, Date eventEnd, Date paramStart, Date paramEnd){
		boolean isStartMatch;
		boolean isEndMatch;
		
		isStartMatch = isStartTimeWithinRange(eventStart, paramStart, paramEnd);
		if(paramStart.equals(Constant.MIN_DATE)){
			isStartMatch = true;
		}
		
		isEndMatch = isEndTimeWithinRange(eventEnd, paramStart, paramEnd);
		
		if(paramStart.equals(Constant.MAX_DATE)){
			isEndMatch = true;
		}
		
		
		return isStartMatch && isEndMatch;
	}
	
	public boolean isStartTimeWithinRange(Date eventStart, Date paramStart, Date paramEnd){
		
		return ((eventStart.getTime() >= paramStart.getTime()) && (eventStart.getTime() <= paramEnd.getTime()));
	}

	private boolean isEndTimeWithinRange(Date eventEnd, Date paramStart, Date paramEnd){
		
		return ((eventEnd.getTime() >= paramStart.getTime()) && (eventEnd.getTime() <= paramEnd.getTime()));
	}
	
	
	/**
	 * updates the displayedEvents with new information
	 */
	public void updatedDisplayedEvents(){
		completeState.displayedEvents.clear();
		completeState.displayedEvents.addAll(completeState.completedEvents);
		completeState.displayedEvents.addAll(completeState.incompletedEvents);
		//completeState.displayedEvents.addAll(completeState.floatingEvents);		
	}
}

