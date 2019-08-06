package ca.jhosek.main.client.ui.priv.student;

import java.util.Set;
import java.util.logging.Logger;

import ca.jhosek.main.shared.DayOfWeekEnum;

/**
 * a row in the availability schedule
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class HourOfDay {
	private static final Logger logger = Logger.getLogger( HourOfDay.class.getName() );
	private static final Integer maxHourOfWeek = 7 * 24;
	
	// graph of total hours 24 * 7 
	private final Set<Integer> hoursAvailable;

	// Display
	private Integer hourOfDay;

	
	/**
	 * integer hour offset from UTC (plus or minus) 
	 */
	private Integer utcHourOffset;
	/**
	 * @param hoursAvailable
	 * @param hourOfDay
	 */
	public HourOfDay(Set<Integer> hoursAvailable, Integer hourOfDay, int utcHourOffset ) {
		logger.info("Schedule( " + hoursAvailable.size() + ", hour=" + hourOfDay.toString() );
		this.utcHourOffset = utcHourOffset;
		this.hoursAvailable = hoursAvailable;
		this.hourOfDay = hourOfDay;
	}

	/**
	 * toggle availability - 
	 * remove if found; 
	 * add if not;
	 * 
	 * @param day
	 */
	public boolean toggle( DayOfWeekEnum day ) {
		if( day == null ) {
			logger.info( "toggle( day==null ) ");
			return false;
		}
		logger.info( "toggle( " + day.name() + ", hour=" + getHourOfDay() + " )"	);
		// calc hour offset into week for day * hour
		Integer hourOfWeek = getHourOfWeek(day);
		if ( !hoursAvailable.isEmpty() ) {
			for( Integer h : hoursAvailable ) {
				if ( hourOfWeek.equals( h )) {
					// found!!
					hoursAvailable.remove(h);
					return false;
				}
			}
		}
		// not found
		hoursAvailable.add( hourOfWeek );
		return true;
	}
	
	/**
	 * @param day
	 * @return is this hour scheduled?
	 */
	public boolean isScheduled( DayOfWeekEnum day ) {
		if( day == null ) {
			logger.info( "isScheduled( day==null ) ");
			return false;
		}
		
		// calc hour offset into week for day * hour
		Integer hourOfWeek = getHourOfWeek(day);
		
		if ( !hoursAvailable.isEmpty() ) {
			for( Integer h : hoursAvailable ) {
				if ( hourOfWeek.equals( h )) {
					// found!!
					logger.info( "YES isScheduled( " + day.name() + ", hour=" + getHourOfDay() + " HoW:" + hourOfWeek 	);
					return true;
				}
			}
		}
		//-----------
		logger.info( "NO isScheduled( " + day.name() + ", hour=" + getHourOfDay() + " HoW:" + hourOfWeek 	);
		return false;
	}

	/**
	 * @param day
	 * @return
	 */
	Integer getHourOfWeek(DayOfWeekEnum day) {
		Integer how = new Integer( (day.getDow() * 24) + getHourOfDay() - utcHourOffset );
		if ( how > maxHourOfWeek ) {
			// overflow (sunday is monday)
			how = how - maxHourOfWeek;
			
		} else if ( how < 0 ) {
			// underflow  (monday is sunday)
			how = how + maxHourOfWeek;			
		}
		logger.info( "HourOfWeek for " + day.name() + " and " + getHourOfDay() + " is " + how.toString() );
		return how;
	}			

	/**
	 * @param hourOfDay the hourOfDay to set
	 */
	public void setHourOfDay(Integer hourOfDay) {
		this.hourOfDay = hourOfDay;
	}

	/**
	 * @return the hourOfDay
	 */
	public Integer getHourOfDay() {
		return hourOfDay;
	}

	/**
	 * @param dow
	 * @return paint the screen with this string
	 */
	public String getValue(DayOfWeekEnum dow) {
		if ( dow == null ) {
			
			return getHourOfDay().toString() + ":00";

		} else if (isScheduled( dow )) {
			// IS scheduled!
			return("\u2714");
			
		} else {
			// is NOT scheduled
			return "---";		    		  
		}
	}
}