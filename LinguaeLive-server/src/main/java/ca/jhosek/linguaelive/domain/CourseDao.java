/**
 * 
 */
package ca.jhosek.linguaelive.domain;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import ca.jhosek.linguaelive.email.LinkCourseInviteEmail;
import ca.jhosek.linguaelive.email.NewCourseEmail;
import ca.jhosek.linguaelive.LanguageType;
import ca.jhosek.linguaelive.UserType;

// import ca.jhosek.linguaelive.proxy.CourseRequestContext;

/**
 * Course represents a class
 * CourseLinks are partnered classes
 * Members are students registered for this course
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see Course
 * @see CourseLocator
 *  CourseRequestContext
 * 
 * @see NewCourseEmail
 * 
 */
public class CourseDao extends ObjectifyGenericDao<Course> {

	@Override
	public void delete(Course course) {
		// TODO -  BLOCK DELETE FOR COURSES W/ SESSIONS/MEMBERS
		
		// delete all course links		
		final CourseLinkDao courseLinkDao = new CourseLinkDao();
		courseLinkDao.deleteLinkedCourses( course.getId());
		
		// delete all Members & session invites & sessons
		final MemberDao memberDao = new MemberDao();
		memberDao.deleteCourseMembers(course);
		
		// delete this course
		super.delete(course);
	}
	
	private static final Logger logger = Logger.getLogger(CourseDao.class.getName());
		

    public List<Course> listAll()
    {
    	final Query<Course> q = ofy().load().type(clazz);
    	return q.list();
    }
    
     
    /**
     * make this course entity persistent
     * 
     * @param course
     */
    public Course persist( Course course ) {
    	logger.info( "persist() putting course id = " + course.getId() + " w/ ownerId=" + course.getOwnerKey() );
    	final boolean isNewEntity = (course.getId() == null);
    	
//    	if ( newEntity ) {
//        	// set the invite code as a unique string by converting the time to a radix 32 string 
//    		Date now = new Date();
//    		course.setInviteCode( Long.toString(now.getTime(), 32 ) );
//    	}
    	
    	course.setId(this.put(course).getId());
    	
    	if( isNewEntity ) {
    		new NewCourseEmail( course ).send();
    	}
    	// return course
    	return course;
    }
    
	/**
	 * @param id
	 * @return a course
	 */
	public Course findCourse( Long id ){
		try {
			return this.get(id);			
		} catch( EntityNotFoundException e ) {
			return null;
		}
	}
    
	/**
	 * find a course by invite code
	 * used by new student form
	 * 
	 * @param inviteCode
	 * @return a course or null
	 */
	public Course findInviteCode( String inviteCode ){
		return ofy().load().type( Course.class ).filter( "inviteCode", inviteCode ).first().now();
	}
    
    public Course saveAndReturn( Course newEntity ) throws EntityNotFoundException {
    	final boolean isNewEntity = (newEntity.getId() == null);
    	
    	final Key<Course> newKey = this.put(newEntity);
    	
    	final Course course = this.get(newKey);
    	if( isNewEntity ) {
    		new NewCourseEmail( course ).send();
    	}
     	
    	return course;
    }
    
    public void remove(Course entity){
    	this.delete(entity);
    }
    
	/**
	 * @param myCourse
	 * @return list of courses linked to mine
	 */
	public List<CourseLink> listLinkedCourses( Course myCourse ){
		logger.info("listLinkedCourses( " + myCourse.getName() + " ) " );
		// accumulate course keys
		final List<CourseLink> courseLinks = new ArrayList<CourseLink>();
		// show member courses A
		final Query<CourseLink> qA = ofy().load().type( CourseLink.class ).filter( "courseAKey =", myCourse );
		courseLinks.addAll( qA.list() );        
		logger.info("listLinkedCourses() found  " + qA.list().size() + " ) CourseLinks for A" );
		
        
		// show member courses B
		final Query<CourseLink> qB = ofy().load().type( CourseLink.class ).filter( "courseBKey =", myCourse );
		courseLinks.addAll( qB.list());
		logger.info("listLinkedCourses() found  " + qB.list().size() + " ) CourseLinks for B" );
        
        return courseLinks;
	}

   /**
     * get courses for id
     * uses Member.class to map User to Courses
     *  
     * @param userId
     * @return Collection<Courses> myCourses
     */
    public List<Course> listMyCourses(final User user)
    {		
    	logger.info("listMyCourses(user id =" + user.getId() + " )" );
    	// show owned courses
    	if ( user.getUserType() == UserType.INSTRUCTOR || user.getUserType() == UserType.ADMIN ) {

    		// sort courses for this ownerKey by endDate descending
    		final Query<Course> q = ofy().load().type( Course.class ).filter( "ownerKey", user ).order("-endDate");

    		final List<Course> myList = q.list();
    		logger.info( myList.size() +  "courses for instructor user id =" + user.getId() );
    		return myList;

    	} else {
    		// show member courses
    		final Query<Member> q = ofy().load().type( Member.class ).filter( "userKey", user );
    		final List<Key<Course>> courseKeys = new ArrayList<Key<Course>>();
    		for( Member m : q ) {
    			courseKeys.add( m.getCourseKey() );
    		}
    		return fetchCourseKeys(courseKeys);
    	}
    }
    
	
	/**
	 * fetch these courses identified by key
	 * @param courseKeys
	 * @return a list of courses
	 */
	private List<Course> fetchCourseKeys(final List<Key<Course>> courseKeys) {
		logger.info("fetchCourseKeys() size = " + courseKeys.size() );
		final Map<Key<Course>, Course> fetched = ofy().load().keys(courseKeys);
    	final List<Course> linkedCourses = new ArrayList<Course>( fetched.values() );
		return linkedCourses;
	}
	
	/**
	 * Searching for complimentary courses that have overlapping dates
	 * Possibly add filter for minimum overlapping duration
	 * 
	 * @param myCourse
	 * @param showPastCourses show only past courses rather than current
	 * @return list of courses with complimentary languages and time
	 */
	public List<Course> listUnlinkedCourses(final Course myCourse, Boolean showPastCourses ){
		logger.info("listUnlinkedCourses() for " + myCourse.getExpertLanguage() + "/" + myCourse.getTargetLanguage() );
		// accumulate course keys
    	final List<Course> courses = new ArrayList<Course>();
    	
    	// search for complimentary languages
		final Query<Course> q = ofy().load().type( Course.class )
			.filter( "targetLanguage", myCourse.getExpertLanguage() )
			.filter( "expertLanguage", myCourse.getTargetLanguage() )
			.order("-endDate")
			;

		// myCourse start & end
		final Date now = new Date();
		Date s1 = myCourse.getStartDate();
		if (s1.before(now)) {
			// override start to today for courses already started
			// so that previously started & ended courses do not match
			s1 = now;
		}
		final Date e1 = myCourse.getEndDate();
		
        // search for overlapping time frames 
		final List<Course> compSources = q.list();
		for (Course compCourse : compSources ) {
			if (s1.before(compCourse.getEndDate()) 
				&& e1.after(compCourse.getStartDate())) 
			{
				// course overlaps
				if (!showPastCourses) {
					courses.add(compCourse);
				}
				
				// TODO - add overlapping duration calculations if necessary
				// if ( s1 > s & e < e1 )  duration = e - s1
				// if ( s1 < s & e > e1 )  duration = e1 - s
				// if ( s1 > s & e > e1 )  duration = e1 - s1
				// if ( s1 < s & e < e1 )  duration = e - s
			} else if (showPastCourses) {
				// no overlap & showing past course
				courses.add(compCourse);
			}
		}
		logger.info( compSources.size() + " matching languages courses found, " + courses.size() + " eligible" );
		
		// return 
        return courses;
	}
	
	/**
	 * create a new linked course
	 * after checking for duplicates
	 * and checking/confirming for outstanding invite  
	 * 
	 * @param myCourse
	 * @param otherCourse
	 */
	public CourseLink requestToLinkedCourses( final Course myCourse, final Course otherCourse, final String personalMessage ){
		CourseLink courseLink;
		// duplicate request checker 
		// TODO CHECK FILTER FOR KEY
		final Query<CourseLink> duplicate = ofy().load().type( CourseLink.class).filter("courseA", myCourse ).filter("courseB", otherCourse);
		if ( duplicate.list().size() > 0 ) {
			// course already linked
			logger.info("course already linked" );
			return duplicate.list().get(0);
			
		} else {
			// confirm request checker 
			// TODO CHECK FILTER FOR KEY
			final Query<CourseLink> confirm = ofy().load().type( CourseLink.class).filter("courseB", myCourse ).filter("courseA", otherCourse);
			if ( confirm.list().size() > 0 ) {
				logger.info( "confirming existing link request" );
				courseLink = confirm.first().now();
				courseLink .setAccepted(true);
				courseLink .setPending(false);
				ofy().save().entity(courseLink).now();
				
			} else {
				logger.info("linking courses" );
				courseLink = new CourseLink(myCourse, otherCourse);
				ofy().save().entity(courseLink).now();

				// send email to otherCourse instructor
				try {
					new LinkCourseInviteEmail( courseLink, personalMessage ).send();
				} catch (EntityNotFoundException e) {
					// 
					logger.severe("failed to send link course email" + e.getMessage() );
				}
			}
		}
		
		return courseLink;
	}

    /**
     * used by the admin course browser to list courses with these filter criteria
     * 
     * @param languageType the language
     * @param withCourseLinks with course links/no course links
     * @param currentDates end after today/end before today
     * @return list of courses
     */
	public List<Course> listTheseCourses( LanguageType languageTargetType, LanguageType languageExpertType, Boolean withCourseLinks, Boolean currentDates ){
		
		logger.info("listTheseCourses() for " + languageTargetType + "/" + languageExpertType + "," + withCourseLinks + ", " + currentDates );
		// accumulate course keys
    	final List<Course> courses = new ArrayList<Course>();
    	
    	if (languageTargetType == null && languageExpertType == null && withCourseLinks == null && currentDates == null) {
    		// no filtering necessary therefore listAll()
    		return listAll();
    	}
    	
    	// search for courses 
		 Query<Course> q1 = ofy().load().type( Course.class ); // expert lang
		 Query<Course> q2 = ofy().load().type( Course.class ); // target lang

		// filter by language type
//		final boolean doubleQuery = languageExpertType != null;
		if (languageTargetType != null) { 
			// expert
			q1 = q1.filter( "targetLanguage", languageTargetType );
		}
		if (languageExpertType != null) {
			// target
			q2 = q2.filter( "expertLanguage", languageExpertType );			
		}

		// filter by end date before or after TODAY
		if (currentDates != null) {
			final Date now = new Date();
			if (currentDates) {
				// end date after now
				q1 = q1.filter("endDate >=", now);
				q2 = q2.filter("endDate >=", now);
			} else {
				// end date before now
				q1 = q1.filter("endDate <", now);
				q2 = q2.filter("endDate <", now);
			}
		}
			
		// execute query(ies)
		if (withCourseLinks == null) {
			courses.addAll(q1.list());
			if (languageExpertType != null) {
				courses.addAll(q2.list());
			}
		} else {
			// check course links for each course
			List<Key<Course>> foundKeys = new ArrayList<Key<Course>>();
			for (Key<Course> courseKey : q1.keys()) {
				final boolean qlinkA = !(ofy().load().type( CourseLink.class ).filter( "courseAKey =", courseKey ).limit(1).keys().first() == null);
				final boolean qlinkB = !(ofy().load().type( CourseLink.class ).filter( "courseBKey =", courseKey ).limit(1).keys().first() == null);
				if (withCourseLinks && (qlinkA || qlinkB)) {
					foundKeys.add(courseKey);
				} else if (!withCourseLinks && !qlinkA && !qlinkB) {
					foundKeys.add(courseKey);
				}
			}
			if (languageExpertType != null) {
				// check course links for second target language course
				for (Key<Course> courseKey : q2.keys()) {
					final boolean qlinkA = !(ofy().load().type( CourseLink.class ).filter( "courseAKey =", courseKey ).limit(1).keys().first() == null);
					final boolean qlinkB = !(ofy().load().type( CourseLink.class ).filter( "courseBKey =", courseKey ).limit(1).keys().first() == null);
					if (withCourseLinks && (qlinkA || qlinkB)) {
						foundKeys.add(courseKey);
					} else if (!withCourseLinks && !qlinkA && !qlinkB) {
						foundKeys.add(courseKey);
					}
				}
				
			}
			// fetch all courses with these keys
			courses.addAll(ofy().load().keys(foundKeys).values());
		}

		
		logger.info( courses.size() + " matching courses found");
		
		// return 
        return courses;
	}
}
