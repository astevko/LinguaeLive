/**
 * 
 */
package ca.jhosek.main.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * session invite update event happens
 * 
 * @author andy
 * 
 */
public class SessionInviteUpdateEvent extends
		GwtEvent<SessionInviteUpdateEvent.Handler> {

	public interface Handler extends EventHandler {
		/**
		 * @param event
		 *            the {@link SessionInviteUpdateEvent} that was fired
		 */
		void onSessionInviteUpdate(SessionInviteUpdateEvent event);
	}

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<SessionInviteUpdateEvent.Handler> getType() {
		return TYPE;
	}

	/**
	 * session invite id that is updating
	 */
	protected final String sessionInviteId;

	/**
	 * Handler type.
	 */
	public static Type<SessionInviteUpdateEvent.Handler> TYPE = new Type<SessionInviteUpdateEvent.Handler>();

	/**
	 * 
	 */
	public SessionInviteUpdateEvent(final String sessionInviteId) {
		this.sessionInviteId = sessionInviteId;
	}

	/**
	 * boiler plate dispatch an event to this handler
	 * 
	 * @see com.google.web.bindery.event.shared.Event#dispatch(java.lang.Object)
	 */
	@Override
	protected void dispatch(final Handler handler) {
		handler.onSessionInviteUpdate(this);

	}

	/**
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public Type<SessionInviteUpdateEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	/**
	 * @return the SessionInviteId
	 */
	public String getSessionInviteId() {
		return sessionInviteId;
	}
}
