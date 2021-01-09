/**
 * 
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
