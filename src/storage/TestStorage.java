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
import main.Event.Status;
import main.State;

public class TestStorage {
	String testFileName = "./TestStorage.txt";
	
	Storage testStorage = new Storage();
	Event testCompletedEvent= new Event();
	Event testIncompletedEvent= new Event();
	Event testFloatingEvent= new Event();
	State testState= new State();
	
	public void initialize(){
		setUpCompletedEvent();
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
		testStorage.addToStorage(testCompletedEvent,testFileName);
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
			assertEquals(getLineCount(testFileName), 3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testAddToStorage(){
		//check if addToStorage is storing the right number of events
		initialize();
		testStorage.addToStorage(testCompletedEvent, testFileName);
		testStorage.addToStorage(testIncompletedEvent, testFileName);
		testStorage.addToStorage(testFloatingEvent, testFileName);
		try {
			assertEquals(getLineCount(testFileName), 3);
			assertEquals(readFirstLine(),"{\"startTime\":\"Sun Mar 27 00:00:00 SGT 2016\",\"category\":\"EVENT\","
					+ "\"location\":\"NUS\",\"status\":\"COMPLETE\",\"description\":\"project manual\",\"name\":"
					+ "\"CS2103\",\"endTime\":\"Sun Mar 27 23:59:00 SGT 2016\"}");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	@Test
	public void testReadStorage(){
		//This is the case for 'incompleted event' partition
		initialize();
		testStorage.addToStorage(testIncompletedEvent, testFileName);
		testState = testStorage.readStorage(testFileName);
		assertEquals(testState.incompletedEvents.size(), 1);
		assertEquals(testState.completedEvents.size(), 0);
		assertEquals(testState.floatingEvents.size(), 0);
		
		//This is the case for 'completed event' partition
		initialize();
		testStorage.addToStorage(testCompletedEvent, testFileName);
		testState = testStorage.readStorage(testFileName);
		assertEquals(testState.incompletedEvents.size(), 0);
		assertEquals(testState.completedEvents.size(), 1);
		assertEquals(testState.floatingEvents.size(), 0);
		
		//This is the case for 'floating event' partition
		initialize();
		testStorage.addToStorage(testFloatingEvent, testFileName);
		testState = testStorage.readStorage(testFileName);
		assertEquals(testState.incompletedEvents.size(), 0);
		assertEquals(testState.completedEvents.size(), 0);
		assertEquals(testState.floatingEvents.size(), 1);
	}

	
	
	
	
	
	
	
	public void setUpIncompletedEvent(){
		SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd kk:mm:ss z yyyy");
		String startTime = "Sun Mar 27 00:00:00 SGT 2016";
		String endTime = "Sun Mar 27 23:59:00 SGT 2016";
		
		testIncompletedEvent.setCategory("EVENT");
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
	
	public void setUpCompletedEvent(){
		SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd kk:mm:ss z yyyy");
		String startTime = "Sun Mar 27 00:00:00 SGT 2016";
		String endTime = "Sun Mar 27 23:59:00 SGT 2016";
		
		testCompletedEvent.setCategory("EVENT");
		testCompletedEvent.setDescription("project manual");
		testCompletedEvent.setLocation("NUS");
		testCompletedEvent.setName("CS2103");
		testCompletedEvent.setStatus(Status.COMPLETE);
		try {
			testCompletedEvent.setStartTime(sdf.parse(startTime));
			testCompletedEvent.setEndTime(sdf.parse(endTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setUpFloatingEvent(){
		SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd kk:mm:ss z yyyy");
		String startTime = "Sun Mar 27 00:00:00 SGT 2016";
		String endTime = "Sun Mar 27 23:59:00 SGT 2016";
		
		testFloatingEvent.setCategory("EVENT");
		testFloatingEvent.setDescription("project manual");
		testFloatingEvent.setLocation("NUS");
		testFloatingEvent.setName("CS2103");
		testFloatingEvent.setStatus(Status.FLOATING);
		try {
			testFloatingEvent.setStartTime(sdf.parse(startTime));
			testFloatingEvent.setEndTime(sdf.parse(endTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setUpState(){
		testState.addToFloatingList(testFloatingEvent);
		testState.addToCompletedList(testCompletedEvent);
		testState.addToIncompletedList(testIncompletedEvent);
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
