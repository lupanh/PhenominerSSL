package edu.ktlab.phenominer.sslcorpora.nlp.jeniatagger;

import com.jmcejuela.bio.jenia.common.Sentence;

public class JeniaTaggerExample {
	public static void main(String[] args) {
		Jenia.setModelsPath("models/genia");
		String text = "Two eyes";
		Sentence sentence = Jenia.analyzeAll(text, true);
		System.out.println(sentence);		
	}

}
