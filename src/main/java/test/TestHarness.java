/*
 * Copyright 2020 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package test;

/**
 * @author park
 * 
 */
public class TestHarness {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Starting");
		//new FirstFetchTest();
		//new BatchTest();
		new EsQueryTest();
		System.out.println("Did");
	}

}
