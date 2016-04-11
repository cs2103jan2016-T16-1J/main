package parser;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import constant.Constant;
import main.Event;
import main.GenericEvent;
import main.ReservedEvent;
import main.TimePair;
import main.GenericEvent.Category;
import main.GenericEvent.Status;

/**
 * 
 * @@author Ingine 
 *
 */
public class TestParser {

	public void qaTesting() throws ParseException{
		/**Boundary value analysis**/
		testAddValidDate();
		
		testAddInvalidDate();
		
		testAddDeadline();
		
		testAddEvent();
	}

	public void testDirectory(){
		Parser parse = new Parser();
		GenericEvent task = new Event();
		Event expected = new Event();
		task = parse.testingDeterminedStuff("import C:/whatever/data");
		expected.setName("C:/whatever/data");
		
		assertEquals(expected.getName(), task.getName());
	}
	public void testReservedEvent() throws ParseException{
		Date startDate1, endDate1, startDate2, endDate2;
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		Parser parse = new Parser();
		GenericEvent reserved = new ReservedEvent();
		ReservedEvent expected = new ReservedEvent();
		ArrayList<TimePair> times = new ArrayList<>();
		TimePair time1 = new TimePair(format.parse("1/4/2016 00:00:00"),
				format.parse("1/4/2016 23:59:01"));
		TimePair time2 = new TimePair(format.parse("3/4/2016 00:00:00"),
				format.parse("3/4/2016 23:59:01"));
		times.add(time1);
		times.add(time2);

		/*
		reserved = parse.testingReservedStuff("reserve meeting from sat 3 pm to sat 5 pm and from sun 2:00 to sun 3:00");
		assertEquals("meeting", reserved.getName());
		startDate1 = reserved.getReservedTimes().get(0).getStartTime();
		endDate1 = reserved.getReservedTimes().get(0).getEndTime();
		startDate2 = reserved.getReservedTimes().get(1).getStartTime();
		endDate2 = reserved.getReservedTimes().get(1).getEndTime();
		
		reserved = parse.testingReservedStuff("reserve meeting by 2/4/16 and by 3/4/16 at IVLE // description");
				
		expected.setReservedTimes(times);
		
		assertEquals("meeting", reserved.getName());
		assertEquals("description", reserved.getDescription());
		assertEquals("IVLE", reserved.getLocation());
		startDate1 = reserved.getReservedTimes().get(0).getStartTime();
		endDate1 = reserved.getReservedTimes().get(0).getEndTime();
		startDate2 = reserved.getReservedTimes().get(1).getStartTime();
		endDate2 = reserved.getReservedTimes().get(1).getEndTime();
		
		reserved = parse.testingReservedStuff("block meeting by fri and by sun at Library // description");
		
		assertEquals("meeting", reserved.getName());
		assertEquals("description", reserved.getDescription());
		assertEquals("Library", reserved.getLocation());
		startDate1 = reserved.getReservedTimes().get(0).getStartTime();
		endDate1 = reserved.getReservedTimes().get(0).getEndTime();
		startDate2 = reserved.getReservedTimes().get(1).getStartTime();
		endDate2 = reserved.getReservedTimes().get(1).getEndTime();
		
	
		reserved = parse.testingReservedStuff("reserve meeting from 1/4/16 3:00 to 2/4/16 5:00"
				+ " and from 1/4/16 3 pm to 1/4/16 5 pm");
		assertEquals("meeting", reserved.getName());
		startDate1 = reserved.getReservedTimes().get(0).getStartTime();
		endDate1 = reserved.getReservedTimes().get(0).getEndTime();
		startDate2 = reserved.getReservedTimes().get(1).getStartTime();
		endDate2 = reserved.getReservedTimes().get(1).getEndTime();*/
		
		reserved = parse.testingReservedStuff("reserve meeting from 2/4/16 3 pm to 2/4/16 5 pm"
				+ " and from 3/4/16 3 pm to 3/4/16 5 pm and 5/4/16"); 
		String name = reserved.getName();
		
		assertEquals("meeting 5/4/16", reserved.getName());
		startDate1 = ((ReservedEvent) reserved).getReservedTimes().get(0).getStartTime();
		endDate1 = ((ReservedEvent) reserved).getReservedTimes().get(0).getEndTime();
		startDate2 = ((ReservedEvent) reserved).getReservedTimes().get(1).getStartTime();
		endDate2 = ((ReservedEvent) reserved).getReservedTimes().get(1).getEndTime();
		
		reserved = parse.testingReservedStuff("reserve meeting"); 
		name = reserved.getName();
		
		assertEquals("meeting", reserved.getName());
		startDate1 = ((ReservedEvent) reserved).getReservedTimes().get(0).getStartTime();
		endDate1 = ((ReservedEvent) reserved).getReservedTimes().get(0).getEndTime();

	}
	public void testAddValidDate() throws ParseException{
		Parser parse = new Parser();
		GenericEvent task;
		Event expected = new Event();
		task = parse.testingDeterminedStuff("add borderline date on 1/1/1970");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		if(task.getStatus() == Status.UNDETERMINED){
			
		} else{
			expected.setStartTime(format.parse("1/1/1970 00:00:00"));
			expected.setEndTime(format.parse("1/1/1970 23:59:01"));
			assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
			assertEquals(expected.getEndTime(), ((Event) task).getEndTime());	
			
			task = parse.testingDeterminedStuff("add borderline date on 23/3/2036");
			expected.setStartTime(format.parse("23/3/2036 00:00:00"));
			expected.setEndTime(format.parse("23/3/2036 23:59:01"));
			assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
			assertEquals(expected.getEndTime(), ((Event) task).getEndTime());	
			
			task = parse.testingDeterminedStuff("add borderline date on 23/3/9999");
			expected.setStartTime(format.parse("23/3/9999 00:00:00"));
			expected.setEndTime(format.parse("23/3/1999 23:59:01"));
			assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
			assertEquals(expected.getEndTime(), ((Event) task).getEndTime());	
		}

	}
	
	/**	Checking invalid date 
	 * @return initialized value**/
	public void testAddInvalidDate() throws ParseException{
		Parser parse = new Parser();
		GenericEvent task;
		Event expected = new Event();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		task = parse.testingDeterminedStuff("add invalid date on 00/00/0000");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(Constant.MAX_DATE);
		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(), ((Event) task).getEndTime());	
		
		task = parse.testingDeterminedStuff("add invalid date on 1/13/2015");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(Constant.MAX_DATE);
		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(), ((Event) task).getEndTime());	
		
		task = parse.testingDeterminedStuff("add invalid date on 99/99/9999");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(Constant.MAX_DATE);
		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(), ((Event) task).getEndTime());	
	}
	
	public void testFloating() throws ParseException{
		Parser parse = new Parser();
		GenericEvent task;
		Event expected = new Event();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		task = parse.testingDeterminedStuff("add shit");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(Constant.MAX_DATE);
		expected.setStatus(Status.UNDETERMINED);
		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(), ((Event) task).getEndTime());	
		assertEquals(expected.getStatus(), ((Event) task).getStatus());	

	}
	
	/**equivalence partitioning**/
	public void testAddDeadline() throws ParseException{
		Parser parse = new Parser();
		GenericEvent task;
		Event expected = new Event();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		task = parse.testingDeterminedStuff("add deadline by thurs 22:00");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(format.parse("7/4/2016 22:00:00"));
		assertEquals(expected.getEndTime(), ((Event) task).getEndTime());	
		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		
		task = parse.testingDeterminedStuff("add deadline by 27/4/16 23:00");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(format.parse("27/4/2016 23:00:00"));
		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(), ((Event) task).getEndTime());	
		
		task = parse.testingDeterminedStuff("add deadline at 9 am");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(format.parse("5/4/2016 9:00:00"));
		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(), ((Event) task).getEndTime());	
		
		/*
		task = parse.testingDeterminedStuff("add deadline by 27 apr 16");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(format.parse("27/4/2016 23:59:00"));
		assertEquals(expected.getStartTime(), task.getStartTime());
		assertEquals(expected.getEndTime(), task.getEndTime());
		
		task = parse.testingDeterminedStuff("add deadline by sunday");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(format.parse("3/4/2016 23:59:00"));
		assertEquals(expected.getStartTime(), task.getStartTime());
		assertEquals(expected.getEndTime(), task.getEndTime());
		
		task = parse.testingDeterminedStuff("add deadline on Sunday 6 pm");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(format.parse("3/4/2016 18:00:00"));
		assertEquals(expected.getStartTime(), task.getStartTime());
		assertEquals(expected.getEndTime(), task.getEndTime());
		
		
	
		
		
		task = parse.testingDeterminedStuff("add deadline on Sunday at 5 pm");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(format.parse("3/4/2016 17:00:00"));
		assertEquals(expected.getStartTime(), task.getStartTime());
		assertEquals(expected.getEndTime(), task.getEndTime());*/
		
	}
	
	@Test
	public void testNewAddEvent() throws ParseException{
		Parser parse = new Parser();
		GenericEvent task;
		Event expected = new Event();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date startDate, endDate;
		
		task = parse.testingDeterminedStuff("add event location ivle note what starttime 8/4/16 11:30 pm endtime 8/4/16 11:45 pm ");
		expected.setStartTime(format.parse("8/4/2016 23:30:00"));
		expected.setEndTime(format.parse("8/4/2016 23:45:00"));
		expected.setLocation("ivle");
		expected.setDescription("what");
		expected.setCategory(Category.EVENT);
		expected.setStatus(Status.INCOMPLETE);
		
		startDate = ((Event) task).getStartTime();
		endDate = ((Event) task).getEndTime();

		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(), ((Event) task).getEndTime());
		assertEquals(expected.getLocation(), ((Event) task).getLocation());
		assertEquals(expected.getDescription(), ((Event) task).getDescription());
		assertEquals(expected.getCategory(), ((Event) task).getCategory());
		assertEquals(expected.getStatus(), ((Event) task).getStatus());
	}
	
	public void testAddEvent() throws ParseException{
		Parser parse = new Parser();
		GenericEvent task;
		Event expected = new Event();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date startDate, endDate;
		
		
		task = parse.testingDeterminedStuff("add event from 1:30 pm to 1:45 pm at ivle // what");
		expected.setStartTime(format.parse("5/4/2016 13:30:00"));
		expected.setEndTime(format.parse("5/4/2016 13:45:00"));
		expected.setLocation("ivle");
		expected.setDescription("what");
		startDate = ((Event) task).getStartTime();
		endDate = ((Event) task).getEndTime();

		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(), ((Event) task).getEndTime());	
		assertEquals(expected.getLocation(), ((Event) task).getLocation());
		assertEquals(expected.getDescription(), ((Event) task).getDescription());
		
		task = parse.testingDeterminedStuff("add event from 8/4/16 to sat at ivle // what");
		expected.setStartTime(format.parse("8/4/2016 00:00:00"));
		expected.setEndTime(format.parse("9/4/2016 23:59:01"));
		expected.setLocation("ivle");
		expected.setDescription("what");
		startDate = ((Event) task).getStartTime();
		endDate = ((Event) task).getEndTime();

		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(), ((Event) task).getEndTime());	
		assertEquals(expected.getLocation(), ((Event) task).getLocation());
		assertEquals(expected.getDescription(), ((Event) task).getDescription());
		
		task = parse.testingDeterminedStuff("add event from 8/4/16 1:30 am to 12:45 pm 8/4/16");
		expected.setStartTime(format.parse("8/4/2016 01:30:00"));
		expected.setEndTime(format.parse("8/4/2016 12:45:00"));
		startDate = ((Event) task).getStartTime();
		endDate = ((Event) task).getEndTime();

		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(), ((Event) task).getEndTime());	
		
		task = parse.testingDeterminedStuff("add event from 8/4/16 1:00 to sat 12:00");
		expected.setStartTime(format.parse("8/4/2016 01:00:00"));
		expected.setEndTime(format.parse("9/4/2016 12:00:00"));
		startDate = ((Event) task).getStartTime();
		endDate = ((Event) task).getEndTime();

		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(), ((Event) task).getEndTime());	
		
		task = parse.testingDeterminedStuff("add event from friday 1 pm to friday 5 pm");
		expected.setStartTime(format.parse("8/4/2016 13:00:00"));
		expected.setEndTime(format.parse("8/4/2016 17:00:00"));
		startDate = ((Event) task).getStartTime();
		endDate = ((Event) task).getEndTime();

		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(), ((Event) task).getEndTime());	
		
		task = parse.testingDeterminedStuff("add event from friday 1:00 to 12:00");
		expected.setStartTime(format.parse("8/4/2016 01:00:00"));
		expected.setEndTime(format.parse("8/4/2016 12:00:00"));
		startDate = ((Event) task).getStartTime();
		endDate = ((Event) task).getEndTime();

		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(), ((Event) task).getEndTime());	
		/*
		task = parse.testingDeterminedStuff("add event on 3 pm Friday");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(format.parse("8/4/2016 15:00:00"));
		startDate = ((Event) task).getStartTime();
		endDate = ((Event) task).getEndTime();
		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(),((Event) task).getEndTime());
		
		task = parse.testingDeterminedStuff("add event on 15:00 Friday");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(format.parse("8/4/2016 15:00:00"));
		startDate = ((Event) task).getStartTime();
		endDate = ((Event) task).getEndTime();
		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(),((Event) task).getEndTime());
		
		task = parse.testingDeterminedStuff("add event on Friday 15:00");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(format.parse("8/4/2016 15:00:00"));
		startDate = ((Event) task).getStartTime();
		endDate = ((Event) task).getEndTime();
		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(),((Event) task).getEndTime());
		
		task = parse.testingDeterminedStuff("add event on  31 dec 16 23:59");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(format.parse("31/12/2016 23:59:00"));
		startDate = ((Event) task).getStartTime();
		endDate = ((Event) task).getEndTime();
		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(),((Event) task).getEndTime());
		
		task = parse.testingDeterminedStuff("add event on 21:00 31/12/16");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(format.parse("31/12/2016 21:00:00"));
		startDate = ((Event) task).getStartTime();
		endDate = ((Event) task).getEndTime();
		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(),((Event) task).getEndTime());

		task = parse.testingDeterminedStuff("add event on 9 pm 31/12/16");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(format.parse("31/12/2016 21:00:00"));
		startDate = ((Event) task).getStartTime();
		endDate = ((Event) task).getEndTime();
		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(),((Event) task).getEndTime());
		
		task = parse.testingDeterminedStuff("add event on 31/12/16 9 pm");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(format.parse("31/12/2016 21:00:00"));
		startDate = ((Event) task).getStartTime();
		endDate = ((Event) task).getEndTime();
		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(),((Event) task).getEndTime());
		
		task = parse.testingDeterminedStuff("add event on Friday");
		expected.setStartTime(format.parse("8/4/2016 00:00:00"));
		expected.setEndTime(format.parse("8/4/2016 23:59:01"));
		startDate = ((Event) task).getStartTime();
		endDate = ((Event) task).getEndTime();
		assertEquals(expected.getStartTime(), ((Event) task).getStartTime());
		assertEquals(expected.getEndTime(),((Event) task).getEndTime());
		*/
		
		/*
		task = parse.testingDeterminedStuff("add event from friday 1:00 to 12:00");
		expected.setStartTime(format.parse("1/4/2016 01:00:00"));
		expected.setEndTime(format.parse("1/4/2016 12:00:00"));
		assertEquals(expected.getStartTime(), task.getStartTime());
		assertEquals(expected.getEndTime(), task.getEndTime());
		
		
		task = parse.testingDeterminedStuff("add event from 31/12/16 00:00 to 31/12/16 23:59");
		expected.setStartTime(format.parse("31/12/2016 00:00:00"));
		expected.setEndTime(format.parse("31/12/2016 23:59:01"));
		assertEquals(expected.getStartTime(), task.getStartTime());
		assertEquals(expected.getEndTime(), task.getEndTime());
		
		task = parse.testingDeterminedStuff("add event from 31/12/16 00 am to 31/12/16 11:59 pm");
		expected.setStartTime(format.parse("31/12/2016 00:00:00"));
		expected.setEndTime(format.parse("31/12/2016 23:59:01"));
		assertEquals(expected.getStartTime(), task.getStartTime());
		assertEquals(expected.getEndTime(), task.getEndTime());
		
		task = parse.testingDeterminedStuff("add event from 00:00 31/12/16 to 23:59 31/12/16");
		expected.setStartTime(format.parse("31/12/2016 00:00:00"));
		expected.setEndTime(format.parse("31/12/2016 23:59:01"));
		assertEquals(expected.getStartTime(), task.getStartTime());
		assertEquals(expected.getEndTime(), task.getEndTime());
		
		task = parse.testingDeterminedStuff("add event from 01:00 31 dec 16 to 11:59 31 dec 16");
		expected.setStartTime(format.parse("31/12/2016 01:00:00"));
		expected.setEndTime(format.parse("31/12/2016 11:59:00"));
		assertEquals(expected.getStartTime(), task.getStartTime());
		assertEquals(expected.getEndTime(), task.getEndTime());
		
		task = parse.testingDeterminedStuff("add event from 01:00 31 dec 16 to 11:59 31 dec 16");
		expected.setStartTime(format.parse("31/12/2016 01:00:00"));
		expected.setEndTime(format.parse("31/12/2016 11:59:00"));
		assertEquals(expected.getStartTime(), task.getStartTime());
		assertEquals(expected.getEndTime(), task.getEndTime());
		
		task = parse.testingDeterminedStuff("add event from friday 1 am to friday 12 pm");
		expected.setStartTime(format.parse("1/4/2016 01:00:00"));
		expected.setEndTime(format.parse("1/4/2016 12:00:00"));
		assertEquals(expected.getStartTime(), task.getStartTime());
		assertEquals(expected.getEndTime(), task.getEndTime());
		
		task = parse.testingDeterminedStuff("add event from 1:30 am friday to 11:59 pm friday");
		expected.setStartTime(format.parse("1/4/2016 01:30:00"));
		expected.setEndTime(format.parse("1/4/2016 23:59:00"));
		assertEquals(expected.getStartTime(), task.getStartTime());
		assertEquals(expected.getEndTime(), task.getEndTime());*/
	}
	/*
	@Test
	public void testAdd() throws ParseException {
		Parser parse = new Parser();
		Event task = new Event();
		Event expected = new Event();
		
		task = parse.testingParseCommand("add that thing by thurs at IVLE// don't forget your GF");
		//task = parse.testingParseCommand("select something");
		//task = parse.testingParseCommand("add those thing on thurs at IVLE// don't forget your GF");

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		expected.setName("that thing");
		//expected.setStartTime(Constant.MIN_DATE);
		//expected.setEndTime(Constant.MAX_DATE);
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(format.parse("24/03/2016 23:59:00"));
		expected.setCategory(Constant.CATEGORY_DEADLINE);
		expected.setStatus(main.Event.Status.INCOMPLETE);
		expected.setDescription("don't forget your GF");
		expected.setLocation("IVLE");
		assertEquals(expected.getName(), task.getName());
		assertEquals(expected.getStartTime(), task.getStartTime());
		assertEquals(expected.getEndTime(), task.getEndTime());
		assertEquals(expected.getCategory(), task.getCategory());
		assertEquals(expected.getStatus(), task.getStatus());
		assertEquals(expected.getDescription(), task.getDescription());
		assertEquals(expected.getLocation(), task.getLocation());
	}
	*/
	/*
	@Test
	public void testEdit() throws ParseException{
		Parser parse = new Parser();
		Event oldTask = new Event();
		Event newTask = new Event();
		Event expected = new Event();
		
		oldTask = parse.testingParseCommand("add something by 16/03/16 17:30 at IVLE // notes ");
		newTask = parse.testingParseCommand("edit that thing");
		newTask = parse.testingParseCommand("edit from 24/03/16 17:30 to 25/03/16 11:00");
		//newTask = parse.testingParseCommand("edit this thing on weds at IVLE // bring sth");
		//newTask = parse.testingParseCommand("edit // bring sth");
		//newTask = parse.testingParseCommand("edit at cinema");

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		oldTask.setName("something");
		oldTask.setStartTime(Constant.MIN_DATE);
		oldTask.setEndTime(format.parse("16/03/2016 17:30"));
		oldTask.setLocation("IVLE");
		oldTask.setDescription("notes");
		oldTask.setCategory(Constant.CATEGORY_DEADLINE);
		
		expected.setName("that thing");
		expected.setEndTime(format.parse("25/03/2016 11:00"));
		expected.setStartTime(format.parse("24/03/2016 17:30"));
		expected.setCategory(Constant.CATEGORY_EVENT);
		expected.setStatus(main.Event.Status.INCOMPLETE);
		expected.setDescription("notes");
		expected.setLocation("IVLE");
		
		assertEquals(expected.getName(), newTask.getName());
		assertEquals(expected.getStartTime(), newTask.getStartTime());
		assertEquals(expected.getEndTime(), newTask.getEndTime());
		assertEquals(expected.getCategory(), newTask.getCategory());
		assertEquals(expected.getLocation(), newTask.getLocation());
		assertEquals(expected.getDescription(), newTask.getDescription());
		assertEquals(expected.getStatus(), newTask.getStatus());

	}*/

	/*
	@Test
	public void testDelete() throws ParseException{
		Parser parse = new Parser();
		Event task = new Event();
		Event expected = new Event();
		
		task = parse.testingParseCommand("delete something from thurs 1 pm to fri 2 pm");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		List<Integer> selection = new ArrayList<>();

		expected.setName("");
		expected.setStartTime(format.parse("24/03/2016 13:00"));
		expected.setEndTime(format.parse("25/03/2016 14:00"));
		expected.setCategory(null);
		expected.setStatus(Event.Status.NULL);
		expected.setDescription("");
		expected.setLocation("");
		expected.setSelection(selection);
		assertEquals(expected.getName(), task.getName());
		assertEquals(expected.getStartTime(), task.getStartTime());
		assertEquals(expected.getEndTime(), task.getEndTime());
		assertEquals(expected.getCategory(), task.getCategory());
		assertEquals(expected.getLocation(), task.getLocation());
		assertEquals(expected.getDescription(), task.getDescription());
		assertEquals(expected.getStatus(), task.getStatus());
		assertEquals(expected.getSelection(),task.getSelection());
	}
	*/
	/*
	@Test
	public void testUndetermined(){
		Parser parse = new Parser();
		Event expected = new Event();
		Event task = new Event();
		
		task = parse.testingParseCommand("block whatever on weds at JE ");
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		List<Integer> selection = new ArrayList<>();

		expected.setName("");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(Constant.MAX_DATE);
		expected.setCategory(Constant.CATEGORY_FLOATING);
		expected.setStatus(main.Event.Status.INCOMPLETE);
		expected.setDescription("");
		expected.setLocation("IVLE");
		expected.setSelection(selection);
	}
	*/
	/*
	@Test
	public void testSelect() throws ParseException{
		Parser parse = new Parser();
		Event selected = new Event();
		Event expected = new Event();
		
		//selected = parse.testingParseCommand("select something on thurs");
		
		selected = parse.testingParseCommand("select -- uishdiashdaj");
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		List<Integer> selection = new ArrayList<>();
	
		expected.setName("something");
		expected.setStartTime(format.parse("24/3/2016 00:00"));
		expected.setEndTime(format.parse("24/3/2016 23:59"));
		expected.setCategory(Constant.CATEGORY_EVENT);
		expected.setStatus(main.Event.Status.INCOMPLETE);
		expected.setDescription("");
		expected.setLocation("");
		expected.setSelection(selection);
		assertEquals(expected.getName(), selected.getName());
		assertEquals(expected.getStartTime(), selected.getStartTime());
		assertEquals(expected.getEndTime(), selected.getEndTime());
		assertEquals(expected.getCategory(), selected.getCategory());
		assertEquals(expected.getLocation(), selected.getLocation());
		assertEquals(expected.getDescription(), selected.getDescription());
		assertEquals(expected.getStatus(), selected.getStatus());
		assertEquals(expected.getSelection(),selected.getSelection());
	}
	*/
}
