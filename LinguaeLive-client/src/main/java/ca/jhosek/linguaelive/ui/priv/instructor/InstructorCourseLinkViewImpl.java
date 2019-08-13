/**
 * 
 */
package ca.jhosek.linguaelive.ui.priv.instructor;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.NumberLabel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.linguaelive.AppResources;
import ca.jhosek.linguaelive.LLConstants;
import ca.jhosek.linguaelive.activity.mainregion.InstructorCourseLinkActivity;
import ca.jhosek.linguaelive.place.InstructorCourseLinkPlace;
import ca.jhosek.linguaelive.proxy.CourseLinkProxy;

/**
 * Instructor Course Link viewer
 * instructor can accept or decline course link invite
 * instructor can view other course
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see CourseLinkProxy
 * @see InstructorCourseLinkView
 * @see InstructorCourseLinkActivity
 * @see InstructorCourseLinkPlace
 * 
 */
public class InstructorCourseLinkViewImpl extends Composite 
	implements IsWidget, InstructorCourseLinkView,	Editor<CourseLinkProxy> {

	public interface Driver extends RequestFactoryEditorDriver<CourseLinkProxy, InstructorCourseLinkViewImpl> {

	}

	// logger boilerplate
	private static final Logger logger = Logger.getLogger(InstructorCourseLinkViewImpl.class
			.getName());

	private static InstructorCourseLinkViewImplUiBinder uiBinder = 
			GWT.create(InstructorCourseLinkViewImplUiBinder.class);

	interface InstructorCourseLinkViewImplUiBinder extends
		UiBinder<Widget, InstructorCourseLinkViewImpl> {
	}
	// course A
	@Path( "courseA.owner.firstName" )
	@UiField InlineLabel courseA_instructor_fname;
	@Path( "courseA.owner.lastName" )
	@UiField InlineLabel courseA_instructor_lname;
	@Path( "courseA.owner.school" )
	@UiField Label courseA_instructor_school;
	@Path( "courseA.owner.location" )
	@UiField Label courseA_instructor_location;
	@Path( "courseA.owner.emailAddress" )
	@UiField Label courseA_instructor_email;
	@Path( "courseA.owner.userTimeZone" )
	@UiField Label courseA_instructor_timezone;
	@Path( "courseA.name" )
	@UiField Label courseA_name;
	@Path( "courseA.description" )
	@UiField TextArea courseA_description;
	@Path( "courseA.estimatedMemberSize" )
	@UiField NumberLabel<Long> courseA_estimatedMemberSize;
	
	// course B
	@Path( "courseB.owner.firstName" )
	@UiField InlineLabel courseB_instructor_fname;
	@Path( "courseB.owner.lastName" )
	@UiField InlineLabel courseB_instructor_lname;
	@Path( "courseB.owner.school" )
	@UiField Label courseB_instructor_school;
	@Path( "courseB.owner.location" )
	@UiField Label courseB_instructor_location;
	@Path( "courseB.owner.emailAddress" )
	@UiField Label courseB_instructor_email;
	@Path( "courseB.owner.userTimeZone" )
	@UiField Label courseB_instructor_timezone;
	@Path( "courseB.name" )
	@UiField Label courseB_name;
	@Path( "courseB.description" )
	@UiField TextArea courseB_description;
	@Path( "courseB.estimatedMemberSize" )
	@UiField NumberLabel<Long> courseB_estimatedMemberSize;

	@UiField DateLabel createDate;
	
	// buttons
	@UiField Button confirmLink;
	@UiField Button declineLink;
	@UiField Button unlinkCourses;
	@Ignore @UiField Label pendingBlurb;
	@Ignore @UiField Label declinedBlurb;
	@Ignore @UiField Label acceptedBlurb;
	
	private Presenter presenter;

	private Boolean isSender;

	private final String style_link; // = resources.css().listPrompt();

	private final LLConstants constants;


	@Inject
	public InstructorCourseLinkViewImpl(AppResources resources, LLConstants constants) {
		
		this.constants = constants;
		this.style_link = resources.css().listPrompt();
		logger.info("link style name: " + style_link);
		initWidget(uiBinder.createAndBindUi(this));

	}

	/**
	 * @return a CourseProxy editor driver, userProxyDriver, tied to this presenter and view
	 */
	public Driver createEditorDriver(EventBus eventBus, RequestFactory requestFactory) {
		// 
		logger.info( "createEditorDriver( InstructorCourseLinkViewImpl )");
		Driver driver = GWT.create(Driver.class);
		driver.initialize( eventBus, requestFactory, this );
		return driver;
	}


	public void setPresenter(Presenter presenter) {
		// 
		this.presenter = presenter;
	}


	public void clear() {
		// clear styles on course names
		courseA_name.removeStyleName(style_link);
		courseB_name.removeStyleName(style_link);
	}
	
	/**
	 * display only the proper buttons
	 * @see ca.jhosek.linguaelive.ui.priv.instructor.InstructorCourseLinkView#setViewMode(java.lang.Boolean, java.lang.Boolean)
	 */
	public void setViewMode( final Boolean isSender, final Boolean pending, final Boolean accepted ) {
		this.isSender = isSender;
		if (isSender) {
			courseA_name.addStyleName(style_link);
			courseB_name.removeStyleName(style_link);
		} else {
			courseA_name.removeStyleName(style_link);
			courseB_name.addStyleName(style_link);
		}
		// pending
		pendingBlurb.setVisible( pending );
		confirmLink.setVisible( !isSender && !accepted );	// pending shows accept
		declineLink.setVisible( /*!isSender && */ pending);	// pending shows declined
		// accepted
		acceptedBlurb.setVisible( !pending && accepted );
		unlinkCourses.setVisible( !pending && accepted ); // accepted shows unlink
		// declined
		declinedBlurb.setVisible( !pending && !accepted ); // declined
		
		if( pending || !accepted ) {
//			final String hiddenUntilConfirmed = "<hidden until confirmed>";
			courseB_instructor_email.setText(constants.hiddenUntilConfirmed());
		}
		
	}
	
	@UiHandler("confirmLink")
	void onConfirmLinkClick(ClickEvent event) {
		presenter.respondToCourseLinkInvite(true);
	}
	@UiHandler("declineLink")
	void onDeclineLinkClick(ClickEvent event) {
		presenter.respondToCourseLinkInvite(false);
	}
	@UiHandler("unlinkCourses")
	void onUnlinkCoursesClick(ClickEvent event) {
		presenter.respondToCourseLinkInvite(false);		
	}
	@UiHandler("courseB_name")
	void onCourseB_nameClick(ClickEvent event) {
		if (!isSender) {
			// only link course B if current user is NOT sender
			presenter.goToCourseB();
		}
	}
	@UiHandler("courseA_name")
	void onCourseA_nameClick(ClickEvent event) {
		if (isSender) {
			// only link course A if current user is sender
			presenter.goToCourseA();
		}
	}
}
