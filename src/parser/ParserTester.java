package parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTester {

	@Test
	public void testAddCommand() {
		Parser parse = new Parser();
		Task task = parse.parseCommand("add watching movies");
		Task expected = new Task();
		expected.setType(CommandType.ADD);
		expected.setName("watching movies");
		expected.setPlace(Constant.EMPTY_LOCATION);
		expected.setNote(Constant.EMPTY_NOTE);
		expected.setStartTime(Constant.MIN_DATE);
		expected.setEndTime(Constant.MAX_DATE);
		assertEquals(expected.getType(), task.getType());
		assertEquals(expected.getName(), task.getName());
		assertEquals(expected.getPlace(), task.getPlace());
		assertEquals(expected.getNote(), task.getNote());
		assertEquals(expected.getStartTime(), task.getStartTime());
		assertEquals(expected.getEndTime(), task.getEndTime())	}

}
