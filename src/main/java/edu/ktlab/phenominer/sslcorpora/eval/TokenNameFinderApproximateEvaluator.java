package edu.ktlab.phenominer.sslcorpora.eval;

import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.TokenNameFinder;
import opennlp.tools.namefind.TokenNameFinderEvaluationMonitor;
import opennlp.tools.util.Span;
import opennlp.tools.util.eval.Evaluator;

public class TokenNameFinderApproximateEvaluator extends Evaluator<NameSample> {
	private ApproximateFMeasure fmeasure = new ApproximateFMeasure();
	private TokenNameFinder nameFinder;
	
	public TokenNameFinderApproximateEvaluator(TokenNameFinder nameFinder, TokenNameFinderEvaluationMonitor... listeners) {
		super(listeners);
		this.nameFinder = nameFinder;
	}
	
	@Override
	protected NameSample processSample(NameSample reference) {

		if (reference.isClearAdaptiveDataSet()) {
			nameFinder.clearAdaptiveData();
		}

		Span predictedNames[] = nameFinder.find(reference.getSentence());
		Span references[] = reference.getNames();

		for (int i = 0; i < references.length; i++) {
			if (references[i].getType() == null) {
				references[i] = new Span(references[i].getStart(), references[i].getEnd(), "default");
			}
		}

		fmeasure.updateScores(references, predictedNames);

		return new NameSample(reference.getSentence(), predictedNames, reference.isClearAdaptiveDataSet());
	}

	public ApproximateFMeasure getFMeasure() {
		return fmeasure;
	}
}
