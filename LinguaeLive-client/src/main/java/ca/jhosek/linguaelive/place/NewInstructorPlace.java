/**
 */
package ca.jhosek.linguaelive.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.linguaelive.activity.mainregion.NewInstructorFormActivity;
import ca.jhosek.linguaelive.ui.anon.NewInstructorPanel;
import ca.jhosek.linguaelive.ui.anon.NewInstructorPanelImpl;

/**
 * sign up new instructor form
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see NewInstructorPlace
 * @see NewInstructorFormActivity
 * @see NewInstructorPanel
 * @see NewInstructorPanelImpl
 */
public class NewInstructorPlace extends Place {	
	
	@Prefix("signupinstructor")
	public static class Tokenizer implements PlaceTokenizer<NewInstructorPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public NewInstructorPlace getPlace(String token) {
			// 
			return new NewInstructorPlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(NewInstructorPlace place) {
			// 
			return "";
		}
		
	}
}
