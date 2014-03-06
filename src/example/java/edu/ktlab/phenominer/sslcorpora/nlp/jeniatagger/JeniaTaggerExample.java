package edu.ktlab.phenominer.sslcorpora.nlp.jeniatagger;

import com.jmcejuela.bio.jenia.JeniaTagger;
import com.jmcejuela.bio.jenia.common.Sentence;

public class JeniaTaggerExample {
	public static void main(String[] args) {
		JeniaTagger.setModelsPath("models/genia");
		String text = "Two eyes";
		Sentence sentence = JeniaTagger.analyzeAll(text, true);
		System.out.println(sentence);		
	}

}
