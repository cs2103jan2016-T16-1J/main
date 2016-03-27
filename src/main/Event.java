package main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import constant.Constant;
import main.GenericEvent.Category;
import main.GenericEvent.Status;

public class Event extends GenericEvent{
	private Date startTime;
	private Date endTime;
	
	public Event(){
		super();
		startTime = Constant.MIN_DATE; 
		endTime = Constant.MAX_DATE;
	}
	
	public Event(String name, String location, String description, Category category, Date startTime, Date endTime, Status status){
		super(name, location, description, category, status);
		this.startTime = startTime;
		this.endTime = endTime;
		
	}

	public void setStartTime(Date startTime){
		this.startTime = startTime;
	}
	
	public Date getStartTime(){
		return this.startTime;
	}
	
	public void setEndTime(Date endTime){
		this.endTime = endTime;
	}
	
	public Date getEndTime(){
		return this.endTime;
	}

	public Event getClone(){
		Event clonedEvent = new Event(this.getName(), this.getLocation(), this.getDescription(), this.getCategory(), this.getStartTime(), this.getEndTime(), this.getStatus());
		
		return clonedEvent;
	}
	
	

	
}
