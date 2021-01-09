/*
 * Copyright 2018 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.research.carrot2;

import java.util.*;

import org.topicquests.asr.general.document.api.IDocumentClient;
import org.topicquests.hyperbrane.ConcordanceDocument;
import org.topicquests.hyperbrane.api.IDocument;
import org.topicquests.ks.api.ITicket;
import org.topicquests.research.carrot2.api.IDocumentProvider;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class DocumentProvider implements IDocumentProvider {
	private Environment environment;
	private IDocumentClient documentDatabase;

	/**
	 * @param env
	 */
	public DocumentProvider(Environment env) {
		environment = env;
		documentDatabase = environment.getDocumentDatabase();
		environment.logDebug("DocumentProvider+ "+documentDatabase);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IDocumentProvider#getDocument(java.lang.String, org.topicquests.ks.api.ITicket)
	 */
	@Override
	public IResult getDocument(String locator, ITicket credentials) {
		IResult result = documentDatabase.get(locator);
		JSONObject jo = (JSONObject)result.getResultObject();
		if (jo != null)
			result.setResultObject(new ConcordanceDocument(environment, jo));
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.IDocumentProvider#putDocument(org.topicquests.hyperbrane.api.IDocument)
	 */
	@Override
	public IResult putDocument(IDocument node) {
		String label = node.getLabel("en");
		if (label == null)
			label  = "";
		String pmid = node.getPMID();
		if (pmid == null)
			pmid = "";
		String pmcid = node.getPMCID();
		if (pmcid == null)
			pmcid = "";
		String url = node.getURL();
		if (url == null)
			url = "";
		IResult result = documentDatabase.put(node.getId(), pmid, pmcid, url, label, node.getData());
		return result;
	}

	@Override
	public IResult updateDocument(IDocument node) {
		IResult result = documentDatabase.update(node.getId(), node.getData());
		return result;
	}

	@Override
	public Iterator<JSONObject> iterateDocuments() {
		IResult r = documentDatabase.listDocuments(0, -1);
		List<JSONObject> l = (List<JSONObject>)r.getResultObject();
		if (l == null)
			l = new ArrayList<JSONObject>();
		return l.iterator();
	}

}
