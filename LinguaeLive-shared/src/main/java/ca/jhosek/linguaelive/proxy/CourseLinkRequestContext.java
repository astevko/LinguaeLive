/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.proxy;


import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

// import ca.jhosek.linguaelive.domain.CourseLinkDao;
// import ca.jhosek.linguaelive.domain.DaoServiceLocator;

/**
 * Request Service Stub
 * referenced by RequestFactory
 * This is the service for serving up CourseLink objects
 *
 * @see CourseLinkDao
 * @link http://code.google.com/p/google-web-toolkit/wiki/RequestFactoryInterfaceValidation
 */
@ServiceName( value = "ca.jhosek.linguaelive.domain.CourseLinkDao", locator = "ca.jhosek.linguaelive.domain.DaoServiceLocator" )
public interface CourseLinkRequestContext extends RequestContext {

    Request<List<CourseLinkProxy>> listAll();
    Request<Void> persist(CourseLinkProxy course);
    Request<CourseLinkProxy> saveAndReturn(CourseLinkProxy newCourseLink);
    Request<Void> remove(CourseLinkProxy course);
	Request<CourseLinkProxy> findCourseLink( Long id );
	/** 
	 * @param userId
	 * @return a list of courseLink objects with pending == true and courseB.owner.id == userId 
	 */
	Request<List<CourseLinkProxy>> listPendingLinkedCourses( UserProxy user );
	/**
	 * @param userId
	 * @return a list of courseLink objects with pending == true and courseA.owner.id == userId 
	 */
	Request<List<CourseLinkProxy>> listOpenLinkedCourses( UserProxy user );
	
	Request<Void> deleteLinkedCourses(Long courseId);
}
