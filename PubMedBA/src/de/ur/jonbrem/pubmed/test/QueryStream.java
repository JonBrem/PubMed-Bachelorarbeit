package de.ur.jonbrem.pubmed.test;

public class QueryStream {
	public static TrecQuery[] ALL_QUESTIONS_2006 = 
		{
			new TrecQuery(160, 111, "PRNP", "Mad Cow Disease", "What is the role of PrnP in mad cow disease?", "prions", "prion protein", "g1-dependent", "Bovine Spongiform Encephalopathy", "BSE"),
			new TrecQuery(161, 112, "IDE gene", "Alzheimer's Disease", "What is the role of IDE in Alzheimer’s disease", "Insulin Degrading Enzyme", "Insulin Protease", "Insulinase", "Alzheimer Sclerosis", "Senile Dementia", "Presenila Dementia"),
			new TrecQuery(162, 113, "MMS2", "Cancer", "What is the role of MMS2 in cancer?", "DDVit1", "UEV-2", "UBE2V2", "ubiquitin-conjugating enzyme", "EDPF-1", "Neoplasm", "Tumor"),
			new TrecQuery(163, 114, "APC (adenomatous polyposis coli)", "Colon Cancer", "What is the role of APC (adenomatous polyposis coli) in colon cancer?", "Colon Neoplasm"),
			new TrecQuery(164, 115, "Nurr-77", "Parkinson’s Disease", "What is the role of Nurr-77 in Parkinson’s disease?", "Nur77", "NGFI-B", "Nerve Growth Factor-inducible B-Protein", "Orphan Nuclear Receptor HMR", "Early Response Protein NAK1", "Paralysis Agitans"),
			new TrecQuery(165, 117, "Cathepsin D gene (CTSD) and apolipoprotein E (ApoE)", "Alzheimer’s Disease", "How do Cathepsin D (CTSD) and apolipoprotein E (ApoE) interactions contribute to Alzheimer’s disease?", "Apo E Isoproteins", "Aspartic Acid Endopeptidases",  "Alzheimer Sclerosis", "Senile Dementia", "Presenila Dementia"),
			new TrecQuery(166, 118, "Transforming growth factor-beta1 (TGF-beta1)", "Cerebral Amyloid Angiopathy (CAA)", "What is the role of Transforming growth factor-beta1 (TGF-beta1) in cerebral amyloid angiopathy (CAA)?"),
			new TrecQuery(167, 120, "nucleoside diphosphate kinase  (NM23)", "tumor progression", "How does nucleoside diphosphate kinase (NM23) contribute to tumor progression?", "Non-Metastatic Cells 1 Protein", "Granzyme A-activated DNase", "neoplasm", "cancer"),
			new TrecQuery(168, 121, "BARD1", "BRCA1 regulation", "How does BARD1 regulate BRCA1 activity?", "Ubiquitin-Protein Ligases"),
			new TrecQuery(169, 122, "APC (adenomatous polyposis coli)", "actin assembly", "How does APC (adenomatous polyposis coli) protein affect actin assembly", "Isoactin"),
			new TrecQuery(170, 123, "COP2", "transport of CFTR out of the endoplasmic reticulum", "How does COP2 contribute to CFTR export from the endoplasmic reticulum?", "Coat Protein Complex II", "Cystic Fibrosis Transmembrane Conductance Regulator"),
			new TrecQuery(171, 125, "Nurr-77", "preventing auto-immunity by deleting reactive T-cells before they migrate to the spleen or the lymph nodes", "How does Nurr-77 delete T cells before they migrate to the spleen or lymph nodes and how does this impact autoimmunity?", "Nur77", "NGFI-B", "Nerve Growth Factor-inducible B-Protein", "Orphan Nuclear Receptor HMR", "Early Response Protein NAK1"),
			new TrecQuery(172, 126, "P53", "apoptosis", "How does p53 affect apoptosis?", "phosphoprotein p53", "tumor suppressor gene", "programmed cell death", "intrinsic pathway apoptosis"),
//			new TrecQuery(173, 127, "alpha7 nicotinic receptor subunit gene", "ethanol metabolism", "How do alpha7 nicotinic receptor subunits affect ethanol metabolism?"),
			new TrecQuery(174, 130, "BRCA1 regulation of ubiquitin", "cancer", "How does BRCA1 ubiquitinating activity contribute to cancer?", "Ubiquitin-Protein Ligases", "neoplasm", "tumor"),
//			new TrecQuery(175, 131, "L1 and L2 in the HPV11 virus", "role of L2 in the viral capsid", "How does L2 interact with L1 to form HPV11 viral capsids?", "NILE Glycoprotein", "L1CAM", "CALL Protein", "Humane Papillomavirus type 11"),
			new TrecQuery(176, 134, "CFTR and Sec61", "degradation of CFTR leading to cystic fibrosis", "How does Sec61-mediated CFTR degradation contribute to cystic fibrosis?"),
			new TrecQuery(177, 135, "Bop and Pes", "cell growth", "How do Bop-Pes interactions affect cell growth?", "Bilateral Occipital Polymicrogyria", "pescadillo"),
			new TrecQuery(178, 137, "Insulin-like GF and insulin receptor gene", "function in skin", "How do interactions between insulin-like GFs and the insulin receptor affect skin biology?"),
			new TrecQuery(179, 138, "HNF4 and COUP-TF I", "suppression in the function of the liver", "How do interactions between HNF4 and COUP-TF1 suppress liver function?"),
//			new TrecQuery(180, 139, "Ret and GDNF", "kidney development", "How do Ret-GDNF interactions affect liver development?"),
			new TrecQuery(181, 141, "Huntingtin mutations", "role in Huntington’s Disease", "How do mutations in the Huntingtin gene affect Huntington’s disease?", "HAPP", "Huntington Chorea"),
			new TrecQuery(182, 142, "Sonic hedgehog mutations", "role in developmental disorders", "How do  mutations in Sonic Hedgehog genes affect developmental disorders?"),
//			new TrecQuery(183, 143, "Mutations of NM23", "impact on tracheal development", "How do  mutations in the NM23 gene affect tracheal development?", "Non-Metastatic Cells 1 Protein", "Granzyme A-activated DNase"),
			new TrecQuery(184, 144, "Mutations in metazoan Pes", "effect on cell growth", "How do  mutations in the Pes gene affect cell growth?", "pescadillo"),
			new TrecQuery(185, 145, "Mutations of hypocretin receptor 2", "narcolepsy", "How do  mutations in the hypocretin receptor 2 gene affect narcolepsy?", "orexin receptor", "HCRT receptor", "Paroxysmal Sleep", "Gelineaus Syndrome"),
			new TrecQuery(186, 146, "Mutations of presenilin-1 gene", "biological impact in Alzheimer's disease", "How do  mutations in the Presenilin-1 gene affect Alzheimer’s disease?", "psen1", "Alzheimer Sclerosis", "Senile Dementia"),
			new TrecQuery(187, 148, "Mutation of familial hemiplegic migraine type 1 (FHM1)", "neuronal Ca2+ influx in hippocampal neurons", "How do  mutations in familial hemiplegic migraine type 1 (FHM1) gene affect calcium ion influx in hippocampal neurons?", "Migraine with Auras"),
		};
	
	int currentIndex;
	
	public QueryStream() {
		currentIndex = 0;
	}
	
	public TrecQuery getNext() {
		if(currentIndex == -1) return null;
		TrecQuery result = null;
		if(ALL_QUESTIONS_2006.length > currentIndex) {
			result = ALL_QUESTIONS_2006[currentIndex];
			currentIndex++;
		} else {
			currentIndex = -1;
		}
		
		return result;
	}
}
