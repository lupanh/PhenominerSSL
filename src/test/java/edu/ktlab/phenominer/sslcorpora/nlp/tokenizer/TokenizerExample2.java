package edu.ktlab.phenominer.sslcorpora.nlp.tokenizer;

import java.io.File;
import java.nio.charset.Charset;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.tokenize.TokenizerME;
import edu.ktlab.phenominer.sslcorpora.nlp.splitter.SentenceSplitter;
import edu.ktlab.phenominer.sslcorpora.util.FileHelper;

public class TokenizerExample2 {
	static TokenizerME tokenizer;
	static SentenceDetectorME splitter;
	static String modelTokenizer = "models/tokenizer/biotokenizer.1.0.model";
	static String modelSplitter = "models/sentencesplitter/biosent.1.0.model";
	static String folderTest = "corpus/PubmedOMIM";

	public static void main(String[] args) throws Exception {
		splitter = SentenceSplitter.getInstance().createSentenceDetectorModel();
		tokenizer = Tokenizer.getInstance().createTokenizerModel();
		new File("sentenceExample2.txt").delete();
		File folder = new File(folderTest);
		for (File file : folder.listFiles()) {
			System.out.println(file.getName());
			String content = "FILE: " + file.getName() + "\n";
			String text = FileHelper.readFileAsString(file, Charset.forName("UTF-8"));
			String[] sents = splitter.sentDetect(text);
			for (String sent : sents) {
				String[] tokens = tokenizer.tokenize(sent);
				for (String token : tokens)
					content += token + " ";
				content += "\n";
			}

			FileHelper.appendToFile(content, new File("sentenceExample2.txt"), Charset.forName("UTF-8"));
		}
	}
}
