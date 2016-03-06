package main;

import java.util.Date;

import javax.tools.DocumentationTool.Location;

import constant.Constant;

public class Event {
	public enum Status {
		COMPLETE, INCOMPLETE, FLOATING
	};
	
	String name;
	String description;
	String category;
	String location;
	Date startTime;
	Date endTime;
	Status status;
	
	public Event(){
		name = Constant.EMPTY_NAME;
		location = Constant.EMPTY_LOCATION;
		description = Constant.EMPTY_DESCRIPTION;
		category = Constant.EMPTY_CATEGORY;
		startTime = Constant.MIN_DATE;
		endTime = Constant.MAX_DATE;
		status = Status.INCOMPLETE;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setCategory(String category){
		this.category = category;
	}
	
	public String getCategory(){
		return this.category;
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

	public void setStatus(Status status){
		this.status = status;
	}
	
	public Status getStatus(){
		return this.status;
	}
	
	public void setLocation(String location){
		this.location = location;
	}
	
	public String getLocation(){
		return this.location;
	}
	
	/**
	 * Print Event for Debugging purposes
	 */
	public String printEvent(){
		String result = new String("\n" + "Event name: " + this.name + "\n");
		result = result + "\t " + description + "\n";
		result = result + "\t Start Time: " + this.startTime + " End Time: " + this.endTime + "\n";
		result = result + "\t Category: " + this.category + " Status: " + this.status + "\n";
		
		return result;

	}
	
}
