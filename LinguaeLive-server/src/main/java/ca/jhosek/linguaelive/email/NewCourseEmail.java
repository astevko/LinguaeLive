package ca.jhosek.main.server.email;

import ca.jhosek.main.server.domain.Course;
import ca.jhosek.main.server.domain.CourseDao;

/**
 * new course message to Instructor
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see CourseDao
 */
public class NewCourseEmail extends SendEmail {

	private static final String SUBJECT = "<course_name> registered for <school>";
	private static final String BODY = "Hi <first_name> <last_name>,\n" +
			"Your course is now posted on the LinguaeLive site.\n" +
			"Here are the details as we understand them:\n" +
			"Course Name: <course_name>\n" +
			"Description: <description>\n" +
			"Target Language: <target_language>\n" +
			"Expert Language: <expert_language>\n" +
			"Start Date: <start_date>\n" +
			"End Date: <end_date>\n\n" +			
			"You may now search and link to complementary courses by following this link \n" +
			"<course_link>";
	
	
	public NewCourseEmail(Course course) {
		super(course.getOwner(), course);
	}
	
	@Override
	public String getSubject() {
		return SUBJECT;
	}

	@Override
	public String getBody() {
		return BODY;		
	}

}
