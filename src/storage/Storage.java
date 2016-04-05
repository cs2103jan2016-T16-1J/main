package storage;

import java.io.BufferedReader;
import java.io.File;

import main.Event;
import main.GenericEvent;
import main.GenericEvent.Category;
import main.GenericEvent.Status;
import main.ReservedEvent;
import main.State;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import constant.Constant;
import json.*;
import static java.nio.file.StandardCopyOption.*;

/**
 * Storage class- handle storage between state class and local file
 * @author Claudia
 */


public class Storage {
	
	public static String storageFile ;
	public final static String tempFileName = ("temp.txt");
	
	
	public Storage(){
		storageFile =  "./storage/storage.txt";
	}
	
	public String getDirectory(){
		return storageFile;
	}
	
	public void setDirectory(String s){
		storageFile = s;
	}
	
	
	public void exportDir(String newDirectory){
		
		copyStorage(getDirectory(), newDirectory);
		
		/*File oldFile = new File(getDirectory());
		
		if (oldFile.renameTo(new File(newDirectory))){
			System.out.println("File is moved successful!");
    	} else {
    		System.out.println("File is failed to move!"); 
    	}*/
	}
	
	public void importDir(String sourceDirectory){
		copyStorage(sourceDirectory, getDirectory());
		/*
		File sourceFile = new File(sourceDirectory);
		
		if (sourceFile.renameTo(new File(getDirectory()))){
			System.out.println("File is moved successful!");
    	} else {
    		System.out.println("File is failed to move!"); 
    	}*/
	}
	
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
		clearFile(fileName);
		
		for (Event e: completeState.completedEvents){
			addToStorage(e, fileName);
		}
		for (Event e: completeState.incompletedEvents){
			addToStorage(e, fileName);
		}
		for (ReservedEvent e: completeState.undeterminedEvents){
			addToStorage(e, fileName);
		}
		for (ReservedEvent e: completeState.reservedEvents){
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
	
	
	//overloading addToStorage
	public void addToStorage(ReservedEvent event, String fileName) {
		JSONObject jsonObj = castReservedEventToJSONObj(event);
		
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
				JSONObject jsonObj = new JSONObject(line);
				
				if (getStatus(jsonObj).equals( GenericEvent.Status.UNDETERMINED)){
					ReservedEvent event = castJSONObjToReservedEvent(jsonObj);
					storageToState(state, event);
					updatedDisplayedEvents(state);
				} else {
					Event event = castJSONObjToEvent(jsonObj);
					storageToState(state, event);
					updatedDisplayedEvents(state);
				}
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
	
	
	public void changeDirectory(String oldDirectory, String newDirectory){
		try {
			File oldFile = new File(oldDirectory);
			
			if (oldFile.renameTo(new File(newDirectory))){
				System.out.println("File is moved successful!");
	    	} else {
	    		System.out.println("File is failed to move!"); 
	    	}
		    
    	} catch (Exception e){
    		e.printStackTrace();
    	}
	}
	
	
	
	private void storageToState(State state, GenericEvent event) {
		if (event.getStatus() == GenericEvent.Status.COMPLETE){
			state.addToCompletedList((Event)event);
		} else if (event.getStatus() == GenericEvent.Status.INCOMPLETE){
			state.addToIncompletedList((Event)event);
		} else if (event.getStatus() == GenericEvent.Status.UNDETERMINED){
				if (event.getCategory() == GenericEvent.Category.FLOATING){
					if(event instanceof Event) {
						System.out.println("ee");
					}
					state.undeterminedEvents.add((ReservedEvent) event);
					//state.addToUndeterminedList((ReservedEvent) event);
				} else {
					//state.addToReservedList((ReservedEvent)event);
					state.reservedEvents.add((ReservedEvent) event);
				}
		}
		
		
		/*
		if (event.getStatus() == GenericEvent.Status.COMPLETE){
			state.addToCompletedList(event);
		} else if (event.getStatus() == GenericEvent.Status.INCOMPLETE){
			state.addToIncompletedList(event);
		} else if (event.getStatus() == GenericEvent.Status.UNDETERMINED){
			state.addToFloatingList(event);
		}*/
	}
	
	private JSONObject castEventToJSONObj(Event event){
		JSONObject jsonObj = new JSONObject();
		
		DateFormat dfDeadline = new SimpleDateFormat("yyyy-M-dd");
		Date deadLine = null;
		
		try {
			 deadLine = dfDeadline.parse("1970-01-01");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		try {
			jsonObj.put("name", event.getName());
			jsonObj.put("description", event.getDescription());
			jsonObj.put("category", event.getCategory());
			jsonObj.put("status", event.getStatus());
			jsonObj.put("location", event.getLocation());
			
			if (event.getCategory().equals(GenericEvent.Category.FLOATING)){
				jsonObj.put("startTime", "null");
				jsonObj.put("endTime", "null");
			} else if (event.getCategory().equals(GenericEvent.Category.DEADLINE)){
				jsonObj.put("startTime", deadLine);
				jsonObj.put("endTime", event.getEndTime());
			} else {
				jsonObj.put("startTime", event.getStartTime());
				jsonObj.put("endTime", event.getEndTime());
			}
			/*
			if (event.getCategory().equals(GenericEvent.Category.DEADLINE)){
				jsonObj.put("startTime", deadLine);
			} else if (event.getCategory().equals(GenericEvent.Category.FLOATING)){
				jsonObj.put("startTime", "null");
			} else {
				jsonObj.put("startTime", event.getStartTime());
			}
			
			if (event.getCategory().equals(GenericEvent.Category.FLOATING)){
				jsonObj.put("endTime", "null");
			} else {
				jsonObj.put("endTime", event.getEndTime());
			}*/
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jsonObj;
	
	}
	
	////////////////////////
	private JSONObject castReservedEventToJSONObj(ReservedEvent event){
		JSONObject jsonObj = new JSONObject();
		
		DateFormat dfDeadline = new SimpleDateFormat("yyyy-M-dd");
		Date deadLine = null;
		try {
			 deadLine = dfDeadline.parse("1970-01-01");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		try {
			jsonObj.put("name", event.getName());
			jsonObj.put("description", event.getDescription());
			jsonObj.put("category", event.getCategory());
			jsonObj.put("endTime", "");
			jsonObj.put("status", event.getStatus());
			jsonObj.put("location", event.getLocation());
			
			if (event.getCategory().equals(GenericEvent.Category.DEADLINE)){
				jsonObj.put("startTime", deadLine);
			} else {
				jsonObj.put("startTime", "");
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
		/*
		DateFormat dfDeadline = new SimpleDateFormat("yyyy-M-dd");
		Date deadLine = null;
		try {
			 deadLine = dfDeadline.parse("1970-01-01");
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		
		try {
			String startTime = jsonObj.get("startTime").toString();
			String endTime = jsonObj.get("endTime").toString();
			
			event.setName(jsonObj.getString("name"));
			event.setDescription(jsonObj.getString("description"));
			event.setCategory(getCategory(jsonObj));
			event.setStatus(getStatus(jsonObj));
			event.setLocation(jsonObj.getString("location"));
			
			if (event.getCategory().equals(GenericEvent.Category.FLOATING)){
				//doesn't set start time and end time
			} else {
				event.setStartTime(df.parse(startTime));
				event.setEndTime(df.parse(endTime));
			}
			
		} catch (JSONException e2) {
			e2.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		return event;
	}
	
	
	private ReservedEvent castJSONObjToReservedEvent(JSONObject jsonObj){
		ReservedEvent event = new ReservedEvent();
		
		try {
			
			event.setName(jsonObj.getString("name"));
			event.setDescription(jsonObj.getString("description"));
			event.setCategory(getCategory(jsonObj));
			event.setStatus(getStatus(jsonObj));
			event.setLocation(jsonObj.getString("location"));
			
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		
		return event;
	}
	
	
	
	private GenericEvent.Status getStatus(JSONObject jsonObj) throws JSONException{
		if (jsonObj.get("status").equals("COMPLETE")){
			return GenericEvent.Status.COMPLETE;
		} else if (jsonObj.get("status").equals("INCOMPLETE")){
			return GenericEvent.Status.INCOMPLETE;
		} else if (jsonObj.get("status").equals("UNDETERMINED")){
			return GenericEvent.Status.UNDETERMINED;
		} else {
			return GenericEvent.Status.NULL;
		}
	}
	
	private GenericEvent.Category getCategory(JSONObject jsonObj) throws JSONException{
		if (jsonObj.get("category").equals(Constant.CATEGORY_DEADLINE)){
			return GenericEvent.Category.DEADLINE;
		} else if (jsonObj.get("category").equals(Constant.CATEGORY_EVENT)){
			return GenericEvent.Category.EVENT;
		} else {
			return GenericEvent.Category.FLOATING;
		} 
	}

	private void updatedDisplayedEvents(State completeState){
		completeState.displayedEvents.clear();
		completeState.displayedEvents.addAll(completeState.completedEvents);
		completeState.displayedEvents.addAll(completeState.incompletedEvents);
		completeState.displayedEvents.addAll(completeState.undeterminedEvents);		
	}
	
	private void copyStorage(String sorucefileName, String destinationFileName) {
		 		Path source = Paths.get(sorucefileName);
		 		Path output = Paths.get(destinationFileName);
		 		
		 		try {
		 			
		 			File temp = new File (destinationFileName);
			 		if (temp.exists()){
			 			clearFile(destinationFileName);
			 			System.out.println("delete " + destinationFileName);
			 		}
			 		
		 			Files.copy(source, output, REPLACE_EXISTING);
		 			
		 			
		 		} catch (IOException e) {
		 			// TODO Auto-generated catch block
		 			e.printStackTrace();
		 		}
		 	}
	
}
