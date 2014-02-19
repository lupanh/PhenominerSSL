package edu.ktlab.phenominer.sslcorpora.matching;

import java.util.HashMap;

import edu.ktlab.phenominer.sslcorpora.matching.LongestMatching;
import edu.ktlab.phenominer.sslcorpora.matching.Span;

public class LongestMatchingExample {
	public static HashMap<String, String> dict = new HashMap<String, String>();

	public static void main(String[] args) {
		dict.put("kidney", "ANATOMY");
		dict.put("abnormal kidney", "PHENOTYPE");
		dict.put("kidney abnormality", "PHENOTYPE");

		String sentence = "Abnormal kidney is a phenotype entity";
		String tokens[] = sentence.split("[^\\w]+");
		LongestMatching matching = new LongestMatching(dict);
		Span[] spans = matching.tagging(tokens, -1, true);
		System.out.println(Span.getStringAnnotated(spans, tokens));
	}

}
