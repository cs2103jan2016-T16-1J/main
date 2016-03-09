package parser;

import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;

import constant.Constant;
import main.Event;

public class TestParser {

	@Test
	public void testAdd() throws ParseException {
		Parser parse = new Parser();
		main.Event task = new Event();
		main.Event expected = new Event();
		
		task = parse.testingParseCommand("add watch dance on the moon on Sun at 11 am // don't forget your GF");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		expected.setName("watch dance on the moon");
		expected.setStartTime(format.parse("13/3/2016 11:00"));
		expected.setEndTime(format.parse("13/3/2016 11:00"));
		expected.setCategory(Constant.CATEGORY_EVENT);
		expected.setStatus(main.Event.Status.INCOMPLETE);
		expected.setDescription("don't forget your GF");
		expected.setLocation("");
		assertEquals(expected.getName(), task.getName());
		assertEquals(expected.getStartTime(), task.getStartTime());
		assertEquals(expected.getEndTime(), task.getEndTime());
		assertEquals(expected.getCategory(), task.getCategory());
		assertEquals(expected.getStatus(), task.getStatus());
		assertEquals(expected.getDescription(), task.getDescription());
		assertEquals(expected.getLocation(), task.getLocation());
	}
	
	@Test
	public void testSearch() throws ParseException{
		Parser parse = new Parser();
		main.Event task = new Event();
		main.Event expected = new Event();
		
		task = parse.testingParseCommand("display by Tues 5:30 pm");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		expected.setName("");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(format.parse("15/3/2016 17:30:00"));
		expected.setCategory(Constant.CATEGORY_DEADLINE);
		expected.setStatus(main.Event.Status.INCOMPLETE);

		assertEquals(expected.getName(), task.getName());
		assertEquals(expected.getStartTime(), task.getStartTime());
		assertEquals(expected.getEndTime(), task.getEndTime());
		assertEquals(expected.getCategory(), task.getCategory());
		assertEquals(expected.getStatus(), task.getStatus());
		
	}
	
	@Test
	public void testDelete() throws ParseException{
		Parser parser = new Parser();
		main.Event task = new Event();
		main.Event expected = new Event();
		
		task = parser.testingParseCommand("delete report submission");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		expected.setName("report submission");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(Constant.MAX_DATE);
		expected.setCategory(Constant.CATEGORY_FLOATING);
		expected.setStatus(main.Event.Status.INCOMPLETE);
		expected.setDescription("don't forget the references");
		//expected.setLocation("IVLE");
		assertEquals(expected.getName(), task.getName());
		assertEquals(expected.getStartTime(), task.getStartTime());
		assertEquals(expected.getEndTime(), task.getEndTime());
		assertEquals(expected.getCategory(), task.getCategory());
		assertEquals(expected.getStatus(), task.getStatus());
		//assertEquals(expected.getDescription(), task.getDescription());
		//assertEquals(expected.getLocation(), task.getLocation());
	}
	
	@Test
	public void testEdit() throws ParseException{
		Parser parser = new Parser();
		main.Event task = new Event();
		main.Event expected = new Event();
		main.Event oldTask = new Event();

		oldTask = parser.testingParseCommand("add submit thesis on Sunday at 11 pm");

		task = parser.testingParseCommand("edit on Sat by 10 pm at IVLE // reference");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		expected.setName("submit thesis");
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(format.parse("12/3/2016 22:00"));
		expected.setCategory(Constant.CATEGORY_DEADLINE);
		expected.setStatus(main.Event.Status.INCOMPLETE);
		expected.setDescription("reference");
		expected.setLocation("IVLE");
		
		assertEquals(expected.getName(), task.getName());
		assertEquals(expected.getStartTime(), task.getStartTime());
		assertEquals(expected.getEndTime(), task.getEndTime());
		assertEquals(expected.getCategory(), task.getCategory());
		assertEquals(expected.getStatus(), task.getStatus());
		assertEquals(expected.getDescription(), task.getDescription());
		assertEquals(expected.getLocation(), task.getLocation());
		
		assertEquals(expected.getName(), oldTask.getName());
		assertNotSame(expected.getStartTime(), oldTask.getStartTime());
		assertNotSame(expected.getEndTime(), oldTask.getEndTime());
		assertEquals(expected.getCategory(), oldTask.getCategory());
		assertEquals(expected.getStatus(), oldTask.getStatus());
		assertEquals(expected.getDescription(), oldTask.getDescription());
		assertEquals(expected.getLocation(), oldTask.getLocation());
	}
}
