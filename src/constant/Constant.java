package constant;

import java.sql.Date;

public class Constant {
	public static final String EMPTY_STRING = "";
	public static final Date MIN_DATE = new Date(0);
	public static final Date MAX_DATE = new Date(Long.MAX_VALUE);
	
	public static final String CATEGORY_EVENT = "EVENT";
	public static final String CATEGORY_DEADLINE = "DEADLINE";
	public static final String CATEGORY_FLOATING = "FLOATING";
	public static final String CATEGORY_UNDETERMINED = "UNDETERMINED";
	public static final String CATEGORY_NULL = null;
}
