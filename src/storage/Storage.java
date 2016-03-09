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
			
			JSONObject removedJsonObj = castEventToJSONObj(event);
			
			while ((line = br.readLine()) != null){
				JSONObject jsonObj = new JSONObject(line);
				
				/*
				System.out.println(jsonObj.get("name"));
				System.out.println(removedJsonObj.get("name"));
				System.out.println(jsonObj.get("location"));
				System.out.println(removedJsonObj.get("location"));
				System.out.println(jsonObj.get("startTime"));
				System.out.println(removedJsonObj.get("startTime"));
				System.out.println(jsonObj.get("endTime"));
				System.out.println(removedJsonObj.get("endTime"));
				System.out.println(jsonObj.get("category"));
				System.out.println(removedJsonObj.get("category"));
				System.out.println(jsonObj.get("description"));
				System.out.println(removedJsonObj.get("description"));
				System.out.println(jsonObj.get("status"));
				System.out.println(removedJsonObj.get("status"));
				System.out.println(jsonObj.get("startTime"));
				System.out.println(removedJsonObj.get("startTime"));*/
				//System.out.println(isSameJSONObj(jsonObj,removedJsonObj));
				
				if (!isSameJSONObj(jsonObj, removedJsonObj)){
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
	
	
	public static boolean isSameJSONObj (JSONObject js1, JSONObject js2) throws JSONException {
		if (js1.get("name").equals(js2.get("name")) && 
				js1.get("category").equals(js2.get("category")) && 
				js1.get("description").equals(js2.get("description")) &&
				js1.get("location").equals(js2.get("location"))){
			return true;
		}
		
		return false;
	}
	
	/*
	public static boolean isSameJSONObj (JSONObject js1, JSONObject js2) throws JSONException {
	    if (js1 == null || js2 == null) {
	        return (js1 == js2);
	    }

	    List<String> l1 =  Arrays.asList(JSONObject.getNames(js1));
	    Collections.sort(l1);
	    List<String> l2 =  Arrays.asList(JSONObject.getNames(js2));
	    Collections.sort(l2);
	    
	    if (!l1.equals(l2)) { return true;}
	    
	    for (String key : l1) {
	        Object val1 = js1.get(key);
	        Object val2 = js2.get(key);
	        if (val1 instanceof JSONObject) {
	            if (!(val2 instanceof JSONObject)) {
	                return true;
	            }
	            if (!isSameJSONObj((JSONObject)val1, (JSONObject)val2)) {
	                return true;
	            }
	        }

	        if (val1 == null) {
	            if (val2 != null) {
	                return true;
	            }
	        }  else if (!val1.equals(val2)) {
	            return true;
	        }
	    }
	    return false;
	}*/
	
	
}
