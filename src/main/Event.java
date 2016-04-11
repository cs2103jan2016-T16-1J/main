package main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import constant.Constant;
import main.GenericEvent.Category;
import main.GenericEvent.Status;

/**
 * An Event is a subclass of the GenericEvent which is defined with a startTime and endTime
 * @@author Reem Razak
 */
public class Event extends GenericEvent{

	private Date startTime;
	private Date endTime;
	private String stringStartTime;
	private String stringEndTime;
	
	/**
	 * Default Constructor for Event
	 */
	public Event(){
		super();
		startTime = Constant.MIN_DATE; 
		endTime = Constant.MAX_DATE;
		stringStartTime = "";
		stringEndTime = "";
	}
	
	/**
	 * Constructor which creates Event using provided parameters
	 * @param name
	 * @param location
	 * @param description
	 * @param category
	 * @param startTime
	 * @param endTime
	 * @param stringStartTime
	 * @param stringEndTime
	 * @param status
	 */
	public Event(String name, String location, String description, Category category, Date startTime, Date endTime,
			String stringStartTime,String stringEndTime, Status status){
		super(name, location, description, category, status);
		this.startTime = startTime;
		this.endTime = endTime;
		this.stringStartTime = stringStartTime;
		this.stringEndTime = stringEndTime;
	}

	/**
	 * Sets the startTime of the Event
	 * @param startTime
	 */
	public void setStartTime(Date startTime){
		if(startTime == Constant.MIN_DATE){
			this.stringStartTime = "";
		} else{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			this.stringStartTime = sdf.format(startTime);
		}
		this.startTime = startTime;
	}
	
	/**
	 * Returns the startTime of the Event in Date format
	 * @return
	 */
	public Date getStartTime(){
		return this.startTime;
	}
	
	/**
	 * Sets the endTime of the Event
	 * @param endTime
	 */
	public void setEndTime(Date endTime){
		if(endTime == Constant.MAX_DATE){
			this.stringEndTime = "";
		} else{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			this.stringEndTime = sdf.format(endTime);
		}
		this.endTime = endTime;
	}
	
	/**
	 * Returns the endTime of the Event in Date format
	 * @return
	 */
	public Date getEndTime(){
		return this.endTime;
	}
	
	/**
	 * Returns the endTime in string format
	 * @return
	 */
	public String getEndTimeString(){
		return this.stringEndTime;
	}
	
	/**
	 * Returns the startTime in string format
	 * @return
	 */
	public String getStartTimeString(){
		return this.stringStartTime;
	}

	/**
	 * Returns a clone of the Event
	 * @return
	 */
	public Event getClone(){
		Event clonedEvent = new Event(this.getName(), this.getLocation(), this.getDescription(), this.getCategory(), 
				this.getStartTime(), this.getEndTime(), this.getStartTimeString(), this.getEndTimeString(), this.getStatus());
		
		return clonedEvent;
	}
	
	

	
}
