package edu.ktlab.phenominer.sslcorpora.nlp.tokenizer;

import java.io.FileInputStream;
import java.io.InputStream;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class Tokenizer {
	TokenizerME tokenizer;
	final String modelTokenizer = "models/tokenizer/biotokenizer.1.0.model";
	static Tokenizer ourInstance = new Tokenizer();

	public Tokenizer() {
		tokenizer = createTokenizerModel();
	}

	public static Tokenizer getInstance() {
		return ourInstance;
	}

	public TokenizerME createTokenizerModel() {
		InputStream in;
		try {
			in = new FileInputStream(modelTokenizer);
			TokenizerModel tokenizerModel = new TokenizerModel(in);

			return new TokenizerME(tokenizerModel);
		} catch (Exception e) {
		}
		return null;
	}

	public String[] tokenize(String text) {
		String[] tokens = tokenizer.tokenize(text);
		return tokens;
	}
}
