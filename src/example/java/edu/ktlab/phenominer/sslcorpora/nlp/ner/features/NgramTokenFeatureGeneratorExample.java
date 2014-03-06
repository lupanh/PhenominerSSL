package edu.ktlab.phenominer.sslcorpora.nlp.ner.features;

import java.util.ArrayList;
import java.util.List;

import opennlp.tools.util.featuregen.AdaptiveFeatureGenerator;
import opennlp.tools.util.featuregen.WindowFeatureGenerator;

public class NgramTokenFeatureGeneratorExample {
	public static void main(String[] args) {
		AdaptiveFeatureGenerator windowFeatureGenerator = new WindowFeatureGenerator(new NgramTokenFeatureGenerator(true, 2, 2), 2, 2);
		List<String> features = new ArrayList<String>();
		String[] testSentence = new String[] { "Hà", "Nội", "của", "tôi", "là", "ngôi", "sao", "sáng" };
		int testTokenIndex = 4;

		windowFeatureGenerator.createFeatures(features, testSentence, testTokenIndex, null);
		for (String feature : features)
			System.out.println(feature);
	}
}
