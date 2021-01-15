/*
 * Copyright 2021 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.research.carrot2.ocean;

import org.topicquests.hyperbrane.api.IAuthor;
import org.topicquests.hyperbrane.api.IDocument;
import org.topicquests.hyperbrane.api.IWordGram;
import org.topicquests.os.asr.info.InformationEnvironment;
import org.topicquests.os.asr.info.api.IAgent;
import org.topicquests.os.asr.info.api.IInfoOcean;
import org.topicquests.os.asr.info.api.IOceanConstants;
import org.topicquests.os.asr.info.api.IPerson;
import org.topicquests.research.carrot2.Environment;
//import org.topicquests.support.api.IResult;
import org.topicquests.support.api.IResult;

import com.tinkerpop.blueprints.Edge;

import java.util.*;
/**
 * @author jackpark
 * <p>Take apart an {@link IDocument} and grow
 * the <em>ocean</em>
 */
public class OceanThread {
	private Environment environment;
	protected InformationEnvironment oceanEnvironment;
	protected IInfoOcean dsl;
	private List<IDocument> docs;
	private boolean isRunning = true;
	private Worker worker;
	
	/**
	 * 
	 */
	public OceanThread(Environment env) {
		environment = env;
		oceanEnvironment = new InformationEnvironment();
		dsl = oceanEnvironment.getDSL();
		docs = new ArrayList<IDocument>();
		isRunning = true;
		worker = new Worker();
		worker.start();	
	}

	public void addDoc(IDocument doc) {
		environment.logDebug("Ocean.add "+doc.getId());
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
		
		/**
		 * <p>Items of interest:<br/>
		 * <ol><li>this {@code doc}</li>
		 * <li>Authors</li>
		 * <li>Institutions</li>
		 * <li>Key terms</li>
		 * <li>Substances</li></ol></p>
		 * @param doc
		 */
		void processDoc(IDocument doc) {
			List<IAuthor> authors = doc.listAuthors();
			List<String> tags = doc.listTagNames();
			List<String> substances = doc.listSubstanceNames();
			//add the document
			String docId = doc.getId();
			environment.logDebug("Ocean.process "+docId);
			List<String> labels = doc.listLabels();
			String label = "";
			if (labels != null && !labels.isEmpty())
				label = labels.get(0);
			// see if we have this document already
			// seems unlikely
			boolean docExists = dsl.existsWordGram(docId);
			environment.logDebug("Ocean.process-1 "+docExists);
			if (!docExists) {
				org.topicquests.os.asr.info.api.IDocument dn = dsl.createDocumentNode(docId, label, docId);
			}
			Iterator<String> itx;
			Edge e;
			IResult r;
			String x;
			environment.logDebug("Ocean.process-2 "+authors);
			//authors and affiliations
			if (authors != null && !authors.isEmpty()) {
				IAuthor a;
				String fullName = null;
				List<String> affiliations;
				String affiliation;
				String authorId;
				IWordGram ag;
				IAgent affilNode;
				String idxxx;
				Iterator<IAuthor> ita = authors.iterator();
				while (ita.hasNext()) {
					a = ita.next();
					environment.logDebug("Ocean.process-2a "+a.getAuthorFullName()+"\n"
							+a.getAuthorLastName());
					if (a.getAuthorFullName() == null) {
						fullName = a.getAuthorNickName();
						environment.logDebug("Ocean.process-2aa "+a.getAuthorNickName()+"\n"+fullName);
						if (fullName == null)
							fullName = a.getInitials();
						fullName += " "+a.getAuthorLastName();
					} else {
						fullName = a.getAuthorFullName();
					}
					environment.logDebug("Ocean.process-2b "+fullName);
					//Make a wordGram for this author
					r = dsl.processString(fullName, "SystemUser", null);
					authorId = (String)r.getResultObject();
					ag = dsl.getThisWordGram(authorId);
					environment.logDebug("Ocean.process-3c "+authorId+" "+ag);

					//link to doc
					e = dsl.connectAuthorToDocument(authorId, docId);
					environment.logDebug("Ocean.process-4 "+authorId+" "+e);

					affiliations = a.listAffiliationNames();
					environment.logDebug("Ocean.process-5 "+affiliations);
					if (affiliations != null && !affiliations.isEmpty()) {
						itx = affiliations.iterator();
						while (itx.hasNext()) {
							x = itx.next();
							affilNode = dsl.createInstitutionNode(x);
							
							environment.logDebug("Ocean.process-6 "+affilNode);
							e = dsl.connectAuthorToInstitution(authorId, (String)affilNode.getId());
						}
					}
				}
			}
			// keywords
			if (tags != null && !tags.isEmpty()) {
				if (substances != null && !substances.isEmpty())
					tags.addAll(substances);
				environment.keywords(tags); //instrumentation
				environment.logDebug("Ocean.process-7 "+tags);
				itx = tags.iterator();
				while (itx.hasNext()) {
					x = itx.next();
					environment.logDebug("Ocean.process-7a "+x);
					r = dsl.processString(x, "SystemUser", null);
					x = (String)r.getResultObject();
					environment.logDebug("Ocean.process-7b "+x);
					e = dsl.connectKeyWordGramToDocument(x, docId);
					environment.logDebug("Ocean.process-8 "+x+" "+e);
				}
			}
			environment.logDebug("Ocean.process+ "+docId);

		}
	}
}
