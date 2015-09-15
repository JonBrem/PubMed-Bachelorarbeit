package de.ur.jonbrem.pubmed.indexing.pagerank;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import de.ur.jonbrem.pubmed.solrconnection.SolrConnection;
import util.Const;
import util.FileUtil;

/**
 * The LinkRankBuilder only writes stuff in files;
 * this Program reads the link ranks from the files and adds it to the Solr Index.
 */
public class LinkRankAssignment {

	public static void main(String... args) {
		new LinkRankAssignment().run();
	}
	
	private SolrConnection solr;
	private FileUtil fileUtil;
	private List<String> pmids;
	private double[] citationRanks;
	
	public LinkRankAssignment() {
		solr = new SolrConnection();
		fileUtil = new FileUtil();
		
		pmids = new ArrayList<>();
		
		readPMIDs();
		readCitationRanks();
	}
	
	public void run() {
		solr.openConnection();
		
		for(int i = 0; i < pmids.size(); i++) {
			assignCitationRank(pmids.get(i), citationRanks[i]);
			if(i%1000 == 0) Const.log(Const.LEVEL_DEBUG, i + " ranks assigned.");
		}
		
//		fixOldWrongRanks();
		
		solr.commit();

		fileUtil.closeAll();
		solr.closeConnection();
	}
	
	private void assignCitationRank(String pmid, double newRank) {
		SolrQuery q = new SolrQuery();
		q.set("q", "pmid:"+pmid);
		
		QueryResponse response = solr.query(q);
		if(response != null) {
			SolrDocumentList results = response.getResults();
			if(results != null && results.size() > 0) {
				results.get(0).remove("_version_");
				results.get(0).setField("citation_rank", newRank);
				solr.addToIndex(results.get(0));
			}
		}
		
	}
	
	private void fixOldWrongRanks() {
		SolrQuery q = new SolrQuery();
		q.set("q", "citation_rank:" + 1.0);
		QueryResponse response = solr.query(q);
		
		if(response != null) {
			SolrDocumentList results = response.getResults();
			if(results != null && results.size() > 0) {
				for(int i = 0; i < results.size(); i++) {
					results.get(0).remove("_version_");
					results.get(0).setField("citation_rank", 6.162986273457821E-6);
					solr.addToIndex(results.get(0));
				}
			}			
		}
		
	}

	private void readPMIDs() {
		String s;
		while((s = fileUtil.readLine("files/pmids.txt")) != null) {
			pmids.add(s);
		}
		fileUtil.closeReader("files/pmids.txt");
	}
	
	private void readCitationRanks() {
		String s = fileUtil.readLine("files/scores.txt");
		String[] lineParts = s.split("\t");
		
		citationRanks = new double[lineParts.length];
		for(int i = 0; i < lineParts.length; i++) {
			citationRanks[i] = Double.parseDouble(lineParts[i]);
		}
		
		fileUtil.closeReader("files/scores.txt");
	}
	
}
