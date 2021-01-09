/**
 * Copyright 2018, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.hyperbrane;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.topicquests.hyperbrane.api.IAuthor;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class AuthorPojo extends JSONObject implements IAuthor {

	/**
	 * 
	 */
	public AuthorPojo() {
	}

	/**
	 * @param map
	 */
	public AuthorPojo(JSONObject map) {
		super(map);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#setAuthorTitle(java.lang.String)
	 */
	@Override
	public void setAuthorTitle(String title) {
		put(IAuthor.TITLE_FIELD, title);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#getAuthorTitle()
	 */
	@Override
	public String getAuthorTitle() {
		return getAsString(IAuthor.TITLE_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#setAuthorFullName(java.lang.String)
	 */
	@Override
	public void setAuthorFullName(String fullName) {
		put(IAuthor.FULL_NAME_FIELD, fullName);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#getAuthorFullName()
	 */
	@Override
	public String getAuthorFullName() {
		String result = (IAuthor.FULL_NAME_FIELD);
		if (result == null) {
			String firstName = null;
			List<String> l = listAuthorFirstNames();
			if (l != null && !l.isEmpty())
				firstName = l.get(0);
			if (firstName == null)
				firstName = getInitials();
			if (firstName == null)
				firstName = getAuthorNickName();
			result = firstName+" "+getAuthorLastName();
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#setAuthorFirstName(java.lang.String)
	 */
	@Override
	public void addAuthorFirstName(String firstName) {
		List<String> l = listAuthorFirstNames();
		if (l == null)
			l = new ArrayList<String>();
		if (!l.contains(firstName))
			l.add(firstName);
		put(IAuthor.FIRST_NAME_FIELD, l);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#getAuthorFirstName()
	 */
	@Override
	public List<String> listAuthorFirstNames() {
		return (List<String>)get(IAuthor.FIRST_NAME_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#setAuthorMiddleName(java.lang.String)
	 */
	@Override
	public void setAuthorMiddleName(String middleName) {
		put(IAuthor.MIDDLE_NAME_FIELD, middleName);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#getAuthorMiddleName()
	 */
	@Override
	public String getAuthorMiddleName() {
		return getAsString(IAuthor.MIDDLE_NAME_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#setAuthorLastName(java.lang.String)
	 */
	@Override
	public void setAuthorLastName(String lastName) {
		put(IAuthor.LAST_NAME_FIELD, lastName);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#getAuthorLastName()
	 */
	@Override
	public String getAuthorLastName() {
		return getAsString(IAuthor.LAST_NAME_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#setAuthorJrSr(java.lang.String)
	 */
	@Override
	public void setAuthorSuffix(String t) {
		put(IAuthor.SUFFIX, t);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#getAuthorJrSr()
	 */
	@Override
	public String getAuthorSuffix() {
		return getAsString(IAuthor.SUFFIX);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#setAuthorDegree(java.lang.String)
	 */
	@Override
	public void setAuthorDegree(String deg) {
		put(IAuthor.DEGREE_FIELD, deg);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#getAuthorDegree()
	 */
	@Override
	public String getAuthorDegree() {
		return getAsString(IAuthor.DEGREE_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#setAuthorInitials(java.lang.String)
	 */
	@Override
	public void setAuthorInitials(String initials) {
		put(IAuthor.INITIALS_FIELD, initials);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#getInitials()
	 */
	@Override
	public String getInitials() {
		return getAsString(IAuthor.INITIALS_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#setAuthorNickName(java.lang.String)
	 */
	@Override
	public void setAuthorNickName(String name) {
		put(IAuthor.NICK_NAME_FIELD, name);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#getAuthorNickName()
	 */
	@Override
	public String getAuthorNickName() {
		return getAsString(IAuthor.NICK_NAME_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#setDocumentId(java.lang.String)
	 */
	@Override
	public void setDocumentId(String id) {
		put(IAuthor.DOC_ID_FIELD, id);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#getDocumentId(java.lang.String)
	 */
	@Override
	public String getDocumentId(String id) {
		return getAsString(IAuthor.DOC_ID_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#setDocumentTitle(java.lang.String)
	 */
	@Override
	public void setDocumentTitle(String title) {
		put(IAuthor.DOC_TITLE_FIELD, title);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#getDocumentTitle()
	 */
	@Override
	public String getDocumentTitle() {
		return getAsString(IAuthor.DOC_TITLE_FIELD);
	}


	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#setPublisherName(java.lang.String)
	 */
	@Override
	public void setPublisherName(String name) {
		put(IAuthor.PUBLISHER_NAME_FIELD, name);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#getPublisherName()
	 */
	@Override
	public String getPublisherName() {
		return getAsString(IAuthor.PUBLISHER_NAME_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#setPublisherLocator(java.lang.String)
	 */
	@Override
	public void setPublisherLocator(String locator) {
		put(IAuthor.PUBLISHER_LOCATOR_FIELD, locator);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#getPublisherLocator()
	 */
	@Override
	public String getPublisherLocator() {
		return getAsString(IAuthor.PUBLISHER_LOCATOR_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#setPublicationName(java.lang.String)
	 */
	@Override
	public void setPublicationName(String name) {
		put(IAuthor.PUBLICATION_NAME_FIELD, name);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#getPublicationName()
	 */
	@Override
	public String getPublicationName() {
		return getAsString(IAuthor.PUBLICATION_NAME_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#setPublicationLocator(java.lang.String)
	 */
	@Override
	public void setPublicationLocator(String locator) {
		put(IAuthor.PUBLICATION_LOCATOR, locator);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IAuthor#getPublicationLocator()
	 */
	@Override
	public String getPublicationLocator() {
		return getAsString(IAuthor.PUBLICATION_LOCATOR);
	}


	@Override
	public void setAffiliationLocator(String locator) {
		put(IAuthor.AFFILIATION_LOCATOR, locator);
	}

	@Override
	public String getAffiliationLocator() {
		return getAsString(IAuthor.AFFILIATION_LOCATOR);
	}

	@Override
	public void setAuthorLocator(String locator) {
		put(IAuthor.AUTHOR_LOCATOR, locator);
	}

	@Override
	public String getAuthorLocator() {
		return getAsString(IAuthor.AUTHOR_LOCATOR);
	}

	@Override
	public void setId(String id) {
		put(IAuthor.ID, id);
	}

	@Override
	public String getId() {
		return getAsString(IAuthor.ID);
	}

	@Override
	public void addAffiliationName(String name) {
		List<String>l = listAffiliationNames();
		if (l == null)
			l = new ArrayList<String>();
		if (!l.contains(name))
			l.add(name);
		put(IAuthor.AFFILIATION_NAME_FIELD, l);
		
	}

	@Override
	public List<String> listAffiliationNames() {
		return (List<String>)get(IAuthor.AFFILIATION_NAME_FIELD);
	}

	@Override
	public void setAuthorEmail(String email) {
		put(IAuthor.EMAIL_FIELD, email);
	}

	@Override
	public String getAuthorEmail() {
		return getAsString(IAuthor.EMAIL_FIELD);
	}
	
}
