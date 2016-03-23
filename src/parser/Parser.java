package parser;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import command.Add;
import command.Command;
import command.Delete;
import command.Edit;
import command.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import constant.CommandType;
import constant.Constant;
import main.Event;
import main.Event.Status;

public class Parser {

	private static Event oldEvent;
	private boolean isNameDefined = true;
	private boolean isBlock = false;
	private boolean isEdit = false;
	private final String PATTERN_SPACE = "(\\s)";
	private final String PATTERN_AM_OR_PM = "(\\b(am)\\b|\\b(pm)\\b)";
	private final String PATTERN_AT = "(\\bat\\b)";
	private final String PATTERN_AT_OR_BY = "(\\b(at)\\b|\\b(by)\\b)";
	private final String PATTERN_COLUMN = "(\\b(:)\\b)";
	private final String PATTERN_FROM = "(\\bfrom\\b)";
	private final String PATTERN_TO = "(\\bto\\b)";
	private final String PATTERN_ON = "(\\bon\\b)";
	private final String PATTERN_BY = "(\\bby\\b)";
	private final String PATTERN_BEFORE = "(\\bbefore\\b)";
	private final String PATTERN_AFTER = "(\\bafter\\b)";
	private final String PATTERN_NEXT = "(\\bnext\\b)";
	private final String PATTERN_TODAY = "(\\btoday\\b)";
	private final String PATTERN_TOMORROW = "(\\btomorrow\\b)";
	private final String PATTERN_YESTERDAY = "(\\byesterday\\b)";
	private final String PATTERN_TOD = "(\\btod\\b)";
	private final String PATTERN_TOM = "(\\btom\\b)";
	private final String PATTERN_YES = "(\\byes\\b)";
	private final String PATTERN_ALL = "(\\b(on)\\b|\\b(by)\\b|\\b(from)\\b|\\b(at)\\b)";

	private final String TIME_BEFORE_MIDNIGHT = "23:59";
	private final String TIME_BEFORE_MIDNIGHT_SEC = "23:59:01";
	private final String TIME_MIDNIGHT = "00:00";
	private final String ERROR_DATE_FORMAT = "The input date format is not supported";

	public Command parseCommand(String input){
		Command cmdInterface = null;
		Event event = new Event();

		String command = getFirstWord(input);
		CommandType tempCmd = getCommandType(command);

		if (tempCmd == CommandType.INVALID){
			event = null;
			oldEvent = null;
		} else if(tempCmd == CommandType.ADD){
			event = decodeAddData(event, removeFirstWord(input));
			oldEvent = event;
			cmdInterface = new Add(event);
		} else if(tempCmd == CommandType.SEARCH){
			event =  decodeSearchData(event, removeFirstWord(input));
			oldEvent = null;
		} else if(tempCmd == CommandType.DELETE){
			event = decodeDeleteData(event, removeFirstWord(input));
			oldEvent = null;
			cmdInterface = new Delete(event);
		} else if(tempCmd == CommandType.EDIT){
			Event realOldEvent = new Event();
			event = oldEvent;
			realOldEvent.setName(oldEvent.getName());
			realOldEvent.setDescription(oldEvent.getDescription());
			realOldEvent.setCategory(oldEvent.getCategory());
			realOldEvent.setEndTime(oldEvent.getEndTime());
			realOldEvent.setStartTime(oldEvent.getStartTime());
			realOldEvent.setLocation(oldEvent.getLocation());
			realOldEvent.setStatus(oldEvent.getStatus());
			
			isEdit = true;
			event = decodeEditData(event , removeFirstWord(input));
			cmdInterface = new Edit(realOldEvent, event);
			oldEvent = event;
		} else if(tempCmd == CommandType.SELECT){
			event = decodeSelectData(event, removeFirstWord(input));
			cmdInterface = new Select(event);
		} else if(tempCmd == CommandType.BLOCK){
			event = decodeAddData(event, removeFirstWord(input));
			event.setCategory(Constant.CATEGORY_UNDETERMINED);
			isBlock = true;
			oldEvent = event;
			cmdInterface = new Add(event);
		} else if(tempCmd == CommandType.UNBLOCK){
			event = decodeDeleteData(event, removeFirstWord(input));
			event.setCategory(Constant.CATEGORY_UNDETERMINED);
			oldEvent = event;
			cmdInterface = new Delete(event);
		} else if(tempCmd == CommandType.UNDO){
			
		} else if(tempCmd == CommandType.REDO){
			
		} else if(tempCmd == CommandType.CONFIRM){
			event = determineQuotedInput(event, removeFirstWord(input));
			event = determineCategory(event);
			oldEvent = event;
			
		}
		return cmdInterface;

	}


	/**
	 * For testing user input (TEST)
	 * @param input
	 * @return
	 */
	public Event testingParseCommand(String input){
		String command = getFirstWord(input);
		CommandType tempCmd = getCommandType(command);
		Event task = new Event();

		if (tempCmd == CommandType.INVALID){
			return null;
		} else if(tempCmd == CommandType.ADD){
			oldEvent = decodeAddData(task, removeFirstWord(input));
			return oldEvent;
		} else if(tempCmd == CommandType.SEARCH){
			oldEvent = null;
			return decodeSearchData(task, removeFirstWord(input));
		} else if(tempCmd == CommandType.DELETE){
			oldEvent = null;
			return decodeDeleteData(task, removeFirstWord(input));
		} else if(tempCmd == CommandType.EDIT){
			Event oldTask = new Event();
			oldTask = oldEvent;
			task.setName(oldEvent.getName());
			task.setDescription(oldEvent.getDescription());
			task.setCategory(oldEvent.getCategory());
			task.setEndTime(oldEvent.getEndTime());
			task.setStartTime(oldEvent.getStartTime());
			task.setLocation(oldEvent.getLocation());
			task.setStatus(oldEvent.getStatus());
			isEdit = true;
			task = decodeEditData(task , removeFirstWord(input));
			oldEvent = task;
			return task;
		} else if(tempCmd == CommandType.BLOCK){
			task = decodeAddData(task, removeFirstWord(input));
			task.setCategory(Constant.CATEGORY_UNDETERMINED);
			isBlock = true;
			oldEvent = task;
			return task;
		} else if(tempCmd == CommandType.UNBLOCK){
			
		} else if(tempCmd == CommandType.CONFIRM){
			
		} else if(tempCmd == CommandType.SELECT){
			task = decodeSelectData(task, removeFirstWord(input));
			return task;
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
	private int[] matchPatternOfFirstOccurrence(String desiredPattern, String input){
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
	private static int[] matchPatternOfLastOccurrence(String desiredPattern, String input){
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

	private String classifyCategory(String userInput){
		
		if(userInput.equalsIgnoreCase(Constant.CATEGORY_DEADLINE)){
			return Constant.CATEGORY_DEADLINE;
		} else if(userInput.equalsIgnoreCase(Constant.CATEGORY_EVENT)){
			return Constant.CATEGORY_EVENT;
		} else if(userInput.equalsIgnoreCase(Constant.CATEGORY_FLOATING)){
			return Constant.CATEGORY_FLOATING;
		} else if(userInput.equalsIgnoreCase(Constant.CATEGORY_UNDETERMINED)){
			return Constant.CATEGORY_UNDETERMINED;
		} 
		
		return null;
	}
	
	private Event.Status classifyStatus(String userInput){
		
		if(userInput.equalsIgnoreCase(Constant.STATUS_INCOMPLETE)){
			return Event.Status.INCOMPLETE;
		}else if(userInput.equalsIgnoreCase(Constant.STATUS_COMPLETE)){
			return Event.Status.COMPLETE;
		}else if(userInput.equalsIgnoreCase(Constant.STATUS_FLOATING)){
			return Event.Status.FLOATING;
		}else if(userInput.equalsIgnoreCase(Constant.STATUS_BLOCKED)){
			return Event.Status.BLOCKED;
		}else if(userInput.equalsIgnoreCase(Constant.STATUS_OVERDUE)){
			return Event.Status.OVERDUE;
		}
		
		return null;
	}
	/**
	 * Decodes the user input for Adding new task
	 * @param task
	 * @param input
	 * @return Event object with updated values
	 */
	private Event decodeAddData(Event task, String input){
		String remainingInput = extractDescription(task, input);
		return determineQuotedInput(task, remainingInput);
	}
	
	/**
	 * Decodes the user input for selecting task
	 * @param task
	 * @param input
	 * @return
	 */
	private Event decodeSelectData(Event task, String input){
		boolean flag = false;
		int startIndex = 0;
		int endIndex = startIndex;
		
		List<Integer> indexes = new ArrayList<>();
		int userChoice;
		int i = 0;
		
		String remainingInput = extractDescription(task, input);

		if(input.indexOf("--") >= 0){
			startIndex = input.indexOf("--", startIndex) + 1;
			endIndex = input.length();
			
			if(classifyCategory(input) == null){
				task.setCategory(null);
			} else {
				task.setCategory(classifyCategory(remainingInput));
			}
			
			if(classifyStatus(input) == null){
				task.setStatus(null);
			}else{
				task.setStatus(classifyStatus(remainingInput));
			}
			
			return task;
		} else{
			flag = true;
		}
		
		if(input.indexOf("#") >= 0){
			startIndex = input.indexOf("#",startIndex) + 1;
			while(input.indexOf(",", startIndex) > 0){
				endIndex = input.indexOf(",", startIndex);
				userChoice = Integer.parseInt(input.substring(startIndex, endIndex));
				startIndex = endIndex + 1;
				indexes.add(userChoice);
				if(input.indexOf("#",startIndex) >= 0){
					startIndex = input.indexOf("#",startIndex) + 1;
				}
			} 
			userChoice = Integer.parseInt(input.substring(startIndex));
			indexes.add(userChoice);		
			task.setSelection(indexes);
		} else{
			task = determineQuotedInput(task, remainingInput);
		}
		
		if(flag){
			task.setCategory(null);
			task.setStatus(null);
		}
		return task;
	}

	/**
	 * Decodes the user input for Deleting a task by name
	 * @param task
	 * @param input
	 * @return Event object with updated name field to be checked, other fields are initialized as default values
	 */
	private Event decodeDeleteData(Event task, String input){
		boolean flag = false;
		int startIndex = 0;
		int endIndex = startIndex;
		String remainingInput = extractDescription(task, input);
		if(remainingInput.isEmpty()){
			return null;
		}
		
		if(input.indexOf("--") >= 0){
			startIndex = input.indexOf("--", startIndex) + 1;
			endIndex = input.length();
			
			if(classifyCategory(input) == null){
				task.setCategory(null);
			} else {
				task.setCategory(classifyCategory(remainingInput));
			}
			
			if(classifyStatus(input) == null){
				task.setStatus(null);
			}else{
				task.setStatus(classifyStatus(remainingInput));
			}
			
			return task;
		} else{
			flag = true;
		}
		
		task =  determineQuotedInput(task, remainingInput);
		if(flag){
			task.setCategory(null);
			task.setStatus(null);
		}
		
		return task;
	}

	/**
	 * Decodes the user input for Editing task either by name or date time or location or note
	 * @param task
	 * @param input
	 * @return Event object with updated field of which the user choose to edit, other fields that are not
	 * 			edited remain as initial values
	 */
	private Event decodeEditData(Event task, String input){
		String remainingInput = extractDescription(task, input);
		if(remainingInput.isEmpty()){
			return task;
		}
		return determineQuotedInput(task, remainingInput);
	}

	private Event decodeSearchData(Event task, String input){		

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
		} 
		return decodeDataFromInput(task, input);
	}

	/**
	 * Check the input task name for having quotations and extract the data out
	 * @param task
	 * @param input
	 * @return 
	 */
	private Event determineQuotedInput(Event task, String input){
		boolean isDoubleQuoted = false;
		boolean isSingleQuoted = false;

		int endIndex = 0;

		if(input.indexOf("\"") >= 0){
			int startQuoteIndex = input.indexOf("\"");
			endIndex = input.indexOf("\"", startQuoteIndex + 1);
			if(endIndex > 0){
				task.setName(input.substring(startQuoteIndex + 1, endIndex).trim());
				isDoubleQuoted = true;
			}
		} else if(input.indexOf("'") >= 0){
			int startQuoteIndex = input.indexOf("'");
			endIndex = input.indexOf("'",startQuoteIndex+ 1);
			if(endIndex > 0){
				task.setName(input.substring(startQuoteIndex + 1, endIndex));
				isSingleQuoted = true;
			}
		}

		int[] indexes = matchPatternOfFirstOccurrence(PATTERN_ALL, input);
		
		if(isSingleQuoted || isDoubleQuoted){
			if(endIndex != input.length()-1){
				int startIndex = endIndex + 1;
				endIndex = input.length();
				input = input.substring(startIndex, endIndex).trim();
			}
		}else if (isEdit && indexes[0] != 0){
			task.setName(input.substring(0, indexes[0]).trim());
		}

		return decodeDataFromInput(task, input);
	}

	/**
	 * Extract information out from user input based on the structure of the input
	 * @param task
	 * @param input
	 * @return Event object with updated fields
	 */
	private Event decodeDataFromInput(Event task, String input){
		String preposition = "";
		boolean isFound = false;
		int count = 0;
		int startIndex = 0;
		int endIndex = startIndex;

		input = input.trim();
		if(input.isEmpty()){
			task = null;
			return task;
		}

		Pattern pattern = Pattern.compile(PATTERN_ALL,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);

		int[] matchPattern = matchPatternOfFirstOccurrence(PATTERN_ALL, input);
		
		if(matchPattern[0] == 0){
			isNameDefined = false;
		} else{
			isNameDefined = true;
		}
				
		while(matcher.find()){
			endIndex = matcher.start();
	
			preposition = input.substring(matcher.start(), matcher.end()).trim();
			try {
				task = classifyDataFromPreposition(task, input, preposition, startIndex, endIndex);
			} catch (ParseException e) {
			
			}
			
			if(task != null){
				startIndex = matcher.end() + 1;
			} else{
				break;
			}
			isFound = true;
		}

		if(!isFound){
			task.setName(input.substring(startIndex, input.length()));
		}

		return task;
	}

	private Event classifyDataFromPreposition(Event task, String input, String preposition,
			int startIndex, int endIndex) throws ParseException{
		Date inputDate;
		String stringDate = null;
		String time = null;

		int newStartIndex = 0;
		int newEndIndex = 0;
		SimpleDateFormat formatToString = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		if(preposition.equalsIgnoreCase("on")){

			if(task.getName().isEmpty() && isNameDefined){
				String name = input.substring(startIndex , endIndex).trim();
				task.setName(name);
			}
			
			newStartIndex = endIndex + 3;
			newEndIndex = matchPatternOfFirstOccurrence(PATTERN_AT_OR_BY, input)[0];
			if(newEndIndex == 0){
				newEndIndex = input.length();
			}
			stringDate = input.substring(newStartIndex, newEndIndex).trim(); 
			inputDate = DateChecker.validateDate(stringDate);
			
			if(inputDate != null){
				task.setStartTime(inputDate);
				task.setEndTime(DateChecker.writeTime(stringDate, TIME_BEFORE_MIDNIGHT_SEC));
				task.setCategory(Constant.CATEGORY_EVENT);
			}
			
			/*check date time in Day of the week format */
			/* to check date time in the form of "on Sun 11 am" */
			String[] dateTime = extractTimeFromDate(stringDate);
			if(DateChecker.isDay){
				if(dateTime[1] != null){ /*check date time in dd/MM/yy (HH:mm) or dd MMM yy (HH:mm) format*/
					task.setStartTime(Constant.MIN_DATE);
					task.setEndTime(DateChecker.writeTime(stringDate, DateChecker.convertAmPmToTime(dateTime[1])));
					task.setCategory(Constant.CATEGORY_DEADLINE);
					return task;
				}	
			} else if(dateTime[0] == null && dateTime[1] != null){
				task.setStartTime(Constant.MIN_DATE);
				task.setEndTime(DateChecker.writeTime(stringDate, DateChecker.convertAmPmToTime(dateTime[1])));
				task.setCategory(Constant.CATEGORY_DEADLINE);
				return task;
			} else if(dateTime[0] != null && dateTime[1] != null){
				task.setStartTime(Constant.MIN_DATE);
				task.setEndTime(DateChecker.writeTime(dateTime[0], DateChecker.convertAmPmToTime(dateTime[1])));
				task.setCategory(Constant.CATEGORY_DEADLINE);
				return task;
			}
			
			if(task.getStartTime() == null){
				return null;
			} else if(task.getEndTime() == null){
				return null;
			}
		} else if(preposition.equalsIgnoreCase("at")){

			if(task == null){
				String name = input.substring(startIndex, endIndex).trim();
				task.setName(name);
			}

			if(task.getName().isEmpty() && isNameDefined){
				String name = input.substring(startIndex, endIndex).trim();
				task.setName(name);
				task.setCategory(Constant.CATEGORY_FLOATING);
			} 

			newStartIndex = endIndex + 3;
			newEndIndex = matchPatternOfLastOccurrence(PATTERN_AT, input)[0];
			if(newEndIndex == 0){
				newEndIndex = input.length();
			} else if(newEndIndex < newStartIndex){
				newEndIndex = input.length();
			}
			
			stringDate = input.substring(newStartIndex, newEndIndex).trim();
			
			if(DateChecker.validateTime(stringDate) == null){
				task.setLocation(stringDate);
			} else{
				String[] dateTime = extractTimeFromDate(stringDate);

				if(dateTime[0] == null && dateTime[1] != null && !stringDate.equals(dateTime[1])){
					time = DateChecker.convertAmPmToTime(dateTime[1]);
					task.setEndTime(DateChecker.writeTime(stringDate, time));
					task.setCategory(Constant.CATEGORY_DEADLINE);
				} else if(dateTime[0] == null && dateTime[1] != null && stringDate.equals(dateTime[1])) {
					time = DateChecker.convertAmPmToTime(dateTime[1]);
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					String today = sdf.format(new Date());
					Date todayDate = DateChecker.writeTime(today, time);
					task.setEndTime(todayDate);
					task.setCategory(Constant.CATEGORY_DEADLINE);

					if(cal.getTime().after(todayDate)){
						task.setEndTime(DateChecker.findDate(1));

						String writtenDate = formatToString.format(task.getEndTime());
						task.setEndTime(DateChecker.writeTime(writtenDate, time));
					}
				} else if(dateTime[0] != null && dateTime[1] != null){
					time = DateChecker.convertAmPmToTime(dateTime[1]);
					task.setEndTime(DateChecker.writeTime(dateTime[0], time));
					task.setCategory(Constant.CATEGORY_DEADLINE);
				} 
				
				if(time == null){
					inputDate = DateChecker.validateDate(stringDate);
					
					if(inputDate != null){
						task.setStartTime(inputDate);
						task.setEndTime(DateChecker.writeTime(stringDate, TIME_BEFORE_MIDNIGHT));
						task.setCategory(Constant.CATEGORY_EVENT);
						return task;
					} else{
						task.setLocation(stringDate);
					}
				}
			}
		
		} else if(preposition.equalsIgnoreCase("by")){
			if(task.getName().isEmpty() && isNameDefined){
				String name = input.substring(startIndex, endIndex).trim();
				task.setName(name);
				task.setCategory(Constant.CATEGORY_DEADLINE);
			} 

			newStartIndex = endIndex + 3;
			newEndIndex =  matchPatternOfLastOccurrence(PATTERN_AT, input)[0];
			/* no more preposition, the end of the line*/
			if(newEndIndex == 0){
				newEndIndex = input.length();
			}

			stringDate = input.substring(newStartIndex, newEndIndex).trim();
			inputDate = DateChecker.validateDate(stringDate);
			if(inputDate != null){
				task.setStartTime(Constant.MIN_DATE);
				task.setEndTime(DateChecker.writeTime(stringDate, TIME_BEFORE_MIDNIGHT));
				task.setCategory(Constant.CATEGORY_DEADLINE);
			}
			
			/*check date time in Day of the week format*/
			/*e.g. by Sun 11 am or on Sun by 11 am*/
			String[] dateTime = extractTimeFromDate(stringDate);
			
			if(dateTime[0] == null && dateTime[1] != null){
				time = DateChecker.convertAmPmToTime(dateTime[1]);
				task.setEndTime(DateChecker.writeTime(stringDate, time));
				task.setCategory(Constant.CATEGORY_DEADLINE);
			} else if(dateTime[0] != null && dateTime[1] != null){
				time = DateChecker.convertAmPmToTime(dateTime[1]);
				task.setEndTime(DateChecker.writeTime(dateTime[0], time));
				task.setCategory(Constant.CATEGORY_DEADLINE);
			}
	
			return task;

		} else if(preposition.equalsIgnoreCase("from")){
			if(task.getName().isEmpty() && isNameDefined){
				String name = input.substring(startIndex, endIndex).trim();
				task.setName(name);
				task.setCategory(Constant.CATEGORY_EVENT);
			}
			
			newStartIndex = endIndex + 5;
			newEndIndex = matchPatternOfFirstOccurrence(PATTERN_TO, input)[0];
			
			if(newEndIndex == 0){
				return null;
			}
			
			stringDate = input.substring(newStartIndex, newEndIndex).trim();
			inputDate = DateChecker.validateDate(stringDate);
			
			if(inputDate != null){
				task.setStartTime(inputDate);
				task.setCategory(Constant.CATEGORY_EVENT);
			}

			String[] dateTime = extractTimeFromDate(stringDate);
			int[] matchAmPm = {0,0};
			if(dateTime[1] != null){
				matchAmPm = matchPatternOfFirstOccurrence(PATTERN_AM_OR_PM, dateTime[1]);
			}
			if(DateChecker.isDay){
				if(dateTime[1] != null){
					time = DateChecker.convertAmPmToTime(dateTime[1]);
					task.setStartTime(DateChecker.writeTime(stringDate, time));
					task.setCategory(Constant.CATEGORY_EVENT);
				}
			} else if(dateTime[0] == null && dateTime[1] != null && matchAmPm[0] != matchAmPm[1]){
				task.setEndTime(DateChecker.writeTime(stringDate, DateChecker.convertAmPmToTime(dateTime[1])));
				
			} else if(dateTime[0] != null && dateTime[1] != null && matchAmPm[0] != matchAmPm[1]){
				task.setEndTime(DateChecker.writeTime(dateTime[0], DateChecker.convertAmPmToTime(dateTime[1])));
			}
				
			newStartIndex = matchPatternOfFirstOccurrence(PATTERN_TO, input)[1] + 1;
			newEndIndex = matchPatternOfFirstOccurrence(PATTERN_AT, input)[0];
			
			if(newEndIndex == 0){
				newEndIndex = input.length();
			}
			stringDate = input.substring(newStartIndex, newEndIndex).trim();
			inputDate = DateChecker.validateDate(stringDate);
			
			if(inputDate != null){
				task.setEndTime(inputDate);
			} 
			
			dateTime = extractTimeFromDate(stringDate);
			matchAmPm[0] = 0;
			matchAmPm[1] = 0;
			if(dateTime[1] != null){
				matchAmPm = matchPatternOfFirstOccurrence(PATTERN_AM_OR_PM, dateTime[1]);
			}
			
			if(DateChecker.isDay){
				if(dateTime[1] != null){    		/*to friday 3 am or to friday 13:00*/
					time = DateChecker.convertAmPmToTime(dateTime[1]);
					task.setEndTime(DateChecker.writeTime(stringDate, time));
				} else if(dateTime[1] == null){    /* to friday */
					task.setEndTime(DateChecker.writeTime(stringDate, TIME_BEFORE_MIDNIGHT_SEC));
				}
			} else if (dateTime[1] == null){
				task.setEndTime(DateChecker.writeTime(stringDate, TIME_BEFORE_MIDNIGHT_SEC));

			} else if (dateTime[0] == null && dateTime[1] != null && matchAmPm[0] != matchAmPm[1]){
				task.setEndTime(DateChecker.writeTime(stringDate, DateChecker.convertAmPmToTime(dateTime[1])));
				
			} else if(dateTime[0] != null && dateTime[1] != null && matchAmPm[0] != matchAmPm[1]){
				task.setEndTime(DateChecker.writeTime(dateTime[0], DateChecker.convertAmPmToTime(dateTime[1])));
			}
		}

		return task;
	}

	/**
	 * extract time from date, the method looks for pattern such as 11 am, 11 pm, 23:00 
	 * @param date - input date
	 * @return string array of size 2 with date value at index 0 and time value at index 1 
	 */
	private String[] extractTimeFromDate(String date){
		String[] dateTime = {null, null};

		int[] matchedAtData = matchPatternOfFirstOccurrence(PATTERN_AT, date);
		if(matchedAtData[0] > 0){
			date = date.substring(matchedAtData[1], date.length()).trim();
			dateTime[0] = date;
			return dateTime;
		}
		date.trim();
		int[] matchedData = matchPatternOfFirstOccurrence(PATTERN_AM_OR_PM, date);
		int[] matchedSpace = matchPatternOfFirstOccurrence(PATTERN_SPACE, date);

		if(matchedSpace[1] == matchedData[0] && matchedSpace[1] !=0 && matchedData[0] !=0){
			dateTime[1] = date;
			return dateTime;
		}else if(matchedData[0] > 0){
			dateTime[0] = date.substring(0, matchedSpace[0]).trim();
			dateTime[1] = date.substring(matchedSpace[0], date.length()).trim();
			return dateTime;
		} 
		
		int[] matchedColumn = matchPatternOfFirstOccurrence(PATTERN_COLUMN, date);
		int[] matchedLastSpace = matchPatternOfLastOccurrence(PATTERN_SPACE, date);

		if(matchedColumn[0] > 0){
			dateTime[1] = date.substring(matchedLastSpace[0], date.length()).trim();
		} else{
			dateTime[1] = null;
		}
		
		return dateTime;
	}

	

	/**
	 * This method extracts the description out of the input and set the value to the Event object
	 * @param event
	 * @param input
	 * @return the remaining String input which are left to be processed 
	 */
	private String extractDescription(Event event, String input){
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
	
	/**
	 * 
	 * @param event
	 * @return Event object with updated Category field 
	 */
	private Event determineCategory(Event event){
		if(event.getStartTime() == Constant.MIN_DATE && event.getEndTime() == Constant.MAX_DATE){
			event.setCategory(Constant.CATEGORY_FLOATING);
		} else if(event.getStartTime() == Constant.MIN_DATE && event.getEndTime() != Constant.MAX_DATE){
			event.setCategory(Constant.CATEGORY_DEADLINE);
		} else if(event.getStartTime() != Constant.MIN_DATE && event.getEndTime() != Constant.MAX_DATE){
			event.setCategory(Constant.CATEGORY_EVENT);
		}
		
		return event;
	}


	private CommandType getCommandType(String command){
		if (command.equalsIgnoreCase("add")){
			return CommandType.ADD;
		}else if (command.equalsIgnoreCase("delete")){
			return CommandType.DELETE;
		}else if (command.equalsIgnoreCase("display") || 
				command.equalsIgnoreCase("search")){
			return CommandType.SEARCH;
		}else if (command.equalsIgnoreCase("edit") || 
				command.equalsIgnoreCase("e")){
			return CommandType.EDIT;
		}else if (command.equalsIgnoreCase("import")){
			return CommandType.IMPORT;
		}else if (command.equalsIgnoreCase("export")){
			return CommandType.EXPORT;
		}else if (command.equalsIgnoreCase("block")){
			return CommandType.BLOCK;
		}else if (command.equalsIgnoreCase("unblock") || command.equalsIgnoreCase("release")){
			return CommandType.UNBLOCK;
		}else if (command.equalsIgnoreCase("confirm")){
			return CommandType.CONFIRM;
		}else if (command.equalsIgnoreCase("undo")){
			return CommandType.UNDO;
		}else if (command.equalsIgnoreCase("redo")){
			return CommandType.REDO;
		}else if (command.equalsIgnoreCase("select")){
			return CommandType.SELECT;
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
