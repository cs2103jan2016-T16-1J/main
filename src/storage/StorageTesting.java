package storage;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.junit.Test;
import main.Event;
import main.GenericEvent.Category;
import main.GenericEvent.Status;
import main.ReservedEvent;
import main.State;

/**
 * @@author claudia zhou
 */

public class StorageTesting{
	String testFileName = "./TestStorage.txt";
	
	Storage testStorage = new Storage();
	Event testIncompletedEvent= new Event();
	ReservedEvent testFloatingEvent= new ReservedEvent();
	State testState= new State();
	
	public void initialize(){
		setUpIncompletedEvent();
		setUpFloatingEvent();
		setUpState();
		testStorage.clearFile(testFileName);
	}
	
	
	@Test
	public void testCreateFile(){
		//if file doesn't exist, check if the createFile method create a file 
		File file = new File(testFileName);
		file.delete();
		testStorage.createFile(testFileName);
		assertTrue(file.exists());
		
	}
	
	
	@Test
	public void testClearFile(){
		//check if clearFile method clears all contents
		initialize();
		testStorage.stateToStorage(testState,testFileName);
		testStorage.clearFile(testFileName);
		
		try {
			assertEquals(getLineCount(testFileName), 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testStateToStorage(){
		//check if stateToStorage method is adding state to storage
		//state==null should not be considered, so no equivalence partition
		initialize();
		testStorage.stateToStorage(testState, testFileName);
		try {
			assertEquals(getLineCount(testFileName), 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testReadStorage(){
		initialize();
		testStorage.stateToStorage(testState, testFileName);
		testState = testStorage.readStorage(testFileName);
		assertEquals(testState.incompletedEvents.size(), 1);
		assertEquals(testState.completedEvents.size(), 0);
		assertEquals(testState.undeterminedEvents.size(), 1);
	}

	
	public void setUpIncompletedEvent(){
		SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd kk:mm:ss z yyyy");
		String startTime = "Sun Mar 27 00:00:00 SGT 2016";
		String endTime = "Sun Mar 27 23:59:00 SGT 2016";
		
		testIncompletedEvent.setCategory(Category.EVENT);
		testIncompletedEvent.setDescription("project manual");
		testIncompletedEvent.setLocation("NUS");
		testIncompletedEvent.setName("CS2103");
		testIncompletedEvent.setStatus(Status.INCOMPLETE);
		try {
			testIncompletedEvent.setStartTime(sdf.parse(startTime));
			testIncompletedEvent.setEndTime(sdf.parse(endTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void setUpFloatingEvent(){
		testFloatingEvent.setCategory(Category.EVENT);
		testFloatingEvent.setDescription("bring paper");
		testFloatingEvent.setLocation("office");
		testFloatingEvent.setName("meeting");
		testFloatingEvent.setStatus(Status.UNDETERMINED);
		
	}
	
	private void setUpState(){
		testState.addToIncompletedList(testIncompletedEvent);
		testState.addToUndeterminedList(testFloatingEvent);
	}
	
	public String readFirstLine(){
		String result = null;
			try {
				FileReader fr = new FileReader(testFileName);
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
	
	public static int getLineCount(String fileName) throws IOException {
		LineNumberReader reader  = new LineNumberReader(new FileReader(fileName));
		int count = 0;
		
			while ((reader.readLine()) != null) {}
			
		count = reader.getLineNumber(); 
		reader.close();
		return count;
		
	}

}