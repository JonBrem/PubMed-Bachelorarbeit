package de.ur.jonbrem.pubmed.indexing.combined_way;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import de.ur.jonbrem.pubmed.advanced_querying.AdvancedQuery;
import de.ur.jonbrem.pubmed.solrconnection.SolrConnection;

public abstract class GeneralCombination {
	
	protected List<AdvancedQuery> queries;
	
	protected String scoreId;
	
	protected SolrConnection solr;
	
	protected List<String> fields;
	
	protected String fieldName;
	
	protected Map<String, Double> scores;
			
	protected void assignScoresToD2() {
		for(String pmid : scores.keySet()) {
			double score = scores.get(pmid);
			
			SolrInputDocument doc = getDocument(pmid);
			
			if(doc != null) {
				doc.setField(fieldName + scoreId, score);
			}
			
			solr.addToIndex(doc);
		}
	}

	protected SolrInputDocument getDocument(String pmid) {
		SolrQuery q = new SolrQuery();
		q.set("q", "pmid:"+pmid);
		
		QueryResponse response = solr.query(q);
		SolrDocumentList results;
		if(response == null || (results = response.getResults()) == null || results.size() == 0) return null;
		
		SolrInputDocument doc = ClientUtils.toSolrInputDocument(results.get(0));
		
		doc.remove("_version_");
		
		return doc;
	}
	
	protected void openConnection() {
		solr = new SolrConnection();
		solr.openConnection();
	}
	
	protected void closeConnection() {
		solr.closeConnection();
	}
	
	
}
