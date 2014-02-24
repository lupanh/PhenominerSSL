package edu.ktlab.phenominer.sslcorpora.util.splitter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import edu.ktlab.phenominer.sslcorpora.util.FileHelper;

public class SentenceSplitterExample2 {
	static SentenceDetectorME splitter;
	static String modelSplitter = "models/sentencesplitter/biosent.1.0.model";
	static String folderTest = "corpus/PubmedOMIM";

	public static void main(String[] args) throws Exception {
		splitter = createSentenceDetectorModel();
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

	public static SentenceDetectorME createSentenceDetectorModel() {
		InputStream in;
		try {
			in = new FileInputStream(modelSplitter);
			SentenceModel sentModel = new SentenceModel(in);

			return new SentenceDetectorME(sentModel);
		} catch (Exception e) {
		}
		return null;
	}

}
