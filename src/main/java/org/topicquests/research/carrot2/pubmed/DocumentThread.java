/**
 * 
 */
package org.topicquests.research.carrot2.pubmed;

import java.util.ArrayList;
import java.util.List;

import org.topicquests.asr.general.document.api.IDocumentClient;
import org.topicquests.hyperbrane.ConcordanceDocument;
import org.topicquests.hyperbrane.api.IDocument;
import org.topicquests.research.carrot2.Environment;
import org.topicquests.research.carrot2.pubmed.ParserThread.Worker;
import org.topicquests.support.api.IResult;

/**
 * @author jackpark
 *
 */
public class DocumentThread {
	private Environment environment;
	private IDocumentClient documentDatabase;
	private List<JSONDocumentObject> docs;
	private boolean isRunning = true;
	private Worker worker;
	/**
	 * 
	 */
	public DocumentThread(Environment env) {
		environment = env;
		documentDatabase = environment.getDocumentDatabase();
		docs = new ArrayList<JSONDocumentObject>();
		isRunning = true;
		worker = new Worker();
		worker.start();	
	}

	public void addDoc(JSONDocumentObject doc) {
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
			JSONDocumentObject doc = null;
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
		
		
		void processDoc(JSONDocumentObject doc) {
			environment.logDebug("DocThread\n"+doc);
			IDocument d = new ConcordanceDocument(environment, doc);
			String docId = d.getId();
			String pmid = d.getPMID();
			String pmcid = d.getPMCID();
			String url = null;
			List<String>labels = d.listLabels();
			String label = labels.get(0);
			IResult r  = documentDatabase.put(docId, pmid, pmcid, url, label, d.getData());
			environment.logDebug("DocThread+ "+r.getErrorString());
		}
	}
}
