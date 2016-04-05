package main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import constant.Constant;
import main.GenericEvent.Category;
import main.GenericEvent.Status;

public class Event extends GenericEvent{
	private Date startTime;
	private Date endTime;
	private String stringStartTime;
	private String stringEndTime;
	
	public Event(){
		super();
		startTime = Constant.MIN_DATE; 
		endTime = Constant.MAX_DATE;
		stringStartTime = "";
		stringEndTime = "";
	}
	
	public Event(String name, String location, String description, Category category, Date startTime, Date endTime,
			String stringStartTime,String stringEndTime, Status status){
		super(name, location, description, category, status);
		this.startTime = startTime;
		this.endTime = endTime;
		
	}

	public void setStartTime(Date startTime){
		if(startTime == Constant.MIN_DATE){
			this.stringStartTime = "";
		} else{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			this.stringStartTime = sdf.format(startTime);
		}
		this.startTime = startTime;
	}
	
	public Date getStartTime(){
		return this.startTime;
	}
	
	public void setEndTime(Date endTime){
		if(endTime == Constant.MAX_DATE){
			this.stringEndTime = "";
		} else{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			this.stringEndTime = sdf.format(endTime);
		}
		this.endTime = endTime;
	}
	
	public Date getEndTime(){
		return this.endTime;
	}
	
	public String getEndTimeString(){
		return this.stringEndTime;
	}
	public String getStartTimeString(){
		return this.stringStartTime;
	}

	public Event getClone(){
		Event clonedEvent = new Event(this.getName(), this.getLocation(), this.getDescription(), this.getCategory(), 
				this.getStartTime(), this.getEndTime(), this.getStartTimeString(), this.getEndTimeString(), this.getStatus());
		
		return clonedEvent;
	}
	
	

	
}
