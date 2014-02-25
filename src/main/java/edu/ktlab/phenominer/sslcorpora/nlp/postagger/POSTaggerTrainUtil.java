package edu.ktlab.phenominer.sslcorpora.nlp.postagger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.model.ModelType;

public class POSTaggerTrainUtil {
	static int cutoff = 1;
	static int iteration = 200;

	private static ObjectStream<POSSample> createSampleStream() throws IOException {
		InputStream in = new FileInputStream("data/trainset/postagger/biopos.wsj_genia.1.0.train");
		return new POSTaggerSampleStream((new InputStreamReader(in)));
	}

	@SuppressWarnings("deprecation")
	static POSModel trainPOSModel(ModelType type) throws Exception {
		return POSTaggerME.train("en", createSampleStream(), type, null, null, cutoff, iteration);
	}

	public static void trainPOSTaggerModel() throws Exception {
		POSModel posModel = trainPOSModel(ModelType.MAXENT);

		OutputStream out = new FileOutputStream("models/postagger/biopos.wsj_genia.1.0.model");
		posModel.serialize(out);
		out.close();
	}

	public static void main(String[] args) throws Exception {
		trainPOSTaggerModel();
	}

}
