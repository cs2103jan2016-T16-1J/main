package main;

import static org.junit.Assert.*;
import java.io.IOException;
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
		
		String command = "add Test Event Name on sunday at Supahotfire's house";
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
			
			testState = testController.executeCommand(command, testStorage, testState);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(testNewEvent.getName(), testState.getAllEvents().get(0).getName());
		assertEquals(testNewEvent.getCategory(), testState.getAllEvents().get(0).getCategory());
		assertEquals(testNewEvent.getDescription(), testState.getAllEvents().get(0).getDescription());
		assertEquals(testNewEvent.getLocation(), testState.getAllEvents().get(0).getLocation());
		assertEquals(testNewEvent.getStartTime(), testState.getAllEvents().get(0).getStartTime());
		assertEquals(testNewEvent.getEndTime(), testState.getAllEvents().get(0).getEndTime());
		assertEquals(testNewEvent.getStatus(), testState.getAllEvents().get(0).getStatus());
		
		
	}
	
	
}
