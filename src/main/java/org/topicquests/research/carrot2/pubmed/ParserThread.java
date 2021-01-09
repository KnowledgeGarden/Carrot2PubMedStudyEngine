/**
 * 
 */
package org.topicquests.research.carrot2.pubmed;

import java.util.*;
import org.topicquests.research.carrot2.Environment;
import org.topicquests.support.api.IResult;

/**
 * @author jackpark
 *
 */
public class ParserThread {
	private Environment environment;
	private DocumentThread docThread;
	private PubMedReportPullParser parser;
	private List<String> docs;
	private boolean isRunning = true;
	private Worker worker;

	/**
	 * 
	 */
	public ParserThread(Environment env) {
		environment = env;
		parser = new PubMedReportPullParser(environment);
		docThread = new DocumentThread(environment);
		docs = new ArrayList<String>();
		isRunning = true;
		worker = new Worker();
		worker.start();
		environment.logDebug("ParserThread");
	}
	
	public void addDoc(String xml) {
		environment.logDebug("PT.add");
		synchronized(docs) {
			docs.add(xml);
			docs.notify();
		}
	}

	public void shutDown() {
		synchronized(docs) {
			isRunning = false;
			docs.notify();
		}
		docThread.shutDown();
	}
	class Worker extends Thread {
		
		public void run() {
			environment.logDebug("ParserThread.starting");
			String doc = null;
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
		 * This receives {@code xml} and will get back
		 * an object which must then be sent off to 
		 * another thread for turning into an IDocument
		 * @param xml
		 */
		void processDoc(String xml) {
			environment.logDebug("PT.process "+parser);

			IResult r = parser.parseXML(xml);
			JSONDocumentObject j = (JSONDocumentObject)r.getResultObject();
			environment.logDebug("PT+\n"+j);
			docThread.addDoc(j);
		}
	}
}