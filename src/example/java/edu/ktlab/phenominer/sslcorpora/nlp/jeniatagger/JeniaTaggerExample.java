package edu.ktlab.phenominer.sslcorpora.nlp.jeniatagger;

import com.jmcejuela.bio.jenia.JeniaTagger;
import com.jmcejuela.bio.jenia.common.Sentence;

public class JeniaTaggerExample {
	public static void main(String[] args) {
		JeniaTagger.setModelsPath("models/genia");
		String text = "My name Vu";
		Sentence sentence = JeniaTagger.analyzeAll(text, false);
		System.out.println(sentence);
	}

}
