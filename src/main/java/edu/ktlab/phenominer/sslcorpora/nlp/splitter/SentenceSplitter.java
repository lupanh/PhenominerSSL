package edu.ktlab.phenominer.sslcorpora.nlp.splitter;

import java.io.FileInputStream;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceSplitter {
	final String modelSplitter = "models/sentencesplitter/biosent.1.0.model";
	SentenceDetectorME splitter;
	static SentenceSplitter ourInstance = new SentenceSplitter();

	SentenceSplitter() {
		splitter = createSentenceDetectorModel();
	}

	public static SentenceSplitter getInstance() {
		return ourInstance;
	}

	public SentenceDetectorME createSentenceDetectorModel() {
		InputStream in;
		try {
			in = new FileInputStream(modelSplitter);
			SentenceModel sentModel = new SentenceModel(in);

			return new SentenceDetectorME(sentModel);
		} catch (Exception e) {
		}
		return null;
	}

	public String[] split(String text) {
		String[] sents = splitter.sentDetect(text);
		return sents;
	}
}
