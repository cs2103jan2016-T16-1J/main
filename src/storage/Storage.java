package storage;

import java.io.BufferedReader;
import java.io.File;

import main.Event;
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


public class Storage {
	
	public static final String fileName =  ("./storage.txt");
	public static final String tempFileName = ("temp.txt");
	
	public void createFile() throws FileNotFoundException {
		File file = new File(fileName);
		if (!file.exists()){
			 PrintWriter writer = new PrintWriter(fileName);
			 writer.close();
		}
	}
	
	public static void addToStorage(Event event) throws IOException, JSONException{
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
	
	public static void clearStorage(){
		try {
			PrintWriter writer = new PrintWriter(fileName);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	public static void removeFromStorage(Event event)  {
		String line = null;
		
		try {
			File file = new File(fileName);
			File tempFile = new File(tempFileName);
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
			FileReader fr = new FileReader(fileName);
			BufferedReader br =  new BufferedReader(fr);
			
			while ((line = br.readLine()) != null){
				JSONObject jsonObj = new JSONObject();
				try {
					jsonObj = new JSONObject(line);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					if (!isSameObject(jsonObj, event)){
						pw.println(line);
						pw.flush();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
			br.close();
			pw.close();
			fr.close();
			
			if (!tempFile.renameTo(file)){
                System.out.println("Could not rename file");
            }
			
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");                
        } catch(IOException ex) {
                ex.printStackTrace();
        }
	}
	
	
	public State readStorage() {
		String line = null;
		State state = new State();
		
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			
			while ( (line = br.readLine()) != null) {
				JSONObject jsonObj ;
				Event event = new Event();
				
				try {
					jsonObj = new JSONObject(line);
					event = castJSONObjToEvent(jsonObj);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				//System.out.println(event.getStartTime());
				
					if (event.getStatus() == Status.COMPLETE){
						state.addToCompletedList(event);
						System.out.println("complete list" + state.completedEvents.size());
						//System.out.println("add to complete list");
					} else if (event.getStatus() == Status.INCOMPLETE){
						state.addToIncompletedList(event);
						System.out.println("incomplete list" + state.incompletedEvents.size());
						//System.out.println("add to incomplete list");
					} else if (event.getStatus() == Status.FLOATING){
						state.addToFloatingList(event);
						System.out.println("add to floating list");
					}
					
					updatedDisplayedEvents(state);
			}
			
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");                
        } catch(IOException ex) {
                ex.printStackTrace();
        }
		
		return state;
	}
	
	
	public static JSONObject castEventToJSONObj(Event event) throws JSONException{
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("name", event.getName());
		jsonObj.put("description", event.getDescription());
		jsonObj.put("category", event.getCategory());
		jsonObj.put("startTime", event.getStartTime());
		jsonObj.put("endTime", event.getEndTime());
		jsonObj.put("status", event.getStatus());
		jsonObj.put("location", event.getLocation());
		
		return jsonObj;
	
	}
	
	public static Event castJSONObjToEvent(JSONObject jsonObj) throws JSONException{
		Event event = new Event();
		DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
		DateFormat dfDeadline = new SimpleDateFormat("yyyy-M-dd");
		String startTime = jsonObj.get("startTime").toString();
		//System.out.println(startTime);
		String endTime = jsonObj.get("endTime").toString();
		//System.out.println(df.parse(endTime));
		
		event.setName(jsonObj.getString("name"));
		event.setDescription(jsonObj.getString("description"));
		event.setCategory(jsonObj.getString("category"));
		event.setStatus(decideStatus(jsonObj));
		event.setLocation(jsonObj.getString("location"));
		
		if (jsonObj.getString("category").equals("DEADLINE")){
				
			try {
				event.setStartTime(dfDeadline.parse(startTime));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				event.setEndTime(df.parse(endTime));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				event.setStartTime(df.parse(startTime));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				event.setEndTime(df.parse(endTime));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		return event;
	}
	
	private static  Event.Status decideStatus(JSONObject jsonObj) throws JSONException{
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
	
	/*
	private static String decideCategory (String category) {
		if (category.equals("DEADLINE")){
			return
		}
	}*/
	
	public static boolean isSameObject (JSONObject js1, Event e) throws JSONException {
		if (js1.get("name").equals(e.getName()) && 
				js1.get("category").equals(e.getCategory()) && 
				js1.get("description").equals(e.getDescription()) &&
				js1.get("location").equals(e.getLocation()) && 
				js1.get("status").equals(e.getStatus().toString()) && 
				js1.get("startTime").equals(e.getStartTime().toString()) && 
				js1.get("endTime").equals(e.getEndTime().toString())){
			return true;
		}
		
		return false;
	}
	
	
	private void updatedDisplayedEvents(State completeState){
		completeState.displayedEvents.clear();
		completeState.displayedEvents.addAll(completeState.completedEvents);
		completeState.displayedEvents.addAll(completeState.incompletedEvents);
		completeState.displayedEvents.addAll(completeState.floatingEvents);		
	}
	
}
