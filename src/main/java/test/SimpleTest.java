/**
 * 
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
