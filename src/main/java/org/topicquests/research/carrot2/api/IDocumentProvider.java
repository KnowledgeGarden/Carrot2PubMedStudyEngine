/*
 * Copyright 2018 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.research.carrot2.api;

import java.util.Iterator;

import org.topicquests.hyperbrane.api.IDocument;
import org.topicquests.ks.api.ITicket;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public interface IDocumentProvider {

	Iterator<JSONObject> iterateDocuments();
	
	IResult getDocument(String locator, ITicket credentials);
	
	IResult putDocument(IDocument node);
	
	IResult updateDocument(IDocument node);

}
