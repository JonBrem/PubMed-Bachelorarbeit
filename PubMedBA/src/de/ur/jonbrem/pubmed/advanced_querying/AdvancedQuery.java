package de.ur.jonbrem.pubmed.advanced_querying;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import de.ur.jonbrem.pubmed.solrconnection.SolrConnection;
import util.Const;

public class AdvancedQuery {
	
	static final int ROWS = 2000;
	
	private List<FieldQuery> queries;
	
	private List<Boost> boosts;
	
	private List<FieldQuery> combinedScoreQueries;
	
	public AdvancedQuery() {
		queries = new ArrayList<>();
		boosts = new ArrayList<>();
		combinedScoreQueries = new ArrayList<>();
	}
	
	/**
	 * @param field
	 * The field (defined in Solr's schema.xml) to query
	 * @param searchFor
	 * The search terms
	 * @param boost
	 * Every score that is achieved with this query will be multiplied by the boost
	 */
	public void addFieldQuery(String field, String searchFor, float boost) {
		addFieldQuery(field, searchFor, field, boost);
	}
	
	/**
	 * @param field
	 * The field (defined in Solr's schema.xml) to query
	 * @param searchFor
	 * The search terms
	 */
	public void addFieldQuery(String field, String searchFor) {
		addFieldQuery(field, searchFor, field);
	}
	
	/**
	 * @param field
	 * The field (defined in Solr's schema.xml) to query
	 * @param searchFor
	 * The search terms
	 * @param nameForResults
	 * In the results, this will be stored under this name.
	 */
	public void addFieldQuery(String field, String searchFor, String nameForResults) {
		addFieldQuery(field, searchFor, nameForResults, 1.0f);
	}
	
	/**
	 * @param field
	 * The field (defined in Solr's schema.xml) to query
	 * @param searchFor
	 * The search terms
	 * @param nameForResults
	 * In the results, this will be stored under this name.
	 * @param boost
	 * Every score that is achieved with this query will be multiplied by the boost
	 */
	public void addFieldQuery(String field, String searchFor, String nameForResults, float boost) {
		queries.add(new FieldQuery(this, field, searchFor, boost, nameForResults));
	}
	
	/**
	 * The field specified by the "boost" parameter will also be retrieved. It must be a float or double field.
	 * @param boost
	 * The name of the boost field (defined in Solr's schema.xml)
	 * @param weight
	 * Multiplier for the boost
	 */
	public void useBoost(String boost, float weight) {
		boosts.add(new Boost(boost, weight));
	}
	
	/**
	 * The field specified by the "boost" parameter will also be retrieved. It must be a float or double field.
	 * Uses a weight of 1.0.
	 * @param boost
	 * The name of the boost field (defined in Solr's schema.xml)
	 */
	public void useBoost(String boost) {
		this.useBoost(boost, 1.0f);
		
	}

	/**
	 * Like useBoost, but this will find all documents that have the field, not just store the field
	 * for documents that are already in the results.
	 */
	public void addCombinedScoreQuery(String scoreName, String storeAs) {
		this.combinedScoreQueries.add(new FieldQuery(this, scoreName, "*", 1.0f, storeAs));
	}
	
	/**
	 * @param solr
	 * @return
	 * The results for the queries specified with addFieldQuery and useBoost
	 */
	public List<DocScore> getResults(SolrConnection solr) {
		QueryResults results = query(solr);
		List<DocScore> scores = calculateScores(results);
		
		Collections.sort(scores);
		Collections.reverse(scores);
		
		return scores;
	}

	private List<DocScore> calculateScores(QueryResults results) {
		List<DocScore> scores = new ArrayList<>();
		
		for(String pmid : results.getResultPMIDs()) {
			DocScore currentScore = new DocScore(pmid);
			
			Map<String, Object> doc = results.getDoc(pmid);
			
			for(FieldQuery fq : queries) {
				if(doc.containsKey(fq.getField())) {
					currentScore.addToScore((float) doc.get(fq.getNameForResults()) * fq.getBoost()); 
				}
			}
			
			for(Boost b : boosts) {
				if(doc.containsKey(b.getName())) {
					currentScore.addToScore(((double) doc.get(b.getName())) * b.getWeight());
				}
			}
			
			scores.add(currentScore);
		}
		return scores;
	}

	/**
	 * @return
	 * The results of the query with all the different field types and boosts.
	 */
	public QueryResults query(SolrConnection solr) {
		QueryResults results = setupResults();
		
		int queryIndex = 0;
		for(FieldQuery query : queries) {
			while(true) {
				QueryResponse response = solr.query(query.getSolrQuery());
				SolrDocumentList docs;
				if(response == null || (docs = response.getResults()) == null) break;
				processSingleQueryResults(results, query, docs);
				
				if(docs.size() < ROWS) break;
				query.nextPage();
			}
			queryIndex++;
			if(queryIndex % 20 == 0)
				Const.log(Const.LEVEL_INFO, "AdvancedQuery: Finished with " + queryIndex + " out of: " + queries.size() + " sub-field-queries.");
			
		}
				
		return results;
	}

	private void processSingleQueryResults(QueryResults results, FieldQuery query, SolrDocumentList docs) {
		for(SolrDocument doc : docs) {
			results.add((String) doc.get("pmid"), query.getNameForResults(), (Float) doc.get("score")); 
			for(Boost boost : boosts) {
				if(doc.containsKey(boost.getName())) {
					results.put((String) doc.get("pmid"), boost.getName(), (Double) doc.get(boost.getName()));
				}
			}
			for(FieldQuery combinedScoreQuery : combinedScoreQueries) {
				if(doc.containsKey(combinedScoreQuery.getField())) {
					results.add((String) doc.get("pmid"), combinedScoreQuery.getNameForResults(), (Double) doc.get(combinedScoreQuery.getField()));
				}
			}
		}
	}

	private QueryResults setupResults() {
		QueryResults results = new QueryResults();
		
		for(FieldQuery fq : queries) results.addQueryString(fq.getNameForResults());
		for(Boost b : boosts) results.addQueryString(b.getName());
		for(FieldQuery fq : combinedScoreQueries) results.addQueryString(fq.getNameForResults());
		return results;
	}
	
	public String getFl() {
		StringBuilder fl = new StringBuilder();
		for(int i = 0; i < boosts.size(); i++) {
			fl.append(boosts.get(i).getName()).append(",");
		}
		for(int i = 0; i < combinedScoreQueries.size(); i++) {
			fl.append(combinedScoreQueries.get(i).field).append(",");
		}
		fl.append("pmid").append(",").append("score");
		
		return fl.toString();
	}
}
