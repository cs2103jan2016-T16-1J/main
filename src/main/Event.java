package main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import constant.Constant;

public class Event {
	public enum Status {
		NULL, INCOMPLETE, OVERDUE, BLOCKED, FLOATING, COMPLETE
	};
	
	public enum Category {
		NULL, DEADLINE, EVENT, FLOATING, UNDETERMINED
	};
	
	private String name;
	private String description;
	private Category category;
	private String location;
	private Date startTime;
	private Date endTime;
	private Status status;
	private List<Integer> selection;
	
	public Event(){
		name = Constant.EMPTY_STRING;
		location = Constant.EMPTY_STRING;
		description = Constant.EMPTY_STRING;
		category = Category.FLOATING;
		startTime = Constant.MIN_DATE; 
		endTime = Constant.MAX_DATE;
		status = Status.INCOMPLETE;
		selection = new ArrayList<>();
	}
	
	public Event(String name, String location, String description, Category category, Date startTime, Date endTime, Status status){
		this.name = name;
		this.location = location;
		this.description = description;
		this.category = category;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = status;
		
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
	
	public void setCategory(Category category){
		this.category = category;
	}
	
	public Category getCategory(){
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

	public Event getClone(){
		Event clonedEvent = new Event(this.getName(), this.getLocation(), this.getDescription(), this.getCategory(), this.getStartTime(), this.getEndTime(), this.getStatus());
		
		return clonedEvent;
	}
	
	public List<Integer> getSelection(){
		return selection;
	}
	
	public void setSelection(List<Integer> selection){
		this.selection = selection;
	}
	
	
	/**
	 * Print Event for Debugging purposes
	 */
	
	public boolean isDeadline() {
		if (this.category == Category.DEADLINE) {
			return true;
		}
		return false;
	}
	
	public boolean isEvent() {
		if (this.category == Category.EVENT) {
			return true;
		}
		return false;
	}
	
	public String printEvent(){
		String result = new String("\n" + "Event name: " + this.name + "\n");
		result = result + "\t " + description + "\n";
		result = result + "\t Start Time: " + this.startTime + " End Time: " + this.endTime + "\n";
		result = result + "\t Category: " + this.category + " Status: " + this.status + "\n";
		
		return result;

	}
	
}
