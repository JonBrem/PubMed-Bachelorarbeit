package de.ur.jonbrem.pubmed.indexing.combined_way;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import de.ur.jonbrem.pubmed.solrconnection.SolrConnection;
import de.ur.jonbrem.pubmed.test.TrecQuery;

public class SimpleCombination {

	private SolrConnection solr;
	private SolrDocumentList results;
	
	private List<String> pmidsOfCitedDocs;
	private Map<String, Integer> pmidScores;
	private int totalNumberOfCitedDocs;
	private String meshId;
	
	public SimpleCombination(SolrConnection solr, SolrDocumentList results, String meshId) {
		this.solr = solr;
		this.results = results;
		this.meshId = meshId;
		
		totalNumberOfCitedDocs = 0;
		this.pmidsOfCitedDocs = new ArrayList<>();
		pmidScores = new HashMap<>();
	}
	
	public void retrieveCitedDocuments() {
		SolrQuery q = new SolrQuery();
		q.set("start", 0);
		q.set("fl", "pmid");
		
		for(int i = 0; i < results.size(); i++) {
			q.set("q", "cited_by:" + results.get(i).get("pmid"));
			QueryResponse queryResults = solr.query(q);
			if(queryResults != null) {
				for(SolrDocument doc : queryResults.getResults()) {
					pmidsOfCitedDocs.add(String.valueOf(doc.get("pmid"))); 
				}
			}
		}

		totalNumberOfCitedDocs = pmidsOfCitedDocs.size();
	}
	
	public void countOccurences() {
		for(int i = 0; i < pmidsOfCitedDocs.size(); i++) {
			String pmid = pmidsOfCitedDocs.get(i);
			if(pmidScores.containsKey(pmid)) {
				pmidScores.put(pmid, pmidScores.get(pmid) + 1);
			} else {
				pmidScores.put(pmid, 1);
			}
		}
		
	}
	
	public void update() {
		for(String pmid : pmidScores.keySet()) {
			SolrQuery q = new SolrQuery();
			q.set("q", "pmid:" + pmid);
			QueryResponse queryResponse = solr.query(q);
			if(queryResponse != null) {
				SolrDocumentList results = queryResponse.getResults();
				if(results.size() > 0) {
					SolrDocument result = results.get(0);
					SolrInputDocument toUpdate = ClientUtils.toSolrInputDocument(result);
					toUpdate.remove("_version_");
					toUpdate.setField("rank1_for_" + meshId, pmidScores.get(pmid) / (double) totalNumberOfCitedDocs);
					solr.addToIndex(toUpdate);
				}
			}
			q = null;
		}
	}
	
}
