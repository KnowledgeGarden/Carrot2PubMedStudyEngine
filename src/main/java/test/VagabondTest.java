/*
 * Copyright 2021 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package test;

import org.topicquests.research.carrot2.Environment;
import org.topicquests.research.carrot2.search.VagabondThread;
import org.topicquests.support.util.TextFileHandler;

/**
 * @author jackpark
 *
 */
public class VagabondTest {
	private Environment environment;
	private VagabondThread vagabondThread;
	private final String PATH = "data/vagabond/interests.txt";

	/**
	 * 
	 */
	public VagabondTest() {
		environment = new Environment();
		vagabondThread  = environment.getVagabondThread();
		TextFileHandler h = new TextFileHandler();
		String line = h.readFirstLine(PATH);
		while (line != null) {
			if (!line.startsWith("#") && 
				!line.trim().contentEquals(""))
				vagabondThread.addDoc(line);
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new VagabondTest();

	}

}
