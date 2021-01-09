/*
 * Copyright 2014, 2018 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.hyperbrane;

import java.util.*;

import net.minidev.json.JSONObject;

import org.topicquests.hyperbrane.api.IParagraph;
import org.topicquests.hyperbrane.api.ISentence;
import org.topicquests.hyperbrane.api.IWordGram;
import org.topicquests.ks.api.ITQCoreOntology;
//import org.topicquests.ks.tm.api.IDataProvider;
//import org.topicquests.os.asr.AsrCoreEnvironment;
//import org.topicquests.os.asr.api.ISentenceProvider;
//import org.topicquests.pg.PostgresConnectionFactory;
import org.topicquests.research.carrot2.Environment;
import org.topicquests.support.api.IResult;

//import com.tinkerpop.blueprints.impls.sql.SqlGraph;

/**
 * @author park
 *
 */
public class ConcordanceParagraph  implements IParagraph {
	private Environment environment;
//	private AsrCoreEnvironment environment;
//	private ISentenceProvider sentenceProvider;
//	private WordGramCache cache;

	private final String 
		ID				 	= "id",
		PARAGRAPH_KEY		= "para",
		DOC_ID				= "docId",
		LANGUAGE_KEY		= "lang",
		SENTENCE_LIST_KEY 	= "sentences",
		NOUN_LIST			= "nouns",
		SUBJECT_NOUN_STACK	= "subNounStk";
	private JSONObject data;
	/**
	 * 
	 */
	public ConcordanceParagraph(Environment env) {
		data = new JSONObject();
		environment = env;
		//sentenceProvider = environment.getSentenceProvider();
		//cache = environment.getWordGramCache();
	}

	/**
	 * @param jo
	 */
	public ConcordanceParagraph(Environment env, JSONObject jo) {
		data = jo;
		environment = env;
	//	sentenceProvider = environment.getSentenceProvider();
	//	cache = environment.getWordGramCache();
	}


	/* (non-Javadoc)
	 * @see org.topicquests.hyperbrane.api.IParagraph#setParagraph(java.lang.String)
	 */
	@Override
	public void setParagraph(String paragraph, String language) {
		data.put(PARAGRAPH_KEY, paragraph);
		if (language != null)
			data.put(LANGUAGE_KEY, language);
	}

	public String getParagraph() {
		return (String)data.get(PARAGRAPH_KEY);
	}
	/* (non-Javadoc)
	 * @see org.topicquests.hyperbrane.api.IParagraph#addSentence(org.topicquests.hyperbrane.api.ISentence)
	 */
	@Override
	public boolean addSentence(ISentence sentence) {
		List<String> sents = (List<String>)data.get(SENTENCE_LIST_KEY);
		if (sents == null)
			sents = new ArrayList<String>();
		if (!sents.contains(sentence.getID())) {
			sents.add(sentence.getID());
			setSentencesIds(sents);
			return true;
		}
		return false;
	}

	@Override
	public void setSentencesIds(List<String> sentences) {
		data.put(SENTENCE_LIST_KEY, sentences);
	}


	/* (non-Javadoc)
	 * @see org.topicquests.hyperbrane.api.IParagraph#removeSentence(java.lang.String)
	 */
	@Override
	public void removeSentence(String sentenceId) {
		List<String> sents = (List<String>)data.get(SENTENCE_LIST_KEY);
		if (sents != null && !sents.isEmpty()) {
			if (sents.remove(sentenceId))
				data.put(SENTENCE_LIST_KEY, sents);
		}
	}

	/* (non-Javadoc)
	 * @see org.topicquests.hyperbrane.api.IParagraph#listSentences()
	 */
	@Override
	public List<ISentence> listSentences() {
//		List<String> sents = (List<String>)data.get(SENTENCE_LIST_KEY);
/*		if (sents != null && !sents.isEmpty()) {
			List<ISentence> result = new ArrayList<ISentence>();
			Iterator<String>itr = sents.iterator();
			IResult r;
			while (itr.hasNext()) {
				r = sentenceProvider.getSentence(itr.next());
				JSONObject jo = (JSONObject)r.getResultObject();
				if (jo != null)
					result.add(new ConcordanceSentence(jo));
			}
			return result;
		} */
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.hyperbrane.api.IParagraph#addNoun(org.topicquests.hyperbrane.api.IWordGram, java.lang.String)
	 */
	@Override
	public void addNoun(IWordGram noun, String sentenceId) {
		JSONObject jo = (JSONObject)data.get(NOUN_LIST);
		if (jo == null)
			jo = new JSONObject();
		List<String>nouns = (List<String>)jo.get(sentenceId);
		if (nouns == null)
			 nouns = new ArrayList<String>();
		if (!nouns.contains(noun.getId()))
			nouns.add((String)noun.getId());
		jo.put(sentenceId, nouns);
		data.put(NOUN_LIST, jo);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.hyperbrane.api.IParagraph#getNouns()
	 */
	@Override
	public JSONObject getNouns() {
		return (JSONObject)data.get(NOUN_LIST);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.hyperbrane.api.IParagraph#pushSubjectNoun(org.topicquests.hyperbrane.api.IWordGram)
	 * /
	@Override
	public void pushSubjectNoun(IWordGram noun) {
		List<String> l = (List<String>)data.get(SUBJECT_NOUN_STACK);
		if (l == null)
			l = new ArrayList<String>();
		l.add((String)noun.getId());
		data.put(SUBJECT_NOUN_STACK, l);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.hyperbrane.api.IParagraph#popSubjectNoun()
	 * /
	@Override
	public IWordGram popSubjectNoun() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.hyperbrane.api.IParagraph#showSubjectNouns()
	 *  /
	@Override
	public Stack<String> showSubjectNouns() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.hyperbrane.api.IParagraph#pushObjectNoun(org.topicquests.hyperbrane.api.IWordGram)
	 * /
	@Override
	public void pushObjectNoun(IWordGram noun) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.topicquests.hyperbrane.api.IParagraph#popObjectNoun()
	 * /
	@Override
	public IWordGram popObjectNoun() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.hyperbrane.api.IParagraph#showObjectNouns()
	 * /
	@Override
	public Stack<IWordGram> showObjectNouns() {
		// TODO Auto-generated method stub
		return null;
	}
*/

	@Override
	public void setDocumentId(String id) {
		data.put(DOC_ID, id);
	}

	@Override
	public String getDocumentId() {
		return (String)data.get(DOC_ID);
	}

	@Override
	public JSONObject getData() {
		return data;
	}

	@Override
	public String getLanguage() {
		return (String)data.get(LANGUAGE_KEY);
	}

	@Override
	public void setID(String id) {
		data.put(ID, id);
	}

	@Override
	public String getID() {
		return data.getAsString(ID);
	}


}
