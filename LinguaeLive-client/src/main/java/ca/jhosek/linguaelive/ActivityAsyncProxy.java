package ca.jhosek.main.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

/**
 * @author andy
 *
 * @param <T>
 * 
 * https://groups.google.com/forum/?fromgroups=#!searchin/google-web-toolkit/%22GWT$202.1$20ACTIVITIES$20$2B$20CODE$20SPLITTING$20$2B$20GIN?/google-web-toolkit/8_P_d4aT-0E/E7XgKLDOYZIJ
 * 
 */
public class ActivityAsyncProxy<T> implements Activity {

	@Inject
	private AsyncProvider<T> provider;
	private boolean canceled = false;
	private Activity impl;

	@Override
	public String mayStop() {
		if (impl != null) return impl.mayStop();
		return null;
	}

	@Override
	public void onCancel() {
		if (impl != null) {
			impl.onCancel();
		} else {
			canceled = true;
		}
	}

	@Override
	public void onStop() {
		if (impl != null) {
			impl.onStop();
		} else {
			canceled = true;
		}
	}

	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		provider.get(new AsyncCallback<T>() {

			@Override
			public void onSuccess(T result) {
				// Do not starts loaded activity if it has been canceled
				if (!canceled) {
					impl = (Activity) result;
					impl.start(panel, eventBus);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO : send error message
			}
		});
	}
}
