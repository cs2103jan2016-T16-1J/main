package command;

import main.Event;
import state.State;

public class Add implements Command{

	Event modifiedEvent;
	State completeState;
	
	public Add(Event modifiedEvent){
		this.modifiedEvent = modifiedEvent;
	}
	
	public State execute(State completeState){
		
		this.completeState = completeState;
		
		switch (modifiedEvent.getStatus()){
		case COMPLETE:
			addToCompleteList();
			break;
		case INCOMPLETE:
			addToIncompleteList();
			break;
		case FLOATING:
			addToFloatingList();
			break;
		}
		
		return completeState;
	}
	
	public void addToCompleteList(){
		completeState.completedTasks.add(modifiedEvent);
	}
	
	public void addToIncompleteList(){
		completeState.incompletedTasks.add(modifiedEvent);
	}
	
	public void addToFloatingList(){
		completeState.floatingTasks.add(modifiedEvent);
		
	}
}
