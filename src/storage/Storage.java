package storage;

import java.io.BufferedReader;
import org.json.*;
import main.Event;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Storage {
	
	public static final String fileName =  ("./storage.txt");
	
	public  void createFile() throws FileNotFoundException {
		 PrintWriter writer = new PrintWriter(fileName);
		 writer.close();
	}
	
	public void writeFile(Event event) throws IOException, JSONException{
		JSONObject dataSet = new JSONObject();
		
		dataSet.put("name", event.getName());
		dataSet.put("description", event.getDescription());
		dataSet.put("category", event.getCategory());
		dataSet.put("startTime", event.getStartTime());
		dataSet.put("endTime", event.getEndTime());
		dataSet.put("status", event.getStatus());
		
		try {
			FileWriter fw = new FileWriter(fileName, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(dataSet.toString());
			pw.close();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public void readFile() throws JSONException{
		String line = null;
		
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			
			while ( (line = br.readLine()) != null){
				JSONObject jsonObj = new JSONObject(line);
				
				//cast JSONObject to an event object
			}
			
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");                
        } catch(IOException ex) {
                ex.printStackTrace();
        }
	}
	
	
}
