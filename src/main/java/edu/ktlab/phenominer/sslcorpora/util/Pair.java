/*******************************************************************************
 * Copyright (c) 2013 Mai-Vu Tran.
 ******************************************************************************/
package edu.ktlab.phenominer.sslcorpora.util;

public class Pair<T1, T2> implements Comparable<Pair<T1, T2>> {
	T1 first;
	T2 second;
	String label;

	public Pair() {
	}

	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}

	public Pair(T1 first, T2 second, String label) {
		this.first = first;
		this.second = second;
		this.label = label;
	}

	public T1 first() {
		return first;
	}

	public T2 second() {
		return second;
	}

	public void setFirst(T1 o) {
		first = o;
	}

	public void setSecond(T2 o) {
		second = o;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "(" + first + "," + second + ")[" + label + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair<T1, T2> other = (Pair<T1, T2>) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	@SuppressWarnings("unchecked")
	public int compareTo(Pair<T1, T2> another) {
		int comp = ((Comparable<T1>) first()).compareTo(another.first());
		if (comp != 0) {
			return comp;
		} else {
			return ((Comparable<T2>) second()).compareTo(another.second());
		}
	}
}
