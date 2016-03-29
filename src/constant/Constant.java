package constant;

import java.sql.Date;

import main.GenericEvent;
import main.GenericEvent.Category;
import main.GenericEvent.Status;

public class Constant {
	public static final String EMPTY_STRING = "";
	public static final Date MIN_DATE = new Date(0);
	public static final Date MAX_DATE = new Date(Long.MAX_VALUE);
	
	public static final String CATEGORY_EVENT = "EVENT";
	public static final String CATEGORY_DEADLINE = "DEADLINE";
	public static final String CATEGORY_FLOATING = "FLOATING";
	public static final String CATEGORY_UNDETERMINED_EVENT = "UNDETERMINED_EVENT";
	public static final String CATEGORY_UNDETERMINED_DEADLINE = "UNDETERMINED_DEADLINE";
	public static final String CATEGORY_UNDETERMINED_FLOATING = "UNDETERMINED_FLOATING";
	
	public static final GenericEvent.Category CATEGORY_NULL = GenericEvent.Category.NULL;
	
	public static final String STATUS_INCOMPLETE = "EVENT";
	public static final String STATUS_COMPLETE = "DEADLINE";
	public static final String STATUS_UNDETERMINED = "UNDETERMINED";
	public static final GenericEvent.Status STATUS_NULL = GenericEvent.Status.NULL;
	
	public static final int TAB_UNDETERMINED = 1;
	public static final int TAB_COMPLETED = 2;
	public static final int TAB_INCOMPLETE = 3;
}
