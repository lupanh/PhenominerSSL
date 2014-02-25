package edu.ktlab.phenominer.sslcorpora.nlp.postagger;

import java.io.FileInputStream;
import java.io.InputStream;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class POSTagger {
	static String modelPOSTagger = "models/postagger/biopos.wsj_genia.1.0.model";
	static POSTaggerME tagger;
	static POSTagger ourInstance = new POSTagger();

	POSTagger() {
		tagger = createSentenceDetectorModel();
	}

	public static POSTagger getInstance() {
		return ourInstance;
	}

	public POSTaggerME createSentenceDetectorModel() {
		InputStream in;
		try {
			in = new FileInputStream(modelPOSTagger);
			POSModel posModel = new POSModel(in);
			in.close();
			return new POSTaggerME(posModel);
		} catch (Exception e) {
		}
		return null;
	}

	public String[] tagging(String[] tokens) {
		String tags[] = tagger.tag(tokens);
		return tags;
	}
}
