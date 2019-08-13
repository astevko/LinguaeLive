/**
 * 
 */
package ca.jhosek.linguaelive.proxy;

import java.util.Set;

import com.google.web.bindery.requestfactory.shared.ProxyForName;

// import ca.jhosek.linguaelive.domain.Member;
// import ca.jhosek.linguaelive.domain.ObjectifyLocator;

/**
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see Member
 */
@ProxyForName( value="ca.jhosek.linguaelive.domain.Member", locator="ca.jhosek.linguaelive.domain.ObjectifyLocator")
public interface MemberProxy extends DatastoreObjectProxy {

	/**
	 * @return the available
	 */
	public Boolean getAvailable();
	/**
	 * @return the courseKey
	 */
	public CourseProxy getCourse();
	/**
	 * @return the userKey
	 */
	public UserProxy getUser();
	/**
	 * @param available the available to set
	 */
	public void setAvailable(Boolean available);
	/**
	 * @param courseKey the courseKey to set
	 */
	public void setCourse( CourseProxy course );

	/**
	 * @param userKey the userKey to set
	 */
	public void setUser( UserProxy user );

	/**
	 * @param totalSessionMinutes the totalSessionMinutes to set
	 */
	public void setTotalSessionMinutes(Long totalSessionMinutes);
	/**
	 * @return the totalSessionMinutes
	 */
	public Long getTotalSessionMinutes();
	public Set<Integer> getAvailableHoursOfWeek();
	public void  setAvailableHoursOfWeek( Set<Integer> schedule );

	/**
	 * @return the number of hours scheduled for the user
	 */
	public Integer getScheduleSize();

	/**
	 * @param targetSessionMinutes the targetSessionMinutes to set
	 */
	public void setTargetSessionMinutes(Long targetSessionMinutes);
	/**
	 * @return the targetSessionMinutes
	 */
	public Long getTargetSessionMinutes();
	/**
	 * @param expertSessionMinutes the expertSessionMinutes to set
	 */
	public void setExpertSessionMinutes(Long exportSessionMinutes);
	/**
	 * @return the expertSessionMinutes
	 */
	public Long getExpertSessionMinutes();

	/**
	 * @param totalSessions the totalSessions to set
	 */
	public void setTotalSessions(Long totalSessions);
	/**
	 * @return the totalSessions
	 */
	public Long getTotalSessions();
	/**
	 * @param targetSessions the targetSessions to set
	 */
	public void setTargetSessions(Long targetSessions);
	/**
	 * @return the targetSessions
	 */
	public Long getTargetSessions();
	/**
	 * @param expertSessions the expertSessions to set
	 */
	public void setExpertSessions(Long expertSessions);
	/**
	 * @return the expertSessions
	 */
	public Long getExpertSessions();
	
}
