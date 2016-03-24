package storage;

import java.io.BufferedReader;
import java.io.File;

import main.Event;
import main.Event.Category;
import main.Event.Status;
import main.State;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import json.*;

/**
 * Storage class- handle storage between state class and local file
 * @author Claudia
 */






public class Storage {
	
	public final static String storageFile =  ("./storage.txt");
	public final static String tempFileName = ("temp.txt");
	
	/** 
	 * Check if the file is exist. If not, create a new file
	 */
	public void createFile(String fileName)  {
		File file = new File(fileName);
		if (!file.exists()){
			 PrintWriter writer;
			try {
				writer = new PrintWriter(fileName);
				 writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/** 
	 * Clean up every content in file
	 */
	public void clearFile(String fileName){
		try {
			PrintWriter writer = new PrintWriter(fileName);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	/** 
	 * Import all the events under every lists in state class into storage
	 * @param completeState
	 */
	public void stateToStorage(State completeState, String fileName){
		clearFile(storageFile);
		for (Event e: completeState.completedEvents){
			addToStorage(e, fileName);
		}
		for (Event e: completeState.incompletedEvents){
			addToStorage(e, fileName);
		}
		for (Event e: completeState.floatingEvents){
			addToStorage(e, fileName);
		}
	}
	
	/**
	 * cast an event to JSONObject and then write to file
	 * @param event
	 */
	public void addToStorage(Event event, String fileName) {
		JSONObject jsonObj = castEventToJSONObj(event);
		
		try {
			FileWriter fw = new FileWriter(fileName, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(jsonObj.toString());
			pw.close();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * Read storage and cast every JSONObject into event, then add to the specific list
	 * in the state class
	 * @return State
	 */
	public State readStorage(String fileName) {
		String line = null;
		State state = new State();
		
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			
			while ( (line = br.readLine()) != null) {
				JSONObject jsonObj = new JSONObject(line);;
				Event event = castJSONObjToEvent(jsonObj);
				
				storageToState(state, event);
				updatedDisplayedEvents(state);
			}
			
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");                
        } catch(IOException ex) {
                ex.printStackTrace();
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return state;
	}

	private void storageToState(State state, Event event) {
		if (event.getStatus() == Status.COMPLETE){
			state.addToCompletedList(event);
		} else if (event.getStatus() == Status.INCOMPLETE){
			state.addToIncompletedList(event);
		} else if (event.getStatus() == Status.FLOATING){
			state.addToFloatingList(event);
		}
	}
	
	private JSONObject castEventToJSONObj(Event event){
		JSONObject jsonObj = new JSONObject();
		
		DateFormat dfDeadline = new SimpleDateFormat("yyyy-M-dd");
		Date deadLine = null;
		try {
			 deadLine = dfDeadline.parse("1970-01-01");
			 //System.out.println(deadLine);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			jsonObj.put("name", event.getName());
			jsonObj.put("description", event.getDescription());
			jsonObj.put("category", event.getCategory());
			
			//jsonObj.put("startTime", event.getStartTime());
			//	System.out.println(event.getStartTime());
			jsonObj.put("endTime", event.getEndTime());
			jsonObj.put("status", event.getStatus());
			jsonObj.put("location", event.getLocation());
			
			if (event.getCategory().equals(Category.DEADLINE)){
				jsonObj.put("startTime", deadLine);
				System.out.println("yes!!!!");
			} else {
				jsonObj.put("startTime", event.getStartTime());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObj;
	
	}
	
	public Event castJSONObjToEvent(JSONObject jsonObj){
		Event event = new Event();
		DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
		DateFormat dfDeadline = new SimpleDateFormat("yyyy-M-dd");
		Date deadLine = null;
		try {
			 deadLine = dfDeadline.parse("1970-01-01");
			 //System.out.println(deadLine);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			String startTime = jsonObj.get("startTime").toString();
			String endTime = jsonObj.get("endTime").toString();
			
			event.setName(jsonObj.getString("name"));
			event.setDescription(jsonObj.getString("description"));
			//event.setCategory(jsonObj.getString("category"));
			event.setCategory(getCategory(jsonObj));
			event.setStatus(getStatus(jsonObj));
			event.setLocation(jsonObj.getString("location"));
			event.setStartTime(df.parse(startTime));
			event.setEndTime(df.parse(endTime));
			
			/*
			if (jsonObj.getString("category").equals("DEADLINE")){
				event.setStartTime(dfDeadline.parse(startTime));
				
					System.out.println(dfDeadline.parse(startTime));
				event.setEndTime(df.parse(endTime));
					System.out.println(df.parse(endTime));
			} else {
				
			}*/
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return event;
	}
	
	private Event.Status getStatus(JSONObject jsonObj) throws JSONException{
		if (jsonObj.get("status").equals("COMPLETE")){
			return Event.Status.COMPLETE;
		} else if (jsonObj.get("status").equals("INCOMPLETE")){
			return Event.Status.INCOMPLETE;
		} else if (jsonObj.get("status").equals("BLOCKED")){
			return Event.Status.BLOCKED;
		} else if (jsonObj.get("status").equals("OVERDUE")){
			return Event.Status.OVERDUE;
		} else {
			return Event.Status.FLOATING;
		}
	}
	
	private Event.Category getCategory(JSONObject jsonObj) throws JSONException{
		if (jsonObj.get("category").equals("DEADLINE")){
			return Event.Category.DEADLINE;
		} else if (jsonObj.get("category").equals("EVENT")){
			return Event.Category.EVENT;
		} else if (jsonObj.get("category").equals("FLOATING")){
			return Event.Category.FLOATING;
		} else {
			return Event.Category.UNDETERMINED;
		}
	}

	private void updatedDisplayedEvents(State completeState){
		completeState.displayedEvents.clear();
		completeState.displayedEvents.addAll(completeState.completedEvents);
		completeState.displayedEvents.addAll(completeState.incompletedEvents);
		completeState.displayedEvents.addAll(completeState.floatingEvents);		
	}
	
}
