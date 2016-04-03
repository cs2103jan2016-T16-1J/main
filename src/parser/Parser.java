package parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import command.Add;
import command.ChangeTab;
import command.Command;
import command.Delete;
import command.Edit;
import command.Export;
import command.Import;
import command.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import constant.CommandType;
import constant.Constant;
import main.Event;
import main.GenericEvent;
import main.ReservedEvent;
import main.TimePair;
import main.GenericEvent.Category;
import main.GenericEvent.Status;

public class Parser {

	private static Event oldEvent;
	private static ReservedEvent oldReservedEvent;
	private static String recordedDate;
	private boolean isNameDefined = true;
	private boolean isEdit = false;
	private final String PATTERN_SPACE = "(\\s)";
	private final String PATTERN_AM_OR_PM = "(\\b(am)\\b|\\b(pm)\\b)";
	private final String PATTERN_AT = "(\\bat\\b)";
	private final String PATTERN_AT_OR_BY = "(\\b(at)\\b|\\b(by)\\b)";
	private final String PATTERN_COLUMN = "(\\b(:)\\b)";
	private final String PATTERN_AND = "(\\b(and)\\b|\\b(&)\\b)";
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
	private final String PATTERN_PREP_ALL = "(\\b(on)\\b|\\b(by)\\b|\\b(from)\\b|\\b(at)\\b)";
	private final String PATTERN_ALL = "(\\b(starttime)\\b|\\b(startdate)\\b|\\b(endtime)\\b"
			+ "|\\b(enddate)\\b|\\b(location)\\b|\\b(note)\\b)";
	
	private final String TIME_BEFORE_MIDNIGHT = "23:59";
	private final String TIME_BEFORE_MIDNIGHT_SEC = "23:59:01";
	private final String TIME_MIDNIGHT = "00:00";
	private final String ERROR_DATE_FORMAT = "The input date format is not supported";

	private final String TAB_UNDETERMINED = "UNDETERMINE";
	private final String TAB_COMPLETED = "COMPLETE";
	private final String TAB_INCOMPLETE = "INCOMPLETE";
	private final int DAYS_IN_WEEK = 7;

	public Command parseCommand(String input){
		Command cmdInterface = null;
		String command = getFirstWord(input);
		CommandType tempCmd = getCommandType(command);

		if (tempCmd == CommandType.INVALID){
			Event event = new Event();
			event = null;
			oldEvent = null;
			oldReservedEvent = null;
		} else if(tempCmd == CommandType.ADD){
			Event event = new Event();
			decodeAddData(event, removeFirstWord(input));
			oldEvent = event;
			oldReservedEvent = null;
			cmdInterface = new Add(event);
		} else if(tempCmd == CommandType.DELETE){
			Event event = new Event();
			event = decodeDeleteData(event, removeFirstWord(input));
			oldEvent = null;
			oldReservedEvent = null;
			cmdInterface = new Delete(event);
		} else if(tempCmd == CommandType.EDIT){
			if(oldEvent != null){
				Event event = new Event();
				event = oldEvent;
				
				cloneEvent(oldEvent, event);
				event = decodeEditData(event , removeFirstWord(input));
				oldEvent = event;
			} else{
				ReservedEvent event = new ReservedEvent();
				event = oldReservedEvent;
				
				cloneReservedEvent(oldReservedEvent, event);
				//event = decodeEditData(event , removeFirstWord(input));
				oldReservedEvent = event;

			}
		
			isEdit = true;
			//cmdInterface = new Edit(event);
		} else if(tempCmd == CommandType.SELECT){		
			Event event = new Event();
			event = decodeSelectData(event, removeFirstWord(input));
			cmdInterface = new Select(event);
		} else if(tempCmd == CommandType.BLOCK){
			Event event = new Event();
			ReservedEvent reserved = new ReservedEvent();
			reserved = decodeReservedData(event, removeFirstWord(input));
			oldReservedEvent = reserved;
			oldEvent = null;
			//cmdInterface = new Add(event);
		} else if(tempCmd == CommandType.UNBLOCK){
			Event event = new Event();
			ReservedEvent reserved = new ReservedEvent();
			reserved = decodeUnblockData(event, removeFirstWord(input));
			reserved.setStatus(GenericEvent.Status.UNDETERMINED);
			oldReservedEvent = reserved;
			oldEvent = null;
			//cmdInterface = new Delete(event);
		} else if(tempCmd == CommandType.UNDO){
			
		} else if(tempCmd == CommandType.REDO){
			
		} else if(tempCmd == CommandType.CONFIRM){
			Event event = new Event();
			event = determineQuotedInput(event, removeFirstWord(input));
			event = decodeDataFromInput(event, input);
			event = determineCategory(event);
			oldEvent = event;
		} else if(tempCmd == CommandType.CHANGDIR) {
			Event event = new Event();
			event = decodeImportExportData(event, removeFirstWord(input));
			cmdInterface = new Import(event);
		} else if(tempCmd == CommandType.CHANGETAB){
			Status tab = decodeChangeTab(removeFirstWord(input));
			cmdInterface = new ChangeTab(tab);
		}
		return cmdInterface;
	}

	

	/**
	 * For testing user input (TEST)
	 * @param input
	 * @return
	 */
	public Event testingDeterminedStuff(String input){
		String command = getFirstWord(input);
		CommandType tempCmd = getCommandType(command);
		Event task = new Event();

		if (tempCmd == CommandType.INVALID){
			return null;
		} else if(tempCmd == CommandType.ADD){
			decodeAddData(task, removeFirstWord(input));
			oldEvent = task;
			return task;
		} else if(tempCmd == CommandType.DELETE){
			oldEvent = null;
			return decodeDeleteData(task, removeFirstWord(input));
		} else if(tempCmd == CommandType.EDIT){
			Event oldTask = new Event();
			oldTask = (Event) oldEvent;
		
			isEdit = true;
			task = decodeEditData(task , removeFirstWord(input));
			oldEvent = task;
			return task;
		} else if(tempCmd == CommandType.CHANGDIR){
			task = decodeImportExportData(task, removeFirstWord(input));
			return task;
		} 
		return null;
	}
	
	public GenericEvent testingReservedStuff(String input){
		String command = getFirstWord(input);
		CommandType tempCmd = getCommandType(command);
		Event task = new Event();
		if(tempCmd == CommandType.BLOCK){
			ReservedEvent reserved = new ReservedEvent();
			reserved = decodeReservedData(task, removeFirstWord(input));
			//oldEvent = reserved;
			return reserved;
		} else if(tempCmd == CommandType.UNBLOCK){
			GenericEvent event = new Event();
			event = decodeReservedData((Event) event, removeFirstWord(input));
			event.setStatus(GenericEvent.Status.UNDETERMINED);
			oldEvent = (Event) event;
			return event;
		} else if(tempCmd == CommandType.CONFIRM){
			GenericEvent event = new Event();
			
		}
		return null;
	}

	
	/**
	 * 
	 * @param eventToBeCloned
	 * @param eventCloned
	 * @return the cloned event
	 */
	public void cloneEvent(Event eventToBeCloned, Event eventCloned){
		eventCloned.setName(eventToBeCloned.getName());
		eventCloned.setDescription(eventToBeCloned.getDescription());
		eventCloned.setCategory(eventToBeCloned.getCategory());
		eventCloned.setEndTime(((Event) eventToBeCloned).getEndTime());
		eventCloned.setStartTime(((Event) eventToBeCloned).getStartTime());
		eventCloned.setLocation(eventToBeCloned.getLocation());
		eventCloned.setStatus(eventToBeCloned.getStatus());
	}
	
	public void cloneReservedEvent(ReservedEvent eventToBeCloned, ReservedEvent eventCloned){
		eventCloned.setName(eventToBeCloned.getName());
		eventCloned.setDescription(eventToBeCloned.getDescription());
		eventCloned.setCategory(eventToBeCloned.getCategory());
		
		eventCloned.setLocation(eventToBeCloned.getLocation());
		eventCloned.setStatus(eventToBeCloned.getStatus());
	}

	private GenericEvent.Category classifyCategory(String userInput){
		
		if(userInput.equalsIgnoreCase(Constant.CATEGORY_DEADLINE)){
			return GenericEvent.Category.DEADLINE;
		} else if(userInput.equalsIgnoreCase(Constant.CATEGORY_EVENT)){
			return GenericEvent.Category.EVENT;
		} else if(userInput.equalsIgnoreCase(Constant.CATEGORY_FLOATING)){
			return GenericEvent.Category.FLOATING;
		}
		
		return GenericEvent.Category.NULL;
	}
	
	private GenericEvent.Status classifyStatus(String userInput){
		
		if(userInput.equalsIgnoreCase(Constant.STATUS_INCOMPLETE)){
			return GenericEvent.Status.INCOMPLETE;
		}else if(userInput.equalsIgnoreCase(Constant.STATUS_COMPLETE)){
			return GenericEvent.Status.COMPLETE;
		}else if(userInput.equalsIgnoreCase(Constant.STATUS_UNDETERMINED)){
			return GenericEvent.Status.UNDETERMINED;
		}
		return GenericEvent.Status.NULL;

	}
	/**
	 * Decodes the user input for Adding new task
	 * @param task
	 * @param input
	 * @return Event object with updated values
	 */
	private void decodeAddData(Event task, String input){
		String remainingInput = extractDescription(task, input);
		task =  determineQuotedInput(task, remainingInput);
		task =  decodeDataFromInput(task, input);
	}
	
	/**
	 * Decodes the user input for Reserving new task
	 * @param task
	 * @param input
	 * @return
	 */
	private ReservedEvent decodeReservedData(Event task, String input){
		int startIndex = 0;
		int endIndex = startIndex;
		String name = null;
		ArrayList<String> choppedInputData = new ArrayList<>();
		ArrayList<TimePair> reservedTimes = new ArrayList<>();
		
		/**extract notes from the input if it is declared**/
		String remainingInput = extractDescription(task, input);
		
		/**extract location from the input if it is declared**/
		remainingInput = extractLocation(task, remainingInput);
		
		/**extract "task name that has preposition on, from, to, by, at" **/
		task = determineQuotedInput(task, remainingInput);
		
		/**to find AND in a sentence**/
		Pattern pattern = Pattern.compile(PATTERN_AND,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(remainingInput);
				
		while(matcher.find()){
			endIndex = matcher.start();
			choppedInputData.add(remainingInput.substring(startIndex, endIndex).trim());
			startIndex = matcher.end();
		}
		choppedInputData.add(remainingInput.substring(startIndex,remainingInput.length()).trim());
	
		/**decode all the other information like name, location from input**/
		task = decodeDataFromInput(task, choppedInputData.get(0));
		TimePair reservedTime = new TimePair(task.getStartTime(),task.getEndTime());
		reservedTimes.add(reservedTime);
		name = task.getName().trim();
		/**check if the connected data are date time **/
		for(int i = 1; i < choppedInputData.size(); i++){
			task.setStartTime(Constant.MIN_DATE);
			task.setEndTime(Constant.MAX_DATE);
			task = decodeDataFromInput(task, choppedInputData.get(i));

			if(task.getStartTime() == Constant.MIN_DATE && task.getEndTime() == Constant.MAX_DATE){
				name = name + " "+ task.getName().trim();
			} else{
				reservedTime = new TimePair(task.getStartTime(),task.getEndTime());
				reservedTimes.add(reservedTime);
			}
		}
		ReservedEvent reserved = new ReservedEvent(name, task.getLocation(),
				task.getDescription(), task.getCategory(), reservedTimes, GenericEvent.Status.UNDETERMINED);
		return reserved;
	}
	
	private Event decodeImportExportData(Event task, String input){

		if(input.indexOf('\\') >= 0){
			return null;
		} else{
			task.setName(input);
		}
		return task;
	}
	
	private Status decodeChangeTab(String input){
		input = input.toUpperCase();
		if(input.contains(TAB_UNDETERMINED)){
			return Constant.TAB_UNDETERMINED;
		} else if(input.contains(TAB_COMPLETED)){
			return Constant.TAB_COMPLETE;
		} else if(input.contains(TAB_INCOMPLETE)){
			return Constant.TAB_INCOMPLETE;
		}
		
		return Constant.TAB_INCOMPLETE;
	}
	
	/**
	 * Decodes the user input for selecting task
	 * @param task
	 * @param input
	 * @return
	 */
	private Event decodeSelectData(Event task, String input){
		boolean isCategoryDefined = false;
		int startIndex = 0;
		int endIndex = startIndex;
		
		List<Integer> indexes = new ArrayList<>();
		int userChoice;
		int i = 0;
		
		String remainingInput = extractDescription(task, input);

		/** Look for Selecting Category type command **/
		if(input.indexOf("--") >= 0){
			startIndex = input.indexOf("--", startIndex) + 1;
			endIndex = input.length();
			
			if(classifyCategory(input) == null){
				task.setCategory(null);
			} else {
				task.setCategory(classifyCategory(remainingInput));
			}
			
			task.setStatus(classifyStatus(remainingInput));

			return task;
		} else{
			isCategoryDefined = true;
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
			task = decodeDataFromInput(task, input);
		}
		
		if(isCategoryDefined){
			task.setCategory(null);
			task.setStatus(GenericEvent.Status.NULL);
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
		boolean isCategoryDefined = false;
		int startIndex = 0;
		int endIndex = startIndex;
		String remainingInput = extractDescription(task, input);
		if(remainingInput.isEmpty()){
			return null;
		}
		
		/** Look for Selecting Category type command **/
		if(input.indexOf("--") >= 0){
			startIndex = input.indexOf("--", startIndex) + 1;
			endIndex = input.length();
			
			if(classifyCategory(input) == null){
				task.setCategory(null);
			} else {
				task.setCategory(classifyCategory(remainingInput));
			}
			task.setStatus(classifyStatus(remainingInput));
			
			return task;
		} else{
			isCategoryDefined = true;
		}
		
		task = determineQuotedInput(task, remainingInput);
		task = decodeDataFromInput(task, input);
		
		if(isCategoryDefined){
			task.setCategory(null);
			task.setStatus(GenericEvent.Status.NULL);
		}
		
		return task;
	}
	
	private ReservedEvent decodeUnblockData(Event task, String input){
		boolean isCategoryDefined = false;
		String name = null;
		int startIndex = 0;
		int endIndex = startIndex;		
		ArrayList<String> choppedInputData = new ArrayList<>();
		ArrayList<TimePair> reservedTimes = new ArrayList<>();
		
		/**extract notes from the input if it is declared**/
		String remainingInput = extractDescription(task, input);
		
		if(remainingInput.isEmpty()){
			return null;
		}
		
		/**extract location from the input if it is declared**/
		remainingInput = extractLocation(task, remainingInput);
		
		/**extract "task name that has preposition on, from, to, by, at" **/
		task = determineQuotedInput(task, remainingInput);
		
		/**to find AND in a sentence**/
		Pattern pattern = Pattern.compile(PATTERN_AND,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(remainingInput);
				
		while(matcher.find()){
			endIndex = matcher.start();
			choppedInputData.add(remainingInput.substring(startIndex, endIndex).trim());
			startIndex = matcher.end();
		}
		choppedInputData.add(remainingInput.substring(startIndex,remainingInput.length()).trim());
	
		/**decode all the other information like name, location from input**/
		task = decodeDataFromInput(task, choppedInputData.get(0));
		TimePair reservedTime = new TimePair(task.getStartTime(),task.getEndTime());
		reservedTimes.add(reservedTime);
		name = task.getName().trim();
		/**check if the connected data are date time **/
		for(int i = 1; i < choppedInputData.size(); i++){
			task.setStartTime(Constant.MIN_DATE);
			task.setEndTime(Constant.MAX_DATE);
			task = decodeDataFromInput(task, choppedInputData.get(i));

			if(task.getStartTime() == Constant.MIN_DATE && task.getEndTime() == Constant.MAX_DATE){
				name = name + " "+ task.getName().trim();
			} else{
				reservedTime = new TimePair(task.getStartTime(),task.getEndTime());
				reservedTimes.add(reservedTime);
			}
		}
		ReservedEvent reserved = new ReservedEvent(name, task.getLocation(),
				task.getDescription(), task.getCategory(), reservedTimes, GenericEvent.Status.UNDETERMINED);
		return reserved;
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
			return null;
		}
		task = determineQuotedInput(task, remainingInput);
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

		int[] indexes = matchPatternOfFirstOccurrence(PATTERN_PREP_ALL, input);
		
		if(isSingleQuoted || isDoubleQuoted){
			if(endIndex != input.length()-1){
				int startIndex = endIndex + 1;
				endIndex = input.length();
				input = input.substring(startIndex, endIndex).trim();
			}
		}else if (isEdit && isNameDefined){
			task.setName(input.substring(0, indexes[0]).trim());
		}
		return task;
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

		Pattern pattern = Pattern.compile(PATTERN_PREP_ALL,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);

		int[] matchPattern = matchPatternOfFirstOccurrence(PATTERN_PREP_ALL, input);
		
		if(matchPattern[0] == 0 && matchPattern[1]!=0){
			isNameDefined = false;
		} else if(matchPattern[0] == 0 && matchPattern[1] ==0){
			isNameDefined = true;
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

		/*if preposition describing other fields is not found */
		if(!isFound){
			task.setName(input.substring(startIndex, input.length()));
		}

		return task;
	}

	private Event classifyDataFromFormat(Event task, String input, String pattern, int startIndex, int endIndex){
		Date inputDate;
		String stringDate = null;
		String time = null;
		boolean isDay = false;
		int newStartIndex = 0;
		int newEndIndex = 0;
		SimpleDateFormat formatToString = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
		
		
		
		return task;
	}
	
	private Event classifyDataFromPreposition(Event task, String input, String preposition,
			int startIndex, int endIndex) throws ParseException{
		Date inputDate;
		String stringDate = null;
		String time = null;
		boolean isDay = false;
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
			isDay = DateChecker.isDay;
			if(inputDate != null){/*set starttime and endtime for the whole day event, e.g. on Sun*/
				task.setStartTime(inputDate);
				task.setEndTime(DateChecker.writeTime(stringDate, TIME_BEFORE_MIDNIGHT_SEC));
				task.setCategory(GenericEvent.Category.EVENT);
			}
			
			/*extract time from the date if it is declared otherwise, dateTime[1] = null*/
			String[] dateTime = extractTimeFromDate(stringDate);
			
			/*check date time in Day of the week format e.g. "on Sun 11 am" */
			if(isDay){
				if(dateTime[0] != null && dateTime[1] != null){ /*check date time in day time format, e.g. Friday 11 pm*/
					task.setStartTime(Constant.MIN_DATE);
					task.setEndTime(DateChecker.writeTime(dateTime[0], DateChecker.convertAmPmToTime(dateTime[1])));
					task.setCategory(GenericEvent.Category.DEADLINE);
					return task;
				} else if(dateTime[0] == null && dateTime[1] != null){
					task.setStartTime(Constant.MIN_DATE);
					task.setEndTime(DateChecker.writeTime(stringDate, DateChecker.convertAmPmToTime(dateTime[1])));
					task.setCategory(GenericEvent.Category.DEADLINE);
					return task;
				}
			} else if(dateTime[0] == null && dateTime[1] != null){
				task.setStartTime(Constant.MIN_DATE);
				task.setEndTime(DateChecker.writeTime(stringDate, DateChecker.convertAmPmToTime(dateTime[1])));
				task.setCategory(GenericEvent.Category.DEADLINE);
				return task;
			} else if(dateTime[0] != null && dateTime[1] != null){ /*e.g. on 31/6/16 9 am or 31/6/16 21:00*/
				task.setStartTime(Constant.MIN_DATE);
				task.setEndTime(DateChecker.writeTime(dateTime[0], DateChecker.convertAmPmToTime(dateTime[1])));
				task.setCategory(GenericEvent.Category.DEADLINE);
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
				task.setCategory(GenericEvent.Category.FLOATING);
			} 
			
			/*check for anymore preposition 'at' which describe location*/
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
					task.setCategory(GenericEvent.Category.DEADLINE);
				} else if(dateTime[0] == null && dateTime[1] != null && stringDate.equals(dateTime[1])) {
					task.setCategory(GenericEvent.Category.DEADLINE);
					time = DateChecker.convertAmPmToTime(dateTime[1]);
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					String today = sdf.format(new Date());
					Date todayDate = DateChecker.writeTime(today, time);
					task.setEndTime(todayDate);

					if(cal.getTime().after(todayDate)){
						int interval = 1;
						task.setEndTime(DateChecker.findDate(interval));

						String writtenDate = formatToString.format(task.getEndTime());
						task.setEndTime(DateChecker.writeTime(writtenDate, time));
					}
				} else if(dateTime[0] != null && dateTime[1] != null){
					time = DateChecker.convertAmPmToTime(dateTime[1]);
					task.setEndTime(DateChecker.writeTime(dateTime[0], time));
					task.setCategory(GenericEvent.Category.DEADLINE);
				} 
				
				if(time == null){
					inputDate = DateChecker.validateDate(stringDate);
					
					if(inputDate != null){
						task.setStartTime(inputDate);
						task.setEndTime(DateChecker.writeTime(stringDate, TIME_BEFORE_MIDNIGHT));
						task.setCategory(GenericEvent.Category.DEADLINE);
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
				task.setCategory(GenericEvent.Category.DEADLINE);
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
				task.setEndTime(DateChecker.writeTime(stringDate, TIME_BEFORE_MIDNIGHT));
				task.setCategory(GenericEvent.Category.DEADLINE);
			}
			
			/*check date time in Day of the week format*/
			/*e.g. by Sun 11 am or on Sun by 11 am*/
			String[] dateTime = extractTimeFromDate(stringDate);
			
			if(dateTime[0] == null && dateTime[1] != null){
				time = DateChecker.convertAmPmToTime(dateTime[1]);
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String today = sdf.format(new Date());
				Date todayDate = DateChecker.writeTime(today, time);

				task.setEndTime(DateChecker.writeTime(stringDate, time));
				task.setCategory(GenericEvent.Category.DEADLINE);
				
				if(cal.getTime().after(task.getEndTime())){
					int interval = 7;
					task.setEndTime(DateChecker.findDate(interval));

					String writtenDate = formatToString.format(task.getEndTime());
					task.setEndTime(DateChecker.writeTime(writtenDate, time));
				}
			} else if(dateTime[0] != null && dateTime[1] != null){
				time = DateChecker.convertAmPmToTime(dateTime[1]);
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String today = sdf.format(new Date());
				Date todayDate = DateChecker.writeTime(today, time);

				task.setEndTime(DateChecker.writeTime(dateTime[0], time));				
				task.setCategory(GenericEvent.Category.DEADLINE);				
				if(cal.getTime().after(task.getEndTime())){
					int intDaySet = task.getEndTime().getDay();
					Calendar calendar = Calendar.getInstance();
					Date dateToday = new Date();
					calendar.setTime(dateToday);
					int intToday = calendar.get(Calendar.DAY_OF_WEEK);
					task.setEndTime(DateChecker.findDate(DAYS_IN_WEEK));

					String writtenDate = formatToString.format(task.getEndTime());
					task.setEndTime(DateChecker.writeTime(writtenDate, time));
				}
			}
			
			return task;

		} else if(preposition.equalsIgnoreCase("from")){
			if(task.getName().isEmpty() && isNameDefined){
				String name = input.substring(startIndex, endIndex).trim();
				task.setName(name);
				task.setCategory(GenericEvent.Category.EVENT);
			}
			
			newStartIndex = endIndex + 5;
			newEndIndex = matchPatternOfFirstOccurrence(PATTERN_TO, input)[0];
			
			if(newEndIndex == 0){
				return null;
			}
			
			stringDate = input.substring(newStartIndex, newEndIndex).trim();
			recordedDate = stringDate;
			inputDate = DateChecker.validateDate(stringDate);
			isDay = DateChecker.isDay;
			if(inputDate != null){
				task.setStartTime(inputDate);
				task.setCategory(GenericEvent.Category.EVENT);
			}

			String[] dateTime = extractTimeFromDate(stringDate);
			int[] matchAmPm = {0,0};
			if(dateTime[1] != null){
				matchAmPm = matchPatternOfFirstOccurrence(PATTERN_AM_OR_PM, dateTime[1]);
			}
			if(isDay){
				
				if(dateTime[0] == null && dateTime[1] != null && dateTime[1].contains("11:59 pm")
						|| dateTime[1].contains(TIME_BEFORE_MIDNIGHT_SEC)){
					time = TIME_BEFORE_MIDNIGHT_SEC;
					task.setStartTime(DateChecker.writeTime(stringDate, time));
					task.setCategory(Category.EVENT);
				} else if(dateTime[0] == null && dateTime[1] != null){
					time = DateChecker.convertAmPmToTime(dateTime[1]);
					task.setStartTime(DateChecker.writeTime(stringDate, time));
					task.setCategory(GenericEvent.Category.EVENT);
				} else if(dateTime[0] != null && dateTime[1] != null){
					time = DateChecker.convertAmPmToTime(dateTime[1]);
					task.setStartTime(DateChecker.writeTime(dateTime[0], time));
					task.setCategory(Category.EVENT);

				}
			} else if(dateTime[0] == null && dateTime[1] != null && matchAmPm[0] != matchAmPm[1]){
				
				if(dateTime[1].contains("11:59 pm") || dateTime[1].contains(TIME_BEFORE_MIDNIGHT_SEC)){
					task.setEndTime(DateChecker.writeTime(stringDate, TIME_BEFORE_MIDNIGHT_SEC));
				} else{
					task.setEndTime(DateChecker.writeTime(stringDate, DateChecker.convertAmPmToTime(dateTime[1])));
				}
				
			} else if(dateTime[0] != null && dateTime[1] != null && matchAmPm[0] != matchAmPm[1]){ /*12/6/16 2 am*/
					task.setEndTime(DateChecker.writeTime(dateTime[0],TIME_BEFORE_MIDNIGHT_SEC));
			}
				
			newStartIndex = matchPatternOfFirstOccurrence(PATTERN_TO, input)[1] + 1;
			newEndIndex = matchPatternOfFirstOccurrence(PATTERN_AT, input)[0];
			
			if(newEndIndex == 0){
				newEndIndex = input.length();
			}
			stringDate = input.substring(newStartIndex, newEndIndex).trim();
			
			if(stringDate.contains(TIME_BEFORE_MIDNIGHT)){
				inputDate = DateChecker.writeTime(stringDate, TIME_BEFORE_MIDNIGHT_SEC);
			} else{
				inputDate = DateChecker.validateDate(stringDate);
			}
			
			if(inputDate != null){
				task.setEndTime(inputDate);
			} 
			
			dateTime = extractTimeFromDate(stringDate);
			matchAmPm[0] = 0;
			matchAmPm[1] = 0;
			if(dateTime[1] != null){
				matchAmPm = matchPatternOfFirstOccurrence(PATTERN_AM_OR_PM, dateTime[1]);
			}
			
			if(isDay){
				if(dateTime[0] == null && dateTime[1] != null){    		/*to friday 3 am or to friday 13:00*/
					time = DateChecker.convertAmPmToTime(dateTime[1]);
					/*to 3 am or 13:00*/
					if(stringDate.equals(time)){
						task.setEndTime(DateChecker.writeTime(recordedDate, time));
					} else{
						task.setEndTime(DateChecker.writeTime(stringDate, time));
					}
					
				} else if(dateTime[0] != null && dateTime[1] != null){
					time = DateChecker.convertAmPmToTime(dateTime[1]);
					task.setEndTime(DateChecker.writeTime(dateTime[0], time));
				} else if(dateTime[0] == null && dateTime[1] == null){    /* to friday */
					task.setEndTime(DateChecker.writeTime(stringDate, TIME_BEFORE_MIDNIGHT_SEC));
				} else if(dateTime[0] != null && dateTime[1] == null){
					task.setEndTime(DateChecker.writeTime(stringDate, TIME_BEFORE_MIDNIGHT_SEC));
				} 
			} else if (dateTime[1] == null){
				task.setEndTime(DateChecker.writeTime(stringDate, TIME_BEFORE_MIDNIGHT_SEC));

			} else if (dateTime[0] == null && dateTime[1] != null && matchAmPm[0] != matchAmPm[1]){
				
				if(dateTime[1].contains(TIME_BEFORE_MIDNIGHT)){
					task.setEndTime(DateChecker.writeTime(stringDate, TIME_BEFORE_MIDNIGHT_SEC));

				} else{
				/*to 3:00 or 3 pm */
					task.setEndTime(DateChecker.writeTime(stringDate, DateChecker.convertAmPmToTime(dateTime[1])));
				}
				
			} else if(dateTime[0] != null && dateTime[1] != null && matchAmPm[0] != matchAmPm[1]){
				
				if(dateTime[1].contains(TIME_BEFORE_MIDNIGHT) || dateTime[1].contains("11:59 pm")){
					task.setEndTime(DateChecker.writeTime(dateTime[0], TIME_BEFORE_MIDNIGHT_SEC));

				}else{
					task.setEndTime(DateChecker.writeTime(dateTime[0], DateChecker.convertAmPmToTime(dateTime[1])));

				}
			} else if(dateTime[0] == null && dateTime[1] != null && matchAmPm[0] == matchAmPm[1]){
				
				
			} else if(dateTime[0] != null && dateTime[1] != null && matchAmPm[0] == matchAmPm[1]){
				
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
		int startIndex = 0;
		int endIndex = 0;
		String[] dateTime = {null, null};

		int[] matchedAtData = matchPatternOfFirstOccurrence(PATTERN_AT, date);
		/*if 'at' is found in date*/
		if(matchedAtData[0] > 0){
			date = date.substring(matchedAtData[1], date.length()).trim();
			dateTime[0] = date;
			return dateTime;
		}
		date.trim();
		int[] matchedData = matchPatternOfFirstOccurrence(PATTERN_AM_OR_PM, date);
		int[] matchedSpace = matchPatternOfFirstOccurrence(PATTERN_SPACE, date);
		/*if 'am' or 'pm' keyword comes before the date e.g. 3 pm Friday*/
		if(matchedData[1] != date.length() && matchedData[0] > 0){
			dateTime[0] = date.substring(matchedData[1], date.length()).trim();
			dateTime[1] = date.substring(0, matchedData[1]).trim();
		} else {
			dateTime[1] = date;
		}
		
		if(matchedSpace[1] == matchedData[0] && matchedSpace[1] !=0 && matchedData[0] !=0){
			return dateTime;
		}else if(matchedData[0] > 0){			/*put date in dateTime[0] and time in dateTime[1]*/
			dateTime[0] = date.substring(0, matchedSpace[0]).trim();
			dateTime[1] = date.substring(matchedSpace[0], date.length()).trim();
			return dateTime;
		} 
		
		int[] matchedColumn = matchPatternOfFirstOccurrence(PATTERN_COLUMN, date);
		int[] matchedLastSpace = matchPatternOfLastOccurrence(PATTERN_SPACE, date);
		
		if(matchedColumn[1] < matchedLastSpace[1]){
			startIndex =  0;
			endIndex = matchedLastSpace[0];
			dateTime[0] = date.substring(matchedLastSpace[1], date.length());
					
		} else if(matchedColumn[1] > matchedLastSpace[1]){
			endIndex = date.length();
			dateTime[0] = date.substring(0,matchedLastSpace[0]).trim();
			if(dateTime[0]!= null && dateTime[0].isEmpty()){
				dateTime[0] = null;
			}else if(dateTime[0]!= null && !dateTime[0].isEmpty() && DateChecker.convertDayToDate(dateTime[0]) != null){/**friday 2:00 to 3:00**/
				startIndex = matchedLastSpace[0];
			}else if(dateTime[0]!= null && !dateTime[0].isEmpty() && DateChecker.validateDate(dateTime[0]) != null){
				startIndex = matchedLastSpace[0];
			}else{
				dateTime[0] = null;
			}
		}

		if(matchedColumn[0] > 0){
			dateTime[1] = date.substring(startIndex, endIndex).trim();

		} else{
			dateTime[1] = null;
		}
		
		return dateTime;
	}

	private String extractLocation(Event event, String input){
		String location = "";
		int[] indexes = matchPatternOfLastOccurrence(PATTERN_AT, input);
		
		if(indexes[0] != 0 && indexes[1] != 0 && indexes[0] != indexes[1]){
			location = input.substring(indexes[1], input.length()).trim();
			if(DateChecker.validateDate(location) ==null){
				event.setLocation(location);
				return (input.substring(0, indexes[0]).trim());
			}
		} 
		return input;
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
	
	/**
	 * 
	 * @param event
	 * @return Event object with updated Category field 
	 */
	private Event determineCategory(Event event){
		if(event.getStartTime() == Constant.MIN_DATE && event.getEndTime() == Constant.MAX_DATE){
			event.setCategory(GenericEvent.Category.FLOATING);
		} else if(event.getStartTime() == Constant.MIN_DATE && event.getEndTime() != Constant.MAX_DATE){
			event.setCategory(GenericEvent.Category.DEADLINE);
		} else if(event.getStartTime() != Constant.MIN_DATE && event.getEndTime() != Constant.MAX_DATE){
			event.setCategory(GenericEvent.Category.EVENT);
		}
		
		return event;
	}


	private CommandType getCommandType(String command){
		command = command.toLowerCase();
		if (command.equalsIgnoreCase("add")){
			return CommandType.ADD;
		}else if (command.equalsIgnoreCase("delete")){
			return CommandType.DELETE;
		}else if (command.equalsIgnoreCase("edit") || 
				command.equalsIgnoreCase("e")){
			return CommandType.EDIT;
		}else if (command.contains("changedir")){
			return CommandType.CHANGDIR;
		}else if (command.equalsIgnoreCase("block") || command.equalsIgnoreCase("reserve")){
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
		}else if (command.equalsIgnoreCase("changetab")){
			return CommandType.CHANGETAB;
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
