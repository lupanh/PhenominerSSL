package edu.ktlab.phenominer.sslcorpora.nlp.postagger;

import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;

public class POSTaggerExample1 {
	static POSTaggerME tagger;

	public static void main(String[] args) {
		tagger = POSTagger.getInstance().createSentenceDetectorModel();
		String sentenceString = "increased brown adipose tissue amount";
		String tags[] = tagger.tag(WhitespaceTokenizer.INSTANCE.tokenize(sentenceString));

		for (String tag : tags)
			System.out.print(tag + " ");
	}
}
