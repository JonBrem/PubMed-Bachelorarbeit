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
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import de.ur.jonbrem.pubmed.advanced_querying.AdvancedQuery;
import de.ur.jonbrem.pubmed.advanced_querying.DocScore;
import de.ur.jonbrem.pubmed.solrconnection.SolrConnection;
import util.Const;
import util.FileUtil;

public class SecondFormula extends GeneralCombination {

	public static void main(String[] args) {
		new SecondFormula().run(new String[]{
				"PRNP", "prions", "prion protein", "g1-dependent"
		}, "PRNP");
		new SecondFormula().run(new String[]{
				"Mad Cow Disease", "Bovine Spongiform Encephalopathy", "BSE"
		}, "BSE");
		
		new SecondFormula().run(new String[]{
				"IDE", "IDE Gene", "Insulin Degrading Enzyme", "Insulin Protease", "Insulinase"
		}, "IDE");
		new SecondFormula().run(new String[]{
				"Alzheimer's Disease", "Alzheimer Sclerosis", "Senile Dementia", "Presenila Dementia"
		}, "Alzheimer");
		
		new SecondFormula().run(new String[]{
				"DDVit1", "UEV-2", "UBE2V2", "ubiquitin-conjugating enzyme", "EDPF-1"
		}, "MMS2");
		new SecondFormula().run(new String[]{
				"Neoplasm", "Tumor", "Cancer"
		}, "Cancer");
		
		new SecondFormula().run(new String[]{
				"APC", "adenomatous polyposis coli"
		}, "APC");
		new SecondFormula().run(new String[]{
				"Colon Cancer", "Colon Neoplasm"
		}, "ColonCancer");
		
		new SecondFormula().run(new String[]{
				"Nurr-77", "Nur77", "NGFI-B", "Nerve Growth Factor-inducible B-Protein", "Orphan Nuclear Receptor HMR", "Early Response Protein NAK1"
		}, "Nurr77");
		new SecondFormula().run(new String[]{
				"Parkinson’s Disease", "Paralysis Agitans", "Idiopathic Parkinson Disease"
		}, "Parkinson");
		
		new SecondFormula().run(new String[]{
				"Cathepsin D gene", "CTSD"
		}, "CTSD");
		new SecondFormula().run(new String[]{
				"apolipoprotein E", "Apo E Isoproteins", "Aspartic Acid Endopeptidases", "ApoE"
		}, "ApoE");
		// + Alzheimer
		
		new SecondFormula().run(new String[]{
				"TGF-beta1", "Transforming growth factor-beta1"
		}, "TGFb1");
		new SecondFormula().run(new String[]{
				"CAA", "Cerebral Amyloid Angiopathy"
		}, "CAA");
		
		new SecondFormula().run(new String[]{
				"NM23", "nucleoside diphosphate kinase", "Non-Metastatic Cells 1 Protein", "Granzyme A-activated DNase"
		}, "NM23");
		// + cancer
		
		new SecondFormula().run(new String[]{
				"BARD1", "Ubiquitin-Protein Ligases"
		}, "BARD1");
		new SecondFormula().run(new String[]{
				"BRCA1", "BRCA1 regulations"
		}, "BRCA1");
		
		// + APC
		new SecondFormula().run(new String[]{
				"actin", "actin assembly", "Isoactin"
		}, "actin");
		
		new SecondFormula().run(new String[]{
				"COP2"," Coat Protein Complex II"
		}, "COP2");
		new SecondFormula().run(new String[]{
				"CFTR", "Cystic Fibrosis Transmembrane Conductance Regulator"
		}, "CFTR");
		new SecondFormula().run(new String[]{
				"endoplasmatic reticulum"
		}, "endoplasmaticreticulum");
		
		new SecondFormula().run(new String[]{
				"P53","phosphoprotein p53", "tumor suppressor gene"
		}, "P53");
		new SecondFormula().run(new String[]{
				"apoptosis", "programmed cell death", "intrinsic pathway apoptosis"
		}, "apoptosis");
		
		new SecondFormula().run(new String[]{
				"L1", "L2", "NILE Glycoprotein", "L1CAM", "CALL Protein"
		}, "L1L2");
		new SecondFormula().run(new String[]{
				"Humane Papillomavirus type 11", "HPV11"
		}, "HPV11");
		
		new SecondFormula().run(new String[]{
				"HNF4", "Hepatocyte Nuclear Factor 4", "HNF4 Transcription Factor"
		}, "HNF4");
		new SecondFormula().run(new String[]{
				"COUP-TF I", "COUP-TF1 Protein", "Chicken Ovalbumin Upstream Promoter-Transcription Factor I", "Nuclear Receptor Subfamily 2 Group F Member 1"
		}, "COUP_TFI");
		new SecondFormula().run(new String[]{
				"liver"
		}, "liver");
		
		new SecondFormula().run(new String[]{
				"Huntingtin", "Huntingtin mutations", "HAPP"
		}, "Huntingtin");
		new SecondFormula().run(new String[]{
				"Huntington’s Disease", "Huntington Chorea", "Chronic Progressive Hereditary Chorea"
		}, "Huntington");
		
		new SecondFormula().run(new String[]{
				"Sonic hedgehog", "Sonic hedgehog mutations", "Vertebrate Hedgehog Protein"
		}, "sonichedgehog");
		new SecondFormula().run(new String[]{
				"developmental disorders"
		}, "devdisorder");
		
		// another nm23
		new SecondFormula().run(new String[]{
				"tracheal development"
		}, "tracheal");
		
		new SecondFormula().run(new String[]{
				"pescadillo", "Pes"
		}, "Pes");
		
		new SecondFormula().run(new String[]{
				"orexin receptor", "HCRT receptor"
		}, "hypocretinreceptor2");
		new SecondFormula().run(new String[]{
				"narcolepsy", "Paroxysmal Sleep", "Gelineaus Syndrome"
		}, "narcolepsy");
		
		new SecondFormula().run(new String[]{
				"presenilin-1 gene", "psen1"
		}, "psen1");
		// + alzheimer
		
		new SecondFormula().run(new String[]{
				"FHM1", "familial hemiplegic migraine type 1", "Migraine with Auras"
		}, "FHM1");	
		new SecondFormula().run(new String[]{
				"hippocampal neurons"
		}, "hippneur");
	}
	
	private Map<String, Double> citationScores;
	private Set<String> usedPMIDs;
	
	private double totalCitations;
	
	public SecondFormula() {
		super();
		scores = new HashMap<String, Double>();
		citationScores = new HashMap<>();
		usedPMIDs = new HashSet<>();
		this.totalCitations = 0d;
		this.fieldName = "rank2_for_";
		this.fields = new ArrayList<>();
		fields.add("pubmed_language_worddelimiter_rank_lm_4_abstract");
		fields.add("pubmed_language_stemming_rank_lm_4_title");
	}
	
	public void run(String[] searchTerms, String termId) {
		Const.log(Const.LEVEL_INFO, "Starting with: " + termId);
		this.solr = new SolrConnection();
		solr.openConnection();
		
		findDocsInD1(searchTerms);
		Const.log(Const.LEVEL_INFO, "Found docs in d1.");
		
		calculateScores();
		Const.log(Const.LEVEL_INFO, "Calculated scores.");
		
		storeScores(termId);
		Const.log(Const.LEVEL_INFO, "Stored scores.");
		
		solr.closeConnection();
		Const.log(Const.LEVEL_INFO, "Done.");
	}
	
	private void storeScores(String termId) {
		SolrQuery q = new SolrQuery();
		
		List<String> pmids = new ArrayList<>(scores.keySet());
		
		Collections.sort(pmids);
		
		FileUtil fileUtil = new FileUtil();
		for(int i = 0; i < pmids.size(); i++) {
			fileUtil.writeLine("files/combined_approach_2_scores/" + termId + ".csv", pmids.get(i) + "\t" + scores.get(pmids.get(i)));
		}
		fileUtil.closeWriter("files/combined_approach_2_scores/" + termId + ".csv");
	}
	
	// costs too much performance. will be done later in another program.
	private void assignScores(String termId) {
		SolrQuery q = new SolrQuery();
		
		
		int count = 0;
		for(String pmid : scores.keySet()) {
			q.set("q", "pmid:" + pmid);
			QueryResponse response = solr.query(q);

			SolrDocumentList results;
			
			if(response == null || (results = response.getResults()) == null || results.size() == 0) continue;
			
			SolrInputDocument doc = ClientUtils.toSolrInputDocument(results.get(0));
			doc.remove("_version_");
			doc.setField(fieldName + termId, scores.get(pmid));
			
			solr.addToIndex(doc);
			count++;
			if(count % 500 == 0) 
				Const.log(Const.LEVEL_INFO, "assigned "  + count + " out of " + scores.keySet().size() + " scores.");
		}
	}

	private void findDocsInD1(String[] searchTerms) {
		AdvancedQuery q = new AdvancedQuery();
		
		for(String searchTerm : searchTerms) {
			q.addFieldQuery("pubmed_language_simple_rank_tfidf_title", searchTerm, 1.0f);
			q.addFieldQuery("pubmed_language_stemming_rank_lm_4_abstract", searchTerm, 17.39864f);
		}
		
		for(DocScore pmid : q.getResults(solr)) {
			forEveryDocInD1(pmid.getPmid(), pmid.getScore());
		}

	}
	
	private void forEveryDocInD1(String pmid, double score) {
		increaseCountersForDocsCitedBy(pmid, score);
	}

	private void calculateScores() {
		for(String pmid : citationScores.keySet()) {
			scores.put(pmid, citationScores.get(pmid) / (double) totalCitations);
		}
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
				totalCitations += score;
				increaseCounterForDoc(String.valueOf(doc.get("pmid")), score);
			}
			
			
			if(results.size() < rows) break;
			
			start += rows;
			q.set("start", start);
		}
	}

	private void increaseCounterForDoc(String pmid, double score) {
		if(citationScores.containsKey(pmid)) {
			citationScores.put(pmid, citationScores.get(pmid) + score);
		} else {
			citationScores.put(pmid, score);
		}
	}

}
