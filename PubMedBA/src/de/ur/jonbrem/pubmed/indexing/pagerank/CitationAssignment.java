package de.ur.jonbrem.pubmed.indexing.pagerank;

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
import util.FileUtil;

/**
 * Puts the information "Who is cited by whom" in the Solr documents.
 */
public class CitationAssignment {

	public static void main(String[] args) {
		new CitationAssignment().run();
	}

	private FileUtil fileUtil;
	private SolrConnection solr;
	
	private Map<String, List<String>> citedBy;

	public CitationAssignment() {
		this.fileUtil = new FileUtil();
		solr = new SolrConnection();
		citedBy = new HashMap<>();
	}

	public void run() {
		solr.openConnection();

		readCitations();
		assignCitations();

		solr.closeConnection();
		fileUtil.closeAll();
	}
	
	private void readCitations() {
		String s;
		while((s = fileUtil.readLine("files/citations.txt")) != null) {
			String[] parts = s.split("\t");

			String citing = parts[0].substring(0, parts[0].indexOf("."));
			String cited = parts[1].replaceAll("/", "\\:");
			
			if(citedBy.containsKey(cited)) {
				citedBy.get(cited).add(citing);
			} else {
				List<String> list = new ArrayList<>();
				list.add(citing);
				citedBy.put(cited, list);
			}
		}
		fileUtil.closeReader("files/citations.txt");
	}

	private void assignCitations() {
		for(String hwid : citedBy.keySet()) {
			SolrQuery query = new SolrQuery();
			query.set("q", "hwid:\"" + hwid + "\"");
			query.set("fl", "*");
			query.setRequestHandler("select");
			
			QueryResponse response = solr.query(query);
			SolrDocumentList results;
			if(response != null && (results = response.getResults()) != null && results.size() > 0) {
				for(SolrDocument doc : results) {
					SolrInputDocument inputDoc = ClientUtils.toSolrInputDocument(doc);
					inputDoc.remove("_version_");
					inputDoc.setField("cited_by", citedBy.get(hwid));
					
					solr.addToIndex(doc);
				}
			}
			
		}

		solr.commit();
	}

}
