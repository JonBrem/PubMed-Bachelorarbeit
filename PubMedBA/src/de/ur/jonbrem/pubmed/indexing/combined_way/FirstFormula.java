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

import de.ur.jonbrem.pubmed.solrconnection.SolrConnection;
import util.Const;
import util.FileUtil;

public class FirstFormula extends GeneralCombination {

	public static void main(String[] args) {
		new FirstFormula().run(new String[]{
				"PRNP", "prions", "prion protein", "g1-dependent"
		}, "PRNP");
		new FirstFormula().run(new String[]{
				"Mad Cow Disease", "Bovine Spongiform Encephalopathy", "BSE"
		}, "BSE");
		
		new FirstFormula().run(new String[]{
				"IDE", "IDE Gene", "Insulin Degrading Enzyme", "Insulin Protease", "Insulinase"
		}, "IDE");
		new FirstFormula().run(new String[]{
				"Alzheimer's Disease", "Alzheimer Sclerosis", "Senile Dementia", "Presenila Dementia"
		}, "Alzheimer");
		
		new FirstFormula().run(new String[]{
				"DDVit1", "UEV-2", "UBE2V2", "ubiquitin-conjugating enzyme", "EDPF-1"
		}, "MMS2");
		new FirstFormula().run(new String[]{
				"Neoplasm", "Tumor", "Cancer"
		}, "Cancer");
		
		new FirstFormula().run(new String[]{
				"APC", "adenomatous polyposis coli"
		}, "APC");
		new FirstFormula().run(new String[]{
				"Colon Cancer", "Colon Neoplasm"
		}, "ColonCancer");
		
		new FirstFormula().run(new String[]{
				"Nurr-77", "Nur77", "NGFI-B", "Nerve Growth Factor-inducible B-Protein", "Orphan Nuclear Receptor HMR", "Early Response Protein NAK1"
		}, "Nurr77");
		new FirstFormula().run(new String[]{
				"Parkinson’s Disease", "Paralysis Agitans", "Idiopathic Parkinson Disease"
		}, "Parkinson");
		
		new FirstFormula().run(new String[]{
				"Cathepsin D gene", "CTSD"
		}, "CTSD");
		new FirstFormula().run(new String[]{
				"apolipoprotein E", "Apo E Isoproteins", "Aspartic Acid Endopeptidases", "ApoE"
		}, "ApoE");
		// + Alzheimer
		
		new FirstFormula().run(new String[]{
				"TGF-beta1", "Transforming growth factor-beta1"
		}, "TGFb1");
		new FirstFormula().run(new String[]{
				"CAA", "Cerebral Amyloid Angiopathy"
		}, "CAA");
		
		new FirstFormula().run(new String[]{
				"NM23", "nucleoside diphosphate kinase", "Non-Metastatic Cells 1 Protein", "Granzyme A-activated DNase"
		}, "NM23");
		// + cancer
		
		new FirstFormula().run(new String[]{
				"BARD1", "Ubiquitin-Protein Ligases"
		}, "BARD1");
		new FirstFormula().run(new String[]{
				"BRCA1", "BRCA1 regulations"
		}, "BRCA1");
		
		// + APC
		new FirstFormula().run(new String[]{
				"actin", "actin assembly", "Isoactin"
		}, "actin");
		
		new FirstFormula().run(new String[]{
				"COP2"," Coat Protein Complex II"
		}, "COP2");
		new FirstFormula().run(new String[]{
				"CFTR", "Cystic Fibrosis Transmembrane Conductance Regulator"
		}, "CFTR");
		new FirstFormula().run(new String[]{
				"endoplasmatic reticulum"
		}, "endoplasmaticreticulum");
		
		new FirstFormula().run(new String[]{
				"P53","phosphoprotein p53", "tumor suppressor gene"
		}, "P53");
		new FirstFormula().run(new String[]{
				"apoptosis", "programmed cell death", "intrinsic pathway apoptosis"
		}, "apoptosis");
		
		new FirstFormula().run(new String[]{
				"L1", "L2", "NILE Glycoprotein", "L1CAM", "CALL Protein"
		}, "L1L2");
		new FirstFormula().run(new String[]{
				"Humane Papillomavirus type 11", "HPV11"
		}, "HPV11");
		
		new FirstFormula().run(new String[]{
				"HNF4", "Hepatocyte Nuclear Factor 4", "HNF4 Transcription Factor"
		}, "HNF4");
		new FirstFormula().run(new String[]{
				"COUP-TF I", "COUP-TF1 Protein", "Chicken Ovalbumin Upstream Promoter-Transcription Factor I", "Nuclear Receptor Subfamily 2 Group F Member 1"
		}, "COUP_TFI");
		new FirstFormula().run(new String[]{
				"liver"
		}, "liver");
		
		new FirstFormula().run(new String[]{
				"Huntingtin", "Huntingtin mutations", "HAPP"
		}, "Huntingtin");
		new FirstFormula().run(new String[]{
				"Huntington’s Disease", "Huntington Chorea", "Chronic Progressive Hereditary Chorea"
		}, "Huntington");
		
		new FirstFormula().run(new String[]{
				"Sonic hedgehog", "Sonic hedgehog mutations", "Vertebrate Hedgehog Protein"
		}, "sonichedgehog");
		new FirstFormula().run(new String[]{
				"developmental disorders"
		}, "devdisorder");
		
		// another nm23
		new FirstFormula().run(new String[]{
				"tracheal development"
		}, "tracheal");
		
		new FirstFormula().run(new String[]{
				"pescadillo", "Pes"
		}, "Pes");
		
		new FirstFormula().run(new String[]{
				"orexin receptor", "HCRT receptor"
		}, "hypocretinreceptor2");
		new FirstFormula().run(new String[]{
				"narcolepsy", "Paroxysmal Sleep", "Gelineaus Syndrome"
		}, "narcolepsy");
		
		new FirstFormula().run(new String[]{
				"presenilin-1 gene", "psen1"
		}, "psen1");
		// + alzheimer
		
		new FirstFormula().run(new String[]{
				"FHM1", "familial hemiplegic migraine type 1", "Migraine with Auras"
		}, "FHM1");	
		new FirstFormula().run(new String[]{
				"hippocampal neurons"
		}, "hippneur");
	}
	
	private Map<String, Integer> citationCount;
	private Set<String> usedPMIDs;
	
	private long totalCitations;
	
	public FirstFormula() {
		super();
		scores = new HashMap<String, Double>();
		citationCount = new HashMap<>();
		usedPMIDs = new HashSet<>();
		this.totalCitations = 0l;
		this.fieldName = "rank1_for_";
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
			fileUtil.writeLine("files/combined_approach_1_scores/" + termId + ".csv", pmids.get(i) + "\t" + scores.get(pmids.get(i)));
		}
		fileUtil.closeWriter("files/combined_approach_1_scores/" + termId + ".csv");
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
		SolrQuery query = new SolrQuery();
		
		String queryString = "";
		for(String searchTerm : searchTerms) {
			for(String field : fields) {
				queryString += field + ":" + searchTerm + " ";
			}
		}
		queryString = queryString.trim();
		
		query.set("q", queryString);
		
		int start = 0,
			rows = 2000;
		
		while(true) {
			query.set("start", start);
			query.set("rows", rows);
			QueryResponse response = solr.query(query);
			SolrDocumentList results;
			
			if(response == null || (results = response.getResults()) == null) break;
			
			for(SolrDocument doc : results) {
				forEveryDocInD1(doc);
			}
			
			if(results.size() < rows) break;
			start += rows;
		}
	}
	
	private void calculateScores() {
		for(String pmid : citationCount.keySet()) {
			scores.put(pmid, citationCount.get(pmid) / (double) totalCitations);
		}
	}
	
	protected void forEveryDocInD1(SolrDocument doc) {
		String pmid = String.valueOf(doc.get("pmid"));
		if(usedPMIDs.contains(pmid)) return;
		increaseCountersForDocsCitedBy(pmid);
		usedPMIDs.add(pmid);
	}

	private void increaseCountersForDocsCitedBy(String pmid) {
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
				totalCitations++;
				increaseCounterForDoc(String.valueOf(doc.get("pmid")));
			}
			
			
			if(results.size() < rows) break;
			
			start += rows;
			q.set("start", start);
		}
	}

	private void increaseCounterForDoc(String pmid) {
		if(citationCount.containsKey(pmid)) {
			citationCount.put(pmid, citationCount.get(pmid) + 1);
		} else {
			citationCount.put(pmid, 1);
		}
	}

}
