package edu.ktlab.phenominer.sslcorpora.corpora;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.obolibrary.oboformat.model.Clause;
import org.obolibrary.oboformat.model.Frame;
import org.obolibrary.oboformat.model.OBODoc;
import org.obolibrary.oboformat.parser.OBOFormatConstants.OboFormatTag;

import edu.ktlab.phenominer.sslcorpora.matching.LongestMatching;
import edu.ktlab.phenominer.sslcorpora.matching.Span;
import edu.ktlab.phenominer.sslcorpora.ontology.OBOParser;
import edu.ktlab.phenominer.sslcorpora.util.FileHelper;
import edu.ktlab.phenominer.sslcorpora.util.Pair;
import edu.ktlab.phenominer.sslcorpora.util.PairList;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

public class HPODatasetGenerating {
	static String Pubmed2HPO = "data/mapping/pubmed2hpo.13.02.14.txt";
	static String HPOFile = "data/ontology/hp.18-02-14.obo";
	static String PubmedDataFolder = "corpus/PubmedOMIM";
	static String folderOutput = "corpus/SSLPhenominer2014";

	static OBODoc hpodoc;
	static PairList<String, String> PubmedHPO = new PairList<String, String>();

	static void init() throws Exception {
		loadHPO();
		loadPubmed2HPO();
	}

	static void loadHPO() throws Exception {
		hpodoc = OBOParser.parseOBOFile(HPOFile);
	}

	static void loadPubmed2HPO() throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(Pubmed2HPO), "UTF-8"));
		String line = new String();
		while ((line = in.readLine()) != null) {
			String[] segs = line.split("\t");
			Pair<String, String> pair = new Pair<String, String>(segs[0], segs[1]);
			PubmedHPO.add(pair);
		}
		in.close();
	}

	static List<String> findTermsbyID(String hpoid) {
		List<String> terms = new ArrayList<String>();
		Frame frame = hpodoc.getTermFrame(hpoid);
		if (frame == null)
			return null;
		if (frame.getTagValue(OboFormatTag.TAG_IS_OBSELETE) != null)
			return null;

		terms.add((String) frame.getTagValue(OboFormatTag.TAG_NAME));

		Iterator<Clause> cls = frame.getClauses(OboFormatTag.TAG_SYNONYM).iterator();
		while (cls.hasNext()) {
			Clause cl = cls.next();
			terms.add((String) cl.getValue());
		}
		return terms;
	}

	static Map<String, String> findTermsbyID(String hpoid, String type, boolean lowcase) {
		Map<String, String> terms = new HashMap<String, String>();
		Frame frame = hpodoc.getTermFrame(hpoid);
		if (frame == null)
			return null;
		if (frame.getTagValue(OboFormatTag.TAG_IS_OBSELETE) != null)
			return null;

		if (lowcase)
			terms.put(((String) frame.getTagValue(OboFormatTag.TAG_NAME)).toLowerCase(), type);
		else
			terms.put((String) frame.getTagValue(OboFormatTag.TAG_NAME), type);

		Iterator<Clause> cls = frame.getClauses(OboFormatTag.TAG_SYNONYM).iterator();
		while (cls.hasNext()) {
			Clause cl = cls.next();
			if (lowcase)
				terms.put(((String) cl.getValue()).toLowerCase(), type);
			else
				terms.put((String) cl.getValue(), type);
		}
		return terms;
	}

	static Map<String, String> findTermsbyIDs(List<String> hpoids, String type, boolean lowcase) {
		Map<String, String> terms = new HashMap<String, String>();
		for (String hpoid : hpoids) {
			Map<String, String> map = findTermsbyID(hpoid, type, lowcase);
			terms.putAll(map);
		}
		return terms;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	static void taggingPubmedFile(File pubmedFile) throws Exception {
		String pubmedID = pubmedFile.getName();
		System.out.println(pubmedID);
		Map<String, String> dict = findTermsbyIDs(PubmedHPO.findLeft(pubmedID), "PHENOTYPE", true);
		LongestMatching matching = new LongestMatching(dict);
		String content = FileHelper.readFileAsString(pubmedFile, Charset.forName("UTF-8"));

		int countSpans = 0;
		String taggedContent = "";
		for (String sentence : content.split("\n")) {
			if (sentence.trim().length() == 0)
				continue;
			PTBTokenizer tokenizer = new PTBTokenizer(new StringReader(sentence), new CoreLabelTokenFactory(), "");
			List<String> tokens = new ArrayList<String>();
			for (CoreLabel label; tokenizer.hasNext();) {
				label = (CoreLabel) tokenizer.next();
				tokens.add(label.originalText());
			}
			String[] tokensArr = tokens.toArray(new String[tokens.size()]);

			Span[] spans = matching.tagging(tokensArr, -1, true);
			String annotated = Span.getStringAnnotated(spans, tokensArr);

			countSpans += spans.length;
			taggedContent += annotated + "\n";
		}

		if (countSpans != 0)
			FileHelper.writeToFile(normalize(taggedContent), new File(folderOutput + "/" + pubmedID), Charset.forName("UTF-8"));
	}

	static String normalize(String text) {
		text = text.replace("-LRB-", "(");
		text = text.replace("-RRB-", ")");
		text = text.replace("-LSB-", "[");
		text = text.replace("-RSB-", "]");
		text = text.replace("-LCB-", "{");
		text = text.replace("-RCB-", "}");
		return text;
	}

	public static void main(String[] args) throws Exception {
		init();
		File folder = new File(PubmedDataFolder);
		for (File file : folder.listFiles()) {
			taggingPubmedFile(file);
		}
	}

}
