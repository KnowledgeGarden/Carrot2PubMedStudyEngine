/**
 * 
 */
package org.topicquests.research.carrot2.ocean;

import org.topicquests.hyperbrane.api.IDocument;
import org.topicquests.os.asr.info.InformationEnvironment;
import org.topicquests.os.asr.info.api.IInfoOcean;
import org.topicquests.research.carrot2.Environment;
//import org.topicquests.support.api.IResult;

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
			//TODO
		}
	}
}
