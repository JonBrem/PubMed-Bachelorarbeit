package de.ur.jonbrem.pubmed.test.advanced_querybuilders;

import de.ur.jonbrem.pubmed.advanced_querying.AdvancedQuery;
import de.ur.jonbrem.pubmed.test.AdvancedQueryBuilder;
import de.ur.jonbrem.pubmed.test.TrecQuery;

/**
 * 
 * The Vanilla Query Builder uses no special ways to rank (no MeSH, no nothing.)
 * 
 */
public class AVanillaQueryBuilderPlus extends AdvancedQueryBuilder {

	public AVanillaQueryBuilderPlus() {
		
	}
	
	public AdvancedQuery getNextQuery(TrecQuery trecQuery) {
		AdvancedQuery query = new AdvancedQuery();
		
		query.addFieldQuery("article_title", trecQuery.getGene());
		query.addFieldQuery("article_title", trecQuery.getOtherConcept());
//		query.addFieldQuery("abstract", trecQuery.getGene());
//		query.addFieldQuery("abstract", trecQuery.getOtherConcept());
//		query.addFieldQuery("keyword", trecQuery.getGene());
//		query.addFieldQuery("keyword", trecQuery.getOtherConcept());
		
//		query.addFieldQuery("article_title", trecQuery.getGene(), -0.0388995f);
//		query.addFieldQuery("article_title", trecQuery.getOtherConcept(), -0.0388995f);
//		query.addFieldQuery("article_title_loose", trecQuery.getGene(), 0.6979301f);
//		query.addFieldQuery("article_title_loose", trecQuery.getOtherConcept(), 0.6979301f);
//		query.addFieldQuery("abstract_loose", trecQuery.getGene(), 0.9324755f);
//		query.addFieldQuery("abstract_loose", trecQuery.getOtherConcept(), 0.9324755f);
//		query.useBoost("citation_rank", 6.7781447f);
				
		return query;
	}	
}
