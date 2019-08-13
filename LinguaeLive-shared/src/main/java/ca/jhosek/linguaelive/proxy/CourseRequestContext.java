/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.proxy;


import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

// import ca.jhosek.linguaelive.activity.mainregion.InstructorYourCourseActivity;
// import ca.jhosek.linguaelive.domain.CourseDao;
// import ca.jhosek.linguaelive.domain.DaoServiceLocator;
import ca.jhosek.linguaelive.LanguageType;

/**
 * Request Service Stub
 * referenced by RequestFactory
 * This is the service for serving up Course objects
 *
 * @see CourseDao
 * @see InstructorYourCourseActivity
 * 
 * @link http://code.google.com/p/google-web-toolkit/wiki/RequestFactoryInterfaceValidation
 */
@ServiceName( value = "ca.jhosek.linguaelive.domain.CourseDao", locator = "ca.jhosek.linguaelive.domain.DaoServiceLocator" )
public interface CourseRequestContext extends RequestContext {

    Request<List<CourseProxy>> listAll();
    Request<CourseProxy> persist(CourseProxy course);
    Request<CourseProxy> saveAndReturn(CourseProxy newCourse);
    Request<Void> remove(CourseProxy course);
	Request<CourseProxy> findCourse( Long id );
	Request<CourseProxy> findInviteCode( String inviteCode );
    
    Request<List<CourseProxy>> listMyCourses( UserProxy user );
    
    /**
     * used by the admin course browser to list courses with these filter criteria
     * 
     * @param languageTargetType
     * @param languageExpertType
     * @param withCourseLinks with course links/no course links
     * @param currentDates end after today/end before today
     * @return list of courses
     * 
     * @see CourseDao.listTheseCourses
     * 
     */
    Request<List<CourseProxy>> listTheseCourses( LanguageType languageTargetType, LanguageType languageExpertType, Boolean withCourseLinks, Boolean currentDates );
    
	Request<List<CourseLinkProxy>> listLinkedCourses( CourseProxy myCourse );
//	Request<List<CourseProxy>> searchComplimentaryCourses( CourseProxy myCourse );
	Request<CourseLinkProxy> requestToLinkedCourses( CourseProxy myCourse, CourseProxy otherCourse, String personalMessage );
//	Request<Void> confirmLinkedCourses( CourseProxy myCourse, CourseProxy otherCourse );
	/**
	 * @param myCourse course to match against
	 * @param showPastCourses show true == past courses; false = current
	 * @return list of comp. courses
	 * 
	 * @see CourseDao.listUnlinkedCourses
	 */
	Request<List<CourseProxy>> listUnlinkedCourses(CourseProxy myCourse, Boolean showPastCourses);
    
	Request<Void>		delete(CourseProxy course);
//	Request<Long> countUsers();
//	
//	Request<List<UserProxy>> findAllUsers();
//	
//	Request<UserProxy> findUser( Long id );
//	
//	Request<UserProxy> loginEmailAddress( String emailAddress, String pswd );
//	
//	Request<UserProxy> loginCookie( String emailAddress, String hash );
//	
//	Request<UserProxy> persist( UserProxy userProxy );
//
//	Request<UserProxy> update( UserProxy userProxy );
//	
//	Request<Void> remove( UserProxy userProxy );
//
//	/**
//	 * recover password 
//	 * 
//	 * @param emailAddress
//	 * @return
//	 */
//	Request<Void>  recoverPassword(String emailAddress);

}
