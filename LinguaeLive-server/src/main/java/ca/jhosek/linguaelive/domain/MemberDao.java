/**
 * 
 */
package ca.jhosek.main.server.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import ca.jhosek.main.server.email.NewStudentEmail;
import ca.jhosek.main.shared.proxy.MemberProxy;
import ca.jhosek.main.shared.proxy.MemberRequestContext;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Members are students of the course
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see Member
 * @see MemberProxy
 * @see User	
 * @see Course
 * @see MemberRequestContext
 * @see NewStudentEmail
 */
public class MemberDao extends ObjectifyGenericDao<Member> {

	private static final Logger logger = Logger.getLogger(MemberDao.class.getName());


	/**
	 * @return list of all members
	 */
	public List<Member> listAll()
	{
		logger.info( " listAll()");
		Query<Member> q = ofy().load().type(clazz);
		return q.list();
	}

	public void persist( Member entity ) {
		logger.info( "persist() putting member id = " + entity.getId() + " w/ courseId=" + entity.getCourseKey() );
		this.put(entity);
	}

	public Member findMember( Long id ){
		logger.info( " findMember()");
		try {
			return this.get(id);			
		} catch( EntityNotFoundException e ) {
			logger.warning( "findMember() id " + id.toString() +
					" not found-" + e.getMessage());
			return null;
		}
	}

	public Member saveAndReturn( Member newEntity ) throws EntityNotFoundException {
		logger.info( "saveAndReturn( )");
		Key<Member> newKey = this.put(newEntity);
		return this.get(newKey);
	}

	public void remove(Member entity){
		logger.info( " remove()");
		this.delete(entity);
	}



	/**
	 * create a member record for this user and course
	 * 
	 * @param joinThisCourse
	 */
	public Member joinCourse( User user, Course joinThisCourse ) {
		logger.info( " joinCourse()");

		// make sure we don't oversubscribe
		Member dupMember = findCourseMember(user.getId(), joinThisCourse.getId() );
		if (dupMember != null ) {
			logger.warning( "duplicate course join request encoutered.");
			return null;
		}

		//  
		Member member = new Member( user, joinThisCourse );
		ofy().save().entity(member).now();

		// sending email to the new Student Member
		new NewStudentEmail(user,joinThisCourse).send();  

		return  member;
	}

	/**
	 * create a member record for this user and course invite code
	 * 
	 * @param user
	 * @param joinThisCourse
	 * @return
	 */
	public Member joinInviteCode( User user, String inviteCode ) {
		logger.info( " joinInviteCode()");
		CourseDao courseDao = new CourseDao();
		Course joinThisCourse = courseDao.findInviteCode(inviteCode);
		if( joinThisCourse == null ) {
			logger.warning( "could not find the invite code" + inviteCode );
			return null;
		}
		// 		
		return  joinCourse(user, joinThisCourse);
	}


	/**
	 * @param userId
	 * @param courseId
	 * @return the member for a pair of course and user ids 
	 */
	public Member findCourseMember( Long userId, Long courseId ){
		logger.info( " findCourseMember()");
		return ofy().load().type( Member.class )
		.filter("courseKey", Key.create( Course.class, courseId) )
		.filter("userKey",   Key.create(   User.class,   userId) )
		.first().now();
	}

	/**
	 * fetch these members identified by keys
	 * 
	 * @param memberKeys list of Key<Member>
	 * @return a list of Members
	 */
	public List<Member> fetchMemberKeys(final List<Key<Member>> memberKeys) {
		final Map<Key<Member>, Member> fetched = ofy().load().keys(memberKeys);
		final List<Member> members = new ArrayList<Member>(fetched.values());
		logger.info(members.size() + " members");
		return members;
	}
	
	public List<Member> getMembersOfCourse( Course course ){			
		logger.info( " getMembersOfCourse()");		
		final List<Member> list =  ofy().load().type(Member.class).filter("courseKey", course).list();
		logger.info("found " + list.size() + 
				" members of this course");
		return list;
	}


	/**
	 * find members with this user
	 * @param user
	 * @return
	 */
	public List<Member> getMembersOfUser( User user ){			
		logger.info( " getMembersOfCourse()");
		return ofy().load().type(Member.class).filter("userKey", user).list();

	}

	/**
	 * show available members of this course
	 * 
	 * @param course
	 * @return list of members
	 */
	public List<Member> getAvailableMembersOfCourse( Course course ){			
		logger.info( "getAvailableMembersOfCourse( id=" + course.getId() + 	" )");
		return ofy().load().type(Member.class)
		.filter("courseKey =", Key.create(Course.class,course.getId()) )
		.filter("available =", true)
		.list();
	}

	/**
	 * want to find the students that are available for pairing!
	 * 
	 * these are all the members of each associated course
	 * minus invites/links already established
	 * minus those that are unavailable
	 * filtered by available hours of the week 
	 * 
	 * @param courseId
	 * @return list of all available complementary students to this course
	 */
	public List<Member> getComplementaryStudents( Long memberId, Course myCourse ){
		logger.info( "getComplementaryStudents() for member id=" + memberId + ", course id=" + myCourse.getId() );
		// results of this search 
		List<Member> compMembers = new ArrayList<Member>();
		
		// searching on behaft of this Member
		Member member = findMember( memberId );
		if ( member.getAvailable() ) {
			final Set<Integer> availableHoursOfWeek = member.getAvailableHoursOfWeek();

			// find all linked courses
			final CourseLinkDao courseLinkDao = new CourseLinkDao();
			final List<Course> courses = courseLinkDao.getAcceptedLinkedCourses( myCourse.getId() );
			logger.info( "found " + courses.size() + " complementary courses to Course id=" + myCourse.getId() );

			// get all the session invites too
			final SessionInviteDao sessionInviteDao = new SessionInviteDao();
			final List<SessionInvite> invites = sessionInviteDao.getSessionInvitesForMember(member);
			
			//------------
			// for every linked course,
			for( Course course : courses) {
				if (invites.isEmpty()) {
					// find all available members of that course
					compMembers.addAll( getAvailableMembersOfCourse(course) );
					
				} else {
					
					// for every member - filter with list of invites sent/received
					for (Member other : getAvailableMembersOfCourse(course)) {
						boolean found = false;
						for (SessionInvite invite : invites) {
							final Key<Member> otherKey = key(other);
							if (otherKey.equals(invite.getMember1Key()) ||
								otherKey.equals(invite.getMember2Key())) {
								// invite created for this member... ignore
								found  = true;
								break;
							}
						}
						if (!found) {
							// add member to list
							compMembers.add(other);
						}
					}
				}
			}
			//-------------
			// are my hours are different than yours?
			if ( !availableHoursOfWeek.isEmpty() ){
				// 
				final List<Member> matches = new ArrayList<Member>();
				// hours chosen... find matches
				for( Integer h1 : availableHoursOfWeek ) {
					logger.info("for h1 hour of week: " + h1 );
					for ( Member candidate : compMembers ) {
						logger.info("for member: " + candidate.getId() );
						if ( matches.contains(candidate)) {
							// already selected...
							continue;
							
						} else if( candidate.getAvailableHoursOfWeek().isEmpty() ) {
							// candidate did not select							
							logger.info( "candidate did not select any hours so pick him");
							matches.add(candidate);
														
						} else {
							for ( Integer h2 : candidate.getAvailableHoursOfWeek() ) {
								logger.info( "for h2 hour of week: " + h2);
								if ( h1.equals(h2)) {
									logger.info( "found match for hour " + h1 );
									// found match!
									//if (!matches.contains(candidate)) {
									matches.add(candidate);
									break;
									//								}
								}
							}
						}
					}
				}
				// return matches rather than all
				compMembers = matches;
			}

			logger.info( "returning " + compMembers.size() + " complementary members to Course id=" +  myCourse.getId()  );
		}
		return compMembers;
	}


	/**
	 * @param courseId
	 * @return list of all available complementary students to this course
	 */
	public List<Member> getAllComplementaryStudents( Course myCourse ){
		logger.info( "getAllComplementaryStudents() for course id=" + myCourse.getId() );
		List<Member> compMembers = new ArrayList<Member>();


		// find all linked courses
		CourseLinkDao courseLinkDao = new CourseLinkDao();
		List<Course> courses = courseLinkDao.getAcceptedLinkedCourses( myCourse.getId() );
		logger.info( "found " + courses.size() + " complementary courses to Course id=" + myCourse.getId() );

		// for every linked course,
		for( Course course : courses) {
			// find all available members of that course
			compMembers.addAll( getAvailableMembersOfCourse(course) );
		}

		logger.info( "returning " + compMembers.size() + " complementary members to Course id=" +  myCourse.getId()  );
		return compMembers;
	}


	public void addSchedule( Long memberId, Integer hourOfWeekToAdd ) {
		Member member = findMember(memberId);
		Set<Integer> schedule = member.getAvailableHoursOfWeek();
		if ( !schedule.isEmpty() ) {
			for( Integer h : schedule ) {
				if ( hourOfWeekToAdd.equals( h )) {
					// duplicate found!!
					logger.warning( "addScheduled() duplicate schedule for " + hourOfWeekToAdd 	);
					return;
				}
			}
		}
		logger.info( "adding " + hourOfWeekToAdd + " to schedule");
		schedule.add(hourOfWeekToAdd);
		put(member);
	}

	public void dropSchedule( Long memberId, Integer hourOfWeekToRemove ) {
		Member member = findMember(memberId);
		Set<Integer> schedule = member.getAvailableHoursOfWeek();
		for( Integer h : schedule ) {
			if ( hourOfWeekToRemove.equals( h )) {
				// found!!
				logger.info( "dropping " + hourOfWeekToRemove + " from schedule");
				schedule.remove(h);
				put(member);
				return;
			}
		}
		// missing ??
		logger.warning("dropSchedule( " + hourOfWeekToRemove + " ) could not find in schedule");
	}

	/**
	 * set or clear isAvailable
	 * @param id
	 * @param isAvailable
	 * @return
	 */
	public void setAvailable(Long memberId, Boolean isAvailable){
		Member member = findMember(memberId);
		member.setAvailable(isAvailable);
		put(member);
	}

	/**
	 * delete members for this course
	 * 
	 * @param courseId
	 */
	public void deleteCourseMembers(Course course) {
		// 
		
		List<Member> members = getMembersOfCourse(course);
		for (Member member : members){
			// delete session invites
			SessionInviteDao sessionInviteDao = new SessionInviteDao();
			sessionInviteDao.deleteSessionInvitesForMember(member);
			// delete sessions
			SessionDao sessionDao = new SessionDao();
			sessionDao.deleteSessionsForMember(member);
		}
		// delete Members
		ofy().delete().entities(members);
	}

}
