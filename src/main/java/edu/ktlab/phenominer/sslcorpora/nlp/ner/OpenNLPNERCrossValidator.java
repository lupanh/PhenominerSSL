package edu.ktlab.phenominer.sslcorpora.nlp.ner;

import java.io.IOException;
import java.util.Collections;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.TokenNameFinderEvaluationMonitor;
import opennlp.tools.namefind.TokenNameFinderEvaluator;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.eval.CrossValidationPartitioner;
import opennlp.tools.util.eval.FMeasure;
import opennlp.tools.util.featuregen.AdaptiveFeatureGenerator;

public class OpenNLPNERCrossValidator {
	String languageCode;
	TokenNameFinderEvaluationMonitor[] listeners;
	AdaptiveFeatureGenerator featureGenerator;
	int iterations;
	int cutoff;
	FMeasure fmeasure = new FMeasure();

	public OpenNLPNERCrossValidator(String languageCode, int cutoff, int iterations, AdaptiveFeatureGenerator featureGenerator) {
		this.languageCode = languageCode;
		this.featureGenerator = featureGenerator;
		this.iterations = iterations;
		this.cutoff = cutoff;
	}

	public void evaluate(ObjectStream<NameSample> samples, int nFolds) throws IOException {
		CrossValidationPartitioner<NameSample> partitioner = new CrossValidationPartitioner<NameSample>(samples, nFolds);
		while (partitioner.hasNext()) {
			CrossValidationPartitioner.TrainingSampleStream<NameSample> trainingSampleStream = partitioner.next();
			TokenNameFinderModel model = NameFinderME.train("en", "PHENOTYPE", trainingSampleStream, featureGenerator,
					Collections.<String, Object> emptyMap(), iterations, cutoff);
			TokenNameFinderEvaluator evaluator = new TokenNameFinderEvaluator(new NameFinderME(model), listeners);
			evaluator.evaluate(trainingSampleStream.getTestSampleStream());
			fmeasure.mergeInto(evaluator.getFMeasure());
		}
	}

	public FMeasure getFMeasure() {
		return fmeasure;
	}
}
