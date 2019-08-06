/**
 * 
 */
package ca.jhosek.main.client;


import com.google.gwt.activity.shared.Activity;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author andy
 *
 */
public class ActivityAsyncProxyProvider<T extends Activity> implements
		Provider<ActivityAsyncProxy<T>> {
	
	@Inject
	Provider<ActivityAsyncProxy<T>> provider;

	@Override
	public ActivityAsyncProxy<T> get() {
		return provider.get();
	}
}