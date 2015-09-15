package de.ur.jonbrem.pubmed.test;

import org.apache.solr.client.solrj.SolrQuery;

/**
 * 
 * A Query Builder is used to 
 * 
 */
public abstract class QueryBuilder {

	public abstract SolrQuery getNextQuery(TrecQuery q);

}
