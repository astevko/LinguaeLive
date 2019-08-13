package ca.jhosek.linguaelive.proxy;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyForName;

// import ca.jhosek.linguaelive.domain.CourseLink;
// import ca.jhosek.linguaelive.domain.CourseLinkDao;
// import ca.jhosek.linguaelive.domain.ObjectifyLocator;

/**
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see CourseLink
 *  CourseLinkDao
 */
@ProxyForName( value="ca.jhosek.linguaelive.domain.CourseLink", locator="ca.jhosek.linguaelive.domain.ObjectifyLocator" )
public interface CourseLinkProxy extends DatastoreObjectProxy {

	public Boolean getAccepted();

	public Boolean getPending();

	public CourseProxy getCourseA();

	public CourseProxy getCourseB();

	public Date getCreateDate();

	public void setAccepted(Boolean accepted);

	public void setPending(Boolean pendingStatus);

	public void setCourseA( CourseProxy courseA);

	public void setCourseB( CourseProxy courseB);

	public void setCreateDate(Date createDate);
	
}
