package command;

import main.Event;
import main.State;

/**
 * Edit class must be instantiated two Events- the original Event and the new edited Event
 * @author Reem
 *
 */
public class Edit implements Command{
	Event originalEvent;
	Event editedEvent;
	State completeState;

	/**
	 * Edit Event constructor
	 * @param originalEvent the original event located in completeState
	 * @param editedEvent new information in the form of an Event object that will replace the original event
	 */
	public Edit(Event originalEvent, Event editedEvent){
		this.originalEvent = originalEvent;
		this.editedEvent = editedEvent;
		
	}

	/**
	 * Inherited from the Command interface
	 * execute will edit the event from its corresponding list based on status
	 * @param completeState the state of all the tasks in the program
	 */
	public State execute(State completeState){
		this.completeState = completeState;
		this.originalEvent = completeState.selectedEvent;
		int index = findIndexOfEvent();
		
		if(index == -1){
			completeState.setStatusMessage(State.MESSAGE_EVENT_NOT_FOUND);
			return completeState;
		}
		
		switch (originalEvent.getStatus()){
		case COMPLETE:
			editInCompletedEventList(index);
			break;
		case INCOMPLETE:
			editInIncompletedEventList(index);
			break;
		case FLOATING:
			editInFloatingEventList(index);
			break;
		}
		
		updatedDisplayedEvents();
		
		return completeState;
	}
	
	/**
	 * Replaces the event if it is in the completed list
	 * @param index the index of the event
	 */
	public void editInCompletedEventList(int index){
		completeState.completedEvents.set(index, editedEvent);
	}
	
	/**
	 * Replaces the event if it is in the incompleted list
	 * @param index the index of the event
	 */
	public void editInIncompletedEventList(int index){
		completeState.incompletedEvents.set(index, editedEvent);
	}
	
	/**
	 * Replaces the event if it is in the floating list
	 * @param index the index of the event
	 */
	public void editInFloatingEventList(int index){
		completeState.floatingEvents.set(index, editedEvent);
	}
	
	/**
	 * returns the index of the event so that the new event can be replaced at the old event's index
	 * @return
	 */
	public int findIndexOfEvent(){
		int index = 0;
		
		switch (originalEvent.getStatus()){
		case COMPLETE:
			completeState.completedEvents.indexOf(originalEvent);
			break;
		case INCOMPLETE:
			completeState.incompletedEvents.indexOf(originalEvent);
			break;
		case FLOATING:
			completeState.floatingEvents.indexOf(originalEvent);
			break;
		}
		
		return index;
	}
	
	/**
	 * updates the displayedEvents with new information
	 */
	public void updatedDisplayedEvents(){
		completeState.displayedEvents.clear();
		completeState.displayedEvents.addAll(completeState.completedEvents);
		completeState.displayedEvents.addAll(completeState.incompletedEvents);
		completeState.displayedEvents.addAll(completeState.floatingEvents);		
	}
}
