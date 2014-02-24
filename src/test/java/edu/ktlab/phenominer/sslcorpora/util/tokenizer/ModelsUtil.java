package edu.ktlab.phenominer.sslcorpora.util.tokenizer;

import java.io.FileInputStream;
import java.io.InputStream;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class ModelsUtil {
	static String modelSentDetect = "models/SentDetect/sentdetect.model";
	static String modelTokenizer = "models/Tokenizer/token.model";
	static String modelPOS = "models/POS/wsj_genia.pos.1.model";

	public static SentenceDetectorME createSentenceDetectorModel() {
		InputStream in;
		try {
			in = new FileInputStream(modelSentDetect);
			SentenceModel sentModel = new SentenceModel(in);

			return new SentenceDetectorME(sentModel);
		} catch (Exception e) {
		}
		return null;
	}

	public static TokenizerME createTokenizerModel() {
		InputStream in;
		try {
			in = new FileInputStream(modelTokenizer);
			TokenizerModel tokenizerModel = new TokenizerModel(in);

			return new TokenizerME(tokenizerModel);
		} catch (Exception e) {
		}
		return null;
	}

	public static POSTagger createPOSModel() {
		InputStream in;
		try {
			in = new FileInputStream(modelPOS);
			POSModel posModel = new POSModel(in);

			return new POSTaggerME(posModel);
		} catch (Exception e) {
		}
		return null;
	}	
}
