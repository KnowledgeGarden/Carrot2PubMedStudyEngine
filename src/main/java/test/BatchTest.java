/*
 * Copyright 2020 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package test;

import org.topicquests.research.carrot2.BatchFileHandler;
import org.topicquests.research.carrot2.Environment;

/**
 * @author park
 *
 */
public class BatchTest {
	private Environment environment;

	/**
	 * 
	 */
	public BatchTest() {
		environment = new Environment();
		BatchFileHandler h = environment.getBatchFileHandler();
		//h.runBatchQueries();
		h.runSimpleBatchQueries();
	}

	public static void main(String[] args) {
		new BatchTest();
	}

}
