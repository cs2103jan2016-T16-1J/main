package main;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.junit.Test;
import command.Add;
import command.Command;
import controller.Controller;
import json.JSONException;
import parser.Parser;
import storage.Storage;


/**
 * Integraion testing using Top-down approach
 *
 */

public class IntegrationTesting {
	State testState;
	Controller testController;
	Parser testParser;
	private static final String VALID_NAME = "Test Event Name";
	private static final String VALID_LOCATION = "Supahotfire's house";
	private static final String startTime = "Sun Apr 03 00:00:00 SGT 2016";
	private static final String endTime = "Sun Apr 03 23:59:01 SGT 2016";
	private static final GenericEvent.Status VALID_STATUS = GenericEvent.Status.INCOMPLETE;
	private static final GenericEvent.Category VALID_CATEGORY = GenericEvent.Category.EVENT;
	private static final String testStorage = "./storage/storageTesting.txt";
	
	@Test
	public void testExecuteCommandAdd(){
		testState = new State();
		testController = new Controller(testStorage, testState);
		testParser = new Parser();
		
		String addCommand = "add Test Event Name on sunday at Supahotfire's house";
		
		SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd kk:mm:ss z yyyy");
		Event testNewEvent = null;
		
		try {
			testNewEvent = new Event(VALID_NAME, VALID_LOCATION, "", VALID_CATEGORY, 
					sdf.parse(startTime), sdf.parse(endTime), VALID_STATUS);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			
			testState = testController.executeCommand(addCommand, testStorage, testState);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//test executeCommand adds the right thing into state class
		assertEquals(testNewEvent.getName(), testState.getAllEvents().get(0).getName());
		assertEquals(testNewEvent.getCategory(), testState.getAllEvents().get(0).getCategory());
		assertEquals(testNewEvent.getDescription(), testState.getAllEvents().get(0).getDescription());
		assertEquals(testNewEvent.getLocation(), testState.getAllEvents().get(0).getLocation());
		assertEquals(testNewEvent.getStartTime(), testState.getAllEvents().get(0).getStartTime());
		assertEquals(testNewEvent.getEndTime(), testState.getAllEvents().get(0).getEndTime());
		assertEquals(testNewEvent.getStatus(), testState.getAllEvents().get(0).getStatus());
	
		//no need to test parser because it's in the executeCommand
		
		//testing storage
		assertEquals(readFirstLine(), "{\"startTime\":\"Sun Apr 03 00:00:00 SGT 2016\",\"category\":\"EVENT\","
				+ "\"location\":\"Supahotfire's house\",\"status\":\"INCOMPLETE\",\"description\":\"\",\"name\":"
				+ "\"Test Event Name\",\"endTime\":\"Sun Apr 03 23:59:01 SGT 2016\"}");
		
	}
	
	
	@Test
	public void testExecuteCommandSelect(){
		testState = new State();
		testController = new Controller(testStorage, testState);
		testParser = new Parser();
		
		String selectCommand = "select Test Event Name";
		String addCommand = "add Test Event Name on sunday at Supahotfire's house";
		
		SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd kk:mm:ss z yyyy");
		Event testNewEvent = null;
		
		try {
			testNewEvent = new Event(VALID_NAME, VALID_LOCATION, "", VALID_CATEGORY, 
					sdf.parse(startTime), sdf.parse(endTime), VALID_STATUS);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			testState = testController.executeCommand(addCommand, testStorage, testState);
			testState = testController.executeCommand(selectCommand, testStorage, testState);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(testNewEvent.getName(), testState.selectedEvent.getName());
		assertEquals(testNewEvent.getName(), testState.selectedEvent.getName());
		assertEquals(testNewEvent.getCategory(), testState.selectedEvent.getCategory());
		assertEquals(testNewEvent.getDescription(), testState.selectedEvent.getDescription());
		assertEquals(testNewEvent.getLocation(), testState.selectedEvent.getLocation());
		assertEquals(testNewEvent.getStartTime(), testState.selectedEvent.getStartTime());
		assertEquals(testNewEvent.getEndTime(), testState.selectedEvent.getEndTime());
		assertEquals(testNewEvent.getStatus(), testState.selectedEvent.getStatus());
		
	}
	
	
	@Test
	public void executeCommandDelete(){
		testState = new State();
		testController = new Controller(testStorage, testState);
		testParser = new Parser();
		
		String addCommand = "add Test Event Name on sunday at Supahotfire's house";
		String selectCommand = "select Test Event Name";
		String deleteCommand = "delete Test Event Name";
		
		SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd kk:mm:ss z yyyy");
		Event testNewEvent = null;
		
		try {
			testNewEvent = new Event(VALID_NAME, VALID_LOCATION, "", VALID_CATEGORY, 
					sdf.parse(startTime), sdf.parse(endTime), VALID_STATUS);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			testState = testController.executeCommand(addCommand, testStorage, testState);
			testState = testController.executeCommand(selectCommand, testStorage, testState);
			testState = testController.executeCommand(deleteCommand, testStorage, testState);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//check if state class is empty
		assertEquals(testState.getAllEvents().size(), 0);
		
		//check if storage is empty
		try {
			assertEquals(getLineCount(testStorage), 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private String readFirstLine(){
		String result = null;
			try {
				FileReader fr = new FileReader(testStorage);
				BufferedReader br = new BufferedReader(fr);
				result = br.readLine();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return result;
	}
	

	private static int getLineCount(String fileName) throws IOException {
		LineNumberReader reader  = new LineNumberReader(new FileReader(fileName));
		int count = 0;
		
			while ((reader.readLine()) != null) {}
			
		count = reader.getLineNumber(); 
		reader.close();
		return count;
		
	}
	
}
