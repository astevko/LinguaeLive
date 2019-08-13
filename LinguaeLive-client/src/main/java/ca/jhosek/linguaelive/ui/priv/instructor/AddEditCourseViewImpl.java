/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.priv.instructor;

import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.ui.client.ValueBoxEditorDecorator;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.linguaelive.activity.mainregion.AddEditCourseActivity;
import ca.jhosek.linguaelive.place.AddEditCoursePlace;
import ca.jhosek.linguaelive.ui.EnumRenderer;
import ca.jhosek.linguaelive.LanguageType;
import ca.jhosek.linguaelive.proxy.CourseProxy;

/**
 * admin display of user information
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see AddEditCourseView
 * @see AddEditCourseActivity
 * @see AddEditCoursePlace
 */
public class AddEditCourseViewImpl extends Composite implements IsWidget, Editor<CourseProxy>, AddEditCourseView {
	
	private static final String START_DATE_FORMAT = "EEE, MMM d, yyyy";


	interface AdminCourseViewerUiBinder extends UiBinder<Widget, AddEditCourseViewImpl> {
	}

	public interface Driver extends RequestFactoryEditorDriver<CourseProxy, AddEditCourseViewImpl> {		
	}

	// logger boilerplate
	private static final Logger logger = Logger.getLogger(AddEditCourseViewImpl.class
			.getName());

	static EnumRenderer<LanguageType> languageTypeRenderer = new EnumRenderer<LanguageType>();

	private static DateBox.Format mdyDateFormat = new DateBox.DefaultFormat( DateTimeFormat.getFormat(  START_DATE_FORMAT) );  

	private static AdminCourseViewerUiBinder uiBinder = GWT
	.create(AdminCourseViewerUiBinder.class);

	@UiField ValueBoxEditorDecorator<String> name;
	@UiField ValueBoxEditorDecorator<String> description;
	@UiField ValueBoxEditorDecorator<Long> estimatedMemberSize;
	@UiField DateBox startDate;
	@UiField DateBox endDate;
	@UiField(provided=true) ValueListBox<LanguageType> targetLanguage
		= new ValueListBox<LanguageType>( languageTypeRenderer );
	@UiField(provided=true) ValueListBox<LanguageType> expertLanguage 
		= new ValueListBox<LanguageType>( languageTypeRenderer );
	@UiField 	CheckBox singlePartnerPreferred;

	@UiField Button saveCourseButton;
	@UiField Button cancelButton;


	private Presenter presenter;

	public AddEditCourseViewImpl() {
		// initialize language widget values
		targetLanguage.setAcceptableValues( Arrays.asList( LanguageType.values()) );
		expertLanguage.setAcceptableValues( Arrays.asList( LanguageType.values()) );


		initWidget(uiBinder.createAndBindUi(this));

		startDate.setFormat(mdyDateFormat);
		endDate.setFormat(  mdyDateFormat);
	}

	/**
	 * @return a CourseProxy editor driver, userProxyDriver, tied to this presenter and view
	 */
	public Driver createEditorDriver(EventBus eventBus, RequestFactory requestFactory) {
		// 
		logger.info( "createEditorDriver( AdminCourseViewerImpl )");
		Driver driver = GWT.create(Driver.class);
		driver.initialize( eventBus, requestFactory, this );
		return driver;
	}

	public void fixupDisplay() {
		// fixup the date pickers current month to match selected month
		//		if( startDate.getValue() != null ){
		//			startDate.setCurrentMonth( startDate.getValue() );
		//		}
		//		if( endDate.getValue() != null ){
		//			endDate.setCurrentMonth( endDate.getValue() );
		//		}
		// enable save the course button
		saveCourseButton.setEnabled(true);

	}

	@UiHandler("cancelButton")
	void onCancelButtonClick( ClickEvent event ){
		// enable save the course button
		saveCourseButton.setEnabled(true);

		presenter.cancel();
	}

	@UiHandler("saveCourseButton")
	void onSaveCourseButtonClick(ClickEvent event) {
		// do some error checking here...
		{
			// date pickers
			Date today = new Date();
			Date start = startDate.getValue();
			Date end = endDate.getValue();
			if ( start == null || end == null ) {
				Window.alert("Must pick a start and end date for the course." );
				return;
			} else if ( start.after(end) ) {
				Window.alert("End date must be after start." );				
				return;
			} else if ( end.before( today )) {
				Window.alert("Class needs to end in the future." );
				return;
			}
		}
		{
			// target languages
			LanguageType target = targetLanguage.getValue();
			LanguageType expert = expertLanguage.getValue();
			if ( target == null || expert == null ){
				Window.alert("Please specify both target and expert languges.");
				return;
			} else if ( target == expert ){
				Window.alert("Please specify different target and expert languges.");
				return;				
			}
		}
		// disable save the course button
		saveCourseButton.setEnabled(false);
		// move content from edit widgets to value
		presenter.save();
	}


	/**
	 * @param presenter the presenter to set
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}
