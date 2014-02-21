package edu.ktlab.phenominer.sslcorpora.ontology;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.obolibrary.oboformat.model.Clause;
import org.obolibrary.oboformat.model.Frame;
import org.obolibrary.oboformat.model.OBODoc;
import org.obolibrary.oboformat.parser.OBOFormatConstants.OboFormatTag;

public class HPOParsing {
	static String oboFile = "data/ontology/hp.18-02-14.obo";
	static OBODoc hpodoc;

	public static void main(String[] args) throws Exception {
		hpodoc = OBOParser.parseOBOFile(oboFile);
		System.out.println(findTermsbyID("HP:0000007"));

		Iterator<Frame> itr = hpodoc.getTermFrames().iterator();
		while (itr.hasNext()) {
			Frame frame = itr.next();
			if (frame.getTagValue(OboFormatTag.TAG_IS_OBSELETE) != null)
				continue;
			System.out.println(frame.getId());
			System.out.println(frame.getTagValue(OboFormatTag.TAG_NAME));
			Iterator<Clause> cls = frame.getClauses(OboFormatTag.TAG_SYNONYM).iterator();
			while (cls.hasNext()) {
				Clause cl = cls.next();
				System.out.println("Synonym: " + cl.getValue());
			}
			System.out.println("=========================");
		}
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

}
