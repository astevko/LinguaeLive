package ca.jhosek.main.client.widgets;

import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

public class OnlineOfflineIndicator extends Composite
implements IsEditor<LeafValueEditor<Boolean>>, TakesValue<Boolean>, LeafValueEditor<Boolean>{

	/**
	 * trinary or more states
	 * @author andy
	 *
	 */
	enum OnlineState { CONNECTING, OFFLINE, ONLINE };
	
	
	// TODO - move to static messages 
	static final String onlineHtml = "<b>Online</b>";
	
	static final String offlineHtml = "Offline";
	
	static final String init = "";//<i>connecting...</i>";
	
	static final String reinit = "reconnecting...";

	private final HTML widget = new HTML(init);
	
	/**
	 * null means initializing; otherwise online/offline
	 */
	private Boolean value = null;

	/**
	 * used to ID tag the widget in the DOM
	 */
	private final String contextId;
	
	private static final String BASE_ID = "onlineIndicator"; 

	public OnlineOfflineIndicator(final String contextId) {
		this.contextId = contextId;
		//
		initWidget(widget);
	}
		
	public OnlineOfflineIndicator() {
		//
		contextId = "default";
		initWidget(widget);
	}
	
	@Override
	public void setValue(Boolean value) {
		this.value = value;
		//
		widget.setHTML(this.asSafeHtml());
	}

	@Override
	public Boolean getValue() {
		// 
		return value;
	}

	@Override
	public LeafValueEditor<Boolean> asEditor() {
		// 
		return this;
	}
	
	/**
	 * @return 
	 */
	public Boolean isOnline() {
		return value != null && value.equals(true); 
	}

	/**
	 * @return full HTML tag
	 */
	SafeHtml asSafeHtml() {
		//<span style='background:green'>ONLINE</span>
		StringBuffer div = new StringBuffer("<div ");	// open div
		div.append( (isOnline()) ? " style='color:green' " : " style='color:red' " ); // color it
		div.append( " id='" + BASE_ID + "." + contextId + "' "); // ID tag it
		div.append( ">"); // close attributes 
		if (value==null) {
			div.append(init); // content
		} else if (value) {
			div.append(onlineHtml);
		} else {
			div.append(offlineHtml);
		}
		div.append( "</div>") ; // close it
		return SafeHtmlUtils.fromTrustedString(div.toString());
	}

	public void setOnlineState(OnlineState onlineState) {
		if (onlineState.equals(OnlineState.CONNECTING)) {
			setValue(null);
		} else {
			setValue(onlineState.equals(OnlineState.ONLINE));
		}
	}
}
