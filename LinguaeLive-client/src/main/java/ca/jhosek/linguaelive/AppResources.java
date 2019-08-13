/**
 * 
 */
package ca.jhosek.linguaelive;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource.NotStrict;

/**
 * app resources client bundle.
 * 
 * @author andy
 *
 * inject AppResources resources
 */
public interface AppResources extends ClientBundle {
	@NotStrict
	@Source("style.css")
	StylesCssResource css();
}
