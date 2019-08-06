/**
 * 
 */
package ca.jhosek.main.server.domain;

import com.googlecode.objectify.ObjectifyService;

/**
 * @author andy
 *
 */
public class ObjectifyMain {

	public static void init() {
		//--------------
		// initialize Objectify and register classes
		// 6.0 ObjectifyService.init();
		ObjectifyService.register(User.class );
		ObjectifyService.register(Course.class );
		ObjectifyService.register(Member.class );
		ObjectifyService.register(CourseLink.class );
		ObjectifyService.register(Session.class );
		ObjectifyService.register(School.class );
		ObjectifyService.register(ContactInfo.class );
		ObjectifyService.register(SessionInvite.class );	
		
		
		
	}

}
