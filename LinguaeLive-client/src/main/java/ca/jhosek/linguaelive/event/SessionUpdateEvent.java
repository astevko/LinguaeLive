/**
 * 
 */
package ca.jhosek.main.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * session update event happens
 * 
 * @author andy
 * 
 */
public class SessionUpdateEvent extends GwtEvent<SessionUpdateEvent.Handler> {

	public interface Handler extends EventHandler {
		/**
		 * @param event
		 *            the {@link SessionUpdateEvent} that was fired
		 */
		void onSessionUpdate(SessionUpdateEvent event);
	}

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<SessionUpdateEvent.Handler> getType() {
		return TYPE;
	}

	protected final String sessionId;

	/**
	 * Handler type.
	 */
	public static Type<SessionUpdateEvent.Handler> TYPE = new Type<SessionUpdateEvent.Handler>();

	/**
	 * 
	 */
	public SessionUpdateEvent(final String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * boiler plate dispatch an event to this handler
	 * 
	 * @see com.google.web.bindery.event.shared.Event#dispatch(java.lang.Object)
	 */
	@Override
	protected void dispatch(final Handler handler) {
		handler.onSessionUpdate(this);

	}

	/**
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public Type<SessionUpdateEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

}
