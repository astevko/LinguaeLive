package ca.jhosek.linguaelive.ui;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.IconCellDecorator;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.cell.client.ImageLoadingCell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.editor.client.adapters.OptionalFieldEditor;
import com.google.gwt.editor.ui.client.ValueBoxEditorDecorator;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.NumberLabel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SimpleRadioButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.ValueBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.ValueLabel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.ValuePicker;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ca.jhosek.linguaelive.mvp.MainRegionActivityMapper;
import ca.jhosek.linguaelive.mvp.MenuRegionActivityMapper;
import ca.jhosek.linguaelive.mvp.UserRegionActivityMapper;

/**
 * Main frame within which all the panels are arranged in a 
 * three layer header/center/footer arrangement.
 * 
 * The header is further broken into logo/user/menu panels. 
 * 
 * These are the available Cell Presentation widgets in the GWT
 * http://gwt.google.com/samples/Showcase/Showcase.html#!CwFlowPanel
 * 
 * 
 * 
 * Abstract
 * @see  ValueBox
 * 
 * Date
 * @see  DateBox
 * @see  DateLabel
 * @see  DatePicker
 * 
 * Boolean
 * @see  CheckBox
 * 
 * Numbers
 * @see  DoubleBox		edit
 * @see  IntegerBox		edit
 * @see  LongBox		edit
 * @see  NumberLabel	display
 * 
 * @see  Hidden
 * 
 * String
 * @see  HTML
 * @see  InlineHTML
 * @see  InlineLabel
 * @see  Label
 * @see  PasswordTextBox
 * @see  SuggestBox
 * @see  TextBoxBase  A
 * @see  TextBox
 * @see  TextArea
 * @see  LabelEditor
 * 
 * @see  RadioButton
 * @see  SimpleCheckBox

 * @see  SimpleRadioButton
 * @see  ToggleButton
 * 
 * @see  ValueBoxBase
 * @see  ValueBoxEditorDecorator
 * @see  ValueLabel
 * @see  ValueListBox
 * @see  ValuePicker
 * 
 * @see OptionalFieldEditor
 * @see ListEditor			manage a list of editors and their editors
 * 
 * -------------------------- cells -----------------------------------
 *  @see  AbstractEditableCell
 *  @see  AbstractSafeHtmlCell	
 *  @see  AbstractInputCell	used to render input elements that can receive focus.
 *  
 *  @see  CompositeCell		is composed of other Cells.
 *  
 *  @see  ImageCell			used to render an image. The String value is the url of the image.
 *  @see  ImageLoadingCell	used to render an image. A loading indicator is used until the image is fully loaded.
 *  @see  ImageResourceCell	used to render an ImageResource.
 *  @see  IconCellDecorator used to render an Icon
 *  
 *  @see  SafeHtmlCell		used to render safe HTML markup. 
 *  @see  CheckboxCell		used to render a checkbox.
 *  
 *  @see  DateCell			used to render Dates.
 *  @see  DatePickerCell	used to render and edit Dates. When a cell is selected by clicking on it, a DatePicker is popped up.
 *  
 *  @see  ActionCell		renders a button and takes a delegate to perform actions on mouseUp.
 *  @see  ButtonCell		used to render a button.
 *   
 *  @see  SelectionCell		used to render a drop-down list.
 *  
 *  @see  EditTextCell		An editable text cell. Click to edit, escape to cancel, return to commit. Important
 *  @see  TextInputCell		used to render a text input
 *  @see  ClickableTextCell	used to render text. Clicking on the cell causes its ValueUpdater to be called.
 *  @see  TextCell			used to render text. *
 *
 *  @see  NumberCell		used to render formatted numbers.
 *  
 * @author copyright (C) 2011 Andrew Stevko
 * 
 */
@Singleton
public class MainView extends Composite {

  private static MainViewUiBinder uiBinder = GWT
  .create(MainViewUiBinder.class);

  interface MainViewUiBinder extends UiBinder<Widget, MainView> {
  }
  
  @UiField SimplePanel userPanel;	// Header Region used for displaying user information
  @UiField SimplePanel menuPanel;	// Menu Region used for displaying menu information
  @UiField HTMLPanel   footerPanel;
  @UiField SimplePanel centerPanel;	//Center Region used for displaying current place/activity/view
  /*@UiField SimplePanel logoPanel;	//Logo Region used for displaying current place/activity/view //Added by Rumman */
  @Inject
  public MainView(
		  MainRegionActivityMapper mainRegionActivityMapper,
		  MenuRegionActivityMapper menuRegionActivityMapper,
		  UserRegionActivityMapper userRegionActivityMapper,
                  EventBus eventBus) {     
    initWidget(uiBinder.createAndBindUi(this));
    
    // Register a activity manager for each region 
    ActivityManager userRegionActivityMapperManager = new ActivityManager(userRegionActivityMapper, eventBus);
    userRegionActivityMapperManager.setDisplay(userPanel);

    ActivityManager menuRegionActivityManager = new ActivityManager(menuRegionActivityMapper, eventBus);
    menuRegionActivityManager.setDisplay(menuPanel);
    
    ActivityManager mainRegionActivityManager = new ActivityManager(mainRegionActivityMapper, eventBus);
    mainRegionActivityManager.setDisplay(centerPanel);
  }
}

