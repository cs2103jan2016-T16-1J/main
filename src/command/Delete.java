package command;

import main.Event;
import state.CompleteState;

/**
 * Delete class must be instantiated with an Event object to delete
 * @author Reem
 *
 */
public class Delete implements Command{
	Event modifiedEvent;
	CompleteState completeState;

	public Delete(Event modifiedEvent){
		this.modifiedEvent = modifiedEvent;
	}
	
	public CompleteState execute(CompleteState completeState){
		
		this.completeState = completeState;
		
		switch(modifiedEvent.getStatus()){
		case COMPLETE:
			deleteFromCompleteList();
			break;
		case INCOMPLETE:
			deleteFromIncompleteList();
			break;
		case FLOATING:
			deleteFromFloatingList();
			break;
				
		}
		return completeState;
	}
	
	public void deleteFromCompleteList(){
		completeState.completedTasks.remove(modifiedEvent);
	}
	
	public void deleteFromIncompleteList(){
		completeState.incompletedTasks.remove(modifiedEvent);
	}
	
	public void deleteFromFloatingList(){
		completeState.incompletedTasks.remove(modifiedEvent);
	}
}
