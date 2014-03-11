package edu.ktlab.phenominer.sslcorpora.nlp.ner.features;

import java.io.FileInputStream;
import java.nio.charset.Charset;

import opennlp.model.EventStream;
import opennlp.tools.namefind.DefaultNameContextGenerator;
import opennlp.tools.namefind.NameFinderEventStream;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.featuregen.AdaptiveFeatureGenerator;
import opennlp.tools.util.featuregen.BigramNameFeatureGenerator;
import opennlp.tools.util.featuregen.CachedFeatureGenerator;
import opennlp.tools.util.featuregen.OutcomePriorFeatureGenerator;
import opennlp.tools.util.featuregen.PrefixFeatureGenerator;
import opennlp.tools.util.featuregen.PreviousMapFeatureGenerator;
import opennlp.tools.util.featuregen.SentenceFeatureGenerator;
import opennlp.tools.util.featuregen.SuffixFeatureGenerator;
import opennlp.tools.util.featuregen.TokenClassFeatureGenerator;
import opennlp.tools.util.featuregen.TokenFeatureGenerator;
import opennlp.tools.util.featuregen.WindowFeatureGenerator;
import edu.ktlab.phenominer.sslcorpora.nlp.jeniatagger.Jenia;

public class FeatureEventStreamExample {
	static String trainingPath = "test1.txt";

	public static void main(String[] args) throws Exception {
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
				new BigramNameFeatureGenerator(), 
				new OutcomePriorFeatureGenerator(), 
				new PreviousMapFeatureGenerator(),
				new SentenceFeatureGenerator(true, false) 
		});

		Charset charset = Charset.forName("ISO-8859-1");
		ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStream(trainingPath), charset);
		ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);
		
		EventStream es = new NameFinderEventStream(sampleStream, "EN", new DefaultNameContextGenerator(featureGenerator));
		
		while (es.hasNext()) {
			System.out.println(es.next());
		}

	}

}
