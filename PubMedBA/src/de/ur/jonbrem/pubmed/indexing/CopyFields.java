package de.ur.jonbrem.pubmed.indexing;

import static util.Const.log;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

import de.ur.jonbrem.pubmed.solrconnection.SolrConnection;
import util.Const;

/**
 * Solr's "schema.xml" can handle CopyFields, but that causes errors when
 * the "field to copy to" already has a value, i.e. when the document is indexed
 * again.
 * <br>Therefore, this class was created to take care of that.
 *
 */
public class CopyFields {
	
	public static void main(String[] args) {
		List<CopyField> fieldsToCopy = new ArrayList<>();
		
//		fieldsToCopy.add(new CopyField("article_title", "article_title_loose"));
		
		String[] fieldTypes = new String[]{
				  "pubmed_language_stemming_worddelimiter_rank_bm25_1",
			      "pubmed_language_stemming_worddelimiter_rank_bm25_2",
			      "pubmed_language_stemming_worddelimiter_rank_bm25_3",
			      "pubmed_language_stemming_worddelimiter_rank_bm25_4",
			      "pubmed_language_stemming_worddelimiter_rank_bm25_5",
			      "pubmed_language_stemming_worddelimiter_rank_lm_1",
			      "pubmed_language_stemming_worddelimiter_rank_lm_2",
			      "pubmed_language_stemming_worddelimiter_rank_lm_3",
			      "pubmed_language_stemming_worddelimiter_rank_lm_4",
			      "pubmed_language_stemming_worddelimiter_rank_lm_5",
			      "pubmed_language_stemming_worddelimiter_rank_lm_6",
			      "pubmed_language_stemming_worddelimiter_rank_lm_7",
			      "pubmed_language_stemming_rank_lm_5",
			      "pubmed_language_stemming_rank_lm_6",
			      "pubmed_language_stemming_rank_lm_7",
			      "pubmed_language_worddelimiter_rank_lm_5",
			      "pubmed_language_worddelimiter_rank_lm_6",
			      "pubmed_language_worddelimiter_rank_lm_7"
		};
		
		for(String fieldType : fieldTypes) {
			CopyField abstractCopyField = new CopyField("abstract", fieldType + "_abstract");
			CopyField titleCopyField = new CopyField("article_title", fieldType + "_title");
			fieldsToCopy.add(abstractCopyField);
			fieldsToCopy.add(titleCopyField);
		}
		
		
		new CopyFields(fieldsToCopy).run();
	}

	private SolrConnection solr;
	private List<CopyField> copy;

	public CopyFields(List<CopyField> copy) {
		this.copy = copy;
		this.solr = new SolrConnection();
	}
	
	public void run() {
		solr.openConnection();
		
		int offset = 0;
		
		while(true) {
			SolrQuery q = new SolrQuery();
			q.set("q", "pmid:*");
			q.set("sort", "pmid asc");
			q.set("rows", 100);
			q.set("start", offset);

			QueryResponse response = solr.query(q);
			
			if(response.getResults().size() == 0) break;
			
			for(SolrDocument result : response.getResults()) {
				for(CopyField copyField : copy) {
					result.setField(copyField.getFieldToCopyTo(), result.get(copyField.getFieldToCopy()));
				}
				
				result.remove("_version_");
//				result.remove("own_meshheading");
//				result.remove("own_meshheading_loose");

				solr.addToIndex(result);
			}
			
			offset += 100;
			
			if(offset % 300 == 0) solr.commit();
			
			if(offset % 1000 == 0) log(Const.LEVEL_DEBUG, "Done updating " + offset + " documents.");
		}
		
		solr.commit();
		solr.closeConnection();
	}
	
}
