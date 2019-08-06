/**
 * 
 */
package ca.jhosek.main.shared.proxy;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyFor;

import ca.jhosek.main.server.domain.Course;
import ca.jhosek.main.server.domain.CourseDao;
import ca.jhosek.main.server.domain.ObjectifyLocator;
import ca.jhosek.main.shared.LanguageType;

/**
 * @author copyright (C) 2011,2012 Andrew Stevko
 *
 * @see Course
 * @see CourseDao
 *
 */
@ProxyFor( value=Course.class, locator=ObjectifyLocator.class )
public interface CourseProxy extends DatastoreObjectProxy {

	/**
	 * @return the description
	 */
	public String getDescription();
	/**
	 * @return the endDate
	 */
	public Date getEndDate();
	/**
	 * @return the estimatedMemberSize
	 */
	public Long getEstimatedMemberSize();
	/**
	 * @return the expertLanguage
	 */
	public LanguageType getExpertLanguage();
	/**
	 * @return the name
	 */
	public String getName();
	/**
	 * @return the startDate
	 */
	public Date getStartDate();
	/**
	 * @return the targetLanguage
	 */
	public LanguageType getTargetLanguage();
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description);
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate);
	/**
	 * @param estimatedMemberSize the estimatedMemberSize to set
	 */
	public void setEstimatedMemberSize(Long estimatedMemberSize);
	/**
	 * @param expertLanguage the expertLanguage to set
	 */
	public void setExpertLanguage(LanguageType expertLanguage);
	/**
	 * @param name the name to set
	 */
	public void setName(String name);
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate);
	/**
	 * @param targetLanguage the targetLanguage to set
	 */
	public void setTargetLanguage(LanguageType targetLanguage);

	public void setOwner( UserProxy owner );
	public UserProxy getOwner( );

	/**
	 * @param schoolName the schoolName to set
	 */
	public void setSchoolName(String schoolName);
	/**
	 * @return the schoolName
	 */
	public String getSchoolName();

	/**
	 * @param inviteCode
	 */
	public void setInviteCode(String inviteCode);
	/**
	 * @return invite code
	 */
	public String getInviteCode();

	/**
	 * used to indicate that when students accept an invitation, they should be marked as unavailable
	 * @param singlePartnerPreferred the singlePartnerPreferred to set
	 */
	public void setSinglePartnerPreferred(Boolean singlePartnerPreferred);
	/**
	 * @return the singlePartnerPreferred
	 */
	public Boolean getSinglePartnerPreferred();

}
