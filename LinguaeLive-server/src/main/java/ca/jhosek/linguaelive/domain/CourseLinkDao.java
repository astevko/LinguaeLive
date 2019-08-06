/**
 * 
 */
package ca.jhosek.main.server.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import ca.jhosek.main.server.email.LinkCourseAcceptEmail;
import ca.jhosek.main.server.email.LinkCourseInviteEmail;
import ca.jhosek.main.shared.proxy.CourseLinkRequestContext;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * DAO for CourseLinks
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see LinkCourseInviteEmail
 * @see CourseLinkRequestContext
 */
public class CourseLinkDao extends ObjectifyGenericDao<CourseLink> {


	private static final Logger logger = Logger.getLogger(CourseLinkDao.class.getName());
		

    public List<CourseLink> listAll()
    {
            Query<CourseLink> q = ofy().load().type(clazz);
            return q.list();
    }
    
     
    /**
     * save this courselink
     * note - courselink created in CourseDao
     * 
     * @param entity
     */
    public void persist( CourseLink entity ) {
    	logger.info( "persist() putting CourseLink id = " + entity.getId());
//    	boolean newEntity = (entity.getId() == null);
    	this.put(entity);
//    	if (newEntity) {
//    		try {
//				new LinkCourseInviteEmail(entity).send();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				logger.warning("failed to send courselink email" );
//			}
//    	}
//    	
    }
    
	public CourseLink findCourseLink( Long id ){
		try {
			return this.get(id);			
		} catch( EntityNotFoundException e ) {
			return null;
		}
	}
    
    public CourseLink saveAndReturn( CourseLink newEntity ) throws EntityNotFoundException {
    	Key<CourseLink> newKey = this.put(newEntity);
    	
    	//send accept/decline email to requestor
    	//this is where I need to decide which email to send
		try {
			new LinkCourseAcceptEmail( newEntity ).send();
			
			
		} catch (EntityNotFoundException e) {
			// 
			logger.severe("failed to send link course email" + e.getMessage() );
		}
    	
    	
    	return this.get(newKey);
    }
    
    public void remove(CourseLink entity){
    	this.delete(entity);
    }

    /**
     * 
     * @param courseId
     * @return a list of courses linked to this course
     */
    public List<Course> getAllLinkedCourses( Long courseId ) {
    	logger.info( "getLinkedCourses( course id= " + courseId );
    	Key<Course> courseKey = Key.create( Course.class, courseId );
    	List<Course> linkedCourses = new ArrayList<Course>();
    	List<CourseLink> linkedA = ofy().load().type( CourseLink.class ).filter( "courseAKey =", courseKey ).list();
    	for( CourseLink link : linkedA ) {
    		try {
				linkedCourses.add( link.getCourseB() );
			} catch (EntityNotFoundException e) {
				// 
				logger.severe("bad course key in CourseLink id=" + courseKey.getId() );
			}
    	}
    		
    	List<CourseLink> linkedB = ofy().load().type( CourseLink.class ).filter( "courseBKey =", courseKey ).list();
    	for( CourseLink link : linkedB ) {
    		try {
				linkedCourses.add( link.getCourseA() );
			} catch (EntityNotFoundException e) {
				// 
				logger.severe("bad course key in CourseLink id=" + courseId );
			}
    	}
    	logger.info( "found " + linkedCourses.size() + " courses linked to Course id=" + courseId );
    	return linkedCourses;
    }

    /**
     * 
     * @param courseId
     * @return a list of courses linked to this course
     */
    public List<Course> getAcceptedLinkedCourses( Long courseId ) {
    	logger.info( "getLinkedCourses( course id= " + courseId );
    	Key<Course> courseKey = Key.create( Course.class, courseId );
    	List<Course> linkedCourses = new ArrayList<Course>();
    	List<CourseLink> linkedA = ofy().load().type( CourseLink.class ).filter("accepted =", true).filter( "courseAKey =", courseKey ).list();
    	for( CourseLink link : linkedA ) {
    		try {
				linkedCourses.add( link.getCourseB() );
			} catch (EntityNotFoundException e) {
				// 
				logger.severe("bad course key in CourseLink id=" + courseKey.getId() );
			}
    	}
    		
    	List<CourseLink> linkedB = ofy().load().type( CourseLink.class ).filter("accepted =", true).filter( "courseBKey =", courseKey ).list();
    	for( CourseLink link : linkedB ) {
    		try {
				linkedCourses.add( link.getCourseA() );
			} catch (EntityNotFoundException e) {
				// 
				logger.severe("bad course key in CourseLink id=" + courseId );
			}
    	}
    	logger.info( "found " + linkedCourses.size() + " courses linked to Course id=" + courseId );
    	return linkedCourses;
    }

	/** 
	 * These are pending invitations sent to this User.
	 * 
	 * @param user
	 * @return a list of courseLink objects with pending == true and courseA.owner.id == userId 
	 */
	public List<CourseLink> listPendingLinkedCourses( User user ){
    	logger.info( "listPendingLinkedCourses( user id= " + user.getId() );
		List<CourseLink> pending = new ArrayList<CourseLink>();
		
		// find all courses owned by userId
		CourseDao courseDao = new CourseDao();
		List<Course> courses = courseDao.listMyCourses(user);
		
		// find all pending courseLinks for courses
		for (Course c : courses ) {
			pending.addAll( ofy().load().type( CourseLink.class )
					.filter("pending =", true)
					.filter( "courseAKey =", courseDao.key( c ) ).list() );
		}
		return pending;
	}

	/**
	 * These are Pending Invitations sent to this User.
	 *  
	 * @param user
	 * @return a list of courseLink objects with pending == true and courseB.owner.id == userId 
	 */
	public List<CourseLink> listOpenLinkedCourses(  User user ){
    	logger.info( "listOpenLinkedCourses( user id= " + user.getId() );
		List<CourseLink> open = new ArrayList<CourseLink>();
		
		// find all courses owned by userId
		CourseDao courseDao = new CourseDao();
		List<Course> courses = courseDao.listMyCourses(user);
		
		// find all pending courseLinks for courses
		for (Course c : courses ) {
			open.addAll( ofy().load().type( CourseLink.class )
					.filter("pending =", true)
					.filter( "courseBKey =", courseDao.key( c ) ).list() );
		}
		return open;
		
	}
    
   /**
     * delete linked courses
     * @param courseId
     */
    public void deleteLinkedCourses( Long courseId ) {
    	logger.info( "deleteLinkedCourses( course id= " + courseId );
    	Key<Course> courseKey = Key.create( Course.class, courseId );
    	ofy().delete().entities( ofy().load().type( CourseLink.class ).filter( "courseAKey =", courseKey ).list()).now();
    	ofy().delete().entities( ofy().load().type( CourseLink.class ).filter( "courseBKey =", courseKey ).list()).now();
    	logger.info( "deleteLinkedCourses complete" );
    }
}
