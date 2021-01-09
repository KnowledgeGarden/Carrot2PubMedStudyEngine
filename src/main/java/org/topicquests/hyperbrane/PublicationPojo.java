/*
 * Copyright 2018 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.hyperbrane;

import java.util.*;

import org.topicquests.hyperbrane.api.IGrant;
import org.topicquests.hyperbrane.api.IPublication;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class PublicationPojo extends JSONObject implements IPublication {

	/**
	 * 
	 */
	public PublicationPojo() {
	}

	/**
	 * @param map
	 */
	public PublicationPojo(JSONObject map) {
		super(map);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		put(IPublication.TITLE_FIELD, title);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#getTitle()
	 */
	@Override
	public String getTitle() {
		return getAsString(IPublication.TITLE_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#setPublicationName(java.lang.String)
	 */
	@Override
	public void setPublicationName(String name) {
		put(IPublication.PUBLICATION_NAME_FIELD, name);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#getPublicationName()
	 */
	@Override
	public String getPublicationName() {
		return getAsString(IPublication.PUBLICATION_NAME_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#setPublicationDate(java.lang.String)
	 */
	@Override
	public void setPublicationDate(String date) {
		put(IPublication.PUBLICATION_DATE_FIELD, date);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#getPublicationDate()
	 */
	@Override
	public String getPublicationDate() {
		return getAsString(IPublication.PUBLICATION_DATE_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#setPublicationYear(java.lang.String)
	 */
	@Override
	public void setPublicationYear(String year) {
		put(IPublication.PUBLICATION_YEAR_FIELD, year);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#getPublicationYear()
	 */
	@Override
	public String getPublicationYear() {
		return getAsString(IPublication.PUBLICATION_YEAR_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#setPubicationVolume(java.lang.String)
	 */
	@Override
	public void setPubicationVolume(String volume) {
		put(IPublication.PUBLICATION_VOLUME_FIELD, volume);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#getPublicationVolume()
	 */
	@Override
	public String getPublicationVolume() {
		return getAsString(IPublication.PUBLICATION_VOLUME_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#setPublicationNumber(java.lang.String)
	 */
	@Override
	public void setPublicationNumber(String number) {
		put(IPublication.PUBLICATION_NUMBER_FIELD, number);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#getPublicationNumber()
	 */
	@Override
	public String getPublicationNumber() {
		return getAsString(IPublication.PUBLICATION_NUMBER_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#setPages(java.lang.String)
	 */
	@Override
	public void setPages(String pages) {
		put(IPublication.PAGES_FIELD, pages);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#getPages()
	 */
	@Override
	public String getPages() {
		return getAsString(IPublication.PAGES_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#setPublisherName(java.lang.String)
	 */
	@Override
	public void setPublisherName(String name) {
		put(IPublication.PUBLISHER_NAME_FIELD, name);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#getPublisherName()
	 */
	@Override
	public String getPublisherName() {
		return getAsString(IPublication.PUBLISHER_NAME_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#setPublisherLocation(java.lang.String)
	 */
	@Override
	public void setPublisherLocation(String location) {
		put(IPublication.PUBLISHER_LOCATION_FIELD, location);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#getPublisherLocation()
	 */
	@Override
	public String getPublisherLocation() {
		return getAsString(IPublication.PUBLISHER_LOCATION_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#setDOI(java.lang.String)
	 */
	@Override
	public void setDOI(String doi) {
		put(IPublication.DOI_FIELD, doi);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#getDOI()
	 */
	@Override
	public String getDOI() {
		return getAsString(IPublication.DOI_FIELD);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#setISSN(java.lang.String)
	 */
	@Override
	public void setISSN(String issn) {
		put(IPublication.ISSN_FIELD, issn);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IPublication#getISSN()
	 */
	@Override
	public String getISSN() {
		return getAsString(IPublication.ISSN_FIELD);
	}

	@Override
	public void setPublicationType(String type) {
		put(IPublication.PUBLICATION_TYPE_FIELD, type);
	}

	@Override
	public String getPublicationType() {
		return getAsString(IPublication.PUBLICATION_TYPE_FIELD);
	}

	@Override
	public void setISOAbbreviation(String abbrev) {
		put(IPublication.ISO_ABBREVIATION_FIELD, abbrev);
	}

	@Override
	public String getISOAbbreviation() {
		return getAsString(IPublication.ISO_ABBREVIATION_FIELD);
	}

	@Override
	public void setPublicationLocator(String locator) {
		put(IPublication.PUBLICATION_LOCATOR_FIELD, locator);
	}

	@Override
	public String getPublicationLocator() {
		return getAsString(IPublication.PUBLICATION_LOCATOR_FIELD);
	}

	@Override
	public void setPublisherLocator(String locator) {
		put(IPublication.PUBLISHER_LOCATOR_FIELD, locator);
	}

	@Override
	public String getPublisherLocator() {
		return getAsString(IPublication.PUBLISHER_LOCATOR_FIELD);
	}

	@Override
	public void setISBN(String isbn) {
		put(IPublication.ISBN_FIELD, isbn);
	}

	@Override
	public String getISBN() {
		return getAsString(IPublication.ISBN_FIELD);
	}

	@Override
	public void setDocumentLocator(String locator) {
		put(IPublication.DOCUMENT_LOCATOR, locator);
	}

	@Override
	public String getDocumentLocator() {
		return getAsString(IPublication.DOCUMENT_LOCATOR);
	}


	@Override
	public void addGrant(IGrant g) {
		List<IGrant> l = listGrants();
		if (l == null)
			l = new ArrayList<IGrant>();
		if (!l.contains(g))
			l.add(g);
		put(IPublication.GRANT_LIST, l);
	}

	@Override
	public List<IGrant> listGrants() {
		return (List<IGrant>)get(IPublication.GRANT_LIST);
	}

	@Override
	public void setCopyright(String c) {
		put(IPublication.COPYRIGHT_FIELD, c);
	}

	@Override
	public String getCopyright() {
		return getAsString(IPublication.COPYRIGHT_FIELD);
	}

	@Override
	public void setMonth(String month) {
		put(IPublication.PUBLICATION_MONTH_FIELD, month);
	}

	@Override
	public String getMonth() {
		return getAsString(IPublication.PUBLICATION_MONTH_FIELD);
	}

}
