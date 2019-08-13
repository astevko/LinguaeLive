/**
 * 
 */
package ca.jhosek.linguaelive.domain;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.*;

import ca.jhosek.linguaelive.LanguageType;

/**
 * a Course
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see CourseDao
 * 
 */
@Entity
public class Course extends DatastoreObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2398416359335439934L;

	private String description = "";

	@Index
	private Date endDate;

	@Index
	private Long estimatedMemberSize;
	
	@Index
	private LanguageType expertLanguage;

	private String name = "";
	
	@Index
	private Date startDate;
	
	@Index
	private LanguageType targetLanguage;

	private String schoolName;
	
	private Boolean singlePartnerPreferred;
	
	@Ignore
	transient private User owner;
	
	@Index
	private Key<User> ownerKey;
	
	/**
	 * user switch to try to get users to have one partner only.
	 */
	private Boolean onePartnerOnlyPreferred = new Boolean(false);
	/**
	 * code string used within invitations for students
	 * TODO: should be unique
	 */
	@Index
	private String inviteCode;
	
	/**
	 * c-tor
	 */
	public Course() {
		// 
       	// set the invite code as a unique string by converting the time to a radix 32 string 
		Date now = new Date();
		setInviteCode( Long.toString(now.getTime(), 32 ).toUpperCase() );
		
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @return the estimatedMemberSize
	 */
	public Long getEstimatedMemberSize() {
		return estimatedMemberSize;
	}
	/**
	 * @return the expertLanguage
	 */
	public LanguageType getExpertLanguage() {
		return expertLanguage;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @return the targetLanguage
	 */
	public LanguageType getTargetLanguage() {
		return targetLanguage;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @param estimatedMemberSize the estimatedMemberSize to set
	 */
	public void setEstimatedMemberSize(Long estimatedMemberSize) {
		this.estimatedMemberSize = estimatedMemberSize;
	}
	/**
	 * @param expertLanguage the expertLanguage to set
	 */
	public void setExpertLanguage(LanguageType expertLanguage) {
		this.expertLanguage = expertLanguage;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @param targetLanguage the targetLanguage to set
	 */
	public void setTargetLanguage(LanguageType targetLanguage) {
		this.targetLanguage = targetLanguage;
	}
	/**
	 * get owner of class
	 * @return
	 */
	public User getOwner() {
		try {
			this.owner = new UserDao().get( ownerKey );
		} catch (EntityNotFoundException e) {
			// 
			e.printStackTrace();
		}
		return owner;
	}
	/**
	 * set who owns this class
	 * @param owner
	 */
	public void setOwner( User owner ) {
		this.owner = owner;	// transient
		this.ownerKey = new UserDao().key(owner);
	}
	public void setOwnerKey(Key<User> ownerKey) {
		this.ownerKey = ownerKey;
	}
	public Key<User> getOwnerKey() {
		return ownerKey;
	}
	/**
	 * @param schoolName the schoolName to set
	 */
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	/**
	 * @return the schoolName
	 */
	public String getSchoolName() {
		return schoolName;
	}
	/**
	 * @param inviteCode
	 */
	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
	/**
	 * @return invite code
	 */
	public String getInviteCode() {
		return inviteCode;
	}
	/**
	 * @return the onePartnerOnlyPreferred
	 */
	public Boolean getOnePartnerOnlyPreferred() {
		if (onePartnerOnlyPreferred == null) {
			return false;
		}
		return onePartnerOnlyPreferred;
	}
	/**
	 * @param onePartnerOnlyPreferred the onePartnerOnlyPreferred to set
	 */
	public void setOnePartnerOnlyPreferred(Boolean onePartnerOnlyPreferred) {
		this.onePartnerOnlyPreferred = onePartnerOnlyPreferred;
	}
	/**
	 * @param singlePartnerPreferred the singlePartnerPreferred to set
	 */
	public void setSinglePartnerPreferred(Boolean singlePartnerPreferred) {
		this.singlePartnerPreferred = singlePartnerPreferred;
	}
	/**
	 * @return the singlePartnerPreferred
	 */
	public Boolean getSinglePartnerPreferred() {
		if (singlePartnerPreferred == null) {
			return false;
		}
		return singlePartnerPreferred;
	}
}
