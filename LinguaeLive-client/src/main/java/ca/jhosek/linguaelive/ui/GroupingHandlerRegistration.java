/**
 * 
 */
package ca.jhosek.main.client.ui;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Grouping of handler registrations
 * @author sencha gxt 3.0.x
 * 
 * http://grepcode.com/file/repo1.maven.org/maven2/com.sencha.gxt/gxt/3.0.0-beta1/com/sencha/gxt/core/shared/event/GroupingHandlerRegistration.java
 * 
 */
public class GroupingHandlerRegistration implements HandlerRegistration {

	/**
	 * 
	 */
	private final Set<HandlerRegistration> registrations;

	/**
	 * 
	 */
	public GroupingHandlerRegistration() {
		registrations = new HashSet<HandlerRegistration>();
	}

	/**
	 * @param registration
	 */
	public void add(final HandlerRegistration registration) {
		registrations.add(registration);
	}

	/**
	 * @see com.google.web.bindery.event.shared.HandlerRegistration#removeHandler()
	 */
	@Override
	public void removeHandler() {
		for (final HandlerRegistration r : registrations) {
			r.removeHandler();
		}
		registrations.clear();
	}
}
