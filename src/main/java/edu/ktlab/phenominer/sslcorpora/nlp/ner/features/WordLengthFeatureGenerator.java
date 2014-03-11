package edu.ktlab.phenominer.sslcorpora.nlp.ner.features;

import java.util.List;

import opennlp.tools.util.featuregen.FeatureGeneratorAdapter;

public class WordLengthFeatureGenerator extends FeatureGeneratorAdapter {
	@Override
	public void createFeatures(List<String> features, String[] tokens, int index, String[] previousOutcomes) {
		features.add("w=length=" + tokens[index].length());
	}

}
