package de.ur.jonbrem.pubmed.advanced_querying;

import org.apache.solr.client.solrj.SolrQuery;

/**
 * A FieldQuery contains a field (that Solr knows), a query String for that field,
 * the name that the results for that field should be stored with, and a possible boost
 * for the scores.
 */
class FieldQuery {
	
	private AdvancedQuery advancedQuery;
	private float boost;
	public String field;
	private String terms;
	private String nameForResults;
	
	private int start;
	
	public FieldQuery(AdvancedQuery advancedQuery, String field, String terms, float boost) {
		this(advancedQuery, field, terms, boost, field);
	}
	
	public FieldQuery(AdvancedQuery advancedQuery, String field, String terms, float boost, String nameForResults) {
		this.advancedQuery = advancedQuery;
		this.boost = boost;
		this.terms = terms;
		this.field = field;
		this.nameForResults = nameForResults;
		this.start = 0;
	}
	
	public SolrQuery getSolrQuery() {
		SolrQuery query = new SolrQuery();
		
		query.set("fl", this.advancedQuery.getFl());
		query.set("start", start);
		query.set("rows", AdvancedQuery.ROWS);
		query.set("q", field + ":(" + terms + ")");
		
		return query;
	}
	
	String getNameForResults() {
		return nameForResults;
	}
	
	String getField() {
		return field;
	}
	
	public float getBoost() {
		return boost;
	}
	
	void nextPage() {
		start += AdvancedQuery.ROWS;
	}
	
}