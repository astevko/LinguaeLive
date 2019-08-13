/**
 * 
 */
package ca.jhosek.linguaelive.domain;

import java.io.Serializable;
import java.util.Date;

// import ca.jhosek.linguaelive.proxy.CourseLinkProxy;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

/**
 * Two courses that are linked together
 * w/ invite mechanism
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see CourseLinkDao
 * @see CourseLinkProxy
 */
@Entity
public class CourseLink  extends DatastoreObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2398416359335439934L;

	private Date		createDate;

	@Ignore
	transient private Course courseA;
	@Index
	private Key<Course> courseAKey;

	@Ignore
	transient private Course courseB;
	@Index
	private Key<Course> courseBKey;

	// invite responded too
	@Index
	private Boolean		pending = true;
	
	// invite accepted or rejected
	@Index
	private Boolean		accepted  = false;

	/**
	 * default used to serialize
	 */
	public CourseLink() {
		//
		createDate = new Date();
	}

	/**
	 * default used to serialize
	 */
	public CourseLink( Course a, Course b ) {
		// 
		createDate = new Date();
		courseAKey = Key.create( Course.class, a.getId() );
		courseBKey = Key.create( Course.class, b.getId() );		
	}

	public Boolean getAccepted() {
		return accepted;
	}

	public Course getCourseA() throws EntityNotFoundException {
		return new CourseDao().get( courseAKey );
	}

	public Course getCourseB() throws EntityNotFoundException {
		return new CourseDao().get( courseBKey );
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}

	public void setCourseA(Course courseA) {
		this.courseAKey = Key.create( Course.class, courseA.getId() );;
	}

	public void setCourseB(Course courseB) {
		this.courseBKey = Key.create( Course.class, courseB.getId() );;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @param pending invite
	 */
	public void setPending(Boolean pending) {
		this.pending = pending;
	}

	/**
	 * @return the responded
	 */
	public Boolean getPending() {
		return pending;
	}
	
}
