package object;

import java.awt.Window.Type;
import java.sql.Date;

import constant.CommandType;

public class Task {

	private CommandType _type;
	private Date _startTime, _endTime;
	private String _name, _location, _note;
	
	private final String EMPTY_NAME = "";
	private final String EMPTY_LOCATION = "";
	private final String EMPTY_NOTE = "";
	private final Date MIN_DATE = new Date(0);
	private final Date MAX_DATE = new Date(Long.MAX_VALUE);
	
	public Task(){
		_type =  null;
		_name = EMPTY_NAME;
		_location = EMPTY_LOCATION;
		_note = EMPTY_NOTE;
		_startTime = MIN_DATE;
		_endTime = MAX_DATE;
	}

	public CommandType getType(){
		return _type;
	}

	public void setType(CommandType type){
		_type = type;
	}

	public String getName(){
		return _name;
	}

	public void setName(String name){
		_name = name;
	}

	public String getPlace(){
		return _location;
	}

	public void setPlace(String location){
		_location = location;
	}

	public String getNote(){
		return _note;
	}

	public void setNote(String note){
		_note = note;
	}

	public Date getStartTime(){
		return _startTime;
	}

	public void setStartTime(Date startTime){
		_startTime = startTime;
	}

	public Date getEndTime(){
		return _endTime;
	}

	public void setEndTime(Date endTime) {
		_endTime = endTime;
	}
}
