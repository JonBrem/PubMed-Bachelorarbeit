package de.ur.jonbrem.pubmed.test.advanced_querybuilders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import de.ur.jonbrem.pubmed.advanced_querying.AdvancedQuery;
import de.ur.jonbrem.pubmed.solrconnection.SolrConnection;
import de.ur.jonbrem.pubmed.test.TrecQuery;

public class AMachineLearningQueryBuilder {

	public static final String[] queryStrings = {
//			"pubmed_language_worddelimiter_rank_tfidf",
//			"pubmed_language_worddelimiter_rank_bm25_1",
//			"pubmed_language_worddelimiter_rank_bm25_2",
//			"pubmed_language_worddelimiter_rank_bm25_3",
//			"pubmed_language_worddelimiter_rank_bm25_4",
//			"pubmed_language_worddelimiter_rank_bm25_5",
//			"pubmed_language_worddelimiter_rank_lm_1",
			"pubmed_language_worddelimiter_rank_lm_2",
//			"pubmed_language_worddelimiter_rank_lm_3",
//			"pubmed_language_worddelimiter_rank_lm_4",
//			"pubmed_language_remove_nonnumeric_rank_tfidf",
//			"pubmed_language_remove_nonnumeric_rank_bm25_1",
//			"pubmed_language_remove_nonnumeric_rank_bm25_2",
//			"pubmed_language_remove_nonnumeric_rank_bm25_3",
//			"pubmed_language_remove_nonnumeric_rank_bm25_4",
//			"pubmed_language_remove_nonnumeric_rank_bm25_5",
//			"pubmed_language_remove_nonnumeric_rank_lm_1",
//			"pubmed_language_remove_nonnumeric_rank_lm_2",
//			"pubmed_language_remove_nonnumeric_rank_lm_3",
//			"pubmed_language_remove_nonnumeric_rank_lm_4",
			"pubmed_language_simple_rank_tfidf",
//			"pubmed_language_simple_rank_bm25_1",
//			"pubmed_language_simple_rank_bm25_2",
//			"pubmed_language_simple_rank_bm25_3",
//			"pubmed_language_simple_rank_bm25_4",
//			"pubmed_language_simple_rank_bm25_5",
//			"pubmed_language_simple_rank_lm_1",
//			"pubmed_language_simple_rank_lm_2",
//			"pubmed_language_simple_rank_lm_3",
//			"pubmed_language_simple_rank_lm_4",
//			"pubmed_language_stemming_rank_tfidf",
//			"pubmed_language_stemming_rank_bm25_1",
//			"pubmed_language_stemming_rank_bm25_2",
//			"pubmed_language_stemming_rank_bm25_3",
//			"pubmed_language_stemming_rank_bm25_4",
//			"pubmed_language_stemming_rank_bm25_5",
//			"pubmed_language_stemming_rank_lm_1",
//			"pubmed_language_stemming_rank_lm_2",
//			"pubmed_language_stemming_rank_lm_3",
//			"pubmed_language_stemming_rank_lm_4"
			
//		"keyword",
//		"keyword_loose",
//		"keyword_lm",
//		"keyword_bm25"
	};
	
	public static final String[] queryStringsAdvanced = {
			"pubmed_language_worddelimiter_rank_bm25_2",
			"pubmed_language_worddelimiter_rank_bm25_3",
			"pubmed_language_worddelimiter_rank_bm25_5",
			"pubmed_language_worddelimiter_rank_lm_3",
			"pubmed_language_worddelimiter_rank_lm_4",
			
			"pubmed_language_stemming_rank_bm25_2",
			"pubmed_language_stemming_rank_bm25_3",
			"pubmed_language_stemming_rank_bm25_5",
			"pubmed_language_stemming_rank_lm_3",
			"pubmed_language_stemming_rank_lm_4"
	};	
	
	public static final int ROWS = 4000;
	
	public AdvancedQuery getQueriesFor(TrecQuery trecQuery) {
		AdvancedQuery query = new AdvancedQuery();
		
		String[] queryStringsAll = new String[queryStrings.length];
		for(int i = 0; i < queryStrings.length; i++) queryStringsAll[i] = queryStrings[i];
		
		for(int i = 0; i < queryStringsAll.length; i++) {
			query.addFieldQuery(queryStringsAll[i] + "_abstract", trecQuery.getGene(), queryStringsAll[i] + "_abstract");
			query.addFieldQuery(queryStringsAll[i] + "_abstract", trecQuery.getOtherConcept(), queryStringsAll[i] + "_abstract");
			
			query.addFieldQuery(queryStringsAll[i] + "_title", trecQuery.getGene(), queryStringsAll[i] + "_title");
			query.addFieldQuery(queryStringsAll[i] + "_title", trecQuery.getOtherConcept(), queryStringsAll[i] + "_title");		

//			query.addFieldQuery(queryStringsAll[i] + "_abstract", trecQuery.getGene() + " " + trecQuery.getOtherConcept(), queryStringsAll[i] + "_abstract");
//			query.addFieldQuery(queryStringsAll[i] + "_title", trecQuery.getGene() + " " + trecQuery.getOtherConcept(), queryStringsAll[i] + "_title");
			
			for(String meshTerm : trecQuery.getMeshTerms()) {
				query.addFieldQuery(queryStringsAll[i] + "_abstract", meshTerm, "mesh_" + queryStringsAll[i] + "_abstract");
				query.addFieldQuery(queryStringsAll[i] + "_title", meshTerm, "mesh_" + queryStringsAll[i] + "_title");
			}
		}
				
//		query.addFieldQuery("pubmed_language_simple_rank_tfidf_abstract", trecQuery.getGene());
//		query.addFieldQuery("pubmed_language_simple_rank_tfidf_abstract", trecQuery.getOtherConcept());
//		query.addFieldQuery("pubmed_language_simple_rank_tfidf_title", trecQuery.getGene());
//		query.addFieldQuery("pubmed_language_simple_rank_tfidf_title", trecQuery.getOtherConcept());
		
//		for(String meshTerm : trecQuery.getMeshTerms()) {
//			query.addFieldQuery("pubmed_language_simple_rank_tfidf_abstract", meshTerm, "mesh_pubmed_language_simple_rank_tfidf_abstract");
//			query.addFieldQuery("pubmed_language_simple_rank_tfidf_title", meshTerm, "mesh_pubmed_language_simple_rank_tfidf_title");
//		}
		
		setCombinedApproachScores(trecQuery, query);
		
		query.useBoost("citation_rank", 1.0f);
				
		return query;
	}
	
	private void setCombinedApproachScores(TrecQuery trecQuery, AdvancedQuery query) {
		String[] fields = null;
		switch(trecQuery.getId()) {
			case 160:
				fields = new String[]{"PRNP", "BSE"};
				break;
			case 161:
				fields = new String[]{"IDE", "Alzheimer"};
				break;
			case 162:
				fields = new String[]{"MMS2", "Cancer"};
				break;
			case 163:
				fields = new String[]{"APC", "ColonCancer"};
				break;
			case 164:
				fields = new String[]{"Nurr77", "Parkinson"};
				break;
			case 165:
				fields = new String[]{"CTSD", "ApoE", "Alzheimer"};
				break;
			case 166:
				fields = new String[]{"TGFb1", "CAA"};
				break;
			case 167:
				fields = new String[]{"NM23", "Cancer"};
				break;
			case 168:
				fields = new String[]{"BARD1", "BRCA1"};
				break;
			case 169:
				fields = new String[]{"APC", "actin"};
				break;
			case 170:
				fields = new String[]{"COP2", "CFTR", "endoplasmaticreticulum"};
				break;
			case 171:
				fields = new String[]{"Nurr77", "actin"};
				break;
			case 172:
				fields = new String[]{"P53", "apoptosis"};
				break;

			case 174:
				fields = new String[]{"BRCA1", "Cancer"};
				break;
			case 175:
				fields = new String[]{"L1L2", "HPV11"};
				break;
			case 176:
				fields = new String[]{"CFTR"}; // possible TODO
				break;
			case 177:
				fields = new String[]{"Pes"};
				break;
			case 178:
				fields = new String[]{};
				break;
			case 179:
				fields = new String[]{"HNF4", "COUP_TFI", "liver"};
				break;

			case 181:
				fields = new String[]{"Huntingtin", "Huntington"};
				break;
			case 182:
				fields = new String[]{"sonichedgehog", "devdisorder"};
				break;
			case 183:
				fields = new String[]{"NM23", "tracheal"};
				break;
			case 184:
				fields = new String[]{"Pes"};
				break;
			case 185:
				fields = new String[]{"hypocretinreceptor2", "narcolepsy"};
				break;
			case 186:
				fields = new String[]{"psen1", "Alzheimer"};
				break;
			case 187:
				fields = new String[]{"FHM1", "hippneur"};
				break;
			default: 
				fields = new String[]{};
		}
		
		for(String field : fields) {
//			query.addCombinedScoreQuery("rank1_for_" + field, "combined_score_1");
			query.addCombinedScoreQuery("rank2_for_" + field, "combined_score_2");
//			query.addCombinedScoreQuery("rank3_for_" + field, "combined_score_3");
		}
		
	}
}
