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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
	
	public static void removeFromStorage(Event event) throws JSONException {
		String line = null;
		
		try {
			File file = new File(fileName);
			File tempFile = new File(tempFileName);
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
			FileReader fr = new FileReader(fileName);
			BufferedReader br =  new BufferedReader(fr);
			
			while ((line = br.readLine()) != null){
				JSONObject jsonObj = new JSONObject(line);
				
				if (!isSameObject(jsonObj, event)){
					pw.println(line);
					pw.flush();
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
	
	
	public static void readStorage() throws JSONException{
		String line = null;
		
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			
			while ( (line = br.readLine()) != null){
				JSONObject jsonObj = new JSONObject(line);
				Event event = castJSONObjToEvent(jsonObj);
				
					if (event.getStatus() == Status.COMPLETE){
						State.addToCompletedList(event);
					} else if (event.getStatus() == Status.INCOMPLETE){
						State.addToIncompletedList(event);
					} else if (event.getStatus() == Status.FLOATING){
						State.addToFloatingList(event);
					}
			}
			
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");                
        } catch(IOException ex) {
                ex.printStackTrace();
        }
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
		
		event.setName(jsonObj.getString("name"));
		event.setDescription(jsonObj.getString("description"));
		event.setCategory(jsonObj.getString("category"));
		event.setStartTime((Date) jsonObj.get("startTime"));
		event.setEndTime((Date) jsonObj.get("endTime"));
		event.setStatus((Status) jsonObj.get("status"));
		event.setLocation(jsonObj.getString("location"));
		
		return event;
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
