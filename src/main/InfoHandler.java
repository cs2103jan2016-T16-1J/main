package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InfoHandler {
	public static final int NAME = 0;
	public static final int DESCRIPTION = 1;
	public static final int START_TIME = 2;
	public static final int END_TIME = 3;
	public static final int STATUS = 4;
	public static final int CATEGORY = 5;
	
	
	
	public static int numberOfEvents;
	public static ArrayList<String> categoryTypes;
	
	private static String[][] parsedStrings;
	public static ArrayList<Event> allEvents;
	private static Event currentEvent;
	
	InfoHandler(){
		this.parsedStrings = null;
	}
	InfoHandler(String[][] parsedStrings){
		this.parsedStrings = parsedStrings;
		this.numberOfEvents = 0;
		this.categoryTypes = new ArrayList<String>();
		this.allEvents = new ArrayList<Event>();
	}
	
	public void setNumberOfEvents(int numberOfEvents){
		this.numberOfEvents = numberOfEvents;
	}
	
	public int getNumberOfEvents(){
		return numberOfEvents;
	}
	
	public void setCategoryTypes(ArrayList<String> categoryTypes){
		this.categoryTypes = categoryTypes;
	}
	
	public ArrayList<String> getCategoryTypes(){
		return this.categoryTypes;
	}
	
	public ArrayList<String> addCategoryTypes(String category){
		categoryTypes.add(category);
		
		return categoryTypes;
	}
	
	public static ArrayList<Event> processInfo(){
		int i;
		
		for(i = 0; i < numberOfEvents; i++){
			currentEvent = new Event();
			currentEvent = processStrings(currentEvent, parsedStrings[i]);
			currentEvent = processTimes(currentEvent, parsedStrings[i]);
			currentEvent = processCategoryType(currentEvent, parsedStrings[i]);
			currentEvent = processStatusType(currentEvent, parsedStrings[i]);
		
			allEvents.add(currentEvent);
		}
		
		return allEvents;
	}
	
	private static Event processStrings(Event currentEvent, String[] parsedString){
		currentEvent.setName(parsedString[NAME]);
		currentEvent.setDescription(parsedString[DESCRIPTION]);
		
		return currentEvent;
	}
		
	private static Event processCategoryType(Event currentEvent, String[] parsedString){
		if(!categoryTypes.contains(parsedString[CATEGORY])){
			categoryTypes.add(parsedString[CATEGORY]);
		}
			
		currentEvent.setCategory(parsedString[CATEGORY]);
		return currentEvent;
	}
	
	private static Event processStatusType(Event currentEvent, String[] parsedString){
	
		currentEvent.setStatus(Event.Status.valueOf(parsedString[STATUS]));
		return currentEvent;
	}
	
	private static Event processTimes(Event currentEvent, String[] parsedString){
		Date startTime = null;
		Date endTime = null;
		
		try {
			startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(parsedString[START_TIME]);
			endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(parsedString[END_TIME]);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentEvent.setStartTime(startTime);
		currentEvent.setEndTime(endTime);
		
		return currentEvent;
	}
}
