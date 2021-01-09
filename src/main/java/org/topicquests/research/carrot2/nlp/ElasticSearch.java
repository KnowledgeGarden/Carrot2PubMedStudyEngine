/**
 * 
 */
package org.topicquests.research.carrot2.nlp;

import org.topicquests.es.ProviderEnvironment;
import org.topicquests.es.api.IClient;
import org.topicquests.research.carrot2.Environment;
import org.topicquests.research.carrot2.api.IConstants;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class ElasticSearch {
	private Environment environment;
	private ProviderEnvironment esEnvironment;
    private IClient provider;

	/**
	 * 
	 */
	public ElasticSearch(Environment env) {
		environment = env;
		esEnvironment = environment.getElasticSearch();
		provider = esEnvironment.getProvider();
	}
	
	public IResult put(String id, String label, String absText) {
		JSONObject jo = new JSONObject();
		jo.put("id", id);
		jo.put("label", label);
		jo.put("abstract", absText);
		return put(id, jo);
	}
	/**
	 * Index {@code doc}
	 * @param id
	 * @param doc {id, label, abstract}
	 * @return
	 */
	public IResult put(String id, JSONObject doc) {
		IResult result = null;
		result = provider.put(id, IConstants.ES_INDEX_NAME, doc);
		return result;
	}
	
	public IResult get(String query) {
		IResult result = null;
		//TODO
		return result;
	}
}
