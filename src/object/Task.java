package object;

import java.sql.Date;

import constant.CommandType;
import constant.Constant;
import constant.EventStatus;

public class Task {

	private CommandType _type;
	private EventStatus _status;
	private Date _startTime;
	private Date _endTime;
	private String _name;
	private String _location;
	private String _note;

	public Task(){
		_type =  null;
		_name = Constant.EMPTY_NAME;
		_location = Constant.EMPTY_LOCATION;
		_note = Constant.EMPTY_NOTE;
		_startTime = Constant.MIN_DATE;
		_endTime = Constant.MAX_DATE;
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
	
	public EventStatus getStatus(){
		return _status;
	}
	
	public void setStatus(EventStatus status){
		_status = status;
	}
}
