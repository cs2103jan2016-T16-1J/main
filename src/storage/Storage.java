package storage;

import java.io.BufferedReader;
import java.io.File;
import org.json.*;
import main.Event;
import main.Event.Status;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Storage {
	
	public static final String fileName =  ("./storage.txt");
	public static final String tempFileName = ("temp.txt");
	File file = new File(fileName);
	
	public  void createFile() throws FileNotFoundException {
		 PrintWriter writer = new PrintWriter(fileName);
		 writer.close();
	}
	
	public void addToStorage(Event event) throws IOException, JSONException{
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
	
	public void removeFromStorage(Event event) throws JSONException {
		String line = null;
		
		try {
			File tempFile = new File(tempFileName);
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
			FileReader fr = new FileReader(fileName);
			BufferedReader br =  new BufferedReader(fr);
			
			JSONObject removedJsonObj = castEventToJSONObj(event);
			
			while ((line = br.readLine()) != null){
				JSONObject jsonObj = new JSONObject(line);
				
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
	
	
	public void readStorage() throws JSONException{
		String line = null;
		
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			
			while ( (line = br.readLine()) != null){
				JSONObject jsonObj = new JSONObject(line);
				Event event = castJSONObjToEvent(jsonObj);
				
				//import into state class???
			}
			
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");                
        } catch(IOException ex) {
                ex.printStackTrace();
        }
	}
	
	
	public JSONObject castEventToJSONObj(Event event) throws JSONException{
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
	
	public Event castJSONObjToEvent(JSONObject jsonObj) throws JSONException{
		Event event = new Event();
		
		event.setName(jsonObj.getString("name"));
		event.setCategory(jsonObj.getString("category"));
		event.setCategory(jsonObj.getString("category"));
		event.setStartTime((Date) jsonObj.get("startTime"));
		event.setEndTime((Date) jsonObj.get("endTime"));
		event.setStatus((Status) jsonObj.get("status"));
		event.setLocation(jsonObj.getString("location"));
		
		return event;
	}
	
	
	public boolean isSameJSONObj (JSONObject js1, JSONObject js2) throws JSONException {
	    if (js1 == null || js2 == null) {
	        return (js1 == js2);
	    }

	    List<String> l1 =  Arrays.asList(JSONObject.getNames(js1));
	    Collections.sort(l1);
	    List<String> l2 =  Arrays.asList(JSONObject.getNames(js2));
	    Collections.sort(l2);
	    
	    if (!l1.equals(l2)) { return false;}
	    
	    for (String key : l1) {
	        Object val1 = js1.get(key);
	        Object val2 = js2.get(key);
	        if (val1 instanceof JSONObject) {
	            if (!(val2 instanceof JSONObject)) {
	                return false;
	            }
	            if (!isSameJSONObj((JSONObject)val1, (JSONObject)val2)) {
	                return false;
	            }
	        }

	        if (val1 == null) {
	            if (val2 != null) {
	                return false;
	            }
	        }  else if (!val1.equals(val2)) {
	            return false;
	        }
	    }
	    return true;
	}
	
	
}
