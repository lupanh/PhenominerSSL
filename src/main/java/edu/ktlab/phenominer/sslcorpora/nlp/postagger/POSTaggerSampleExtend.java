package edu.ktlab.phenominer.sslcorpora.nlp.postagger;

import java.util.List;

import opennlp.tools.postag.POSSample;
import opennlp.tools.util.InvalidFormatException;

public class POSTaggerSampleExtend extends POSSample{
	public POSTaggerSampleExtend(String sentence[], String tags[]) {
		super(sentence, tags);
	}

	public POSTaggerSampleExtend(List<String> sentence, List<String> tags) {
		super(sentence, tags);
	}
	
	public static POSSample parse(String sentenceString, String regex)
			throws InvalidFormatException {

		String tokenTags[] = sentenceString.split(regex);

		String sentence[] = new String[tokenTags.length];
		String tags[] = new String[tokenTags.length];

		for (int i = 0; i < tokenTags.length; i++) {
			int split = tokenTags[i].lastIndexOf("_");

			if (split == -1) {
				throw new InvalidFormatException(
						"Cannot find \"_\" inside token!");
			}

			sentence[i] = tokenTags[i].substring(0, split);
			tags[i] = tokenTags[i].substring(split + 1);
		}

		return new POSSample(sentence, tags);
	}
	
	public static POSSample parse(String sentenceString, String regex, String character)
			throws InvalidFormatException {

		String tokenTags[] = sentenceString.split(regex);

		String sentence[] = new String[tokenTags.length];
		String tags[] = new String[tokenTags.length];

		for (int i = 0; i < tokenTags.length; i++) {
			int split = tokenTags[i].lastIndexOf(character);

			if (split == -1) {
				throw new InvalidFormatException(
						"Cannot find \"_\" inside token!");
			}

			sentence[i] = tokenTags[i].substring(0, split);
			tags[i] = tokenTags[i].substring(split + 1);
		}

		return new POSSample(sentence, tags);
	}	
}
