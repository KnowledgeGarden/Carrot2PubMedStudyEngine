/*
 * Copyright 2014, 2018 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.hyperbrane;

import java.util.*;

import net.minidev.json.*;

import org.topicquests.hyperbrane.api.IAuthor;
import org.topicquests.hyperbrane.api.IDocument;
import org.topicquests.hyperbrane.api.IHyperMembraneOntology;
import org.topicquests.hyperbrane.api.IParagraph;
import org.topicquests.hyperbrane.api.IPublication;
import org.topicquests.ks.api.ITQCoreOntology;
import org.topicquests.research.carrot2.Environment;
import org.topicquests.research.carrot2.pubmed.JSONDocumentObject;
import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;
import org.topicquests.support.util.DateUtil;


/**
 * @author park
 * 
 */
public class ConcordanceDocument implements IDocument {
	private Environment environment;
	private JSONObject data;
	
	public static final String 
		ID						= "id",
		PMID					= "pmid",
		PMCID					= "pcid",
		TITLE				 	= "title",
		URL						= "url",
		AUTHORS					= "authors", // simple names
		LANGUAGE				= "lang",
		ABSTRACTS				= "abstracts",
		THE_PUBLICATION			= "thePub",
		SENTENCES 				= "sentences",
		SUCCESS_SENTENCES		= "sucSents",
		TAG_NAME_LIST			= "tagList",
		TAG_ID_LIST				= "tagIdList",
		SUBSTANCE_NAME_LIST		= "substanceNameList",
		SUBSTANCE_ID_LIST		= "substanceIdList",
		CITATIONS_OF_ME			= "citationMe",
		MY_CITATIONS			= "myCitations",
		NODE_LOCATOR			= "NodeLocator",
		METADATA				= "MetaData",
		DB_PEDIA_URI_LIST		= "dbpuri",
		WIKDATA_URI_LIST		= "wduri",
		ISA_STRUCTURE			= "isaStruct",
		WORDGRAM_HISTOGRAM		= "wordgramHisto",
		DOCUMENT_TYPE			= "docType";

	
	/**
	 * Used by {@link ConversationModel} newConversation
	 * @param db
	 * @param m can be <code>null</code> if it is not a conversation 
	 */
	public ConcordanceDocument(Environment env) {
		environment = env;
		data = new JSONObject();
	}
	
	/**
	 * Used only for importing
	 * @return
	 */
	public static ConcordanceDocument newDocument(Environment env) {
		return new ConcordanceDocument(env, true);
	}
	
	private ConcordanceDocument(Environment env, boolean t) {
		environment = env;
		data = new JSONObject();
	}
	
	public ConcordanceDocument(Environment env, JSONDocumentObject jdo) {
		environment = env;
		data = new JSONObject();
		this.setTopicLocator(jdo.getLocator());
		this.setId(jdo.getLocator());
		if (jdo.getPMID() != null)
			this.setPMID(jdo.getPMID());
		String lang = jdo.getLanguage();
		List<String> abs = jdo.listAbstract();
		Iterator<String>itr;
		if (abs != null && !abs.isEmpty()) {
			itr = abs.iterator();
			while (itr.hasNext())
				this.addAbstractParagraph(itr.next(), lang);
		}
		IPublication p = jdo.getPublication();
		if (p != null)
			this.setPublication(p);
		this.setLanguage(lang);
		this.addLabel(jdo.getTitle(), lang);
		abs = jdo.listTags();
		if (abs != null && !abs.isEmpty()) {
			itr = abs.iterator();
			while (itr.hasNext())
				this.addTagName(itr.next());
		}
		abs = jdo.listSubstances();
		if (abs != null && !abs.isEmpty()) {
			itr = abs.iterator();
			while (itr.hasNext())
				this.addSubstanceName(itr.next());
		}
		List<JSONObject> ljo = jdo.listCitations();
		if (ljo != null && !ljo.isEmpty()) {
			Iterator<JSONObject>itj = ljo.iterator();
			while (itj.hasNext())
				this.addMyCitation(itj.next());
		}
		this.setCreatorId(jdo.getCreatorId());
		this.setDate(new Date());
				
	}
	/**
	 * Used by {@link ConversationModel} getConversation
	 * @param db
	 * @param jo
	 * @param m
	 */
	public ConcordanceDocument(Environment env, JSONObject jo) {
		data = jo;
		environment = env;
		//We must study the data structure and ensure that
		// what comes in is corrected, as necessary, to what this class expects
		String ix = this.getId();
		if (ix == null) 
			ix = data.getAsString("lox");
		if (ix != null) {
			this.setId(ix);
			data.remove("lox");
		} else
			throw new RuntimeException("ConcordanceDoc.badId "+data.toJSONString());
		
		List<String> l = this.listSubstanceNames();
		Iterator<String> itr;
		if (l == null) {
			l = (List<String>)data.get("substanceList");
			if (l != null) {
				itr = l.iterator();
				while (itr.hasNext())
					addSubstanceName(itr.next());
				data.remove("substanceList");
			}
		}
		/**List<JSONObject> lj = listMyCitations();
		if (lj == null) {
			l = (List<String>)data.get("citations");
			if (l != null) {
				itr = l.iterator();
				while (itr.hasNext())
					addMyCitation(itr.next());
				data.remove("citations");
			}
		}*/
	}


	void startTracer(String id) {
		//if (tracer != null)
		//	tracer = new Tracer("Doc_"+id+"_"+System.currentTimeMillis(), LoggingPlatform.getLiveInstance());
	}

	@Override
	public void setURL(String url) {
		data.put(URL, url);
	}

	@Override
	public String getURL() {
		return data.getAsString(URL);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.concordance.api.IDocument#listAuthors()
	 */
	@Override
	public List<IAuthor> listAuthors() {
		return (List<IAuthor>)data.get(AUTHORS);
	}

	@Override
	public IParagraph addParagraph(String theParagraph, String language) {
		IParagraph p = new ConcordanceParagraph(environment);
		p.setParagraph(theParagraph, language);
		p.setDocumentId(this.getId());
		p.setID(UUID.randomUUID().toString());
		this.addParagraph(p);
		return p;
	}
	
	@Override
	public List<String> listParagraphStrings(String language) {
		List<String>result = new ArrayList<String>();
		List<JSONObject> l = (List<JSONObject>)this.getData().get(IHyperMembraneOntology.PARAGRAPH_PROPERTY_TYPE);
		if (l != null) {
			Iterator<JSONObject>itr = l.iterator();
			JSONObject jo;
			while (itr.hasNext()) {
				jo = itr.next();
				if (jo.getAsString("lang").equals(language))
						result.add(jo.getAsString("text"));
			}
		}
		return result;
	}

	@Override
	public List<IParagraph> listParagraphs() {
		List<IParagraph> result = new ArrayList<IParagraph>();
		JSONObject p = (JSONObject)data.get(IHyperMembraneOntology.PARAGRAPH_PROPERTY_TYPE);
		if (p != null && !p.isEmpty()) {
			Collection<Object> l = p.values();
			Iterator<Object>itr = l.iterator();
			JSONObject jo;
			while (itr.hasNext()) {
				jo = (JSONObject)itr.next(); //TODO will this work?
				result.add(new ConcordanceParagraph(environment, jo));
			}
		}
		return result;
	}


	@Override
	public void traceStatement(String traceMessage) {
	//	synchronized(tracer) {
	//		tracer.trace(System.currentTimeMillis(), traceMessage);
	//	}
	}

	@Override
	public void setMetadata(String key, Object value) {
		JSONObject m = getMetadata();
		if (m == null)
			m = new JSONObject();
		m.put(key, value);
		data.put(METADATA, m);
	}

	@Override
	public JSONObject getMetadata() {
		return (JSONObject)getData().get(METADATA);
	}

	@Override
	public Object getMetadataValue(String key) {
		Object result = null;
		JSONObject m = getMetadata();
		if (m != null)
			result = m.get(key);
		return result;
	}
	
	/////////////////////////
	// The other way is to list paragraphs, then list sentences
	////////////////////////



	@Override
	public void removeSentence(String sentenceLocator) {
		List<String>ss = (List<String>)data.get(SENTENCES);
		if (ss != null)
			ss.remove(sentenceLocator);
		data.put(SENTENCES, ss);
	}


	@Override
	public void setOntologyClassLocator(String locator) {
		setProperty(NODE_LOCATOR, locator);
	}

	@Override
	public String getOntologyClassLocator() {
		return (String)data.get(NODE_LOCATOR);
	}





	//////////////////////////
	// Paragraphs are stored in a struct:
	//  { <id>: <paragraph>, <id>:<paragraph>
	//////////////////////////
	@Override
	public void addParagraph(IParagraph paragraph) {
		//We are studying whole paragraphs
		JSONObject p = (JSONObject)data.get(IHyperMembraneOntology.PARAGRAPH_PROPERTY_TYPE);
		if (p == null)
			p = new JSONObject();
		p.put(paragraph.getID(), paragraph.getData());
		data.put(IHyperMembraneOntology.PARAGRAPH_PROPERTY_TYPE, p);
	}
	
	@Override
	public void updateParagraph(IParagraph paragraph) {
		//Just overwrite what's there
		addParagraph(paragraph);
	}

	@Override
	public void addSuccessfullyReadSentenceId(String sentenceId) {
		List<String>l = (List<String>)data.get(SUCCESS_SENTENCES);
		if (l == null) l = new ArrayList<String>();
		if (!l.contains(sentenceId))
			l.add(sentenceId);
		data.put(SUCCESS_SENTENCES, l);
	}

	@Override
	public List<String> listSuccessfullyReadSentenceIds() {
		return (List<String>)data.get(SUCCESS_SENTENCES);
	}

	@Override
	public void addDbPediaURI(String uri) {
		List<String> l = listDbPediaURIs();
		if (l == null) l = new ArrayList<String>();
		if (!l.contains(uri))
			l.add(uri);
		data.put(DB_PEDIA_URI_LIST, l);
	}

	@Override
	public List<String> listDbPediaURIs() {
		return (List<String>)getData().get(DB_PEDIA_URI_LIST);
	}
	
	@Override
	public void addWikidataURI(String uri) {
		List<String> l = listWikidataURIs();
		if (l == null)
			l = new ArrayList<String>();
		if (!l.contains(uri))
			l.add(uri);
		data.put(WIKDATA_URI_LIST, l);
	}

	@Override
	public List<String> listWikidataURIs() {
		return (List<String>)getData().get(WIKDATA_URI_LIST);
	}

	@Override
	public void addTagName(String tag) {
		List<String>t = listTagNames();
		if (t == null)
			t = new ArrayList<String>();
		if (!t.contains(tag))
			t.add(tag);
		data.put(TAG_NAME_LIST, t);
	}

	@Override
	public List<String> listTagNames() {		// TODO Auto-generated method stub
		return (List<String>)data.get(TAG_NAME_LIST);
	}

	@Override
	public void addTagWordGramId(String id) {
		List<String>t = listTagWordGramIds();
		if (t == null)
			t = new ArrayList<String>();
		if (!t.contains(id))
			t.add(id);
		data.put(TAG_ID_LIST, t);
	}

	@Override
	public List<String> listTagWordGramIds() {
		return (List<String>)data.get(this.TAG_ID_LIST);
	}

	@Override
	public void addSubstanceName(String name) {
		List<String>t = listSubstanceNames();
		if (t == null)
			t = new ArrayList<String>();
		if (!t.contains(name))
			t.add(name);
		data.put(SUBSTANCE_NAME_LIST, t);
	}

	@Override
	public List<String> listSubstanceNames() {
		return (List<String>)data.get(this.SUBSTANCE_NAME_LIST);
	}

	@Override
	public void addSubstanceWordGramId(String id) {
		List<String>t = listSubstanceWordGramIds();
		if (t == null)
			t = new ArrayList<String>();
		if (!t.contains(id))
			t.add(id);
		data.put(SUBSTANCE_ID_LIST, t);
	}

	@Override
	public List<String> listSubstanceWordGramIds() {
		return (List<String>)data.get(this.SUBSTANCE_ID_LIST);
	}

	@Override
	public void setLanguage(String lang) {
		data.put(LANGUAGE, lang);
	}

	@Override
	public String getLanguage() {
		return data.getAsString(LANGUAGE);
	}

	/////////////////////////////////
	// Abstracts in JSON structure
	//  {
	//    "paragraphs": [
	//		{  
	//				"en": "para"
	//		}
	//		]
	//	}
	//

	@Override
	public void addAbstractParagraph(String text, String language) {
		JSONObject abs = getAllAbstracts();
		JSONObject jo = new JSONObject();
		jo.put(language, text);
		if (abs == null) 
			abs = new JSONObject();
		List<JSONObject> paras = (List<JSONObject>)abs.get("paragraphs");
		if (paras == null)
			paras = new ArrayList<JSONObject>();
		if (!paras.contains(jo))
			paras.add(jo);
		abs.put("paragraphs", paras);
		environment.logDebug("ConcordanceDocument.addAbsPara "+language+"\n"+abs);
		data.put(ABSTRACTS, abs);
	}

	@Override
	public List<String> listAbstractsForLanguage(String language) {
		JSONObject abs = getAllAbstracts();
		if (abs != null) {
			List<JSONObject> paras = (List<JSONObject>)abs.get("paragraphs");
			if (paras != null && !paras.isEmpty()) {
				List<String> result = new ArrayList<String>();
				Iterator<JSONObject>itr = paras.iterator();
				JSONObject p;
				String s;
				while (itr.hasNext()) {
					p = itr.next();
					s = p.getAsString(language);
					if (s != null)
						result.add(s);
				}
				return result;
			}
		}
		return null;
	}

	@Override
	public List<JSONObject> listAbstracts() {
		JSONObject abs = getAllAbstracts();
		if (abs != null)
			return (List<JSONObject>)abs.get("paragraphs");
		return null;
	}

	@Override
	public JSONObject getAllAbstracts() {
		return (JSONObject)this.data.get(ABSTRACTS);
	}

	@Override
	public void setVersion(String version) {
	    data.put(ITQCoreOntology.VERSION, version);
	}

	@Override
	public String getVersion() {
		return data.getAsString(ITQCoreOntology.VERSION);
	}

	@Override
	public IResult doUpdate() {
		IResult result = new ResultPojo();
	    String newVersion = Long.toString(System.currentTimeMillis());
	    setVersion(newVersion);
		return result;
	}


	@Override
	public void setCreatorId(String id) {
		data.put(ITQCoreOntology.CREATOR_ID_PROPERTY, id);
	}

	@Override
	public String getCreatorId() {
		return data.getAsString(ITQCoreOntology.CREATOR_ID_PROPERTY);
	}



	@Override
	public void setDate(Date date) {
	    data.put(ITQCoreOntology.CREATED_DATE_PROPERTY, DateUtil.formatIso8601(date));
	}

	@Override
	public void setDate(String date) {
	    data.put(ITQCoreOntology.CREATED_DATE_PROPERTY, date);
	}

	@Override
	public Date getDate() {
	    String dx = data.getAsString(ITQCoreOntology.CREATED_DATE_PROPERTY);
	    return DateUtil.fromIso8601(dx);
	}

	@Override
	public String getDateString() {
	    return data.getAsString(ITQCoreOntology.CREATED_DATE_PROPERTY);
	}

	@Override
	public void setLastEditDate(Date date) {
	    data.put(ITQCoreOntology.LAST_EDIT_DATE_PROPERTY, DateUtil.formatIso8601(date));
	    setVersion(Long.toString(System.currentTimeMillis()));
	}

	@Override
	public void setLastEditDate(String date) {
	    data.put(ITQCoreOntology.LAST_EDIT_DATE_PROPERTY, date);
	    setVersion(Long.toString(System.currentTimeMillis()));
	}

	@Override
	public Date getLastEditDate() {
	    String dx = data.getAsString(ITQCoreOntology.LAST_EDIT_DATE_PROPERTY);
	    return DateUtil.fromIso8601(dx);
	}

	@Override
	public String getLastEditDateString() {
		return data.getAsString(ITQCoreOntology.LAST_EDIT_DATE_PROPERTY);
	}

	@Override
	public JSONObject getData() {
		return data;
	}

	@Override
	public String toJSONString() {
		return data.toJSONString();
	}

	@Override
	public void addLabel(String label, String language) {
		JSONObject jo = getLabels();
		if (jo == null)
			jo = new JSONObject();
		List<String>l = (List<String>)jo.get(language);
		if (l == null)
			l = new ArrayList<String>();
		if (!l.contains(label))
			l.add(label);
		jo.put(language, l);
		data.put(ITQCoreOntology.LABEL_PROPERTY, jo);		
	}

	@Override
	public JSONObject getLabels() {
		JSONObject result = (JSONObject)data.get(ITQCoreOntology.LABEL_PROPERTY);
		return result;
	}

	@Override
	public String getLabel(String language) {
		JSONObject jo = getLabels();
		String result = null;
		if (jo != null) {
			List<String>l = (List<String>)jo.get(language);
			if (l != null && !l.isEmpty())
				result = l.get(0);
		}
		return result;
	}

	@Override
	public List<String> listLabels() {
		List<String>result = null;
		JSONObject jo = getLabels();
		if (jo != null && !jo.isEmpty()) {
			result = new ArrayList<String>();
			Iterator<String>itr = jo.keySet().iterator();
			List<String> l;
			while (itr.hasNext()) {
				l = (List<String>)jo.get(itr.next());
				if (l != null)
					result.addAll(l);
			}
		}
		return result;
	}

	@Override
	public List<String> listLabels(String language) {
		JSONObject jo = getLabels();
		String result = null;
		if (jo != null)
			return (List<String>)jo.get(language);
		return null;
	}

	@Override
	public void addDetails(String details, String language) {
		JSONObject jo = getDetails();
		if (jo == null)
			jo = new JSONObject();
		List<String>l = (List<String>)jo.get(language);
		if (l == null)
			l = new ArrayList<String>();
		if (!l.contains(details))
			l.add(details);
		jo.put(language, l);
		data.put(ITQCoreOntology.DETAILS_PROPERTY, jo);
	}

	@Override
	public JSONObject getDetails() {
		JSONObject jo = (JSONObject)data.get(ITQCoreOntology.DETAILS_PROPERTY);
		return jo;
	}

	@Override
	public String getDetails(String language) {
		JSONObject jo = getDetails();
		String result = null;
		if (jo != null) {
			List<String>l = (List<String>)jo.get(language);
			if (l != null && !l.isEmpty())
				result = l.get(0);
		}
		return result;
	}

	@Override
	public List<String> listDetails() {
		List<String>result = null;
		JSONObject jo = getDetails();
		if (jo != null && !jo.isEmpty()) {
			result = new ArrayList<String>();
			Iterator<String>itr = jo.keySet().iterator();
			List<String> l;
			while (itr.hasNext()) {
				l = (List<String>)jo.get(itr.next());
				if (l != null)
					result.addAll(l);
			}
		}
		return result;
	}

	@Override
	public List<String> listDetails(String language) {
		JSONObject jo = getDetails();
		String result = null;
		if (jo != null)
			return (List<String>)jo.get(language);
		return null;
	}


	@Override
	public void addPropertyValue(String key, String value) {
		List<String>l = (List<String>)data.get(key);
		if (l == null)
			l = new ArrayList<String>();
		if (!l.contains(value))
			l.add(value);
		data.put(key, l);
		
	}

	@Override
	public Object getProperty(String key) {
		return data.get(key);
	}


	@Override
	public void removeProperty(String key) {
		data.remove(key);
	}

	@Override
	public void removePropertyValue(String key, String value) {
		List<String>l = (List<String>)data.get(key);
		if (l != null) {
			l.remove(value);
			data.put(key, l);
		}
	}

	@Override
	public String getTopicLocator() {
		return data.getAsString(ITQCoreOntology.LOCATOR_PROPERTY);
	}

	@Override
	public void setId(String id) {
		data.put(ID, id);
	}

	@Override
	public String getId() {
		return data.getAsString(ID);
	}


	@Override
	public void setProperty(String key, Object value) {
		data.put(key, value);
	}

	@Override
	public void setTopicLocator(String nodeLocator) {
		data.put(ITQCoreOntology.LOCATOR_PROPERTY, nodeLocator);
	}

	@Override
	public void setNodeType(String typeLocator) {
		data.getAsString(ITQCoreOntology.INSTANCE_OF_PROPERTY_TYPE);
	}


	@Override
	public IAuthor addAuthor(String title, String initials, String firstName, String middleName, String lastName,
						  String suffix, String degree, String fullName, String authorLocator, 
						  String publicationName, String publicationLocator, 
						  String publisherName, String publisherLocator, 
						  String affiliationName, String affiliationLocator) {
		IAuthor a = new AuthorPojo();
		if (title != null && !title.equals(""))
			a.setAuthorTitle(title);
		if (initials != null && !initials.equals(""))
			a.setAuthorInitials(initials);
		if (firstName != null && !firstName.equals(""))
			a.addAuthorFirstName(firstName);
		if (middleName != null && !middleName.equals(""))
			a.setAuthorMiddleName(middleName);
		if (lastName != null && !lastName.equals(""))
			a.setAuthorLastName(lastName);
		if (suffix != null && !suffix.equals(""))
			a.setAuthorSuffix(suffix);
		if (degree != null && !degree.equals(""))
			a.setAuthorDegree(degree);
		if (fullName != null && !fullName.equals(""))
			a.setAuthorFullName(fullName);
		if (authorLocator != null && !authorLocator.equals(""))
			a.setAuthorLocator(authorLocator);
		if (publicationName != null && !publicationName.equals(""))
			a.setPublicationName(publicationName);
		if (publicationLocator != null && !publicationLocator.equals(""))
			a.setPublicationLocator(publicationLocator);
		if (publisherName != null && !publisherName.equals(""))
			a.setPublisherName(publisherName);
		if (publisherLocator != null && !publisherLocator.equals(""))
			a.setPublisherLocator(publisherLocator);
		if (affiliationName != null && !affiliationName.equals(""))
			a.addAffiliationName(affiliationName);
		if (affiliationLocator != null && !affiliationLocator.equals(""))
			a.setAffiliationLocator(affiliationLocator);
		this.addAuthor(a);
		return a;
	}

	@Override
	public void addAuthor(IAuthor author) {
		List<IAuthor>a = this.listAuthors();
		if (a == null)
			a = new ArrayList<IAuthor>();
		a.add(author);
		this.setAuthorList(a);
	}

	@Override
	public void setPublication(IPublication doc) {
		data.put(THE_PUBLICATION, doc);
	}

	@Override
	public IPublication getPublication() {
		return (IPublication)data.get(THE_PUBLICATION);
	}

	@Override
	public void addMyCitation(JSONObject citation) {
		List<JSONObject>l = listMyCitations();
		if (l == null)
			l = new ArrayList<JSONObject>();
		if (!l.contains(citation))
			l.add(citation);
		setMyCitationList(l);
	}

	@Override
	public List<JSONObject> listMyCitations() {
		return (List<JSONObject>)data.get(MY_CITATIONS);
	}

	@Override
	public void addCitation(String citation) {
		List<String>l = listCitations();
		if (l == null)
			l = new ArrayList<String>();
		if (!l.contains(citation))
			l.add(citation);
		setCitationList(l);
	}

	@Override
	public List<String> listCitations() {
		return (List<String>)data.get(CITATIONS_OF_ME);
	}

	@Override
	public void setAuthorList(List<IAuthor> authors) {
		data.put(AUTHORS, authors);
	}

	@Override
	public void setMyCitationList(List<JSONObject> citations) {
		data.put(MY_CITATIONS, citations);
	}

	@Override
	public void setCitationList(List<String> citations) {
		data.put(CITATIONS_OF_ME, citations);
	}

	@Override
	public void setPMID(String pmid) {
		data.put(PMID, pmid);
	}

	@Override
	public String getPMID() {
		return data.getAsString(PMID);
	}

	@Override
	public void setPMCID(String pmcid) {
		data.put(PMCID, pmcid);
	}

	@Override
	public String getPMCID() {
		return data.getAsString(PMCID);
	}

	@Override
	public void addToHistogram(String wordgramId) {
		JSONObject histo = (JSONObject)data.get(WORDGRAM_HISTOGRAM);
		if (histo == null)
			histo = new JSONObject();
		Number num = histo.getAsNumber(wordgramId);
		if (num == null)
			num = new Integer(1);
		else {
			int x = num.intValue()+1;
			num = new Integer(x);
		}
		histo.put(wordgramId, num);
		data.put(WORDGRAM_HISTOGRAM, histo);
	}

	@Override
	public int getHistogramCount(String wordgramId) {
		JSONObject histo = (JSONObject)data.get(WORDGRAM_HISTOGRAM);
		if (histo != null) {
			Number num = histo.getAsNumber(wordgramId);
			if (num != null)
				return num.intValue();
		}
		return 0;
	}

	@Override
	public String getNodeType() {
		return data.getAsString(ITQCoreOntology.INSTANCE_OF_PROPERTY_TYPE);
	}

	@Override
	public void setDocumentType(String type) {
		data.put(DOCUMENT_TYPE, type);
	}

	@Override
	public String getDocumentType() {
		return data.getAsString(DOCUMENT_TYPE);
	}



	@Override
	public JSONObject getEntityHistogram() {
		JSONObject histo = (JSONObject)data.get(WORDGRAM_HISTOGRAM);
		return histo;
	}

	@Override
	public void addSentence(String sentenceId) {
		List<String>ss = (List<String>)data.get(SENTENCES);
		if (ss == null) {
			ss = new ArrayList<String>();
		}
		ss.add(sentenceId);
		data.put(SENTENCES, ss);
	}

	@Override
	public List<String> listSentenceIDs() {
		List<String>ss = (List<String>)data.get(SENTENCES);
		return ss;
	}


}
