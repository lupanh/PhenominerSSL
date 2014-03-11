package edu.ktlab.phenominer.sslcorpora.nlp.ner;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderEvaluator;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.eval.FMeasure;
import opennlp.tools.util.featuregen.AdaptiveFeatureGenerator;
import opennlp.tools.util.featuregen.BigramNameFeatureGenerator;
import opennlp.tools.util.featuregen.CachedFeatureGenerator;
import opennlp.tools.util.featuregen.CharacterNgramFeatureGenerator;
import opennlp.tools.util.featuregen.OutcomePriorFeatureGenerator;
import opennlp.tools.util.featuregen.PrefixFeatureGenerator;
import opennlp.tools.util.featuregen.PreviousMapFeatureGenerator;
import opennlp.tools.util.featuregen.SentenceFeatureGenerator;
import opennlp.tools.util.featuregen.SuffixFeatureGenerator;
import opennlp.tools.util.featuregen.TokenClassFeatureGenerator;
import opennlp.tools.util.featuregen.TokenFeatureGenerator;
import opennlp.tools.util.featuregen.WindowFeatureGenerator;
import edu.ktlab.phenominer.sslcorpora.eval.ApproximateFMeasure;
import edu.ktlab.phenominer.sslcorpora.eval.TokenNameFinderApproximateEvaluator;
import edu.ktlab.phenominer.sslcorpora.matching.BioSpan;
import edu.ktlab.phenominer.sslcorpora.nlp.jeniatagger.Jenia;
import edu.ktlab.phenominer.sslcorpora.nlp.ner.features.JeniaFeatureGenerator;
import edu.ktlab.phenominer.sslcorpora.nlp.ner.features.NgramTokenFeatureGenerator;
import edu.ktlab.phenominer.sslcorpora.nlp.ner.features.WordLengthFeatureGenerator;

public class OpenNLPNER {
	AdaptiveFeatureGenerator featureGenerator;
	
	public OpenNLPNER () {
		featureGenerator = createFeatureGenerator();
	}
	
	public OpenNLPNER (AdaptiveFeatureGenerator featureGenerator) {
		this.featureGenerator = featureGenerator;
	}
	
	public AdaptiveFeatureGenerator createFeatureGenerator() {
		Jenia.setModelsPath("models/genia");
		AdaptiveFeatureGenerator featureGenerator = new CachedFeatureGenerator(new AdaptiveFeatureGenerator[] {
				new WindowFeatureGenerator(new TokenClassFeatureGenerator(true), 2, 2),
				new WindowFeatureGenerator(new TokenFeatureGenerator(true), 2, 2),
				new WindowFeatureGenerator(new NgramTokenFeatureGenerator(true, 2, 2), 2, 2),
				new WindowFeatureGenerator(new NgramTokenFeatureGenerator(true, 3, 3), 2, 2),
				new WindowFeatureGenerator(new JeniaFeatureGenerator(), 2, 2),
				new PrefixFeatureGenerator(),
				new SuffixFeatureGenerator(),
				new WordLengthFeatureGenerator(),
				new CharacterNgramFeatureGenerator(2, 5),
				new BigramNameFeatureGenerator(), 
				new OutcomePriorFeatureGenerator(), 
				new PreviousMapFeatureGenerator(),
				new SentenceFeatureGenerator(true, false) 
		});
		return featureGenerator;
	}

	public void trainNER(String trainingPath, String modelFilePath, int iterator, int cutoff) throws Exception {
		Charset charset = Charset.forName("UTF-8");
		ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStream(trainingPath), charset);
		ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);
		TokenNameFinderModel model;

		try {
			model = NameFinderME.train("en", "PHENOTYPE", sampleStream, featureGenerator, Collections.<String, Object> emptyMap(), iterator, cutoff);
		} finally {
			sampleStream.close();
		}

		BufferedOutputStream modelOut = null;
		try {
			modelOut = new BufferedOutputStream(new FileOutputStream(modelFilePath));
			model.serialize(modelOut);
		} finally {
			if (modelOut != null) {
				modelOut.close();
			}
		}
	}

	public void evaluatebyExactMatching(String testPath, String modelPath) throws Exception {
		InputStream modelIn = new FileInputStream(modelPath);
		TokenNameFinderModel NEModel = new TokenNameFinderModel(modelIn);
		TokenNameFinderEvaluator evaluator = new TokenNameFinderEvaluator(new NameFinderME(NEModel));
		Charset charset = Charset.forName("UTF-8");
		ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStream(testPath), charset);
		ObjectStream<NameSample> testStream = new NameSampleDataStream(lineStream);
		evaluator.evaluate(testStream);
		FMeasure result = evaluator.getFMeasure();
		System.out.println(result.toString());
	}
	
	public void evaluatebyApproximateMatching(String testPath, String modelPath) throws Exception {
		InputStream modelIn = new FileInputStream(modelPath);
		TokenNameFinderModel NEModel = new TokenNameFinderModel(modelIn);
		TokenNameFinderApproximateEvaluator evaluator = new TokenNameFinderApproximateEvaluator(new NameFinderME(NEModel));
		Charset charset = Charset.forName("UTF-8");
		ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStream(testPath), charset);
		ObjectStream<NameSample> testStream = new NameSampleDataStream(lineStream);
		evaluator.evaluate(testStream);
		ApproximateFMeasure result = evaluator.getFMeasure();
		System.out.println(result.toString());
	}
	
	public void recognize(String testPath, String modelPath) throws Exception {
		InputStream modelIn = new FileInputStream(modelPath);
		TokenNameFinderModel NEModel = new TokenNameFinderModel(modelIn);
		Charset charset = Charset.forName("UTF-8");
		ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStream(testPath), charset);
		ObjectStream<NameSample> testStream = new NameSampleDataStream(lineStream);
		NameSample sample;
		NameFinderME finder = new NameFinderME(NEModel);
	    while ((sample = testStream.read()) != null) {
	    	Span[] spans = finder.find(sample.getSentence());
	    	System.out.println(BioSpan.getStringNameSample(spans, sample.getSentence()));
	    }
	}
	
	public void nFoldEvaluate(String trainingPath, int numFolds, int iterator, int cutoff) throws Exception {
		FileInputStream sampleDataIn = new FileInputStream(trainingPath);
		Charset charset = Charset.forName("UTF-8");
		ObjectStream<String> lineStream = new PlainTextByLineStream(sampleDataIn.getChannel(), charset);
		ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);

		OpenNLPNERCrossValidator evaluator = new OpenNLPNERCrossValidator("en", cutoff, iterator, featureGenerator);

		evaluator.evaluate(sampleStream, numFolds);
		FMeasure result = evaluator.getFMeasure();
		System.out.println(result.toString());
		sampleDataIn.close();
	}
}
