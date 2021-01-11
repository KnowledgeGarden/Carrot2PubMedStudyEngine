/*
 * Copyright 2021 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.research.carrot2.nlp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;

import org.topicquests.es.ProviderEnvironment;
import org.topicquests.es.api.IClient;
import org.topicquests.es.util.TextQueryUtil;
import org.topicquests.hyperbrane.api.IDocument;
import org.topicquests.research.carrot2.Environment;
import org.topicquests.research.carrot2.api.IConstants;
import org.topicquests.research.carrot2.pubmed.JSONDocumentObject;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class ElasticSearch {
	private Environment environment;
	private SpaCyThread nlp;
	private ProviderEnvironment esEnvironment;
    private IClient provider;
    private TextQueryUtil textQueryUtil;
	private List<IDocument> docs;
	private boolean isRunning = true;
	private Worker worker = null;

	/**
	 * 
	 */
	public ElasticSearch(Environment env) {
		environment = env;
		nlp = new SpaCyThread(environment);
		esEnvironment = environment.getElasticSearchEnvironment();
		provider = esEnvironment.getProvider();
		textQueryUtil = esEnvironment.getTextQueryUtil();
		docs = new ArrayList<IDocument>();
	}
	
	/**
	 * At least {@code label} or {@code absText}
	 * must not be {@code null}
	 * @param id
	 * @param label can be {@code null}
	 * @param absText can be {@code null}
	 * @return
	 */
	public IResult put(String id, String label, String absText) {
		JSONObject jo = new JSONObject();
		jo.put("id", id);
		if (label != null)
			jo.put("label", label);
		if (absText != null)
			jo.put("abstract", absText);
		return put(id, jo);
	}
	/**
	 * Index {@code doc}
	 * @param id
	 * @param doc {id, label, abstract}
	 * @return
	 */
	IResult put(String id, JSONObject doc) {
		IResult result = null;
		result = provider.put(id, IConstants.ES_INDEX_NAME, doc);
		environment.logDebug("ElasticSearch.put "+id+" "+result.getErrorString()+"\n"+result.getResultObject());
		return result;
	}
	
	/**
	 * Should return a List<JSONObject>
	 * @param term
	 * @param start
	 * @param count
	 * @return
	 */
	public IResult get(String term, int start, int count) {
	    String [] indices = new String [1];
	    indices[0] = IConstants.ES_INDEX_NAME;
	    String [] fields = new String[2];
	    fields[0] = "label";
	    fields[1] = "abstract";

		IResult result = textQueryUtil.queryText(term, start, count, IConstants.ES_INDEX_NAME, indices, fields);

		return result;
	}
	
	public void addDoc(IDocument doc) {
		if (worker == null) {
			worker = new Worker();
			worker.start();
		}
		environment.logDebug("ElasticSearch.add "+worker);
		synchronized(docs) {
			docs.add(doc);
			docs.notify();
		}
	}
	
	public void shutDown() {
		synchronized(docs) {
			isRunning = false;
			docs.notify();
		}
	}
	
	class Worker extends Thread {
		
		public void run() {
			IDocument doc = null;
			while (isRunning) {
				synchronized(docs) {
					if (docs.isEmpty()) {
						try {
							docs.wait();
						} catch (Exception e) {}
						
					} else {
						doc = docs.remove(0);
					}
				}
				if (isRunning && doc != null) {
					processDoc(doc);
					doc = null;
				}
			}
		}
		
		
		void processDoc(IDocument doc) {
			String id = doc.getId();
			environment.logDebug("ElasticSearch.process "+id);
			List<String> labels = doc.listLabels();
			List<JSONObject> abstracts = doc.listAbstracts();
			environment.logDebug("ElasticSearch.process-1\n"+abstracts);
			// key = language, value = String
			// we don't care about language for now
			if (labels != null && !labels.isEmpty()) {
				Iterator<String> itr = labels.iterator();
				while (itr.hasNext())
					put(id, itr.next(), null);
			}
			if (abstracts != null && !abstracts.isEmpty()) {
				Iterator<JSONObject> itj = abstracts.iterator();
				List<Object> y = new ArrayList<Object>();
				while (itj.hasNext()) {
					y.clear();
					y.addAll(itj.next().values());
					if (!y.isEmpty()) {
						environment.logDebug("ES-X");
						//send it off to SpaCy
						nlp.addDoc(id, (String)y.get(0));
						//index this
						put(id, null, (String)y.get(0));
					}
				}
			}
			
		}
		
	}
}
