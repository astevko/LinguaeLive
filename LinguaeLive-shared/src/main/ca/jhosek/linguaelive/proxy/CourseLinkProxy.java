package ca.jhosek.main.shared.proxy;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyFor;

import ca.jhosek.main.server.domain.CourseLink;
import ca.jhosek.main.server.domain.CourseLinkDao;
import ca.jhosek.main.server.domain.ObjectifyLocator;

/**
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see CourseLink
 * @see CourseLinkDao
 */
@ProxyFor( value=CourseLink.class, locator=ObjectifyLocator.class )
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
