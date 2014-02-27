package edu.ktlab.phenominer.sslcorpora.matching;

import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.namefind.DictionaryNameFinder;
import opennlp.tools.namefind.TokenNameFinder;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;
import opennlp.tools.util.StringList;

public class DictionaryNameFinderExample {
	static Dictionary mDictionary = new Dictionary(false);
	static TokenNameFinder mNameFinder;

	public static void main(String[] args) {
		mDictionary.put(new StringList(new String[] { "vanessa" }));
		mDictionary.put(new StringList(new String[] { "Hai", "Bà", "Trưng" }));
		mNameFinder = new DictionaryNameFinder(mDictionary);

		String sentence = "Hai Bà Trưng là bạn của Vanessa";
		SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
		String tokens[] = tokenizer.tokenize(sentence);
		Span[] names = mNameFinder.find(tokens);
		String[] spans = BioSpan.spansToStrings(names, tokens);
		for (String span : spans)
			System.out.println(span);
	}

}
