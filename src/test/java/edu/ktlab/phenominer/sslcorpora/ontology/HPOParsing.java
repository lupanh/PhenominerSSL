package edu.ktlab.phenominer.sslcorpora.ontology;

import java.util.Iterator;

import org.obolibrary.oboformat.model.Clause;
import org.obolibrary.oboformat.model.Frame;
import org.obolibrary.oboformat.model.OBODoc;
import org.obolibrary.oboformat.parser.OBOFormatConstants.OboFormatTag;

public class HPOParsing {
	static String oboFile = "data/ontology/hp.18-02-14.obo";
	
	public static void main(String[] args) throws Exception {
		OBODoc hpdoc = OBOParser.parseOBOFile(oboFile);
		
		Iterator<Frame> itr = hpdoc.getTermFrames().iterator();
		while (itr.hasNext()) {
			Frame frame = itr.next();
			if (frame.getTagValue(OboFormatTag.TAG_IS_OBSELETE) != null) continue;
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

}
