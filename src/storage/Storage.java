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
	
	public static void createFile() throws FileNotFoundException {
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
	
	
	public static State readStorage() {
		String line = null;
		State state = new State();
		
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			
			while ( (line = br.readLine()) != null) {
				JSONObject jsonObj = new JSONObject();
				try {
					jsonObj = new JSONObject(line);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Event event = new Event();
				try {
					event = castJSONObjToEvent(jsonObj);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					if (event.getStatus() == Status.COMPLETE){
						state.addToCompletedList(event);
					} else if (event.getStatus() == Status.INCOMPLETE){
						state.addToIncompletedList(event);
					} else if (event.getStatus() == Status.FLOATING){
						state.addToFloatingList(event);
					}
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
		String startTime = jsonObj.get("startTime").toString();
		System.out.println(startTime);
		String endTime = jsonObj.get("endTime").toString();
		//System.out.println(df.parse(endTime));
		
		
		
		event.setName(jsonObj.getString("name"));
		event.setDescription(jsonObj.getString("description"));
		event.setCategory(jsonObj.getString("category"));
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
		event.setStatus(decideStatus(jsonObj));
		event.setLocation(jsonObj.getString("location"));
		
		return event;
	}
	
	private static  Event.Status decideStatus(JSONObject jsonObj) throws JSONException{
		if (jsonObj.get("status") == "COMPLETE"){
			return Event.Status.COMPLETE;
		} else if (jsonObj.get("status") == "INCOMPLETE"){
			return Event.Status.INCOMPLETE;
		} else if (jsonObj.get("status") == "BLOCKED"){
			return Event.Status.BLOCKED;
		} else if (jsonObj.get("status") == "OVERDUE"){
			return Event.Status.OVERDUE;
		} else {
			return Event.Status.FLOATING;
		}
	}
	
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
	
	
}
