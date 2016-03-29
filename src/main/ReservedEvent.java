package main;

import java.util.ArrayList;


public class ReservedEvent extends GenericEvent{
	private ArrayList<TimePair> reservedTimes;
	
	public ReservedEvent(){
		super();
		reservedTimes = new ArrayList<TimePair>();
	}
	
	public ReservedEvent(String name, String location,String description,
			Category category, ArrayList<TimePair> reservedTimes, Status status){
		super(name, location, description, category, status);
		this.reservedTimes = reservedTimes;
		
	}

	public ArrayList<TimePair> getReservedTimes(){
		return this.reservedTimes;
	}
	
	public void setReservedTimes(ArrayList<TimePair> reservedTimes){
		this.reservedTimes = reservedTimes;
		
	}
	
	public ReservedEvent getClone(){
		ReservedEvent clonedEvent = new ReservedEvent(this.getName(), this.getLocation(),
				this.getDescription(), this.getCategory(), this.getReservedTimes(), this.getStatus());
		
		return clonedEvent;
	}
}
