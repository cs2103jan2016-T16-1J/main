package constant;

import java.sql.Date;

import main.Event.Category;
import main.Event.Status;

public class Constant {
	public static final String EMPTY_STRING = "";
	public static final Date MIN_DATE = new Date(0);
	public static final Date MAX_DATE = new Date(Long.MAX_VALUE);
	
	public static final String CATEGORY_EVENT = "EVENT";
	public static final String CATEGORY_DEADLINE = "DEADLINE";
	public static final String CATEGORY_FLOATING = "FLOATING";
	public static final String CATEGORY_UNDETERMINED = "UNDETERMINED";
	public static final Category CATEGORY_NULL = Category.NULL;
	
	public static final String STATUS_INCOMPLETE = "EVENT";
	public static final String STATUS_COMPLETE = "DEADLINE";
	public static final String STATUS_FLOATING = "FLOATING";
	public static final String STATUS_BLOCKED = "BLOCKED";
	public static final String STATUS_OVERDUE = "OVERDUE";
	public static final Status STATUS_NULL = Status.NULL;
}
