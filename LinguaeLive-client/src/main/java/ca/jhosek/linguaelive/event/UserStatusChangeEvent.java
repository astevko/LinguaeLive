/**
 * 
 */
package ca.jhosek.main.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * user status changed
 * 
 * @author andy
 * 
 */
public class UserStatusChangeEvent extends
		GwtEvent<UserStatusChangeEvent.Handler> {

	public interface Handler extends EventHandler {
		/**
		 * @param event
		 *            the {@link UserStatusChangeEvent} that was fired
		 */
		void onUserStatusChange(UserStatusChangeEvent event);
	}

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<UserStatusChangeEvent.Handler> getType() {
		return TYPE;
	}

	/**
	 * email address for this user
	 */
	private final String emailAddress;

	/**
	 * status = OFFLINE | ONLINE | INSESSION | UNKNOWN
	 */
	private final String status;

	/**
	 * timestamp
	 */
	private final Long timestamp;

	/**
	 * Handler type.
	 */
	public static Type<UserStatusChangeEvent.Handler> TYPE = new Type<UserStatusChangeEvent.Handler>();

	/**
	 * 
	 */
	public UserStatusChangeEvent(final String emailAddress,
			final String status, final Long timestamp) {
		this.emailAddress = emailAddress;
		this.status = status;
		this.timestamp = timestamp;
	}

	/**
	 * boiler plate dispatch an event to this handler
	 * 
	 * @see com.google.web.bindery.event.shared.Event#dispatch(java.lang.Object)
	 */
	@Override
	protected void dispatch(final Handler handler) {
		handler.onUserStatusChange(this);

	}

	/**
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public Type<UserStatusChangeEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return the timestamp
	 */
	public Long getTimestamp() {
		return timestamp;
	}

}
