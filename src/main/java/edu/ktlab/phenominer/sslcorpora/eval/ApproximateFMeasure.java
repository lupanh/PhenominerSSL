package edu.ktlab.phenominer.sslcorpora.eval;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import opennlp.tools.util.Span;

public class ApproximateFMeasure {
	public double getPrecisionScore() {
		return mapResult.get("total_PRD") > 0 ? (double) mapResult.get("total_TP") / (double) mapResult.get("total_PRD") : 0;
	}

	public double getPrecisionScore(String type) {
		if (mapResult.get(type + "_PRD") == null || mapResult.get(type + "_TP") == null)
			return 0;
		return mapResult.get(type + "_PRD") > 0 ? (double) mapResult.get(type + "_TP") / (double) mapResult.get(type + "_PRD") : 0;
	}

	public double getRecallScore() {
		return mapResult.get("total_TAG") > 0 ? (double) mapResult.get("total_TP") / (double) mapResult.get("total_TAG") : 0;
	}

	public double getRecallScore(String type) {
		if (mapResult.get(type + "_TAG") == null || mapResult.get(type + "_TP") == null)
			return 0;
		return mapResult.get(type + "_TAG") > 0 ? (double) mapResult.get(type + "_TP") / (double) mapResult.get(type + "_TAG") : 0;
	}

	public double getFMeasure() {
		if (getPrecisionScore() + getRecallScore() > 0) {
			return 2 * (getPrecisionScore() * getRecallScore()) / (getPrecisionScore() + getRecallScore());
		} else {
			// cannot divide by zero, return error code
			return 0;
		}
	}

	public double getFMeasure(String type) {
		if (getPrecisionScore(type) + getRecallScore(type) > 0) {
			return 2 * (getPrecisionScore(type) * getRecallScore(type)) / (getPrecisionScore(type) + getRecallScore(type));
		} else {
			// cannot divide by zero, return error code
			return 0;
		}
	}

	public void updateScores(Span references[], Span predictions[]) {
		Map<String, Integer> map = evaluate(references, predictions);
		if (map != null)
			for (String key : map.keySet()) {
				addMap(mapResult, key, map.get(key));
			}
	}

	@Override
	public String toString() {
		String out = "";
		for (String key : labels) {
			out += "=============" + key + "=============\n";
			out += "Precision: " + Double.toString(getPrecisionScore(key)) 
					+ "\t(" + mapResult.get(key + "_TP") + "/" + mapResult.get(key + "_PRD") + ")\n" 
					+ "Recall: " + Double.toString(getRecallScore(key)) 
					+ "\t(" + mapResult.get(key + "_TP") + "/" + mapResult.get(key + "_TAG") + ")\n"
					+ "F-Measure: "	+ Double.toString(getFMeasure(key)) + "\n";
		}
		return out;
	}

	Map<String, Integer> mapResult = new HashMap<String, Integer>();
	Set<String> labels = new HashSet<String>();

	void addMap(Map<String, Integer> map, String key, int value) {
		if (!map.containsKey(key))
			map.put(key, value);
		else {
			int oldValue = map.get(key);
			int newValue = oldValue + value;
			map.remove(key);
			map.put(key, newValue);
		}
	}

	public Map<String, Integer> evaluate(Span references[], Span predictions[]) {
		if (references.length == 0)
			return null;

		Map<String, Integer> map = new HashMap<String, Integer>();

		Set<Span> used = new HashSet<Span>();

		addMap(map, "total_TAG", references.length);
		addMap(map, "total_PRD", predictions.length);

		for (int predictedIndex = 0; predictedIndex < predictions.length; predictedIndex++) {
			Span predictedName = predictions[predictedIndex];
			addMap(map, predictedName.getType() + "_PRD", 1);
		}

		for (int referenceIndex = 0; referenceIndex < references.length; referenceIndex++) {
			Span referenceName = references[referenceIndex];
			addMap(map, referenceName.getType() + "_TAG", 1);
			labels.add(referenceName.getType());

			for (int predictedIndex = 0; predictedIndex < predictions.length; predictedIndex++) {
				if (used.contains(predictions[predictedIndex]))
					continue;

				if (referenceName.intersects(predictions[predictedIndex])) {
					used.add(predictions[predictedIndex]);
					addMap(map, predictions[predictedIndex].getType() + "_TP", 1);
					addMap(map, "total_TP", 1);
					break;
				}
			}
		}

		return map;
	}

	public Map<String, Integer> getMapResult() {
		return mapResult;
	}

	public void setMapResult(Map<String, Integer> mapResult) {
		this.mapResult = mapResult;
	}

	public Set<String> getLabels() {
		return labels;
	}

	public void setLabels(Set<String> labels) {
		this.labels = labels;
	}
}
