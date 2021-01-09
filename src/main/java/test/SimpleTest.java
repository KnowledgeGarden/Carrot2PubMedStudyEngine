/*
 * Copyright 2020 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package test;

import org.topicquests.research.carrot2.BatchFileHandler;
import org.topicquests.research.carrot2.Environment;

/**
 * @author jackpark
 *
 */
public class SimpleTest {
	private Environment environment;

	/**
	 * 
	 */
	public SimpleTest() {
		environment = new Environment();
		environment.logDebug("ST");
		BatchFileHandler h = environment.getBatchFileHandler();
		h.runSimpleBatchQueries();
	}

	public static void main(String[] args) {
		new SimpleTest();
	}

}
