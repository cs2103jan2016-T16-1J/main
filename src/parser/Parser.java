package parser;

import java.awt.Event;
import java.awt.List;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Date;

import javax.print.attribute.standard.DateTimeAtCompleted;

import constant.CommandType;
import constant.Constant;

public class Parser {

	private final int ZERO = 0;

	public main.Event parseCommand(String input){

		String command = getFirstWord(input);
		CommandType tempCmd = getCommandType(command);

		if(tempCmd == CommandType.INVALID){

		}else if(tempCmd == CommandType.ADD){
			return executeAdd(removeFirstWord(input));
		}

		return null;
	}

	private main.Event executeAdd(String input){
		Boolean isSingleQuoted = false;
		Boolean isDoubleQuoted = false;
		Boolean isStartQuoted = false;
		Boolean isEndQuoted = false;
		Boolean isEndOfTaskName = false;

		String tempWord = ""; 
		StringBuffer sBuffer = new StringBuffer();
		int startIndex = ZERO;
		int endIndex= startIndex;
		main.Event event = new main.Event();

		while(!isEndOfTaskName){
			endIndex = input.indexOf(" ", startIndex);

			//indicates the end of input
			if(endIndex < 0){
				endIndex = input.length();
				isEndOfTaskName = true;
			}
			tempWord = input.substring(startIndex, endIndex);


			if(isPreposition(tempWord)){
				isEndOfTaskName = true;
				event = determineInputData(event, input, startIndex, endIndex);
			}
			
			startIndex = endIndex + 1;	
			sBuffer.append(tempWord + " ");
		}

		String taskName = sBuffer.toString().trim();
		if(taskName.isEmpty()){
			event.setName(Constant.EMPTY_NAME);
		}else{
			event.setName(sBuffer.toString().trim());
		}	

		event.setLocation(Constant.EMPTY_LOCATION);
		event.setDescription(Constant.EMPTY_DESCRIPTION);

		return event;

	}

	private boolean isPreposition(String word){
		if(word.equalsIgnoreCase("by") || word.equalsIgnoreCase("on") || word.equalsIgnoreCase("at")||
				word.equalsIgnoreCase("before") || word.equalsIgnoreCase("after") || word.equalsIgnoreCase("from") || 
				word.equalsIgnoreCase("to")){

			return true;
		}
		return false;
	}
	
	private main.Event determineInputData(main.Event event, String input, int startIndex, int endIndex){
		boolean isTimeDefined = false;
		boolean isLocationDefined = false;
		String tempWord = input.substring(startIndex,endIndex);
		if(tempWord.equalsIgnoreCase("on")){
			startIndex =  endIndex + 1;
			//return -1 if false
			if(input.indexOf("from", startIndex) >= 0){
				endIndex = input.indexOf("from" , startIndex);
			}else if(input.indexOf("at", startIndex) >= 0){
				endIndex = input.indexOf("at" , startIndex);
			}else{
				endIndex = input.length();
			}
			
		}else if(tempWord.equalsIgnoreCase("from")){
			
		}else if(tempWord.equalsIgnoreCase("at")){
			
		}else if(tempWord.equalsIgnoreCase("by")){
			
		}else if(tempWord.equalsIgnoreCase("before")){
			
		}else if(tempWord.equalsIgnoreCase("after")){
			
		}
		
		return event;
	}
	
	private void checkDate(String dateInput){
		ArrayList<SimpleDateFormat> dateFormatList = new ArrayList<>();
		dateFormatList.add(new SimpleDateFormat("dd/MM/yy"));
		dateFormatList.add(new SimpleDateFormat("EEE"));
		dateFormatList.add(new SimpleDateFormat("dd MMM yy"));
		dateFormatList.add(new SimpleDateFormat("dd/MM/yy HH:mm"));
		dateFormatList.add(new SimpleDateFormat("dd MMM yy HH:mm"));
		
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		
		Format dateFormatter = new SimpleDateFormat("dd/MM/yy");
		String today = dateFormatter.format(date);
		
	}
	
	private CommandType getCommandType(String command){
		if(command.equalsIgnoreCase("add")){
			return CommandType.ADD;
		}else if(command.equalsIgnoreCase("delete")){
			return CommandType.DELETE;
		}else if(command.equalsIgnoreCase("display")){
			return CommandType.DISPLAY;
		}else if(command.equalsIgnoreCase("search")){
			return CommandType.SEARCH;
		}else if(command.equalsIgnoreCase("edit")){
			return CommandType.EDIT;
		}else if(command.equalsIgnoreCase("import")){
			return CommandType.IMPORT;
		}else if(command.equalsIgnoreCase("export")){
			return CommandType.EXPORT;
		}else if(command.equalsIgnoreCase("block")){
			return CommandType.BLOCK;
		}else if(command.equalsIgnoreCase("unblock")){
			return CommandType.UNBLOCK;
		}else if(command.equalsIgnoreCase("confirm")){
			return CommandType.CONFIRM;
		}else if(command.equalsIgnoreCase("undo")){
			return CommandType.UNDO;
		}else if(command.equalsIgnoreCase("redo")){
			return CommandType.REDO;
		}

		return CommandType.INVALID;
	}

	private String getFirstWord(String input){
		return input.trim().split("\\s+")[0];
	}

	private String removeFirstWord(String input){
		return input.replace(getFirstWord(input), "").trim();
	}

}
