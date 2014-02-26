/*******************************************************************************
 * Copyright (c) 2013 Mai-Vu Tran.
 ******************************************************************************/
package edu.ktlab.phenominer.sslcorpora.matching;

import java.util.HashMap;
import java.util.Map;

public class BioTupleToken {
	String[] tokens;
	Map<String, BioSpan[]> annotation = new HashMap<String, BioSpan[]>();

	public BioTupleToken(String[] tokens) {
		this.tokens = tokens;
	}

	public BioTupleToken(String[] tokens, Map<String, BioSpan[]> annotation) {
		this.tokens = tokens;
		this.annotation = annotation;
	}

	public void put(String label, BioSpan[] tagged) {
		annotation.put(label, tagged);
	}

	public String[] getTokens() {
		return tokens;
	}

	public void setTokens(String[] tokens) {
		this.tokens = tokens;
	}

	public Map<String, BioSpan[]> getAnnotation() {
		return annotation;
	}

	public void setAnnotation(Map<String, BioSpan[]> annotation) {
		this.annotation = annotation;
	}
}
