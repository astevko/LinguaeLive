/**
 * 
 */
package ca.jhosek.main.server.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.*;

import ca.jhosek.main.shared.proxy.MemberProxy;

/**
 * a User that is a Member of a Course
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see MemberProxy
 * @see MemberDao
 */
@Entity
public class Member  extends DatastoreObject implements Serializable {

//	private static final Logger logger = Logger.getLogger(Member.class.getName());
	/**
	 * 
	 */
	private static final long serialVersionUID = -2398416359335439934L;

	@Ignore
	private transient Course course;
	/**
	 * the user for this contact info
	 */
	@Index
	private Key<Course> courseKey;
	
	/**
	 * the user that is a member of this  
	 */
	@Ignore
	private transient User user;
	@Index
	private Key<User> userKey;
		
	/**
	 * is this student available for new sessions?
	 */
	@Index
	private Boolean available;
	
	private Long totalSessionMinutes = 0L;

	private Long targetSessionMinutes = 0L;

	private Long expertSessionMinutes = 0L;
	
	private Long totalSessions = 0L;

	private Long targetSessions = 0L;

	private Long expertSessions = 0L;
	
	private Set<Integer> availableHourOfWeek = new HashSet<Integer>( );	
	/**
	 * default used to serialize
	 */
	public Member() {
		//   		
	}
	/**
	 * @param userKey
	 * @param courseKey
	 */
	public Member( User user, Course course ) {
		this.userKey = Key.create( User.class, user.getId() );
		this.courseKey = Key.create( Course.class, course.getId() );
		available = true;		
	}
	/**
	 * @return the available
	 */
	public Boolean getAvailable() {
		return available;
	}
	/**
	 * @return the course
	 * @throws EntityNotFoundException 
	 */
	public Course getCourse() throws EntityNotFoundException {
		return new CourseDao().get(courseKey);
	}
	/**
	 * @return the courseKey
	 */
	public Key<Course> getCourseKey() {
		return courseKey;
	}
	/**
	 * @return user member
	 * @throws EntityNotFoundException 
	 */
	public User getUser() throws EntityNotFoundException {
		return new UserDao().get(userKey);
	}
	/**
	 * @return the userKey
	 */
	public Key<User> getUserKey() {
		return userKey;
	}
	/**
	 * @param available the available to set
	 */
	public void setAvailable(Boolean available) {
		this.available = available;
	}
	/**
	 * @param course the course to set
	 */
	public void setCourse(Course course) {
		this.courseKey = new CourseDao().key(course); 
	}
	/**
	 * @param courseKey the courseKey to set
	 */
	public void setCourseKey(Key<Course> courseKey) {
		this.courseKey = courseKey;
	}
	/**
	 * @param user
	 */
	public void setUser(User user) {
		this.userKey = new UserDao().key(user);
	}
	/**
	 * @param userKey the userKey to set
	 */
	public void setUserKey(Key<User> userKey) {
		this.userKey = userKey;
	}
	/**
	 * @param languageType 
	 * @param addSessionMinutes
	 */
	public void addSessionMinutes( boolean isTargetLanguage, Long addSessionMinutes) {
		//========
		this.totalSessionMinutes += addSessionMinutes ;
		this.totalSessions++;
		//========
		if ( isTargetLanguage ){
			this.targetSessionMinutes += addSessionMinutes;
			this.targetSessions++;
		} else {
			this.expertSessionMinutes += addSessionMinutes;
			this.expertSessions++;
		}
	}
	/**
	 * @param totalSessionMinutes the totalSessionMinutes to set
	 */
	public void setTotalSessionMinutes(Long totalSessionMinutes) {
		this.totalSessionMinutes = totalSessionMinutes;
	}
	/**
	 * @return the totalSessionMinutes
	 */
	public Long getTotalSessionMinutes() {
		return totalSessionMinutes;
	}
	/**
	 * @param schedule the schedule to set
	 */
	public void setAvailableHoursOfWeek(Set<Integer> schedule) {
		this.availableHourOfWeek = schedule;
	}
	/**
	 * @return the schedule
	 */
	public Set<Integer> getAvailableHoursOfWeek() {
		if ( availableHourOfWeek == null  ) {
			availableHourOfWeek = new HashSet<Integer>();
		}
		return availableHourOfWeek;
	}
	/**
	 * @return the number of hours available
	 */
	public Integer getScheduleSize() {
		return availableHourOfWeek.size();
	}
	/**
	 * @param targetSessionMinutes the targetSessionMinutes to set
	 */
	public void setTargetSessionMinutes(Long targetSessionMinutes) {
		this.targetSessionMinutes = targetSessionMinutes;
	}
	/**
	 * @return the targetSessionMinutes
	 */
	public Long getTargetSessionMinutes() {
		return targetSessionMinutes;
	}
	/**
	 * @param expertSessionMinutes the exportSessionMinutes to set
	 */
	public void setExpertSessionMinutes(Long expertSessionMinutes) {
		this.expertSessionMinutes = expertSessionMinutes;
	}
	/**
	 * @return the exportSessionMinutes
	 */
	public Long getExpertSessionMinutes() {
		return expertSessionMinutes;
	}
	/**
	 * @param totalSessions the totalSessions to set
	 */
	public void setTotalSessions(Long totalSessions) {
		this.totalSessions = totalSessions;
	}
	/**
	 * @return the totalSessions
	 */
	public Long getTotalSessions() {
		return totalSessions;
	}
	/**
	 * @param targetSessions the targetSessions to set
	 */
	public void setTargetSessions(Long targetSessions) {
		this.targetSessions = targetSessions;
	}
	/**
	 * @return the targetSessions
	 */
	public Long getTargetSessions() {
		return targetSessions;
	}
	/**
	 * @param expertSessions the expertSessions to set
	 */
	public void setExpertSessions(Long expertSessions) {
		this.expertSessions = expertSessions;
	}
	/**
	 * @return the expertSessions
	 */
	public Long getExpertSessions() {
		return expertSessions;
	}
}
