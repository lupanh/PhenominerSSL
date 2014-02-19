/*******************************************************************************
 * Copyright (c) 2013 Mai-Vu Tran.
 ******************************************************************************/
package edu.ktlab.phenominer.sslcorpora.matching;

import java.util.Map;

public class Span implements Comparable<Span> {

	private final int start;
	private final int end;

	private final String type;

	public Span(int s, int e, String type) {

		if (s < 0 || e < 0)
			throw new IllegalArgumentException("start and end index must be zero or greater!");

		if (s > e)
			throw new IllegalArgumentException("start index must not be larger than end index!");

		start = s;
		end = e;
		this.type = type;
	}

	public Span(int s, int e) {
		this(s, e, null);
	}

	public Span(Span span, int offset) {
		this(span.start + offset, span.end + offset, span.getType());
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public String getType() {
		return type;
	}

	public int length() {
		return end - start;
	}

	public boolean contains(Span s) {
		return start <= s.getStart() && s.getEnd() <= end;
	}

	public boolean contains(int index) {
		return start <= index && index < end;
	}

	public boolean startsWith(Span s) {
		return getStart() == s.getStart() && contains(s);
	}

	public boolean intersects(Span s) {
		int sstart = s.getStart();
		// either s's start is in this or this' start is in s
		return (this.contains(s) || s.contains(this) || getStart() <= sstart && sstart < getEnd() || sstart <= getStart()
				&& getStart() < s.getEnd())
				&& (getType() != null ? type.equals(s.getType()) : true) && (s.getType() != null ? s.getType().equals(getType()) : true);
	}

	public boolean crosses(Span s) {
		int sstart = s.getStart();
		// either s's start is in this or this' start is in s
		return !this.contains(s) && !s.contains(this)
				&& (getStart() <= sstart && sstart < getEnd() || sstart <= getStart() && getStart() < s.getEnd());
	}

	public CharSequence getCoveredText(CharSequence text) {
		if (getEnd() > text.length()) {
			throw new IllegalArgumentException("The span " + toString() + " is outside the given text!");
		}

		return text.subSequence(getStart(), getEnd());
	}

	public int compareTo(Span s) {
		if (getStart() < s.getStart()) {
			return -1;
		} else if (getStart() == s.getStart()) {
			if (getEnd() > s.getEnd()) {
				return -1;
			} else if (getEnd() < s.getEnd()) {
				return 1;
			} else {
				return 0;
			}
		} else {
			return 1;
		}
	}

	@Override
	public int hashCode() {
		int res = 23;
		res = res * 37 + getStart();
		res = res * 37 + getEnd();
		if (getType() == null) {
			res = res * 37;
		} else {
			res = res * 37 + getType().hashCode();
		}

		return res;
	}

	@Override
	public boolean equals(Object o) {

		boolean result;

		if (o == this) {
			result = true;
		} else if (o instanceof Span) {
			Span s = (Span) o;

			result = (getStart() == s.getStart()) && (getEnd() == s.getEnd()) && (getType() != null ? type.equals(s.getType()) : true)
					&& (s.getType() != null ? s.getType().equals(getType()) : true);
		} else {
			result = false;
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuffer toStringBuffer = new StringBuffer(15);
		toStringBuffer.append("[");
		toStringBuffer.append(getStart());
		toStringBuffer.append("..");
		toStringBuffer.append(getEnd());
		toStringBuffer.append(")");

		return toStringBuffer.toString();
	}

	public static String[] spansToStrings(Span[] spans, CharSequence s) {
		String[] tokens = new String[spans.length];

		for (int si = 0, sl = spans.length; si < sl; si++) {
			tokens[si] = spans[si].getCoveredText(s).toString();
		}

		return tokens;
	}

	public static String[] spansToStrings(Span[] spans, String[] tokens) {
		String[] chunks = new String[spans.length];
		StringBuffer cb = new StringBuffer();
		for (int si = 0, sl = spans.length; si < sl; si++) {
			cb.setLength(0);
			for (int ti = spans[si].getStart(); ti < spans[si].getEnd(); ti++) {
				cb.append(tokens[ti]).append("_");
			}
			chunks[si] = cb.substring(0, cb.length() - 1);
		}
		return chunks;
	}

	public static String getStringAnnotated(Span[] spans, String[] tokens) {
		String[] chunks = tokens;
		StringBuffer cb = new StringBuffer();
		for (int si = 0, sl = spans.length; si < sl; si++) {
			chunks[spans[si].getStart()] = "<" + spans[si].getType() + ">" + chunks[spans[si].getStart()];
			chunks[spans[si].getEnd() - 1] = chunks[spans[si].getEnd() - 1] + "</" + spans[si].getType() + ">";
		}

		for (int i = 0; i < chunks.length; i++)
			cb.append(chunks[i] + " ");
		return cb.substring(0, cb.length() - 1);
	}

	public static String getStringAnnotated(TupleToken tuple) {
		String[] chunks = tuple.getTokens();
		StringBuffer cb = new StringBuffer();
		Map<String, Span[]> annotation = tuple.getAnnotation();

		for (String label : annotation.keySet())
			for (int si = 0, sl = annotation.get(label).length; si < sl; si++) {
				chunks[annotation.get(label)[si].getStart()] = "<" + label + ":" + annotation.get(label)[si].getType() + ">"
						+ chunks[annotation.get(label)[si].getStart()];
				chunks[annotation.get(label)[si].getEnd() - 1] = chunks[annotation.get(label)[si].getEnd() - 1] + "</" + label + ":"
						+ annotation.get(label)[si].getType() + ">";
			}

		for (int i = 0; i < chunks.length; i++)
			cb.append(chunks[i] + " ");
		return cb.substring(0, cb.length() - 1);
	}
}
