/*
 * Copyright 2021 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.research.carrot2;

/**
 * @author jackpark
 *
 */
public class FileManager {
	private Environment environment;

	/**
	 * 
	 */
	public FileManager(Environment env) {
		environment = env;
	}

	/**
	 * Persist abstracts in groups of 100 in gzip files
	 * @param pmid
	 * @param xml
	 */
	public void persistAbstract(String pmid, String xml) {
	   /*	String filePath = buf.toString();
    	File f = new File(filePath);
    	if (!f.exists()) {
	    	buf.setLength(0);;
	    	buf = buf.append("<?xml version=\"1.0\"?>\n")
	    			.append("<!DOCTYPE PubmedArticleSet PUBLIC \"-//NLM//DTD PubMedArticle, 1st January 2014//EN\" \"http://www.ncbi.nlm.nih.gov/corehtml/query/DTD/pubmed_140101.dtd\">\n")
	    			.append(xml);
	    	System.out.println("PD: "+f.getAbsolutePath());
	    	//FileOutputStream fos = new FileOutputStream(f);
	    	try {
	    	PrintWriter out = new PrintWriter(f, StandardCharsets.UTF_8.toString());
	    	out.print(buf.toString());
	    	System.out.println("PD-1: ");
	    	out.flush();
	    	out.close();
	    	} catch (Exception e) {
	    		System.out.println("DANG!");
	    		e.printStackTrace();
	    	}
	    	
    	}*/
	}
	
	/**
	 * Persist SpaCy results in groups of 100 in gzip files
	 * @param json
	 */
	public void persistSpaCy(String json) {
		//TODO
	}
}
