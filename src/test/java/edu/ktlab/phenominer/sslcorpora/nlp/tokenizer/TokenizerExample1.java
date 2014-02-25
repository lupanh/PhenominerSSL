package edu.ktlab.phenominer.sslcorpora.nlp.tokenizer;

import opennlp.tools.tokenize.TokenizerME;

public class TokenizerExample1 {
	static TokenizerME tokenizer;
	static String modelTokenizer = "models/tokenizer/biotokenizer.1.0.model";

	public static void main(String[] args) {
		String text = "Haemoglobin Rahere (beta Lys-Thr): A new high affinity haemoglobin associated with decreased 2, 3-diphosphoglycerate binding and relative polycythaemia. A new haemoglobin with increased oxygen affinity, beta82 (EF6) lysine leads to threonine (Hb Rahere), was found during the investigation of a patient who was found to have a raised haemoglobin concentration after a routine blood count. The substitution affects one of the 2, 3-diphosphoglycerate binding sites, resulting in an increased affinity for oxygen, but both the haem-haem interaction and the alkaline Bohr effect are normal in the haemolysate. This variant had the same mobility as haemoglobin A on electrophoresis at alkaline pH but was detected by measuring the whole blood oxygen affinity; it could be separated from haemoglobin A, however, by electrophoresis in agar at acid pH. The raised haemoglobin concentration was mainly due to a reduction in plasma volume (a relative polycythaemia) and was associated with a persistently raised white blood count. This case emphasises the need to measure the oxygen affinity of haemoglobin in all patients with absolute or relative polycythaemia when some obvious cause is not evident.";
		tokenizer = Tokenizer.getInstance().createTokenizerModel();
		String[] sents = tokenizer.tokenize(text);
		for (String sent : sents)
			System.out.println(sent);
	}
}
