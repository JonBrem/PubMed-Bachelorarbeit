package de.ur.jonbrem.pubmed.indexing.combined_way;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import de.ur.jonbrem.pubmed.advanced_querying.DocScore;
import de.ur.jonbrem.pubmed.solrconnection.SolrConnection;
import util.Const;
import util.FileUtil;

public class ThirdFormula extends GeneralCombination {

	private static final boolean FIRST_RUN = false;
	
	public static void main(String[] args) {
		new ThirdFormula().run(new String[]{
				"PRNP", "prions", "prion protein", "g1-dependent"
		}, "PRNP");
		new ThirdFormula().run(new String[]{
				"Mad Cow Disease", "Bovine Spongiform Encephalopathy", "BSE"
		}, "BSE");
		
		new ThirdFormula().run(new String[]{
				"IDE", "IDE Gene", "Insulin Degrading Enzyme", "Insulin Protease", "Insulinase"
		}, "IDE");
		new ThirdFormula().run(new String[]{
				"Alzheimer's Disease", "Alzheimer Sclerosis", "Senile Dementia", "Presenila Dementia"
		}, "Alzheimer");
		
		new ThirdFormula().run(new String[]{
				"DDVit1", "UEV-2", "UBE2V2", "ubiquitin-conjugating enzyme", "EDPF-1"
		}, "MMS2");
		new ThirdFormula().run(new String[]{
				"Neoplasm", "Tumor", "Cancer"
		}, "Cancer");
		
		new ThirdFormula().run(new String[]{
				"APC", "adenomatous polyposis coli"
		}, "APC");
		new ThirdFormula().run(new String[]{
				"Colon Cancer", "Colon Neoplasm"
		}, "ColonCancer");
		
		new ThirdFormula().run(new String[]{
				"Nurr-77", "Nur77", "NGFI-B", "Nerve Growth Factor-inducible B-Protein", "Orphan Nuclear Receptor HMR", "Early Response Protein NAK1"
		}, "Nurr77");
		new ThirdFormula().run(new String[]{
				"Parkinson’s Disease", "Paralysis Agitans", "Idiopathic Parkinson Disease"
		}, "Parkinson");
		
		new ThirdFormula().run(new String[]{
				"Cathepsin D gene", "CTSD"
		}, "CTSD");
		new ThirdFormula().run(new String[]{
				"apolipoprotein E", "Apo E Isoproteins", "Aspartic Acid Endopeptidases", "ApoE"
		}, "ApoE");
		// + Alzheimer
		
		new ThirdFormula().run(new String[]{
				"TGF-beta1", "Transforming growth factor-beta1"
		}, "TGFb1");
		new ThirdFormula().run(new String[]{
				"CAA", "Cerebral Amyloid Angiopathy"
		}, "CAA");
		
		new ThirdFormula().run(new String[]{
				"NM23", "nucleoside diphosphate kinase", "Non-Metastatic Cells 1 Protein", "Granzyme A-activated DNase"
		}, "NM23");
		// + cancer
		
		new ThirdFormula().run(new String[]{
				"BARD1", "Ubiquitin-Protein Ligases"
		}, "BARD1");
		new ThirdFormula().run(new String[]{
				"BRCA1", "BRCA1 regulations"
		}, "BRCA1");
		
		// + APC
		new ThirdFormula().run(new String[]{
				"actin", "actin assembly", "Isoactin"
		}, "actin");
		
		new ThirdFormula().run(new String[]{
				"COP2"," Coat Protein Complex II"
		}, "COP2");
		new ThirdFormula().run(new String[]{
				"CFTR", "Cystic Fibrosis Transmembrane Conductance Regulator"
		}, "CFTR");
		new ThirdFormula().run(new String[]{
				"endoplasmatic reticulum"
		}, "endoplasmaticreticulum");
		
		new ThirdFormula().run(new String[]{
				"P53","phosphoprotein p53", "tumor suppressor gene"
		}, "P53");
		new ThirdFormula().run(new String[]{
				"apoptosis", "programmed cell death", "intrinsic pathway apoptosis"
		}, "apoptosis");
		
		new ThirdFormula().run(new String[]{
				"L1", "L2", "NILE Glycoprotein", "L1CAM", "CALL Protein"
		}, "L1L2");
		new ThirdFormula().run(new String[]{
				"Humane Papillomavirus type 11", "HPV11"
		}, "HPV11");
		
		new ThirdFormula().run(new String[]{
				"HNF4", "Hepatocyte Nuclear Factor 4", "HNF4 Transcription Factor"
		}, "HNF4");
		new ThirdFormula().run(new String[]{
				"COUP-TF I", "COUP-TF1 Protein", "Chicken Ovalbumin Upstream Promoter-Transcription Factor I", "Nuclear Receptor Subfamily 2 Group F Member 1"
		}, "COUP_TFI");
		new ThirdFormula().run(new String[]{
				"liver"
		}, "liver");
		
		new ThirdFormula().run(new String[]{
				"Huntingtin", "Huntingtin mutations", "HAPP"
		}, "Huntingtin");
		new ThirdFormula().run(new String[]{
				"Huntington’s Disease", "Huntington Chorea", "Chronic Progressive Hereditary Chorea"
		}, "Huntington");
		
		new ThirdFormula().run(new String[]{
				"Sonic hedgehog", "Sonic hedgehog mutations", "Vertebrate Hedgehog Protein"
		}, "sonichedgehog");
		new ThirdFormula().run(new String[]{
				"developmental disorders"
		}, "devdisorder");
		
		// another nm23
		new ThirdFormula().run(new String[]{
				"tracheal development"
		}, "tracheal");
		
		new ThirdFormula().run(new String[]{
				"pescadillo", "Pes"
		}, "Pes");
		
		new ThirdFormula().run(new String[]{
				"orexin receptor", "HCRT receptor"
		}, "hypocretinreceptor2");
		new ThirdFormula().run(new String[]{
				"narcolepsy", "Paroxysmal Sleep", "Gelineaus Syndrome"
		}, "narcolepsy");
		
		new ThirdFormula().run(new String[]{
				"presenilin-1 gene", "psen1"
		}, "psen1");
		// + alzheimer
		
		new ThirdFormula().run(new String[]{
				"FHM1", "familial hemiplegic migraine type 1", "Migraine with Auras"
		}, "FHM1");	
		new ThirdFormula().run(new String[]{
				"hippocampal neurons"
		}, "hippneur");
	}
	
	private Map<String, Double> oldScores;
	private Map<String, List<Double>> citationScores;
	private double totalCitations;
	private FileUtil fileUtil;
	
	public ThirdFormula() {
		super();
		scores = new HashMap<String, Double>();
		citationScores = new HashMap<>();
		oldScores = new HashMap<String, Double>();
		new HashSet<>();
		this.totalCitations = 0d;
		this.fieldName = "rank2_for_";
		this.fields = new ArrayList<>();
		this.fileUtil = new FileUtil();
		fields.add("pubmed_language_worddelimiter_rank_lm_4_abstract");
		fields.add("pubmed_language_stemming_rank_lm_4_title");
	}
	
	public void run(String[] searchTerms, String termId) {
		Const.log(Const.LEVEL_INFO, "Starting with: " + termId);
		this.solr = new SolrConnection();
		solr.openConnection();
		
		if(FIRST_RUN) {
			findDocsInD1(termId);
			Const.log(Const.LEVEL_INFO, "Found docs in d1.");
			storeScores(termId);
			Const.log(Const.LEVEL_INFO, "Stored scores.");
		} else {
			findDocsInD1(termId);
			Const.log(Const.LEVEL_INFO, "Found docs in d1.");
			
			calculateScores();
			Const.log(Const.LEVEL_INFO, "Calculated scores.");
			
			storeScores(termId);
			Const.log(Const.LEVEL_INFO, "Stored scores.");
			
			solr.closeConnection();
			Const.log(Const.LEVEL_INFO, "Done.");
		}
	}
	
	private void storeScores(String termId) {
		if(FIRST_RUN) {
			fileUtil.closeWriter("files/combined_approach_3_scores/" + termId + ".csv");
		} else {
			SolrQuery q = new SolrQuery();
			
			List<String> pmids = new ArrayList<>(scores.keySet());
			
			Collections.sort(pmids);
			
			FileUtil fileUtil = new FileUtil();
			for(int i = 0; i < pmids.size(); i++) {
				fileUtil.writeLine("files/combined_approach_3_scores/" + termId + ".csv", pmids.get(i) + "\t" + scores.get(pmids.get(i)));
			}
			fileUtil.closeWriter("files/combined_approach_3_scores/" + termId + ".csv");
		}
	}
	
	private void findDocsInD1(String rankName) {
		SolrQuery q = new SolrQuery();
		
		if(FIRST_RUN) {
			q.set("q", "rank1_for_" + rankName + ":*");
			q.set("fl", "pmid,rank1_for_" + rankName);
		} else {
			q.set("q", "rank3_for_" + rankName + ":*");
			q.set("fl", "pmid,rank3_for_" + rankName);
		}
		
		int start = 0, rows = 2000;
		
		q.set("start", start);
		q.set("rows", rows);
		
		while(true) {
			QueryResponse response = solr.query(q);
			SolrDocumentList results;
			if(response == null || (results = response.getResults()) == null || results.size() == 0) break;
			for(SolrDocument result : results) {
				if(FIRST_RUN) {
					forEveryDocInD1((String) result.get("pmid"), (Double) result.get("rank1_for_" + rankName), rankName);
				} else {
					forEveryDocInD1((String) result.get("pmid"), (Double) result.get("rank3_for_" + rankName), rankName);
				}
			}
			
			if(results.size() < rows) {
				break;
			}
			start += rows;
			q.set("start", start);
		}
	}
	
	private void forEveryDocInD1(String pmid, double score, String termId) {
		if(FIRST_RUN) {
			fileUtil.writeLine("files/combined_approach_3_scores/" + termId + ".csv", pmid + "\t" + score);
		} else {
			oldScores.put(pmid, score);
			increaseCountersForDocsCitedBy(pmid, score);
		}
	}

	private void calculateScores() {
		double total = 0;
		for(String pmid : oldScores.keySet()) {
			if(!citationScores.containsKey(pmid)) {
				citationScores.put(pmid, new ArrayList<>());
			}
		}
		
		for(String pmid : citationScores.keySet()) {
			double score = 0;
			if(oldScores.containsKey(pmid)) {
				score += oldScores.get(pmid) / 2;
			}
			score += getTotal(citationScores.get(pmid));
			scores.put(pmid, score);
			total += score;
		}
		
		double normalize = 1 / total;
		
		for(String pmid : scores.keySet()) {
			scores.put(pmid, scores.get(pmid) * normalize);
		}
	}
	
	private double getTotal(List<Double> scores) {
		double total = 0;
		for(double d : scores) total += d;
		return total;
	}

	private void increaseCountersForDocsCitedBy(String pmid, double score) {
		SolrQuery q = new SolrQuery();
		q.set("q", "cited_by:" + pmid);
		
		int start = 0;
		int rows = 2000;
		
		q.set("fl", "pmid");
		q.set("start", start);
		q.set("rows", rows);
		
		while(true) {
			QueryResponse response = solr.query(q);
			SolrDocumentList results;
			
			if(response == null || (results = response.getResults()) == null) break;
			
			for(SolrDocument doc : results) {
				addValuesForDoc(String.valueOf(doc.get("pmid")), score);
			}
			
			
			if(results.size() < rows) break;
			
			start += rows;
			q.set("start", start);
		}
	}

	private void addValuesForDoc(String pmid, double score) {
		if(citationScores.containsKey(pmid)) {
			citationScores.get(pmid).add(score);
		} else {
			List<Double> scores = new ArrayList<>();
			scores.add(score);
			citationScores.put(pmid, scores);
		}
	}

}
