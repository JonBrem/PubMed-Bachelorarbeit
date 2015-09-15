package de.ur.jonbrem.pubmed.test.advanced_querybuilders;

import de.ur.jonbrem.pubmed.advanced_querying.AdvancedQuery;
import de.ur.jonbrem.pubmed.test.AdvancedQueryBuilder;
import de.ur.jonbrem.pubmed.test.TrecQuery;

/**
 * 
 * The Vanilla Query Builder uses no special ways to rank (no MeSH, no nothing.)
 * 
 */
public class AVanillaQueryBuilder extends AdvancedQueryBuilder {

	public AVanillaQueryBuilder() {
		
	}
	
	public AdvancedQuery getNextQuery(TrecQuery trecQuery) {
		AdvancedQuery query = new AdvancedQuery();
		
		query.addFieldQuery("article_title", trecQuery.getGene());
		query.addFieldQuery("article_title", trecQuery.getOtherConcept());
		query.addFieldQuery("abstract", trecQuery.getGene());
		query.addFieldQuery("abstract", trecQuery.getOtherConcept());
		query.addFieldQuery("keyword", trecQuery.getGene());
		query.addFieldQuery("keyword", trecQuery.getOtherConcept());
		query.addFieldQuery("meshheading", trecQuery.getGene());
		query.addFieldQuery("meshheading", trecQuery.getOtherConcept());
				
		return query;
	}	
}
