/**
 * 
 */
package org.topicquests.research.carrot2.nlp;

import java.util.ArrayList;
import java.util.List;

import org.topicquests.os.asr.driver.sp.SpacyDriverEnvironment;
import org.topicquests.research.carrot2.Environment;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class SpaCyThread {
	private Environment environment;
	private SpacyDriverEnvironment spaCy;
	private List<String> paragraphs;
	private boolean isRunning = true;
	private Worker worker;

	/**
	 * 
	 */
	public SpaCyThread(Environment env) {
		environment = env;
		spaCy = new SpacyDriverEnvironment();
		paragraphs = new ArrayList<String>();
	}

	public void addDoc(String paragraph) {
		synchronized(paragraphs) {
			paragraphs.add(paragraph);
			paragraphs.notify();
		}
	}
	
	public void shutDown() {
		synchronized(paragraphs) {
			isRunning = false;
			paragraphs.notify();
		}
	}
	
	class Worker extends Thread {
		
		public void run() {
			environment.logDebug("ParserThread.starting");
			String doc = null;
			while (isRunning) {
				synchronized(paragraphs) {
					if (paragraphs.isEmpty()) {
						try {
							paragraphs.wait();
						} catch (Exception e) {}
						
					} else {
						doc = paragraphs.remove(0);
					}
				}
				if (isRunning && doc != null) {
					processDoc(doc);
					doc = null;
				}
			}
		}
		
		void processDoc(String paragraph) {
			String text = cleanParagraph(paragraph);
			IResult r = spaCy.processSentence(text);
			JSONObject jo = (JSONObject)r.getResultObject();
			//TODO
		}
		
		String cleanParagraph(String paragraph) {
			StringBuilder buf = new StringBuilder();
			int len = paragraph.length();
			char c;
			boolean found = false;
			boolean didSpace = false;
			for (int i=0;i<len;i++) {
				c = paragraph.charAt(i);
				if (!found && c == '(') {
					found = true;
					didSpace = true; // remember you did one before
					// we don't want to accumulate spaces from around ()
				} else if (found && c == ')') {
					found = false;
				} else if (!found) {
					if (c == ' ') {
						if (didSpace)
							didSpace = false;
						else {
							buf.append(c);
						}
					} else {
						buf.append(c);
						didSpace = false;
					}
				}
			}
			return buf.toString().trim();
		}
	}
}
