package edu.ktlab.phenominer.ssl.ontology;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.obolibrary.oboformat.model.OBODoc;
import org.obolibrary.oboformat.parser.OBOFormatParser;

public class OBOParser {
	public static OBODoc parseOBOFile(String fn) throws Exception {
		return parseOBOFile(fn, false);
	}

	public static OBODoc parseOBOFile(String fn, boolean allowEmptyFrames) throws Exception {
		InputStream inputStream = new FileInputStream(fn);
		OBOFormatParser p = new OBOFormatParser();
		OBODoc obodoc = p.parse(new BufferedReader(new InputStreamReader(inputStream)));

		return obodoc;
	}

	public OBODoc parseOBOFile(File file) throws Exception {
		OBOFormatParser p = new OBOFormatParser();
		OBODoc obodoc = p.parse(file.getCanonicalPath());
		return obodoc;
	}
}
