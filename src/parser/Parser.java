package parser;

import java.rmi.dgc.Lease;
import java.security.KeyStore.PrivateKeyEntry;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.Element;
import javax.xml.transform.Templates;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import command.Add;
import command.Command;
import command.Delete;
import command.Edit;

import java.util.Date;

import constant.CommandType;
import constant.Constant;
import main.Event;
import main.State;

public class Parser {

	public static main.Event oldEvent;
	
	private final String PATTERN_SPACE = "(\\s)";
	private final String PATTERN_AT = "(\\bat\\w*\\b)";
	private final String PATTERN_AT_OR_BY = "(\\bat|by\\w*\\b)";
	private final String PATTERN_FROM = "(\\bfrom\\w*\\b)";
	private final String PATTERN_TO = "(\\bto\\w*\\b)";
	private final String PATTERN_ON = "(\\bon\\w*\\b)";
	private final String PATTERN_BY = "(\\bby\\w*\\b)";
	private final String PATTERN_BEFORE = "(\\bbefore\\w*\\b)";
	private final String PATTERN_AFTER = "(\\bafter\\w*\\b)";
	private final String PATTERN_NEXT = "(\\bnext\\w*\\\b)";
	private final String PATTERN_TODAY = "(\\btoday\\w*\\b)";
	private final String PATTERN_TOMORROW = "(\\btomorrow\\w*\\b)";
	private final String PATTERN_YESTERDAY = "(\\byesterday\\w*\\b)";
	private final String PATTERN_TOD = "(\\btod\\w*\\b)";
	private final String PATTERN_TOM = "(\\btom\\w*\\b)";
	private final String PATTERN_YES = "(\\byes\\w*\\b)";
	private final String PATTERN_ALL = "(\\bon|at|by|from\\w*\\b)";

	private final String TIME_BEFORE_MIDNIGHT = "23:59";
	private final String ERROR_DATE_FORMAT = "The input date format is not supported";

	public Command parseCommand(String input){
		Command cmdInterface = null;
		main.Event event;
		
		String command = getFirstWord(input);
		CommandType tempCmd = getCommandType(command);

		if (tempCmd == CommandType.INVALID){

		} else if(tempCmd == CommandType.ADD){
			event = executeAdd(removeFirstWord(input));
			oldEvent = event;
			cmdInterface = new Add(event);
		} else if(tempCmd == CommandType.DISPLAY){
			event =  executeDisplay(removeFirstWord(input));
			oldEvent = null;
		} else if(tempCmd == CommandType.DELETE){
			event = executeDelete(removeFirstWord(input));
			oldEvent = event;
			cmdInterface = new Delete(event);
		} else if(tempCmd == CommandType.EDIT){
			event = oldEvent;
			Event realOldEvent = new Event();
			realOldEvent.setName(oldEvent.getName());
			realOldEvent.setDescription(oldEvent.getDescription());
			realOldEvent.setCategory(oldEvent.getCategory());
			realOldEvent.setEndTime(oldEvent.getEndTime());
			realOldEvent.setStartTime(oldEvent.getStartTime());
			realOldEvent.setLocation(oldEvent.getLocation());
			realOldEvent.setStatus(oldEvent.getStatus());
			String remainingInput = extractDescription(event, removeFirstWord(input));
			event = executeEdit(event , remainingInput);
			cmdInterface = new Edit(realOldEvent, event);
			oldEvent = event;
		}
		return cmdInterface;

	}


	/**
	 * For testing user input (TEST)
	 * @param input
	 * @return
	 */
	public main.Event testingParseCommand(String input){
		String command = getFirstWord(input);
		CommandType tempCmd = getCommandType(command);

		if (tempCmd == CommandType.INVALID){
			return null;
		} else if(tempCmd == CommandType.ADD){
			oldEvent = executeAdd(removeFirstWord(input));
			return oldEvent;
		} else if(tempCmd == CommandType.DISPLAY){
			oldEvent = null;
			return executeDisplay(removeFirstWord(input));
		} else if(tempCmd == CommandType.DELETE){
			oldEvent = null;
			return executeDelete(removeFirstWord(input));
		} else if(tempCmd == CommandType.EDIT){
			main.Event task = oldEvent;
			String remainingInput = extractDescription(task, removeFirstWord(input));
			main.Event newEvent = executeEdit(task, remainingInput);
			return newEvent;
		}
		return null;
	}
	/**
	 * This method finds the pattern provided which is used in data extraction
	 * @param desiredPattern-RegEx Pattern for matching the specific preposition
	 * @param input- user's input without the description or note
	 * @return an integer array which contains indexes of the first occurrence of the desired word
	 * 			[0] - start index  , [1] - end index
	 */
	public int[] matchPatterOfFirstOccurrence(String desiredPattern, String input){
		int startIndex = 0;
		int endIndex = 0;

		Pattern pattern = Pattern.compile(desiredPattern,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);

		if(matcher.find()){
			startIndex = matcher.start();
			endIndex = matcher.end();
		}

		int[] resultedIndex = new int[]{startIndex, endIndex};

		return resultedIndex;
	}

	/**
	 * This method finds the pattern provided which is used in data extraction
	 * @param desiredPattern-RegEx Pattern for matching the specific preposition
	 * @param input- user's input without the description or note
	 * @return an integer array which contains indexes of the last occurrence of the desired word
	 * 			[0] - start index  , [1] - end index
	 */
	public static int[] matchPatternOfLastOccurrence(String desiredPattern, String input){
		int startIndex = 0;
		int endIndex = 0;

		Pattern pattern = Pattern.compile(desiredPattern,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);

		while(matcher.find()){
			startIndex = matcher.start();
			endIndex = matcher.end();
		}

		int[] resultedIndex = new int[]{startIndex, endIndex};

		return resultedIndex;
	}

	/**
	 * This method replaces the hour and minutes of the DateTime with the specified time
	 * @param date
	 * @param setTime 
	 * @return
	 */
	private Date writeTime(String date, String setTime){
		Format formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
		date = formatter.format(DateChecker.validateDate(date));
		if(date.indexOf("00:00") >= 0){
			date = date.replace("00:00", setTime);
		}

		return (DateChecker.validateDate(date));
	}

	private main.Event executeAdd(String input){
		main.Event task = new main.Event();
		String remainingInput = extractDescription(task, input);
		return determineAddedInput(task, remainingInput);
	}

	private main.Event executeDisplay(String input){
		main.Event task = new main.Event();
		return decodeDisplayData(task, input);
	}
	
	private main.Event executeDelete(String input){
		main.Event task = new main.Event();
		return decodeDeleteData(task, input);
	}

	private main.Event executeEdit(main.Event task, String input){
		return decodeEditData(task, input);
	}
	/**
	 * Check the input task name from having quotations
	 * @param task
	 * @param input
	 * @return
	 */
	private main.Event determineAddedInput(main.Event task, String input){
		boolean isDoubleQuoted = false;
		boolean isSingleQuoted = false;
		boolean isAdd = true;
		boolean isEdit = false;
		
		int endIndex = 0;
		int offset = 1;

		if(input.indexOf("\"") >= 0){
			int startQuoteIndex = input.indexOf("\"");
			endIndex = input.indexOf("\"", startQuoteIndex + offset);
			if(endIndex > 0){
				task.setName(input.substring(startQuoteIndex + offset, endIndex));
				isDoubleQuoted = true;
			}
		}

		if(input.indexOf("'") >= 0){
			int startQuoteIndex = input.indexOf("'");
			endIndex = input.indexOf("'",startQuoteIndex+ offset);
			if(endIndex > 0){
				task.setName(input.substring(startQuoteIndex + offset, endIndex));
				isSingleQuoted = true;
			}
		}

		if(isSingleQuoted){
			if(endIndex != input.length()-1){
				int startIndex = endIndex + offset;

			}
		}else if(isDoubleQuoted){
			if(endIndex != input.length()-1){
				int startIndex = endIndex + offset;
			}

		}else{
			task = decodeDataFromInput(task, PATTERN_ON, input, isAdd, isEdit);
		}

		return task;
	}
	
	private main.Event decodeDeleteData(main.Event task, String input){
		final int offset = 1;
		int startIndex = 0;
		int endIndex = startIndex;
		
		if(input.indexOf("#") > 0){
			startIndex = input.indexOf("#") + offset;
			if(input.indexOf("all", startIndex) < 0){
				int selectedIndex = Integer.parseInt(input.substring(startIndex));
			} else{
				
			}
		} else{
			task.setName(input);
		}
		return task;
	}
	
	private main.Event decodeEditData(main.Event task, String input){
		final int INDEX_START = 0;
		final int INDEX_END = 1;
		boolean isAdd = false;
		boolean isEdit = true;
		
		if(input.isEmpty()){
			return task;
		}
		
		int[] index = matchPatterOfFirstOccurrence( PATTERN_ALL, input);
		
		if(index[INDEX_START] == 0){
			task = decodeDataFromInput(task, PATTERN_ON, input, isAdd, isEdit);
			return task;
		}
		
		if(index[INDEX_START] != index[INDEX_END]){
			task.setName(input.substring(INDEX_START, index[INDEX_START]).trim());
			input = input.substring(index[INDEX_START], input.length()).trim();
		}
		task = decodeDataFromInput(task, PATTERN_ON, input, isAdd, isEdit);
		return task;
	}

	private main.Event decodeDisplayData(main.Event task, String input){		
		boolean isAdd = false;
		boolean isEdit = false;
		input = input.toUpperCase();

		if (input.startsWith(Constant.CATEGORY_DEADLINE) && input.endsWith(Constant.CATEGORY_DEADLINE)){
			task.setCategory(Constant.CATEGORY_DEADLINE);
			return task;
		} else if(input.startsWith(Constant.CATEGORY_EVENT) && input.endsWith(Constant.CATEGORY_EVENT)){
			task.setCategory(Constant.CATEGORY_EVENT);
			return task;
		} else if(input.startsWith(Constant.CATEGORY_FLOATING) && input.endsWith(Constant.CATEGORY_FLOATING)){
			task.setCategory(Constant.CATEGORY_FLOATING);
			return task;
		} else if(input.startsWith(Constant.CATEGORY_ALL) && input.endsWith(Constant.CATEGORY_ALL)){
			task.setCategory(Constant.CATEGORY_ALL);
			return task;
		} 
		
		task = decodeDataFromInput(task, PATTERN_ON , input, isAdd, isEdit);
		
		return task;
	}

	/**
	 * 
	 * @param task
	 * @param desiredPattern
	 * @param input
	 * @return
	 */
	private main.Event decodeDataFromInput(main.Event task, String desiredPattern, 
			String input, boolean isAdd, boolean isEdit){
		final int INDEX_START = 0;
		final int INDEX_END = 1;

		boolean isStartDateDefined = false;
		boolean isDeadline = false;

		int startIndex = 0;
		int endIndex = 0;
		String tempDate = "";
		
		if(task.getCategory().equalsIgnoreCase(Constant.CATEGORY_DEADLINE)){
			isDeadline = true;
		}

		switch (desiredPattern) {	

		case PATTERN_ON:
			int[] matchedDate = matchPatternOfLastOccurrence(desiredPattern, input);

			if(matchedDate[INDEX_END] > 0 && isEdit){
				startIndex = matchedDate[INDEX_END] + 1;
			} else if(matchedDate[INDEX_END] > 0){
				endIndex = matchedDate[INDEX_START];
				task.setName(input.substring(startIndex, endIndex).trim());
				startIndex = matchedDate[INDEX_END] + 1;
			} 

		case PATTERN_TODAY:
			/*the input does not contain preposition 'on' or DATE*/
			if(startIndex == 0){

			}

		case PATTERN_TOD:
			if(startIndex == 0){

			}

		case PATTERN_TOMORROW:
			if(startIndex == 0){

			}
		case PATTERN_TOM:
			if(startIndex == 0){

			}

		case PATTERN_BY:
			int[] matchedDueDate = matchPatternOfLastOccurrence(PATTERN_BY, input);

			/*the input does not contain preposition 'on' but 'by'*/
			if(startIndex == 0 && matchedDueDate[INDEX_END] > 0 && isAdd){
				String taskName = input.substring(INDEX_START, matchedDueDate[INDEX_START]).trim();
				task.setName(taskName);
				startIndex = matchedDueDate[INDEX_END] + 1;
				isDeadline = true;
			}else if(matchedDueDate[INDEX_END] > 0){   /* the input contains 'on' and 'by'*/ 
				endIndex = matchedDueDate[INDEX_START];
				tempDate = input.substring(startIndex, endIndex).trim();
				startIndex = matchedDueDate[INDEX_END] + 1;
				isDeadline = true;
			}

		case PATTERN_AT:
			int[] matchedData = matchPatternOfLastOccurrence(PATTERN_AT,  input);

			/*the input contains preposition 'at'*/
			if(matchedData[INDEX_END] > 0){
				endIndex = matchedData[INDEX_START];
				
				if(task.getName().isEmpty() && isAdd){
					String name = input.substring(startIndex, endIndex).trim();
					task.setName(name);
					tempDate = null;
				} else if (tempDate.isEmpty()){
					tempDate = input.substring(startIndex, endIndex).trim();
					
				} else{
					String time = input.substring(startIndex, endIndex).trim();
					task = setTaskCategory(task, tempDate, time, isDeadline);
					tempDate = null;
				}
				
				if(tempDate == null || tempDate.isEmpty()){
					tempDate = null;
				}
				endIndex = input.length();
				startIndex = matchedData[INDEX_END] + 1;
			}else{
				endIndex = input.length();
				if((endIndex - startIndex) == input.length()){
					String taskName = input.substring(startIndex, endIndex);
					task.setName(taskName);
					startIndex = endIndex;
					tempDate = null;
					
					if(!isEdit){
						task.setCategory(Constant.CATEGORY_FLOATING);
					}
					
					break;
				} else if(tempDate.isEmpty()){
					tempDate = input.substring(startIndex, endIndex).trim();
					String[] dateTime = extractTimeFromDate(PATTERN_SPACE, tempDate);

					task = setTaskCategory(task, dateTime[0], dateTime[1], isDeadline);
					startIndex = endIndex;
					break;
				}else{
					String date = tempDate.trim();
					String time = input.substring(startIndex,endIndex).trim();
					startIndex = endIndex;
					task = setTaskCategory(task, date, time, isDeadline);
					break;
				}
			}

			if (startIndex != endIndex){
				String undetermined = input.substring(startIndex, endIndex).trim();
				/*the undetermined value is a location*/
				if (DateChecker.validateDate(undetermined) == null){
					task.setLocation(undetermined);

					/*extracting the time component from the datetime if any */
					/* e.g. on Tues at 5 pm or on Tuesday by 5 pm*/
					String[] dateTme_1 = extractTimeFromDate(PATTERN_AT_OR_BY, tempDate);
					String[] dateTme_2 = extractTimeFromDate(PATTERN_SPACE,  tempDate);

					if(dateTme_1[INDEX_END] != null){
						task = setTaskCategory(task, dateTme_1[INDEX_START], dateTme_1[INDEX_END], isDeadline);

					} else {
						task = setTaskCategory(task, dateTme_2[INDEX_START], dateTme_2[INDEX_END], isDeadline);
					}

				}else {		/*undetermined value is time*/
					String time = convertAmPmToTime(undetermined);
					task.setEndTime(writeTime(tempDate, time));
					if(isDeadline){
						task.setStartTime(Constant.MIN_DATE);
						task.setCategory(Constant.CATEGORY_DEADLINE);
					}else{
						task.setStartTime(writeTime(tempDate, time));
						task.setCategory(Constant.CATEGORY_EVENT);
					}
				}
			}

			if (task.getStartTime() == null){
				task.setStartTime(Constant.MIN_DATE);
			}

			break;

		case PATTERN_FROM:
			int[] matchedStartDate = matchPatternOfLastOccurrence(PATTERN_FROM, input);

			if (matchedStartDate[INDEX_END] > 0){
				endIndex = matchedStartDate[INDEX_START];
				Date startDateTime = DateChecker.validateDate(input.substring(startIndex, endIndex));
				if (startDateTime == null){
					task.setStartTime(Constant.MIN_DATE);
				}else {
					task.setStartTime(startDateTime);
					isStartDateDefined = true;
				}
			}

		case PATTERN_TO:
			if (isStartDateDefined){
				int[] matchedEndDate = matchPatternOfLastOccurrence(PATTERN_TO, input);
			}

		default:
			break;
		}

		return task;

	}

	/**
	 * 
	 * @param pattern - RegEx pattern to match the desired word
	 * @param date - input date
	 * @return string array of size 2 with date value at index 0 and time value at index 1 
	 */
	private String[] extractTimeFromDate(String pattern, String date){
		final int INDEX_START = 0;
		final int INDEX_END = 1;
		String[] dateTime = {null,null};
		int[] matchedData;
		if (date == null){
			return dateTime;
		}

		if (pattern != PATTERN_SPACE){
			matchedData = matchPatternOfLastOccurrence(pattern, date);
		}else {
			matchedData = matchPatterOfFirstOccurrence(pattern, date);
		}

		if (matchedData[INDEX_START] > 0){
			dateTime[0] = date.substring(INDEX_START, matchedData[INDEX_START]).trim();
			dateTime[1] = date.substring(matchedData[INDEX_END], date.length()).trim();
		}else {
			dateTime[0] = date;
			dateTime[1] = null;
		}
		return dateTime;
	}

	/**
	 * 
	 * @param task
	 * @param date
	 * @param time
	 * @return
	 */
	private main.Event setTaskCategory(main.Event task, String date, String time, boolean isDeadline){
		if(date == null){
			return task;
		}
		Date inputDate = DateChecker.validateDate(date);
		/*input task type is of deadline specific type*/
		if (time != null && inputDate != null && isDeadline){
			time = convertAmPmToTime(time);
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
			task.setEndTime(writeTime(formatter.format(inputDate), time));
			task.setStartTime(Constant.MIN_DATE);
			task.setCategory(Constant.CATEGORY_DEADLINE);
		} else if(time != null && inputDate != null && !isDeadline){
			time = convertAmPmToTime(time);
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
			task.setEndTime(writeTime(formatter.format(inputDate), time));
			task.setStartTime(writeTime(formatter.format(inputDate), time));
			task.setCategory(Constant.CATEGORY_EVENT);
		} else if (time == null && inputDate != null && !isDeadline){	/*input task is event type*/
			task.setStartTime(inputDate);
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
			task.setEndTime(writeTime(formatter.format(inputDate), TIME_BEFORE_MIDNIGHT));
			task.setCategory(Constant.CATEGORY_EVENT);
		} else if(time == null && inputDate != null && isDeadline){
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
			task.setEndTime(writeTime(formatter.format(inputDate), TIME_BEFORE_MIDNIGHT));
			task.setStartTime(Constant.MIN_DATE);
			task.setCategory(Constant.CATEGORY_DEADLINE);
		} else if (time == null && inputDate == null && isDeadline){
			task.setStartTime(Constant.MIN_DATE);
			task.setCategory(Constant.CATEGORY_DEADLINE);
		} else if (time == null && inputDate == null && !isDeadline) {
			task.setStartTime(Constant.MIN_DATE);
			task.setCategory(Constant.CATEGORY_FLOATING);

		}
		return task;
	}

	/**
	 * Converts time which is in AM or PM format to 24 hour time format
	 * @param timeInput
	 * @return time in 24 hour format e.g. 23:59
	 */
	private static String convertAmPmToTime(String timeInput){
		SimpleDateFormat formatterInput;
		SimpleDateFormat formatterOutput = new SimpleDateFormat("HH:mm");
		String time = null;
		try {
			formatterInput = new SimpleDateFormat("hh a");
			time = formatterOutput.format(formatterInput.parse(timeInput));
		} catch (ParseException e) {
			//e.printStackTrace();
		}

		try{
			formatterInput = new SimpleDateFormat("hh:mm a");
			time = formatterOutput.format(formatterInput.parse(timeInput));
		} catch (ParseException e){
			//e.printStackTrace();
		}

		return time;
	}

	/**
	 * This method extracts the description out of the input and set the value to the Event object
	 * @param event
	 * @param input
	 * @return the remaining String input which are left to be processed 
	 */
	private String extractDescription(main.Event event, String input){
		int startIndex = input.indexOf("//");
		int offset = 2;

		//no description or note input
		if (startIndex < 0){
			return input;
		}

		int endIndex = input.length();
		String description = input.substring(startIndex + offset, endIndex).trim();
		event.setDescription(description);

		return (input.substring(0,startIndex).trim());
	}


	private CommandType getCommandType(String command){
		if (command.equalsIgnoreCase("add")){
			return CommandType.ADD;
		}else if (command.equalsIgnoreCase("delete")){
			return CommandType.DELETE;
		}else if (command.equalsIgnoreCase("display") || 
				command.equalsIgnoreCase("search")){
			return CommandType.DISPLAY;
		}else if (command.equalsIgnoreCase("edit") || 
				command.equalsIgnoreCase("e")){
			return CommandType.EDIT;
		}else if (command.equalsIgnoreCase("import")){
			return CommandType.IMPORT;
		}else if (command.equalsIgnoreCase("export")){
			return CommandType.EXPORT;
		}else if (command.equalsIgnoreCase("block")){
			return CommandType.BLOCK;
		}else if (command.equalsIgnoreCase("unblock")){
			return CommandType.UNBLOCK;
		}else if (command.equalsIgnoreCase("confirm")){
			return CommandType.CONFIRM;
		}else if (command.equalsIgnoreCase("undo")){
			return CommandType.UNDO;
		}else if (command.equalsIgnoreCase("redo")){
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
