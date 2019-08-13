package ca.jhosek.linguaelive.activity.mainregion;

import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import ca.jhosek.linguaelive.domain.CurrentState;
import ca.jhosek.linguaelive.place.AddEditCoursePlace;
import ca.jhosek.linguaelive.place.CourseBrowsePlace;
import ca.jhosek.linguaelive.place.InstructorYourCoursePlace;
import ca.jhosek.linguaelive.place.MyCoursesPlace;
import ca.jhosek.linguaelive.ui.priv.instructor.AddEditCourseView;
import ca.jhosek.linguaelive.ui.priv.instructor.AddEditCourseViewImpl;
import ca.jhosek.linguaelive.ui.priv.instructor.AddEditCourseViewImpl.Driver;
import ca.jhosek.linguaelive.UserType;
import ca.jhosek.linguaelive.proxy.AppRequestFactory;
import ca.jhosek.linguaelive.proxy.CourseProxy;
import ca.jhosek.linguaelive.proxy.CourseRequestContext;

/**
 * add a new course
 * or cancel to home page
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see AddEditCourseView
 * @see AddEditCourseViewImpl
 * @see AddEditCoursePlace
 * @see MyCoursesPlace
 */
public class AddEditCourseActivity extends AbstractActivity implements AddEditCourseView.Presenter {

	private static final Logger logger = Logger.getLogger( AddEditCourseActivity.class.getName() );

	/**
	 * Guice/Gin Factory Module Builder
	 * {@link} http://google-guice.googlecode.com/svn/trunk/javadoc/com/google/inject/assistedinject/FactoryModuleBuilder.html
	 * @see FactoryModuleBuilder
	 * @see GinFactoryModuleBuilder
	 *  
	 */
	public interface Factory {
		AddEditCourseActivity create( String courseId );
	}


	private final AddEditCourseView view;
	private final PlaceController placeController;
	private final AppRequestFactory requestFactory;
	private final CurrentState currentState;

	private Driver editorDriver;

	private CourseProxy thisCourse = null;
	private final String token;
	private CourseRequestContext courseContext;

//	public AddACourseActivity( 
//			AddACourseView view, 
//			PlaceController placeController,
//			AppRequestFactory requestFactory,
//			CurrentState currentState,
//			Driver driver,
//			EventBus eventBus
//	) {
//		super();
//		this.view = view;
//		this.placeController = placeController;
//		this.requestFactory = requestFactory;
//		this.currentState = currentState;
//		view.setPresenter(this);
//		this.editorDriver = view.createEditorDriver(eventBus, requestFactory);
//
//		this.token = "";
//	}

	@Inject
	public AddEditCourseActivity( 
			AddEditCourseView view, 
			PlaceController placeController,
			AppRequestFactory requestFactory,
			CurrentState currentState,
			Driver driver,
			EventBus eventBus,
			@Assisted String courseId
	) {
		super();
		this.view = view;
		this.placeController = placeController;
		this.requestFactory = requestFactory;
		this.currentState = currentState;
		view.setPresenter(this);
		this.editorDriver = view.createEditorDriver(eventBus, requestFactory);

		this.token = courseId;
	}


	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		courseContext = requestFactory.courseRequest();

		panel.setWidget(view);

		if ( token == null || token.isEmpty() ) {
			logger.info( "creating new course ");
			// ----- create new 
			thisCourse = courseContext.create( CourseProxy.class );
			// fixup bean
			thisCourse.setOwner( currentState.getLoggedInUser());
			thisCourse.setSchoolName(currentState.getLoggedInUser().getSchool());
			thisCourse.setEstimatedMemberSize(0L); // default size nonnull
			// edit it
			editorDriver.edit( thisCourse, courseContext );
			
			logger.info( "Saving new course");
			// save it 
			courseContext.persist(thisCourse).to( new Receiver<CourseProxy>() {
				/**
				 * save and return
				 * 
				 * @see com.google.web.bindery.requestfactory.shared.Receiver#onSuccess(java.lang.Object)
				 */
				@Override
				public void onSuccess(CourseProxy courseResponse) {
					logger.info( "saveAndReturn() successful"	);
					if ( currentState.getLoggedInUser().getUserType() == UserType.ADMIN ) {
						// admin goes to query courses
						placeController.goTo( new CourseBrowsePlace() );
						
					} else {
						// instructor goes to my courses
//						placeController.goTo( new MyCoursesPlace() );
						placeController.goTo( new InstructorYourCoursePlace(courseResponse) );
					}
				}
	
				/* (non-Javadoc)
				 * @see com.google.web.bindery.requestfactory.shared.Receiver#onFailure(com.google.web.bindery.requestfactory.shared.ServerFailure)
				 */
				@Override
				public void onFailure(ServerFailure error) {
					logger.severe( "server error saving course info" );
					// 
					Window.alert("Server failed to save due to error:" + error.getExceptionType() + " " + error.getMessage());
				}
	
				/* (non-Javadoc)
				 * @see com.google.web.bindery.requestfactory.shared.Receiver#onConstraintViolation(java.util.Set)
				 */
				@Override
				public void onConstraintViolation(Set<ConstraintViolation<?>> errors) {
					// 
					for( ConstraintViolation<?> vio : errors )
						Window.alert("Failed to save due to error:" + vio.getMessage());
					super.onConstraintViolation(errors);
				}
			});
//			courseContext.saveAndReturn(thisCourse).to( new Receiver<CourseProxy>() {
//				
//				/**
//				 * save and return
//				 * 
//				 * @see com.google.web.bindery.requestfactory.shared.Receiver#onSuccess(java.lang.Object)
//				 */
//				@Override
//				public void onSuccess(CourseProxy response) {
//					logger.info( "saveAndReturn() successful"	);
//					thisCourse = response;
//					if ( currentState.getLoggedInUser().getUserType() == UserType.ADMIN ) {
//						// admin goes to query courses
//						placeController.goTo( new QueryCoursesPlace() );
//						
//					} else {
//						// instructor goes to my courses
//						placeController.goTo( new MyCoursesPlace() );
//					}
//				}
//
//				/* (non-Javadoc)
//				 * @see com.google.web.bindery.requestfactory.shared.Receiver#onFailure(com.google.web.bindery.requestfactory.shared.ServerFailure)
//				 */
//				@Override
//				public void onFailure(ServerFailure error) {
//					logger.severe( "server error saving course info" );
//					// 
//					Window.alert("Server failed to save due to error:" + error.getExceptionType() + " " + error.getMessage());
//				}
//
//				/* (non-Javadoc)
//				 * @see com.google.web.bindery.requestfactory.shared.Receiver#onViolation(java.util.Set)
//				 */
//				@Override
//				public void onViolation(Set<Violation> errors) {
//					logger.info( "onViolation()");
//					// 
//					for( Violation vio : errors )
//						Window.alert("Failed to save due to error:" + vio.getPath() + vio.getMessage());
//					super.onViolation(errors);
//				}
//			});

		} else {
			// ---- 
			// edit existing
			// ---- 
			final Long courseId = Long.valueOf(token);
			Request<CourseProxy> fetchRequest = courseContext.findCourse(courseId).with("owner");

			fetchRequest.to( new Receiver<CourseProxy>() {

				@Override
				public void onSuccess(CourseProxy response) {
					thisCourse = response;
					// 
					if ( response == null ) {
						logger.severe("Course Id not found: " + token );
						Window.alert("Error - Course Id not found" );
						placeController.goTo( currentState.getHomePlace() );
						
						
						
					} else if ( 
							!thisCourse.getOwner().getId().equals( currentState.getLoggedInUser().getId())// course A owner 
									&& !currentState.isAdminUser() 			 // admin user
							) {
						// access violation
						logger.warning( "Access Violation - by:" + currentState.getLoggedInUser().getId() + " attempt course id=" + response.getId() ); 
						Window.alert( "Access Violation: You are not authorized to edit this course." );
						placeController.goTo( currentState.getHomePlace() );
						
					} else {
						courseContext = requestFactory.courseRequest();
						editorDriver.edit(thisCourse, courseContext);
						view.fixupDisplay();	// fixup date picker display
						courseContext.persist(thisCourse).to( new Receiver<CourseProxy>() {

							@Override
							public void onSuccess(CourseProxy courseResponse) {
								logger.info( "persist() successful"	);
								if ( currentState.getLoggedInUser().getUserType() == UserType.ADMIN ) {
									// admin goes to query courses
									placeController.goTo( new CourseBrowsePlace() );
									
								} else {
									// instructor goes to my courses
									placeController.goTo( new InstructorYourCoursePlace( courseResponse ) );

								}
							}

							/* (non-Javadoc)
							 * @see com.google.web.bindery.requestfactory.shared.Receiver#onFailure(com.google.web.bindery.requestfactory.shared.ServerFailure)
							 */
							@Override
							public void onFailure(ServerFailure error) {
								// 
								Window.alert("Server failed to save due to error:" + error.getExceptionType() + " " + error.getMessage());
								super.onFailure(error);
							}

							/* (non-Javadoc)
							 * @see com.google.web.bindery.requestfactory.shared.Receiver#onConstraintViolation(java.util.Set)
							 */
							@Override
							public void onConstraintViolation(Set<ConstraintViolation<?>> errors) {
								// 
								for( ConstraintViolation<?> vio : errors )
									Window.alert("Failed to save due to error:" + vio.getMessage());
								super.onConstraintViolation(errors);
							}
						});
					}

				}
			}).fire();
		}
	}

	//	@Override
	//	public void addANewCourse() {
	//		// 
	//		logger.info("Saving course edits");
	//		
	//		addThisCourse.setOwner( currentState.getLoggedInUser());
	//		addThisCourse.setSchoolName(currentState.getLoggedInUser().getSchool());
	//		
	//		RequestContext context = editorDriver.flush();
	//
	//		context.fire( new Receiver<Void>() {
	//			
	//			/* (non-Javadoc)
	//			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onFailure(com.google.web.bindery.requestfactory.shared.ServerFailure)
	//			 */
	//			@Override
	//			public void onFailure(ServerFailure error) {
	//				// 
	//				Window.alert("Server failed to save due to error:" + error.getExceptionType() + " " + error.getMessage());
	//				super.onFailure(error);
	//			}
	//
	//			/* (non-Javadoc)
	//			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onViolation(java.util.Set)
	//			 */
	//			@Override
	//			public void onViolation(Set<Violation> errors) {
	//				// 
	//				for( Violation vio : errors )
	//					Window.alert("Failed to save due to error:" + vio.getPath() + vio.getMessage());
	//				super.onViolation(errors);
	//			}
	//
	//			@Override
	//			public void onSuccess(Void response) {
	//				// 
	//				// flush failed w/ errors
	//				if (editorDriver.hasErrors()) {
	//					logger.warning("addACourse() onSuccess() with hasErrors() after flush()" );
	//					StringBuilder errorDesc = new StringBuilder( "Errors found while creating a course. ");
	//					for ( EditorError error : editorDriver.getErrors() ) {
	//						errorDesc.append(error.getMessage());
	//						errorDesc.append(" ");						
	//					}
	//					
	//					Window.alert( errorDesc.toString() 	);
	//					reloadEditor();
	//					return;
	//				}
	//				
	//				CourseRequestContext courseContext = requestFactory.courseRequest();
	//				
	//				// flush object to persistent storage
	//				courseContext.saveAndReturn( addThisCourse ).fire( new Receiver<CourseProxy>() {
	//					@Override
	//					public void onSuccess(CourseProxy response) {
	//						logger.info("createNewInstructor() successful flush & persist" );
	//						// successful save to persistent storage
	//						Window.alert("Created new class " + addThisCourse.getName() );
	//						if ( currentState.getLoggedInUser().getUserType() == UserType.ADMIN ) {
	//							// admin goes to query courses
	//							placeController.goTo( new QueryCoursesPlace() );							
	//						} else {
	//							// instructor goes to my courses
	//							placeController.goTo( new InstructorYourCoursePlace( response ) );
	//							
	//						}
	//					}
	//				});
	//			}
	//		});
	//	}

	//	protected void reloadEditor() {
	//		courseContext = requestFactory.courseRequest();
	//		// set query
	//		CourseProxy newCourse = courseContext.create( CourseProxy.class );
	//		if ( addThisCourse != null ){
	//			cloneCourse(addThisCourse, newCourse );
	//		}
	//		addThisCourse = newCourse; 
	//		editorDriver.edit( addThisCourse, courseContext );						
	//	}
	//
	//	private void cloneCourse(CourseProxy cloneFromCourse, CourseProxy newCourse) {
	//		// clone the course cause we have a AutoBean Frozen error
	//		newCourse.setDescription(cloneFromCourse.getDescription());
	//		newCourse.setEndDate(cloneFromCourse.getEndDate());
	//		newCourse.setEstimatedMemberSize(cloneFromCourse.getEstimatedMemberSize());
	//		newCourse.setExpertLanguage(cloneFromCourse.getExpertLanguage());
	//		newCourse.setName(cloneFromCourse.getName());
	//		newCourse.setOwner(cloneFromCourse.getOwner());
	//		newCourse.setStartDate(cloneFromCourse.getStartDate());
	//		newCourse.setTargetLanguage(cloneFromCourse.getTargetLanguage());
	//	}

	/***
	 * goto home page on cancel
	 * @see ca.jhosek.linguaelive.ui.priv.instructor.AddEditCourseView.Presenter#cancel()
	 */
	public void cancel() {
		// 
		placeController.goTo( currentState.getHomePlace() );
	}

	public void save() {
		logger.info("saving");
		saveCourse();
	}

	private void saveCourse() {
		//
		logger.info("saveCourse()" );

		RequestContext context = editorDriver.flush();

		// Check for errors
		if (editorDriver.hasErrors()) {
			// 
			// flush failed w/ errors
			logger.warning("saveCourse() onSuccess() with hasErrors() after flush()" );
			StringBuilder errorDesc = new StringBuilder( "Errors found while saving the course details: ");
			for ( EditorError error : editorDriver.getErrors() ) {
				errorDesc.append(error.getMessage());
				errorDesc.append(" ");						
			}

			Window.alert( errorDesc.toString() 	);
			//			reloadEditor();
			return;
		}
		// send to server
		context.fire( /** note response handlers defined above **/ );
	}


}
