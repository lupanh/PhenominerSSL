package edu.ktlab.phenominer.sslcorpora.nlp.ner.features;

import java.util.ArrayList;
import java.util.List;

import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.util.StringList;
import opennlp.tools.util.featuregen.AdaptiveFeatureGenerator;
import opennlp.tools.util.featuregen.DictionaryFeatureGenerator;

public class DictionaryFeatureGeneratorExample {
	public static void main(String[] args) {
		StringList entry1 = new StringList(new String[] { "ngôi", "sao" });
		StringList entry2 = new StringList(new String[] { "hà", "nội" });
		StringList entry3 = new StringList(new String[] { "sao", "sáng" });
		Dictionary dict = new Dictionary(false);
		dict.put(entry1);
		dict.put(entry2);
		dict.put(entry3);

		AdaptiveFeatureGenerator dictionaryFeatureGenerator = new DictionaryFeatureGenerator("name", dict);
		List<String> features = new ArrayList<String>();
		String[] testSentence = new String[] { "Hà", "Nội", "của", "tôi", "là", "ngôi", "sao", "sáng" };
		int testTokenIndex = 6;

		dictionaryFeatureGenerator.createFeatures(features, testSentence, testTokenIndex, null);
		for (String feature : features)
			System.out.println(feature);
	}

}
