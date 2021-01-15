/*
 * Copyright 2021 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.research.carrot2.search;

import java.util.*;

import org.topicquests.os.asr.info.InformationEnvironment;
import org.topicquests.os.asr.info.api.IInfoOcean;
import org.topicquests.research.carrot2.Environment;
import org.topicquests.research.carrot2.nlp.ElasticSearch;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class VagabondThread {
	private Environment environment;
	private InformationEnvironment oceanEnvironment;
	private IInfoOcean dsl;
	private ElasticSearch es;

	private List<String> queries;
	private boolean isRunning = true;
	private Worker worker = null;
	
	/**
	 * @param env
	 */
	public VagabondThread(Environment env) {
		environment = env;
		oceanEnvironment = new InformationEnvironment();
		dsl = oceanEnvironment.getDSL();
		es = environment.getElasticSearch();
		queries = new ArrayList<String>();
	}

	public void addDoc(String query) {
		if (worker == null) {
			worker = new Worker();
			worker.start();
		}
		synchronized(queries) {
			queries.add(query);
			queries.notify();
		}
	}
	
	public void shutDown() {
		synchronized(queries) {
			isRunning = false;
			queries.notify();
		}
	}
	
	class Worker extends Thread {
		
		public void run() {
			String doc = null;
			while (isRunning) {
				synchronized(queries) {
					if (queries.isEmpty()) {
						try {
							queries.wait();
						} catch (Exception e) {}
						
					} else {
						doc = queries.remove(0);
					}
				}
				if (isRunning && doc != null) {
					processQuery(doc);
					doc = null;
				}
			}
		}
		
		void processQuery(String query) {
			int begin = 0;
			int count = -1; // ignore cound
			////////
			//TODO
			// If we want an exhaustic query, might limit to 100 rather than -1
			// and repeat until it returns less that count
			////////
			IResult r  = es.get(query, begin, count);
			List<JSONObject> hits = (List<JSONObject>)r.getResultObject();
			if (hits != null && !hits.isEmpty()) {
				Iterator<JSONObject> itr = hits.iterator();
				while (itr.hasNext())
					processHit(itr.next(), query);
			}
			//TODO
		}
		
		void processHit(JSONObject doc, String query) {
			// get the wordgramID
			String gramId = dsl.wordsToGramId(query);
			environment.logDebug("VagabondThread.ph "+query+" | "+gramId);
			// get the wordGramId
			String docId = doc.getAsString("id");
			dsl.connectKeyWordGramToDocument(gramId, docId);
		}
	}


}
