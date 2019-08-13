// package ca.jhosek.linguaelive.activity.mainregion;

// import java.util.Set;
// import java.util.logging.Logger;

// import javax.validation.ConstraintViolation;

// import com.google.gwt.user.client.Window;
// import com.google.inject.Inject;
// import com.google.web.bindery.requestfactory.shared.EntityProxyId;
// import com.google.web.bindery.requestfactory.shared.Receiver;
// import com.google.web.bindery.requestfactory.shared.ServerFailure;
// import com.googlecode.objectify.annotation.Id;

// import ca.jhosek.linguaelive.ui.priv.MyProfileView.Presenter;
// import ca.jhosek.linguaelive.domain.ContactInfo;
// import ca.jhosek.linguaelive.ContactInfoType;
// import ca.jhosek.linguaelive.proxy.AppRequestFactory;
// import ca.jhosek.linguaelive.proxy.ContactInfoProxy;
// import ca.jhosek.linguaelive.proxy.ContactInfoRequestContext;
// import ca.jhosek.linguaelive.proxy.UserProxy;

// /**
//  * sort of a virtual ContactInfo Proxy to add a ADD Button to the ContactInfo Celltable 
//  *   
//  * @author copyright (C) 2011 Andrew Stevko
//  *	@see ContactInfo
//  */
// public class AddMeContactInfoProxy implements ContactInfoProxy {


// 	private static final Logger logger = Logger.getLogger( AddMeContactInfoProxy.class.getName());

	
// 	private static final String CLICK_TO_ADD_YOUR_NUMBER_ID = "click to add your number/id";

// 	@Id
// 	private Long id = null;

// 	transient private UserProxy owner = null;

// 	private ContactInfoType type = ContactInfoType.Other;

// 	private String info = CLICK_TO_ADD_YOUR_NUMBER_ID;

// 	private Boolean preferred = false;

// 	private final AppRequestFactory requestFactory;


// 	@Inject
// 	public AddMeContactInfoProxy(
// 			AppRequestFactory requestFactory
// 			) {
// 				this.requestFactory = requestFactory;
// 		// 
// 	}

	
// 	/**
// 	 * 
// 	 * @param presenter 
// 	 * @return a real contactInfoProxy for this virt proxy
// 	 */
// 	public void addProxy(final Presenter presenter ){ 
// 		ContactInfoRequestContext contactInfoContext = requestFactory.contactInfoRequest();
// 		ContactInfoProxy newContactInfo = contactInfoContext.create( ContactInfoProxy.class 	);
// 		// transfer fields
// 		newContactInfo.setInfo(this.info);
// 		newContactInfo.setType(this.type);
// 		newContactInfo.setPreferred(this.preferred);
		
// 		contactInfoContext.persist(newContactInfo).to( new Receiver<Void>() {
// //			contactInfoContext.update(newContactInfo).to( new Receiver<ContactInfoProxy>() {

// 			@Override
// 			public void onSuccess(Void/*ContactInfoProxy*/ response) {
// 				// added!!
// 				logger.info("ContactInfoProxy added successfully" );
// 				// add to this list
// 				//	addToThisListDataProvider.getList().add(response);
// 				// clear fields and prepare for next add
// 				//	info = CLICK_TO_ADD_YOUR_NUMBER_ID;
// 				//---------
// 				// ok, Its saved...
// //				presenter.loadContactInfo();
				
// 			}

// 			/* (non-Javadoc)
// 			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onFailure(com.google.web.bindery.requestfactory.shared.ServerFailure)
// 			 */
// 			@Override
// 			public void onFailure(ServerFailure error) {
// 				//				
// 				logger.severe( error.getMessage() );
// 				Window.alert("Add failed, please try again." );
// 			}

// 			/* (non-Javadoc)
// 			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onConstraintViolation(java.util.Set)
// 			 */
// 			@Override
// 			public void onConstraintViolation(Set<ConstraintViolation<?>> errors) {
// 				// 
// 				StringBuffer msg = new StringBuffer("Incomplete or errors found. Please correct and resubmit." );
// 				for( ConstraintViolation<?> vio : errors ) {
// 					msg.append( "\n* " );
// 					msg.append( vio.getMessage() );
// 				}
// 				Window.alert( msg.toString() );
// 				super.onConstraintViolation(errors);
// 			}
			
			
// 		}).fire();
// 	}
// 	/**
// 	 * @param id the id to set
// 	 */
// 	public void setId(Long id) {
// 		this.id = id;
// 	}

// 	/**
// 	 * @param type the type to set
// 	 */
// 	public void setType(ContactInfoType type) {
// 		this.type = type;
// 	}

// 	/**
// 	 * @param preferred the preferred to set
// 	 */
// 	public void setPreferred(Boolean preferred) {
// 		this.preferred = preferred;
// 	}

// 	public Integer getVersion() {
// 		// 
// 		return -1;
// 	}

// 	public EntityProxyId<?> stableId() {
// 		// 
// 		return null;
// 	}

// 	public UserProxy getOwner() {
// 		// 
// 		return owner;
// 	}

// 	public void setOwner(UserProxy owner) {
// 		// 
// 		this.owner = owner;
// 	}

// 	public Long getId() {
// 		// 
// 		return id;
// 	}

// 	public String getInfo() {
// 		// 
// 		return info;
// 	}

// 	public Boolean getPrefe1rred() {
// 		// 
// 		return preferred;
// 	}

// 	public ContactInfoType getType() {
// 		// 
// 		return type;
// 	}

// 	public void setInfo(String info) {
// 		// 
// 		this.info = info;

// 	}


// 	public Boolean getPreferred() {
// 		// 
// 		return preferred;
// 	}
// }
