package edu.ktlab.phenominer.sslcorpora.nlp.splitter;

import java.io.File;
import java.nio.charset.Charset;

import opennlp.tools.sentdetect.SentenceDetectorME;
import edu.ktlab.phenominer.sslcorpora.util.FileHelper;

public class SentenceSplitterExample2 {
	static SentenceDetectorME splitter;
	static String modelSplitter = "models/sentencesplitter/biosent.1.0.model";
	static String folderTest = "corpus/PubmedOMIM";

	public static void main(String[] args) throws Exception {
		splitter = SentenceSplitter.getInstance().createSentenceDetectorModel();
		new File("sentenceExample2.txt").delete();
		File folder = new File(folderTest);
		for (File file : folder.listFiles()) {
			System.out.println(file.getName());
			String content = "FILE: " + file.getName() + "\n";
			String text = FileHelper.readFileAsString(file, Charset.forName("UTF-8"));
			String[] sents = splitter.sentDetect(text);
			for (String sent : sents)
				content += sent + "\n";
			FileHelper.appendToFile(content, new File("sentenceExample2.txt"), Charset.forName("UTF-8"));
		}
	}
}
