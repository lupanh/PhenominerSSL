package edu.ktlab.phenominer.sslcorpora.nlp.postagger;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import opennlp.tools.cmdline.postag.POSEvaluationErrorListener;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerCrossValidator;
import opennlp.tools.postag.POSTaggerEvaluationMonitor;
import opennlp.tools.postag.POSTaggerFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class POSTaggerCrossValidation {
	static FileInputStream in;

	private static ObjectStream<POSSample> createSampleStream() throws IOException {
		in = new FileInputStream("data/trainset/postagger/biopos.wsj_genia.1.0.train");
		Charset charset = Charset.forName("ISO-8859-1");
		ObjectStream<String> lineStream = new PlainTextByLineStream(in.getChannel(), charset);
		return new POSTaggerSampleStream(lineStream);
	}

	public static void main(String[] args) throws Exception {
		POSTaggerEvaluationMonitor missclassifiedListener = new POSEvaluationErrorListener();
		opennlp.tools.util.TrainingParameters posParams = new TrainingParameters();
		posParams.put(TrainingParameters.ALGORITHM_PARAM, "MAXENT");
		posParams.put(TrainingParameters.ITERATIONS_PARAM, Integer.toString(100));
		posParams.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(1));
		POSTaggerCrossValidator cv = new POSTaggerCrossValidator("en", posParams, new POSTaggerFactory(), missclassifiedListener);
		cv.evaluate(createSampleStream(), 10);
		in.close();
	}

}
