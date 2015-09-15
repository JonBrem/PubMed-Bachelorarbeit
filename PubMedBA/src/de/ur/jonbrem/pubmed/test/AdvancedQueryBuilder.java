package de.ur.jonbrem.pubmed.test;

import org.apache.solr.client.solrj.SolrQuery;

import de.ur.jonbrem.pubmed.advanced_querying.AdvancedQuery;

public abstract class AdvancedQueryBuilder {

	public abstract AdvancedQuery getNextQuery(TrecQuery q);

}