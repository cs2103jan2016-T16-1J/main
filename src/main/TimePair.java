package main;

import java.text.SimpleDateFormat;
import java.util.Date;

import constant.Constant;

public class TimePair {
	private Date startTime;
	private Date endTime;
	private String stringStartTime;
	private String stringEndTime;
	
	public TimePair(Date startTime, Date endTime){
		this.startTime = startTime;
		this.endTime = endTime;
		
		if(startTime == Constant.MIN_DATE){
			this.stringStartTime = "";
		} else{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			this.stringStartTime = sdf.format(startTime);
		}
		
		if(endTime == Constant.MAX_DATE){
			this.stringEndTime = "";
		} else{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			this.stringEndTime = sdf.format(endTime);
		}
	}

	public Date getStartTime(){
		return this.startTime;
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

