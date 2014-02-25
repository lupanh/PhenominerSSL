package edu.ktlab.phenominer.sslcorpora.nlp.postagger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerCrossValidator;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.model.ModelType;

public class POSTaggerCrossValidation {

	private static ObjectStream<POSSample> createSampleStream()
			throws IOException {
		InputStream in = new FileInputStream("data/POS/WSJ_Genia.pos");

		return new POSTaggerSampleStream((new InputStreamReader(in)));
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		POSTaggerCrossValidator cv = new POSTaggerCrossValidator("en", ModelType.MAXENT, null, null);
		
		cv.evaluate(createSampleStream(), 10);
	}

}
