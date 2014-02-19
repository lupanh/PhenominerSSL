package edu.ktlab.phenominer.sslcorpora.util;

import java.util.ArrayList;
import java.util.List;

public class PairList<T1, T2> {
	List<Pair<T1, T2>> list = new ArrayList<Pair<T1, T2>>();

	public boolean add(Pair<T1, T2> pair) {
		return list.add(pair);
	}

	public List<T2> findLeft(T1 left) {
		List<T2> t2s = new ArrayList<T2>();
		for (Pair<T1, T2> pair : list)
			if (pair.first.equals(left))
				t2s.add(pair.second);
		return t2s;
	}

	public List<T1> findRight(T2 right) {
		List<T1> t1s = new ArrayList<T1>();
		for (Pair<T1, T2> pair : list)
			if (pair.second.equals(right))
				t1s.add(pair.first);
		return t1s;
	}
}
