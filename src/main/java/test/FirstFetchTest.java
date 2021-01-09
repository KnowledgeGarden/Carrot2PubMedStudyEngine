/*
 * Copyright 2020 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package test;


import org.topicquests.research.carrot2.Environment;
import org.topicquests.research.carrot2.QueryEngine;

/**
 * @author park
 *
 */
public class FirstFetchTest {
	private Environment environment;
	private final String query = "Anthocyanins";

	/**
	 * 
	 */
	public FirstFetchTest() {
		environment = new Environment();
		QueryEngine qe = environment.getQueryEngine();
		qe.runQuery(query);
	}

}
