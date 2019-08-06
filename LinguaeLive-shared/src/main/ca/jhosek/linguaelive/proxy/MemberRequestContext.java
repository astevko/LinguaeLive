/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.shared.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import ca.jhosek.main.server.domain.DaoServiceLocator;
import ca.jhosek.main.server.domain.Member;
import ca.jhosek.main.server.domain.MemberDao;

/**
 * Request Service Stub
 * referenced by RequestFactory
 * This is the service for serving up Member objects
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see MemberDao
 * @see DaoServiceLocator
 * @see Member
 * @link http://code.google.com/p/google-web-toolkit/wiki/RequestFactoryInterfaceValidation
 */
@Service( value = MemberDao.class, locator = DaoServiceLocator.class )
public interface MemberRequestContext extends RequestContext {

	// basic operations
    Request<List<MemberProxy>> listAll();
    Request<Void> persist(MemberProxy member);
    Request<MemberProxy> saveAndReturn(MemberProxy newMember);
    Request<Void> remove(MemberProxy member);
    Request<MemberProxy> findMember( Long id );
    Request<MemberProxy> findCourseMember( Long userId, Long courseId );

    // advanced operations
	Request<MemberProxy> joinCourse( UserProxy user, CourseProxy joinThisCourse );
	Request<MemberProxy> joinInviteCode( UserProxy user, String inviteCode );
	Request<List<MemberProxy>> getMembersOfCourse( CourseProxy course );
	Request<List<MemberProxy>> getAvailableMembersOfCourse( CourseProxy course );
	
	Request<List<MemberProxy>> getAllComplementaryStudents(  CourseProxy course );
	Request<List<MemberProxy>> getComplementaryStudents(  Long memberId, CourseProxy course );

	
	/**
	 * add this hour to the schedule
	 * 
	 * @param memberId
	 * @param hourOfWeekToAdd
	 * @return void
	 */
	Request<Void> addSchedule( Long memberId, Integer hourOfWeekToAdd );
	/**
	 * drop this hour from the schedule
	 * @param memberId
	 * @param hourOfWeekToRemove
	 * @return void
	 */
	Request<Void> dropSchedule( Long memberId, Integer hourOfWeekToRemove );
	/**
	 * set or clear isAvailable
	 * @param id
	 * @param isAvailable
	 * @return
	 */
	Request<Void> setAvailable(Long id, Boolean isAvailable);

	Request<Void> deleteCourseMembers(CourseProxy course);
}
