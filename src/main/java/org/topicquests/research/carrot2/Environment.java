/**
 * 
 */
package org.topicquests.research.carrot2;

import org.topicquests.asr.general.GeneralDatabaseEnvironment;
import org.topicquests.asr.general.document.api.IDocumentClient;
import org.topicquests.es.ProviderEnvironment;
import org.topicquests.research.carrot2.api.IDocumentProvider;
import org.topicquests.research.carrot2.pubmed.ParserThread;
import org.topicquests.support.RootEnvironment;

/**
 * @author park
 *
 */
public class Environment extends RootEnvironment {
	private static Environment instance;
	//NOT thread safe
	private StringBuilder buf;
	private QueryEngine engine;
	private BatchFileHandler batcher;
	private ParserThread parserThread;
	private GeneralDatabaseEnvironment generalEnvironment;
	private IDocumentProvider documentProvider;
	private IDocumentClient documentDatabase;
	private ProviderEnvironment esEnvironment;
	private Accountant accountant;
	private FileManager fileManager;
	/**
	 * 
	 */
	public Environment() {
		super("config-props.xml", "logger.properties");
		logDebug("Environment ");
		buf = new StringBuilder();
		accountant = new Accountant(this);
		fileManager = new FileManager(this);
		engine = new QueryEngine(this);
	    esEnvironment = new ProviderEnvironment();

		logDebug("Environment- "+engine);
		System.out.println("E1");
		batcher = new BatchFileHandler(this);
		String schemaName = getStringProperty("DatabaseSchema");
		System.out.println("E2 "+schemaName);
		logDebug("Environment-1 "+schemaName);
		generalEnvironment = new GeneralDatabaseEnvironment(schemaName);
		System.out.println("E3");
		documentDatabase = generalEnvironment.getDocumentClient();
		System.out.println("E4");
		documentProvider = new DocumentProvider(this);
		System.out.println("E5");
		parserThread = new ParserThread(this);
		System.out.println("E6");
		logDebug("Environment-2 "+parserThread);
		instance = this;
		Runtime.getRuntime().addShutdownHook(new Thread() {
			
			@Override
			public void run() {
				shutDown();
			}
		});
		logDebug("Environment booted");
		System.out.println("E7");

	}
	
	/**
	 * Called from PubMedDocumentSource when it has
	 * a new PubMed Doc for processing
	 * @param pmid
	 * @param xml
	 */
	public void addPubMedAbstract(String pmid, String xml) {
		logDebug("Env.add");
		if (!accountant.seenBefore(pmid))
			parserThread.addDoc(xml);
		fileManager.persistAbstract(pmid, xml);
	}
	
	public FileManager getFileManager() {
		return fileManager;
	}
	public Accountant getAccountant() {
		return accountant;
	}
	public ProviderEnvironment getElasticSearch() {
		return esEnvironment;
	}
	
	public static Environment getInstance() {
		return instance;
	}

	public BatchFileHandler getBatchFileHandler() {
		return batcher;
	}
	public QueryEngine getQueryEngine() {
		return engine;
	}
	
	public GeneralDatabaseEnvironment getGeneralDatabaseEnvironment() {
		return generalEnvironment;
	}

	public IDocumentProvider getDocProvider() {
		return documentProvider;
	}
	public IDocumentClient getDocumentDatabase () {
		return documentDatabase;
	}

	/**
	 * Utility to convert a Carrot2 <code>query</code> to a file name
	 * @param path
	 * @param query
	 * @return
	 */
	public String queryToFileName(String path, String query) {
		String x = query.replaceAll(" ", "_");
		buf.setLength(0);
		buf = buf.append(path).append(x).append(".xml");
		return buf.toString();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Environment();
	}

	@Override
	public void shutDown() {
		System.out.println("Environment.shutDown");
		parserThread.shutDown();
	}

}
