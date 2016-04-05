package command;

import main.Event;
import main.GenericEvent;
import main.ReservedEvent;
import main.State;

/**
 * Edit class must be instantiated two Events- the original Event and the new edited Event
 * @author Reem
 *
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
		if(completeState.hasEventSelected()){
			originalEvent = completeState.getSingleSelectedEvent();
		}
		
		return true;
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
		
		updateGenericEvent();
		
		return completeState;
	}
	
	public void updateGenericEvent(){
		originalEvent.setName(selectedParameters.getName());
		originalEvent.setDescription(selectedParameters.getDescription());
		originalEvent.setLocation(selectedParameters.getLocation());
		originalEvent.setCategory(selectedParameters.getCategory());
		originalEvent.setStatus(selectedParameters.getStatus());
		
		if(originalEvent instanceof Event){
			updateEvent();
		}
		if(originalEvent instanceof ReservedEvent){
			updateReservedEvent();
		}
		
	}
	
	public void updateEvent(){
		((Event)originalEvent).setStartTime(((Event)selectedParameters).getStartTime());
		((Event)originalEvent).setEndTime(((Event)selectedParameters).getEndTime());
	}
	
	public void updateReservedEvent(){
		((ReservedEvent)originalEvent).setReservedTimes(((ReservedEvent)selectedParameters).getReservedTimes());
		
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
