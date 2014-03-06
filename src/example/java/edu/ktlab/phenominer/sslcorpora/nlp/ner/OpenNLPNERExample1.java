package edu.ktlab.phenominer.sslcorpora.nlp.ner;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderEvaluator;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
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
import edu.ktlab.phenominer.sslcorpora.nlp.ner.features.NgramTokenFeatureGenerator;

public class OpenNLPNERExample1 {
	public AdaptiveFeatureGenerator createFeatureGenerator() {
		AdaptiveFeatureGenerator featureGenerator = new CachedFeatureGenerator(new AdaptiveFeatureGenerator[] {
				new WindowFeatureGenerator(new TokenClassFeatureGenerator(true), 2, 2),
				new WindowFeatureGenerator(new TokenFeatureGenerator(true), 2, 2),
				new WindowFeatureGenerator(new NgramTokenFeatureGenerator(true, 2, 2), 2, 2),
				new WindowFeatureGenerator(new NgramTokenFeatureGenerator(true, 3, 3), 2, 2),
				new PrefixFeatureGenerator(),
				new SuffixFeatureGenerator(),				
				new CharacterNgramFeatureGenerator(2, 5),
				new BigramNameFeatureGenerator(), 
				new OutcomePriorFeatureGenerator(), 
				new PreviousMapFeatureGenerator(),
				new SentenceFeatureGenerator(true, false) });
		return featureGenerator;
	}

	public void trainNER(String trainingPath, String modelFilePath) throws Exception {
		AdaptiveFeatureGenerator featureGenerator = this.createFeatureGenerator();
		Charset charset = Charset.forName("ISO-8859-1");
		ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStream(trainingPath), charset);
		ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);
		TokenNameFinderModel model;

		try {
			model = NameFinderME.train("en", "PHENOTYPE", sampleStream, featureGenerator, Collections.<String, Object> emptyMap(), 100, 2);
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

	public void evaluateNER(String testPath, String modelPath) throws Exception {
		InputStream modelIn = new FileInputStream(modelPath);
		TokenNameFinderModel NEModel = new TokenNameFinderModel(modelIn);
		TokenNameFinderEvaluator evaluator = new TokenNameFinderEvaluator(new NameFinderME(NEModel));
		Charset charset = Charset.forName("ISO-8859-1");
		ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStream(testPath), charset);
		ObjectStream<NameSample> testStream = new NameSampleDataStream(lineStream);
		evaluator.evaluate(testStream);
		FMeasure result = evaluator.getFMeasure();
		System.out.println(result.toString());
	}

	public Dictionary getDict(String dictPath) throws Exception {
		InputStream is = new FileInputStream(dictPath);
		Dictionary dict = new Dictionary(is);
		return dict;
	}

	public POSTagger initializePOSTagger(String modelPath) throws Exception {
		InputStream modelIn = new FileInputStream(modelPath);
		POSModel model = new POSModel(modelIn);
		modelIn.close();
		POSTaggerME tagger = new POSTaggerME(model);
		return tagger;
	}

	public ChunkerME initializeChunker(String modelPath) throws Exception {
		InputStream modelIn = new FileInputStream(modelPath);
		ChunkerModel model = new ChunkerModel(modelIn);
		modelIn.close();
		ChunkerME chunker = new ChunkerME(model);
		return chunker;
	}

	public void nFoldEvaluate(String trainingPath, int numFolds) throws Exception {
		FileInputStream sampleDataIn = new FileInputStream(trainingPath);
		Charset charset = Charset.forName("ISO-8859-1");
		ObjectStream<String> lineStream = new PlainTextByLineStream(sampleDataIn.getChannel(), charset);
		ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);

		AdaptiveFeatureGenerator featureGenerator = this.createFeatureGenerator();
		OpenNLPNERCrossValidator evaluator = new OpenNLPNERCrossValidator("en", 2, 100, featureGenerator);

		evaluator.evaluate(sampleStream, numFolds);
		FMeasure result = evaluator.getFMeasure();
		System.out.println(result.toString());
		sampleDataIn.close();
	}

	public static void main(String[] args) throws Exception {
		OpenNLPNERExample1 tester = new OpenNLPNERExample1();

		//tester.trainNER("data/phenominer/phenominer2012.full.corpus", "models/phenominer/phenominer2012.full.model");
		//tester.evaluateNER("data/phenominer/phenominer2012.full.corpus", "models/phenominer/phenominer2012.full.model");
		tester.nFoldEvaluate("data/trainset/nlpba/nlpba.train", 10);
	}

}
