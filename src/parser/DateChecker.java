package parser;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateChecker {

	private static final int INDEX_START = 0;
	private static final int INDEX_END = 1;

	private static final String TOM = "tom";
	private static final String TOD = "tod";
	private static final String MON = "mon";
	private static final String TUE = "tue";
	private static final String WED = "wed";
	private static final String THU = "thu";
	private static final String FRI = "fri";
	private static final String SAT = "sat";
	private static final String SUN = "sun";

	private static final int SUNDAY = 1;
	private static final int MONDAY = 2;
	private static final int TUESDAY = 3;
	private static final int WEDNESDAY = 4;
	private static final int THURSDAY = 5;
	private static final int FRIDAY = 6;
	private static final int SATURDAY = 7;
	private static final int DAYS_IN_WEEK = 7;

	private static ArrayList<SimpleDateFormat> supportedDateFormats;

	private static Calendar calendar;
	private static Date dateToday;
	private static int intToday;

	/**
	 * 
	 * @param stringDateInput
	 * @return the converted Date 
	 */
	public static Date validateDate(String stringDateInput){		
		String date = null;
		Date inputDate = null;
	
		Initialization();
		
		inputDate = parseInputDate(stringDateInput);

		if (inputDate == null) {
			inputDate = convertDayToDate(stringDateInput);
		}
		
		return inputDate;
	}

	private static Date parseInputDate(String stringDateInput){
		Date inputDate = null;
		for (SimpleDateFormat format : supportedDateFormats){
			format.setLenient(false);
			try {
				inputDate = format.parse(stringDateInput);
				break;
			} catch (ParseException e) {
				System.out.println(e.toString());
			}

		}
		return inputDate;
	}

	private static void Initialization(){
		calendar = Calendar.getInstance();
		dateToday = new Date();
		calendar.setTime(dateToday);
		intToday = calendar.get(Calendar.DAY_OF_WEEK);

		supportedDateFormats = new ArrayList<>();
		supportedDateFormats.add(new SimpleDateFormat("dd/MM/yy HH:mm"));		
		supportedDateFormats.add(new SimpleDateFormat("dd MMM yy HH:mm"));
		//supportedDateFormats.add(new SimpleDateFormat("HH:mm dd/MM/yyyy"));		
		//supportedDateFormats.add(new SimpleDateFormat("HH:mm dd MMM yyyy"));
		supportedDateFormats.add(new SimpleDateFormat("dd/MM/yy"));
		supportedDateFormats.add(new SimpleDateFormat("dd MMM yy"));
		supportedDateFormats.add(new SimpleDateFormat("hh:mm a"));
		supportedDateFormats.add(new SimpleDateFormat("hh a"));
	}


	private static Date convertDayToDate(String stringDateInput){
		int beginIndex = 0;
		int endIndex = 3;
		String dayAbbreviation = stringDateInput.substring(beginIndex, endIndex);

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
		String stringToday = dateFormat.format(dateToday);

		Date todayDate = null;
		try {
			todayDate = dateFormat.parse(stringToday);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (dayAbbreviation.equalsIgnoreCase(SUN)){
			int interval;
			if(intToday == SUNDAY){
				return todayDate;
			}else if (intToday < SUNDAY) {
				interval = SUNDAY - intToday;
				return findDate(interval);
			}else{
				interval = (SUNDAY + DAYS_IN_WEEK) - intToday;
				return findDate(interval);
			}

		}else if (dayAbbreviation.equalsIgnoreCase(MON)){
			int interval;
			if(intToday == MONDAY){
				return todayDate;
			}else if(intToday < MONDAY){
				interval = MONDAY - intToday;
				return findDate(interval);
			}else{
				interval = (MONDAY + DAYS_IN_WEEK) - intToday;
				return findDate(interval);
			}
		}else if (dayAbbreviation.equalsIgnoreCase(TUE)){
			int interval;
			if(intToday == TUESDAY){
				return todayDate;
			}else if(intToday < TUESDAY){
				interval = TUESDAY - intToday;
				return findDate(interval);
			}else{
				interval = (TUESDAY + DAYS_IN_WEEK) - intToday;
				return findDate(interval);
			}
		}else if (dayAbbreviation.equalsIgnoreCase(WED)){
			int interval;
			if(intToday == WEDNESDAY){
				return todayDate;
			}else if(intToday < WEDNESDAY){
				interval = WEDNESDAY - intToday;
				return findDate(interval);
			}else{
				interval = (WEDNESDAY + DAYS_IN_WEEK) - intToday;
				return findDate(interval);
			}
		}else if (dayAbbreviation.equalsIgnoreCase(THU)){
			int interval;
			if(intToday == THURSDAY){
				return todayDate;

			}else if(intToday < THURSDAY){
				interval = THURSDAY - intToday;
				return findDate(interval);
			}else{
				interval = (THURSDAY + DAYS_IN_WEEK) - intToday;
				return findDate(interval);
			}

		}else if (dayAbbreviation.equalsIgnoreCase(FRI)){
			int interval;
			if(intToday == FRIDAY){
				return todayDate;

			}else if(intToday < FRIDAY){
				interval = FRIDAY - intToday;
				return findDate(interval);
			}else{
				interval = (FRIDAY + DAYS_IN_WEEK) - intToday;
				return findDate(interval);
			}

		}else if (dayAbbreviation.equalsIgnoreCase(SAT)){
			int interval;
			if(intToday == SATURDAY){
				return todayDate;

			}else if(intToday < SATURDAY){
				interval = SATURDAY - intToday;
				return findDate(interval);
			}else{
				interval = (SATURDAY + DAYS_IN_WEEK) - intToday;
				return findDate(interval);
			}
		}
		todayDate = null;
		return todayDate;
	}

	private static Date findDate(int interval){
		calendar.add(Calendar.DATE, interval);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
		String newDate = dateFormat.format(calendar.getTime());
		return (parseInputDate(newDate));
	}
}
