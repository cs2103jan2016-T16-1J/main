package main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import constant.Constant;

/**
 * TimePair class is a tuple class is defined by a startTime and an endTime
 * TimePair must be instantiated with a startTime and an endTime
 * @@author Reem Razak
 */
public class TimePair {
	private Date startTime;
	private Date endTime;
	private String stringStartTime;
	private String stringEndTime;
	
	/**
	 * TimePair constructor which takes in  a startTime and an endTime
	 * @param startTime
	 * @param endTime
	 */
	public TimePair(Date startTime, Date endTime){
		this.startTime = startTime;
		this.endTime = endTime;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		    
		if(startTime == Constant.MIN_DATE){
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(endTime); 
			cal.add(Calendar.HOUR_OF_DAY, -1); 
			this.startTime = cal.getTime(); 	
			this.stringStartTime = "";
		} else{
			this.stringStartTime = sdf.format(startTime);
		}
		
		if(endTime == Constant.MAX_DATE){
			this.stringEndTime = "";
		} else{
			this.stringEndTime = sdf.format(endTime);
		}
	}

	public Date getStartTime(){
		return this.startTime;
	}
	
	public void setStartTime(Date startTime){
		if(startTime == Constant.MIN_DATE){
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(endTime); 
			cal.add(Calendar.HOUR_OF_DAY, -1); 
			this.startTime = cal.getTime(); 
			this.stringStartTime = "";
		} else{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			this.stringStartTime = sdf.format(startTime);
		}
		this.startTime = startTime;
	}
		
	public Date getEndTime(){
		return this.endTime;
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
	
	public String getStartTimeString(){
		return stringStartTime;
	}
	
	public String getEndTimeString(){
		return stringEndTime;
	}
}

