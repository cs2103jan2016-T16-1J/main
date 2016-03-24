package command;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import json.JSONException;
import main.Event;
import main.Event.Category;
import main.Event.Status;
import main.State;

/**
 * Testing Commands
 * @author Reem
 *
 */
public class CommandTesting {

	private static final String VALID_NAME = "Test Event Name";
	private static final String VALID_DESCRIPTION = "Test Event Description";
	private static final String VALID_LOCATION = "Supahotfire's house";
	private static final Status VALID_STATUS = Status.COMPLETE;
	private static final Category VALID_CATEGORY = Category.EVENT;
	private static final String EMPTY_STRING = "";
	private static final Date MAX_DATE = new Date(Long.MAX_VALUE);

	State testState;

	public void test() {

		testAdd();
		testDelete();
		
	}
	
	public void testDelete(){
		testDeleteValidEvent();
		testDeleteInvalidEvent();
	}
	
	public void testAdd(){
		/***Boundary Value Analysis****/
		//testValidEvent tests an EVent that should be within the acceptable parameters by Add		
		testAddValidEvent();

		//testAddInvalidEvent tests a null Event- one that should not be added as an event but should still be handled gracefully
		testAddInvalidEvent();
		
		/***Combining Multiple Inputs****/
		//testAddWithMultipleInputs 2^5 possible inputs by combining empty and valid values of each parameter within event
		testAddWithMultipleInputs();
	}
	
	
	@Test
	public void testDeleteInvalidEvent(){
		Event testNewEvent = null;
		testState = new State();
		testAddValidEvent();

		ArrayList<Event> existingEvents = testState.getAllEvents();
		
		Command deleting = new Add(testNewEvent);
		try {
			testState = deleting.execute(testState);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(existingEvents, testState.getAllEvents());

	
	}
	
	@Test
	public void testDeleteValidEvent(){
		ArrayList<Event> emptyArray = new ArrayList<Event>();

		testState = new State();
		testAddValidEvent();
		
		
		Event eventToDelete = new Event();
		eventToDelete.setName(VALID_NAME);
		Command deleting = new Delete(eventToDelete);
		
		try {
			testState = deleting.execute(testState);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals(emptyArray, testState.getAllEvents());

	}
	
	@Test
	public void testAddInvalidEvent(){
		Event testNewEvent = null;
		testState = new State();
		ArrayList<Event> emptyArray = new ArrayList<Event>();
		
		Command adding = new Add(testNewEvent);
		try {
			testState = adding.execute(testState);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(emptyArray, testState.getAllEvents());

		
	}
	
	@Test
	public void testAddWithMultipleInputs(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateInString = "31-08-1982 10:20:56";
		Date aTime = null;
		try {
			aTime = sdf.parse(dateInString);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int i, j, k, l, m;
		
		String[] name = {VALID_NAME, EMPTY_STRING};
		String[] description = {VALID_DESCRIPTION, EMPTY_STRING};
		String[] location = {VALID_LOCATION, EMPTY_STRING};
		Date[] startDate = {aTime, MAX_DATE};
		Date[] endDate = {aTime, MAX_DATE};
		
		for(i = 0 ; i < 2; i++){
			for(j = 0; j <2; j++){
				for(k = 0; k < 2; k++){
					for(l = 0; l < 2; l++){	
						for(m = 0; m <2; m++){
							testState = new State();
							Event testNewEvent = new Event(name[i], location[j], description[k], VALID_CATEGORY, startDate[l], endDate[m], VALID_STATUS);
							Command adding = new Add(testNewEvent);
							try {
								testState = adding.execute(testState);
							} catch (IOException | JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							assertEquals(testNewEvent, testState.getAllEvents().get(0));
					
						}
					}				
				}
			}
		}
	}
	
	@Test
	public void testAddValidEvent(){
		testState = new State();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateInString = "31-08-1982 10:20:56";
		Date aTime = null;
		try {
			aTime = sdf.parse(dateInString);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Event testNewEvent = new Event(VALID_NAME, VALID_LOCATION, VALID_DESCRIPTION, VALID_CATEGORY, aTime, aTime, VALID_STATUS);

			
		
		Command adding = new Add(testNewEvent);
		try {
			testState = adding.execute(testState);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(testNewEvent, testState.getAllEvents().get(0));
	}


}
