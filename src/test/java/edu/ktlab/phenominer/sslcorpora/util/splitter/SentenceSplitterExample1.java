package edu.ktlab.phenominer.sslcorpora.util.splitter;

import java.io.FileInputStream;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceSplitterExample1 {
	static SentenceDetectorME splitter;
	static String modelSplitter = "models/sentencesplitter/biosent.1.0.model";

	public static void main(String[] args) {
		String text = "The changing purpose of Prader-Willi syndrome clinical diagnostic criteria and proposed revised criteria. Prader-Willi syndrome (PWS) is a complex, multisystem disorder. Its major clinical features include neonatal hypotonia, developmental delay, short stature, behavioral abnormalities, childhood-onset obesity, hypothalamic hypogonadism, and characteristic appearance. The genetic basis of PWS is also complex. It is caused by absence of expression of the paternally active genes in the PWS critical region on 15q11-q13. In approximately 70% of cases this is the result of deletion of this region from the paternal chromosome 15. In approximately 28%, it is attributable to maternal uniparental disomy (UPD; inheritance of 2 copies of a chromosome from the mother and no copies from the father, as opposed to the normal 1 copy from each parent) of chromosome 15, and in <2%, it is the result of a mutation, deletion, or other defect in the imprinting center. Clinical diagnostic criteria were established by consensus in 1993. Subsequently, definitive molecular genetic testing became available for laboratory diagnosis of PWS. However, identification of appropriate patients for testing remains a challenge for most practitioners because many features of the disorder are nonspecific and others can be subtle or evolve over time. For example, hypotonic infants who are still in the failure to thrive phase of the disorder often do not have sufficient features for recognition of PWS and often are not tested. Initial screening with these diagnostic criteria can increase the yield of molecular testing for older children and adults with nonspecific obesity and mental retardation. Therefore, the purpose of clinical diagnostic criteria has shifted from assisting in making the definitive diagnosis to raising diagnostic suspicion, thereby prompting testing. We conducted a retrospective review of patients with PWS confirmed with genetic testing to assess the validity and sensitivity of clinical diagnostic criteria published before the widespread availability of testing for all affected patients and recommend revised clinical criteria. Charts of all 90 patients with laboratory-confirmed PWS were reviewed. For each patient, the presence or absence of the major, minor, and supportive features listed in the published diagnostic criteria was recorded. The sensitivity of each criterion, mean of the total number of major and minor criteria, and mean total score for each patient were calculated. There were 68 patients with a deletion (del 15q11-q13), 21 with maternal UPD of chromosome 15, and 1 with a presumed imprinting defect. Age range at the time of the most recent evaluation was 5 months to 60 years (median: 14.5 years; del median: 14 years; range: 5 months-60 years; UPD median: 18 years; range: 5-42 years). The sensitivities of the major criteria ranged from 49% (characteristic facial features) to 98% (developmental delay). Global developmental delay and neonatal hypotonia were the 2 most consistently positive major criteria and were positive in >97% of the patients. Feeding problems in infancy, excessive weight gain after 1 year, hypogonadism, and hyperphagia were all present in 93% or more of patients. Sensitivities of the minor criteria ranged form 37% (sleep disturbance and apneas) to 93% (speech and articulation defects). Interestingly, the sensitivities of 8 of the minor criteria were higher than the sensitivity of characteristic facial features, which is a major criterion. Fifteen out of 90 patients with molecular diagnosis did not meet the clinical diagnostic criteria retrospectively. When definitive diagnostic testing is not available, as was the case for PWS when the 1993 criteria were developed, diagnostic criteria are important to avoid overdiagnosis and to ensure that diagnostic test development is performed on appropriate samples. When diagnostic testing is available, as is now the case for PWS, diagnostic criteria should serve to raise diagnostic suspicion, ensure that all appropriate people are tested, and avoid the expense of testing unnecessarily. Our results indicate that the sensitivities of most of the published criteria are acceptable. However, 16.7% of patients with molecular diagnosis did not meet the 1993 clinical diagnostic criteria retrospectively, suggesting that the published criteria may be too exclusive. A less strict scoring system may ensure that all appropriate people are tested. Accordingly, we suggest revised clinical criteria to help identify the appropriate patients for DNA testing for PWS. The suggested age groupings are based on characteristic phases of the natural history of PWS. Some of the features (eg, neonatal hypotonia, feeding problems in infancy) serve to diagnose the syndrome in the first few years of life, whereas others (eg, excessive eating) are useful during early childhood. Similarly, hypogonadism is most useful during and after adolescence. Some of the features like neonatal hypotonia and infantile feeding problems are less likely to be missed, whereas others such as characteristic facial features and hypogonadism (especially in prepubertal females) may require more careful and/or expert examination. The issue of who should have diagnostic testing is distinct from the determination of features among confirmed patients. Based on the sensitivities of the published criteria and our experience, we suggest testing all newborns/infants with otherwise unexplained hypotonia with poor suck. For children between 2 and 6 years of age, we consider hypotonia with history of poor suck associated with global developmental delay sufficient criteria to prompt testing. Between 6 and 12 years of age, we suggest testing those with hypotonia (or history of hypotonia with poor suck), global developmental delay, and excessive eating with central obesity (if uncontrolled). At the ages of 13 years and above, we recommend testing patients with cognitive impairment, excessive eating with central obesity (if uncontrolled), and hypogonadotropic hypogonadism and/or typical behavior problems (including temper tantrums and obsessive-compulsive features). Thus, we propose a lower threshold to prompt diagnostic DNA testing, leading to a higher likelihood of diagnosis of this disorder in which anticipatory guidance and intervention can significantly influence outcome.";
		splitter = createSentenceDetectorModel();
		String[] sents = splitter.sentDetect(text);
		for (String sent : sents)
			System.out.println(sent);
	}

	public static SentenceDetectorME createSentenceDetectorModel() {
		InputStream in;
		try {
			in = new FileInputStream(modelSplitter);
			SentenceModel sentModel = new SentenceModel(in);

			return new SentenceDetectorME(sentModel);
		} catch (Exception e) {
		}
		return null;
	}

}
