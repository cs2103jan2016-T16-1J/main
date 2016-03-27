package main;

import java.util.Date;

public class TimePair {
	private Date startTime;
	private Date endTime;
	
	public TimePair(Date startTime, Date endTime){
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Date getStartTime(){
		return this.startTime;
	}
	
	public void setStartTime(Date startTime){
		this.startTime = startTime;
	}
		
	public Date getEndTime(){
		return this.endTime;
	}
	
	public void setEndTime(Date endTime){
		this.endTime = endTime;
	}
}

