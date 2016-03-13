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

import java.util.Calendar;
import java.util.Date;

import constant.CommandType;
import constant.Constant;
import main.Event;
import main.State;

public class Parser {

	public static main.Event oldEvent;

	private final String PATTERN_SPACE = "(\\s)";
	private final String PATTERN_AM_OR_PM = "(\\b(am)\\b|\\b(pm)\\b)";
	private final String PATTERN_AT = "(\\bat\\b)";
	private final String PATTERN_AT_OR_BY = "(\\b(at)\\b|\\b(by)\\b)";
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
		} else if(tempCmd == CommandType.DISPLAY){
			event =  decodeDisplayData(event, removeFirstWord(input));
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
			String remainingInput = extractDescription(event, removeFirstWord(input));
			event = decodeEditData(event , remainingInput);
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
	public Event testingParseCommand(String input){
		String command = getFirstWord(input);
		CommandType tempCmd = getCommandType(command);
		Event task = new Event();

		if (tempCmd == CommandType.INVALID){
			return null;
		} else if(tempCmd == CommandType.ADD){
			oldEvent = decodeAddData(task, removeFirstWord(input));
			return oldEvent;
		} else if(tempCmd == CommandType.DISPLAY){
			oldEvent = null;
			return decodeDisplayData(task, removeFirstWord(input));
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
			String remainingInput = extractDescription(task, removeFirstWord(input));
			task =	decodeEditData(task, remainingInput);
			oldEvent = task;
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
	public int[] matchPatternOfFirstOccurrence(String desiredPattern, String input){
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

	

	private Event decodeAddData(Event task, String input){
		String remainingInput = extractDescription(task, input);
		return determineQuotedInput(task, remainingInput);
	}

	private main.Event decodeDeleteData(Event task, String input){
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

	private Event decodeEditData(Event task, String input){
		final int INDEX_START = 0;
		final int INDEX_END = 1;
		boolean isAdd = false;
		boolean isEdit = true;

		if(input.isEmpty()){
			return task;
		}
		task = decodeDataFromInput(task, input);
		return task;
	}

	private Event decodeDisplayData(Event task, String input){		
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

		task = decodeDataFromInput(task, input);

		return task;
	}

	/**
	 * Check the input task name from having quotations
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

		if(isSingleQuoted || isDoubleQuoted){
			if(endIndex != input.length()-1){
				int startIndex = endIndex + 1;
				endIndex = input.length();
				input = input.substring(startIndex, endIndex).trim();
				task = decodeDataFromInput(task, input);
			}
		}else{
			task = decodeDataFromInput(task, input);
		}

		return task;
	}

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

		while(matcher.find()){
			endIndex = matcher.start();
	
			preposition = input.substring(matcher.start(), matcher.end()).trim();
			try {
				task = classifyDataFromPreposition(task, input, preposition, startIndex, endIndex);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
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

			if(task.getName().isEmpty()){
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
			
			/*check date time in Day of the week format */
			/* to check date time in the form of "on Sun 11 am" */
			String[] dateTime = extractTimeFromDate(stringDate);
			if(dateTime[1] == null){
				task.setStartTime(DateChecker.writeTime(stringDate, TIME_MIDNIGHT));
				task.setEndTime(DateChecker.writeTime(stringDate,TIME_BEFORE_MIDNIGHT));
				task.setCategory(Constant.CATEGORY_EVENT);
				return task;
			}  else if(dateTime[1] != null){ /*check date time in dd/MM/yy (HH:mm) or dd MMM yy (HH:mm) format*/
				task.setStartTime(DateChecker.writeTime(dateTime[0], DateChecker.convertAmPmToTime(dateTime[1])));
				task.setEndTime(DateChecker.writeTime(dateTime[0], DateChecker.convertAmPmToTime(dateTime[1])));
				task.setCategory(Constant.CATEGORY_EVENT);
				return task;
			}	else if(inputDate != null){ 	
				task.setStartTime(inputDate);
				task.setEndTime(DateChecker.writeTime(stringDate, TIME_BEFORE_MIDNIGHT));
				task.setCategory(Constant.CATEGORY_EVENT);
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

			if(task.getName().isEmpty()){
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
			/*there are both time and location*/
			if(newEndIndex != endIndex && newEndIndex != 0){
				time = DateChecker.convertAmPmToTime(stringDate);

				if(time == null){
					inputDate = DateChecker.validateDate(stringDate);
					
					if(inputDate != null){
						task.setStartTime(inputDate);
						task.setEndTime(inputDate);
						task.setCategory(Constant.CATEGORY_EVENT);
						return task;
					} else{
						time = stringDate;
					}
				}
				
				if(DateChecker.validateTime(time) == null){
					task.setLocation(time);
					return task;
				} else if(task.getStartTime() == null){
					return null;
				} else if(task.getStartTime() == Constant.MIN_DATE && task.getCategory() != Constant.CATEGORY_DEADLINE){
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					String today = sdf.format(new Date());
					Date userInputDate = DateChecker.writeTime(today, time);
					if(cal.getTime().before(userInputDate)){
						task.setStartTime(DateChecker.findDate(0));
						task.setEndTime(DateChecker.findDate(0));
					} else{
						task.setStartTime(DateChecker.findDate(1));
						task.setEndTime(DateChecker.findDate(1));
					}
				} 

				String writtenDate = formatToString.format(task.getEndTime());

				if(task.getCategory() != Constant.CATEGORY_DEADLINE){
					task.setStartTime(DateChecker.writeTime(writtenDate, time));
					task.setCategory(Constant.CATEGORY_EVENT);
				}
				task.setEndTime(DateChecker.writeTime(writtenDate, time));
			} else{ 
				String undetermined = input.substring(newStartIndex, input.length());
				if(DateChecker.validateDate(undetermined) != null){
					task.setStartTime(DateChecker.validateDate(undetermined));
					task.setEndTime(DateChecker.validateDate(undetermined));
					task.setCategory(Constant.CATEGORY_EVENT);
				} else if(DateChecker.validateTime(undetermined) == null){
					task.setLocation(undetermined);
				} else{
					String writtenDate = formatToString.format(task.getEndTime());
					if(task.getCategory() == Constant.CATEGORY_DEADLINE){
						task.setEndTime(DateChecker.writeTime(writtenDate, DateChecker.convertAmPmToTime(undetermined)));
					} else{
						task.setStartTime(DateChecker.writeTime(writtenDate, DateChecker.convertAmPmToTime(undetermined)));
						task.setEndTime(DateChecker.writeTime(writtenDate, DateChecker.convertAmPmToTime(undetermined)));
						task.setCategory(Constant.CATEGORY_EVENT);
					}
				}
			}

		} else if(preposition.equalsIgnoreCase("by")){
			if(task.getName().isEmpty()){
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

			/*check date time in dd/MM/yy  (HH:mm) or dd MMM yy (HH:mm) format*/
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
			
			if(dateTime[0].isEmpty()){
				time = DateChecker.convertAmPmToTime(dateTime[1]);
				String writtenDate = formatToString.format(task.getEndTime());
				task.setStartTime(Constant.MIN_DATE);
				task.setEndTime(DateChecker.writeTime(writtenDate, time));
				task.setCategory(Constant.CATEGORY_DEADLINE);

			} else if(dateTime[1] != null){
				time = DateChecker.convertAmPmToTime(dateTime[1]);
				task.setEndTime(DateChecker.writeTime(stringDate, DateChecker.convertAmPmToTime(dateTime[1])));
				task.setCategory(Constant.CATEGORY_DEADLINE);

			}
			return task;

		} else if(preposition.equalsIgnoreCase("from")){
			if(task.getName().isEmpty()){
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
			
			/*check for dd/MM/yyyy or dd MMM yyyy format without HH:mm to replace the originally written time*/
			if(DateChecker.validateSpecificDate(stringDate) != null){
				task.setStartTime(DateChecker.writeTime(stringDate,TIME_MIDNIGHT));
				task.setCategory(Constant.CATEGORY_EVENT);
			} else if(inputDate != null){
					task.setStartTime(inputDate);
					task.setCategory(Constant.CATEGORY_EVENT);
			} else{
				String[] dateTime = extractTimeFromDate(stringDate);
				time = DateChecker.convertAmPmToTime(dateTime[1]);
				
				if(time == null){
					
				}
			}
			newStartIndex = matchPatternOfFirstOccurrence(PATTERN_TO, input)[1] + 1;
			newEndIndex = matchPatternOfFirstOccurrence(PATTERN_AT, input)[0];
			
			if(newEndIndex == 0){
				newEndIndex = input.length();
			}
			stringDate = input.substring(newStartIndex, newEndIndex).trim();
			inputDate = DateChecker.validateDate(stringDate);
			/*check for dd/MM/yyyy or dd MMM yyyy format without HH:mm to replace the originally written time*/
			if(DateChecker.validateSpecificDate(stringDate) != null){
				task.setEndTime(DateChecker.writeTime(stringDate, TIME_BEFORE_MIDNIGHT));
			} else if(inputDate != null){
					task.setEndTime(inputDate);
			} else{
				String[] dateTime = extractTimeFromDate(stringDate);
				time = DateChecker.convertAmPmToTime(dateTime[1]);
				
				if(time == null){
					
				}
			}
		}

		return task;
	}

	/**
	 * 
	 * @param date - input date
	 * @return string array of size 2 with date value at index 0 and time value at index 1 
	 */
	private String[] extractTimeFromDate(String date){
		int[] matchedAtData = matchPatternOfFirstOccurrence(PATTERN_AT, date);
		String[] dateTime = {null, null};
		if(matchedAtData[0] > 0){
			date = date.substring(matchedAtData[1], date.length()).trim();
			dateTime[0] = date;
			return dateTime;
		}
		date.trim();
		int[] matchedData = matchPatternOfFirstOccurrence(PATTERN_AM_OR_PM, date);
		int[] matchedSpace = matchPatternOfFirstOccurrence(PATTERN_SPACE, date);

		if(matchedData[0] > 0){
			dateTime[0] = date.substring(0, matchedSpace[0]).trim();
			dateTime[1] = date.substring(matchedSpace[0], date.length()).trim();
		} else{
			dateTime[0] = date;
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
