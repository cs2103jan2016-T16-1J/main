package main;

import java.util.ArrayList;

/**
 * A ReservedEvent is a subclass of the GenericEvent which is defined with an ArrayList of Time Pairs
 * @@author Reem Razak
 */
public class ReservedEvent extends GenericEvent{
	private ArrayList<TimePair> reservedTimes;
	
	/**
	 * The Default Constructor for Reserved Event
	 */
	public ReservedEvent(){
		super();
		reservedTimes = new ArrayList<TimePair>();
	}
	
	/**
	 * Constructor which creates ReservedEvent using provided parameters
	 * @param name
	 * @param location
	 * @param description
	 * @param category
	 * @param reservedTimes
	 * @param status
	 */
	public ReservedEvent(String name, String location,String description,
			Category category, ArrayList<TimePair> reservedTimes, Status status){
		super(name, location, description, category, status);
		this.reservedTimes = reservedTimes;
		
	}

	/**
	 * Gets the ArrayList of all Reserved Times
	 * @return
	 */
	public ArrayList<TimePair> getReservedTimes(){
		return this.reservedTimes;
	}
	
	/**
	 * Assigns the provided ArrayList to the ArrayList of Reserved Times
	 * @param reservedTimes
	 */
	public void setReservedTimes(ArrayList<TimePair> reservedTimes){
		this.reservedTimes = reservedTimes;
		
	}
	
	/**
	 * Gets an exact copy of the ReservedEvent
	 * @return
	 */
	public ReservedEvent getClone(){
		ReservedEvent clonedEvent = new ReservedEvent(this.getName(), this.getLocation(),
				this.getDescription(), this.getCategory(), this.getReservedTimes(), this.getStatus());
		
		return clonedEvent;
	}
	
}
