/**
 * 
 */
package ca.jhosek.main.client;

import com.google.gwt.resources.client.CssResource;

/**
 * program access to style.css
 * @author andy
 * inject and call AppResources.css()
 */
public interface StylesCssResource extends CssResource {
	String a();
	
    String listPrompt();
    String panel();
    String editField();
    String centerAlign();
    String rightFloat();
    String formPromptDatePicker();
    String link();
    String footerPanel();
    String whatIsPicture();
    String buttonPanelLeft();
    String sectionHeader2();
    String sectionDesc();
    String leftAlign();
//    String gwt-StackLayoutPanelContent();
    String formPromptHelp();
    String passwordEditField();
    String formPromptData();
    String button();
    String stackLayoutPanelHeader();
    String disclosurePanel();
    String rightAlign();
    String sectionHeader();
    String userName();
    String buttonPanel();
    String drillablePromptData();
    String formPromptEdit();
    String underConstruction();
    String coldbutton();
    String menuBar();
    String menuItem();
    String verytop();
    String stackLayoutPanel();
//    String gwt-TextArea();
    String sectionDescIndent();
    String sectionItalic();
    String leftFloat();
    String logoPanel();
    String menuPanel();
    String userPanel();
    String hotbutton();
    String whoIsPicture();
    String disclosurePanelLabel();
    String centerPanel();
    String separator();
    String formPrompt();
    String sectionHeader2b();
    
    String offline();
    String online();

	/**	
	 * @return style for Course Base Button Decorator Panel
	 * StudentHomeViewImpl and InstructorHomeViewImpl 
	 */
	String courseBaseButton();
}
