/**
 * 
 */
package ca.jhosek.main.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * session chat event happens
 * 
 * @author andy
 * 
 */
public class SessionChatEvent extends GwtEvent<SessionChatEvent.Handler> {

	public interface Handler extends EventHandler {
		/**
		 * @param event
		 *            the {@link SessionChatEvent} that was fired
		 */
		void onSessionChat(SessionChatEvent event);
	}

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<SessionChatEvent.Handler> getType() {
		return TYPE;
	}

	/**
	 * for this session id
	 */
	private final String sessionId;

	/**
	 * add this chat line
	 */
	private final String chat;

	/**
	 * Handler type.
	 */
	public static Type<SessionChatEvent.Handler> TYPE = new Type<SessionChatEvent.Handler>();

	/**
	 * 
	 */
	public SessionChatEvent(final String sessionId, final String chat) {
		this.sessionId = sessionId;
		this.chat = chat;
	}

	/**
	 * boiler plate dispatch an event to this handler
	 * 
	 * @see com.google.web.bindery.event.shared.Event#dispatch(java.lang.Object)
	 */
	@Override
	protected void dispatch(final Handler handler) {
		handler.onSessionChat(this);

	}

	/**
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public Type<SessionChatEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	/**
	 * @return the chat string
	 */
	public String getChat() {
		return chat;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

}
