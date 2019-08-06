package ca.jhosek.main.shared;

import java.io.Serializable;

/**
 * Days of the Week
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public enum DayOfWeekEnum implements Serializable {


	MON( 0, 	"Mon", "Monday"	),
	TUE( 1, 	"Tue", "Tuesday"	),
	WED( 2, 	"Wed", "Wednesday"	),
	THU( 3, 	"Thu", "Thursday"	),
	FRI( 4, 	"Fri", "Friday"	),
	SAT( 5, 	"Sat", "Saturday"	),
	SUN( 6,		"Sun", "Sunday"	);

	
	private final Integer dow;
	private final String  day;
	private final String  d;
	
	private DayOfWeekEnum( Integer dow, String d, String day ) {
		this.dow = dow;
		this.day = day;
		this.d = d;		
	}

	/**
	 * @return the dow
	 */
	public Integer getDow() {
		return dow;
	}

	/**
	 * @return the day
	 */
	public String getDay() {
		return day;
	}

	/**
	 * @return the d
	 */
	public String getD() {
		return d;
	}
		
}