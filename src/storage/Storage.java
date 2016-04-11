package storage;

import java.io.BufferedReader;
import java.io.File;

import main.Event;
import main.GenericEvent;
import main.GenericEvent.Category;
import main.GenericEvent.Status;
import main.ReservedEvent;
import main.State;
import main.TimePair;

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
import java.util.ArrayList;
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
 * @@author claudia
 */


public class Storage {
	
	public static String storageFile ;
	public static String folderName;
	public final static String tempFileName = ("temp.txt");
	
	//@@author claudia
	public Storage(){
		storageFile =  "./storage/storage.txt";
		folderName = "./storage/";
	}
	
	public String getDirectory(){
		return storageFile;
	}
	
	public void setDirectory(String s){
		storageFile = s;
	}
	
	
	public void exportDir(String newDirectory){
		
		copyStorage(getDirectory(), newDirectory);
		
	}
	
	public void importDir(String sourceDirectory){
		copyStorage(sourceDirectory, getDirectory());
		
	}
	
	/** 
	 * Check if the file is exist. If not, create a new file
	 */
	public void createFile(String fileName)  {
		new File(folderName).mkdir();
		
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
	
	public void stateToStorage(State completeState, String fileName){
		clearFile(fileName);
		
		JSONArray arrCompleted = new JSONArray();
		JSONArray arrIncompleted = new JSONArray();
		JSONArray arrUndetermined = new JSONArray();
		JSONArray arrReserved = new JSONArray();
		
		
			for (Event e: completeState.completedEvents){
				JSONObject object = new JSONObject();
				object = castEventToJSONObj(e);
				arrCompleted.put(object);
			}
			for (Event e: completeState.incompletedEvents){
				JSONObject object = new JSONObject();
				object = castEventToJSONObj(e);
				arrIncompleted.put(object);
			}
			for (ReservedEvent e: completeState.undeterminedEvents){
				JSONObject object = new JSONObject();
				object = castFloatingEventToJSONObj(e);
				arrUndetermined.put(object);
			}
			for (ReservedEvent e: completeState.reservedEvents){
				JSONObject object = new JSONObject();
				object = castReservedEventToJSONObj(e);
				arrReserved.put(object);
			}
			
			JSONObject o = new JSONObject();
			try {
				o.put("completed", arrCompleted);
				o.put("incompleted", arrIncompleted);
				o.put("undetermined", arrUndetermined);
				o.put("reserved", arrReserved);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		try {
			FileWriter fw = new FileWriter(fileName, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(o.toString());
			pw.close();
		} catch(IOException ex) {
			ex.printStackTrace();
		}

	}

	public State readStorage(String fileName) {
		String line = null;
		State state = new State();
		String jsonData = "";
		
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			
			while ( (line = br.readLine()) != null) {
				jsonData += line + "\n";
				
			}
			
			if (jsonData.contains("{")){
				JSONObject object = new JSONObject(jsonData);
				
				JSONArray arrCompleted = object.getJSONArray("completed");
				JSONArray arrIncompleted = object.getJSONArray("incompleted");
				JSONArray arrUndetermined = object.getJSONArray("undetermined");
				JSONArray arrReserved = object.getJSONArray("reserved");
				
				for (int i = 0; i < arrCompleted.length(); i++){
					Event e = castJSONObjToEvent(arrCompleted.getJSONObject(i));
					state.completedEvents.add(e);
				}
				for (int i = 0; i < arrIncompleted.length(); i++){
					Event e = castJSONObjToEvent(arrIncompleted.getJSONObject(i));
					state.incompletedEvents.add(e);
				}
				for (int i = 0; i < arrUndetermined.length(); i++){
					ReservedEvent e = castJSONObjToFloatingEvent(arrUndetermined.getJSONObject(i));
					state.undeterminedEvents.add(e);
				}
				for (int i = 0; i < arrReserved.length(); i++){
					ReservedEvent e = castJSONObjToReservedEvent(arrReserved.getJSONObject(i));
					state.reservedEvents.add(e);
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
		
		updatedDisplayedEvents(state);
		
		return state;
	}

	
	
	
	private Event castJSONObjToEvent(JSONObject jsonObj){
		Event event = new Event();
		DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
		
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
	
	private ReservedEvent castJSONObjToFloatingEvent(JSONObject jsonObj){
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
	
	private ReservedEvent castJSONObjToReservedEvent(JSONObject jsonObj){
		ReservedEvent event = new ReservedEvent();
		DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
		int max = 100;
		int count = 0;
		
		try {
			event.setName(jsonObj.getString("name"));
			event.setDescription(jsonObj.getString("description"));
			event.setCategory(getCategory(jsonObj));
			event.setStatus(getStatus(jsonObj));
			event.setLocation(jsonObj.getString("location"));
			
			for (int i = 0; i < 100; i ++){
				try{
					jsonObj.getString("startTime" + i);
				} catch (JSONException e){
					count = i; 
					break;
				}
			}
			
			ArrayList<TimePair> reservedTimes = new ArrayList<TimePair>();
			for (int i = 0; i < count; i++){
				TimePair t = new TimePair(df.parse(jsonObj.get("startTime"+i).toString()), 
						df.parse(jsonObj.get("endTime"+i).toString()));
				reservedTimes.add(t);
			}
			
			event.setReservedTimes(reservedTimes);
			
		} catch (JSONException e2) {
			e2.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return event;
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
			
			
			if (event.getCategory().equals(GenericEvent.Category.DEADLINE)){
				jsonObj.put("startTime", deadLine);
				jsonObj.put("endTime", event.getEndTime());
			} else {
				jsonObj.put("startTime", event.getStartTime());
				jsonObj.put("endTime", event.getEndTime());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jsonObj;
	
	}
	
	
	private JSONObject castFloatingEventToJSONObj(ReservedEvent event){
		JSONObject jsonObj = new JSONObject();
		
		try {
			jsonObj.put("name", event.getName());
			jsonObj.put("description", event.getDescription());
			jsonObj.put("category", event.getCategory());
			jsonObj.put("status", event.getStatus());
			jsonObj.put("location", event.getLocation());
			jsonObj.put("startTime", "");
			jsonObj.put("endTime", "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObj;
	
	}
	
	private JSONObject castReservedEventToJSONObj(ReservedEvent event){
		JSONObject jsonObj = new JSONObject();
		
		try {
			jsonObj.put("name", event.getName());
			jsonObj.put("description", event.getDescription());
			jsonObj.put("category", event.getCategory());
			jsonObj.put("status", event.getStatus());
			jsonObj.put("location", event.getLocation());
			
			int count = event.getReservedTimes().size();
			for (int i = 0; i < count; i ++){
				jsonObj.put("startTime" + i, event.getReservedTimes().get(i).getStartTime());
				jsonObj.put("endTime" + i, event.getReservedTimes().get(i).getEndTime());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObj;
		
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
		completeState.displayedEvents.addAll(completeState.reservedEvents);	
	}
	
	private void copyStorage(String sorucefileName, String destinationFileName) {
		 		Path source = Paths.get(sorucefileName);
		 		Path output = Paths.get(destinationFileName);
		 		
		 		try {
		 			
		 			File temp = new File (destinationFileName);
			 		if (temp.exists()){
			 			clearFile(destinationFileName);
			 		}
			 		
		 			Files.copy(source, output, REPLACE_EXISTING);
		 			
		 			
		 		} catch (IOException e) {
		 			// TODO Auto-generated catch block
		 			e.printStackTrace();
		 		}
		 	}
	
}
