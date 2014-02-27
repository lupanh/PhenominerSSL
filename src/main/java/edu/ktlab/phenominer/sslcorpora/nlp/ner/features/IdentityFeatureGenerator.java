package edu.ktlab.phenominer.sslcorpora.nlp.ner.features;

import java.util.List;

import opennlp.tools.util.featuregen.FeatureGeneratorAdapter;

class IdentityFeatureGenerator extends FeatureGeneratorAdapter {

	public void createFeatures(List<String> features, String[] tokens, int index, String[] previousOutcomes) {
		features.add(tokens[index]);
	}
}
