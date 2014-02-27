package edu.ktlab.phenominer.sslcorpora.nlp.ner.features;

import java.util.ArrayList;
import java.util.List;

import opennlp.tools.util.featuregen.AdaptiveFeatureGenerator;
import opennlp.tools.util.featuregen.WindowFeatureGenerator;

public class WindowFeatureGeneratorExample {
	public static void main(String[] args) {
		AdaptiveFeatureGenerator windowFeatureGenerator = new WindowFeatureGenerator(new IdentityFeatureGenerator(), 0, 0);
		List<String> features = new ArrayList<String>();
		String[] testSentence = new String[] { "a", "b", "c", "d", "e", "f", "g", "h" };
		int testTokenIndex = 2;

		windowFeatureGenerator.createFeatures(features, testSentence, testTokenIndex, null);
	}

}
