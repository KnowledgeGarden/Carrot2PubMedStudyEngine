/*
 * Copyright 2018 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.hyperbrane;

import java.util.Map;

import org.topicquests.hyperbrane.api.IGrant;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class GrantPojo extends JSONObject implements IGrant {

	/**
	 * 
	 */
	public GrantPojo() {
	}

	/**
	 * @param map
	 */
	public GrantPojo(JSONObject map) {
		super(map);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.hyperbrane.api.IGrant#setGrantId()
	 */
	@Override
	public void setGrantId(String id) {
		put(IGrant.GRANT_ID, id);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.hyperbrane.api.IGrant#getGrantId()
	 */
	@Override
	public String getGrantId() {
		return getAsString(IGrant.GRANT_ID);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.hyperbrane.api.IGrant#setAgency(java.lang.String)
	 */
	@Override
	public void setAgency(String agency) {
		put(IGrant.AGENCY, agency);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.hyperbrane.api.IGrant#getAgency()
	 */
	@Override
	public String getAgency() {
		return getAsString(IGrant.AGENCY);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.hyperbrane.api.IGrant#setCountry(java.lang.String)
	 */
	@Override
	public void setCountry(String country) {
		put(IGrant.COUNTRY, country);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.hyperbrane.api.IGrant#getCountry()
	 */
	@Override
	public String getCountry() {
		return getAsString(IGrant.COUNTRY);
	}

}
