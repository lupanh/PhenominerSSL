package edu.ktlab.phenominer.sslcorpora.nlp.ner;

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
import edu.ktlab.phenominer.sslcorpora.nlp.ner.features.JeniaFeatureGenerator;
import edu.ktlab.phenominer.sslcorpora.nlp.ner.features.NgramTokenFeatureGenerator;
import edu.ktlab.phenominer.sslcorpora.nlp.ner.features.WordLengthFeatureGenerator;

public class OpenNLPNERExample1 {
	static AdaptiveFeatureGenerator createFeatureGenerator() {
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
		return featureGenerator;
	}
	
	public static void main(String[] args) throws Exception {
		OpenNLPNER tester = new OpenNLPNER(createFeatureGenerator());
		tester.trainNER("data/phenominer/phenominerssl2014.5000.bf.corpus", "models/phenominer/phenominerssl2014.5000.bf.model", 100, 2);
		tester.evaluatebyApproximateMatching("data/phenominer/phenominer2012.bf.corpus", "models/phenominer/phenominerssl2014.5000.bf.model");
		//tester.recognize("data/phenominer/phenominer2012.full.corpus", "models/phenominer/phenominer2012.full.model");
		//tester.nFoldEvaluate("data/phenominer/phenominer2012.bf.corpus", 10, 100, 1);
	}
}
