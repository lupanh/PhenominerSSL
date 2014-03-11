package edu.ktlab.phenominer.sslcorpora.nlp.jeniatagger;

import com.jmcejuela.bio.jenia.JeniaTagger;
import com.jmcejuela.bio.jenia.common.Sentence;

import edu.stanford.nlp.util.StringUtils;

public class Jenia extends JeniaTagger {
	public static Sentence analyzeAll(final String[] tokens, boolean dont_tokenize) {
		try {
			return analyzeAll(StringUtils.join(tokens), dont_tokenize);
		} catch (Exception e) {
			System.out.println(StringUtils.join(tokens));
			return null;
		}

	}
}
