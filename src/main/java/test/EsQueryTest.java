/**
 * 
 */
package test;

import org.topicquests.research.carrot2.Environment;
import org.topicquests.research.carrot2.nlp.ElasticSearch;
import org.topicquests.support.api.IResult;

/**
 * @author jackpark
 *
 */
public class EsQueryTest {
	private Environment environment;
	private ElasticSearch es;

	/**
	 * 
	 */
	public EsQueryTest() {
		environment = new Environment();
		es = environment.getElasticSearch();
		IResult r  = es.get("irritable bowel syndrome", 0, 100);
		
		System.out.println("A "+r.getErrorString());
		System.out.println("B\n"+r.getResultObject());
		environment.shutDown();
		System.exit(0);
	}

}
