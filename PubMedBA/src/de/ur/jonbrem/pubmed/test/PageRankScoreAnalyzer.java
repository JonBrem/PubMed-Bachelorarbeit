package de.ur.jonbrem.pubmed.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import de.ur.jonbrem.pubmed.solrconnection.SolrConnection;
import util.FileUtil;

public class PageRankScoreAnalyzer {

	public static void main(String[] args) throws Exception {
		new PageRankScoreAnalyzer().run();
	}
	
	
	
	private FileUtil fileUtil;
	private SolrConnection solr;
	private RelevanceAssessment relevance;
	
	public PageRankScoreAnalyzer() {
		this.fileUtil = new FileUtil();
		this.solr = new SolrConnection();
		this.relevance = new RelevanceAssessment();
		relevance.loadAssessments();
	}
	
	public void run() throws Exception {
		int totalCount = 0;
		while(fileUtil.readLine("files/citations.txt") != null) {
			totalCount++;
		}
		System.out.println("TotalCount: " + totalCount);
		
		solr.openConnection();
		
		int[] classes = new int[] {0, 5, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 120, 140, 160, 180, 200, 250, 300, 350, 400, 500, 600, 800, 1000, 1200};
		
		int[] classesCount = new int[classes.length + 1];
		int[] classesRelevant = new int[classes.length + 1];
		for(int i = 0; i < classesCount.length; i++) {
			classesCount[i] = 0;
			classesRelevant[i] = 0;
		}
		
		for(int trecTopic : new int[]{ 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 174, 175, 176, 177, 178, 179, 181, 182, 183, 184, 185, 186, 187 }) {
			int totalRelevantCitedByCount = 0;
			SolrQuery q = new SolrQuery();
			q.set("fl", "cited_by");
			for(String pmid : relevance.getRelevantPmids(String.valueOf(trecTopic))) {
				q.set("q", "pmid:" + pmid);
				QueryResponse response = solr.query(q);
				if(response == null || response.getResults() == null || response.getResults().size() == 0) continue;
				SolrDocument result = response.getResults().get(0);
				if(result.containsKey("cited_by")) {
					int size = result.getFieldValues("cited_by").size();
					totalRelevantCitedByCount += size;
					
					for(int i = 0; i < classes.length; i++) {
						if(i == classes.length - 1) {
							classesRelevant[classes.length]++;
						} else if(size >= classes[i] && size < classes[i + 1]) {
							classesRelevant[i]++;
							break;
						}
					}
					
				} else {
					classesRelevant[0]++;
				}
			}
			
			System.out.println("Average cited by for topic: " + trecTopic + " is: " + (totalRelevantCitedByCount  / (double) relevance.getNumRelevant(trecTopic + "")));
		}
		
		SolrQuery q = new SolrQuery();
		q.set("q", "pmid:*");
		q.set("sort", "pmid DESC");
		q.set("fl", "cited_by");
		int start = 0;
		int rows = 100;
		q.set("rows", rows);
		
		while(true) {
			q.set("start", start);
			QueryResponse response = solr.query(q);
			SolrDocumentList results;
			if(response == null || (results = response.getResults()) == null) break;

			for(SolrDocument result : results) {
				if(result.containsKey("cited_by")) {
					int size = result.getFieldValues("cited_by").size();
					
					for(int i = 0; i < classes.length; i++) {
						if(i == classes.length - 1) {
							classesCount[classes.length]++;
						} else if(size >= classes[i] && size < classes[i + 1]) {
							classesCount[i]++;
							break;
						}
					}
				} else {
					classesCount[0]++;
				}
			}
			
			if(results.size() < rows) break;
			
			start += rows;
			q.set("start", start);
		}
		
		for(int i = 0; i < classesCount.length; i++) {
			System.out.print(classesCount[i] + "\t");
		}
		System.out.println();
		for(int i = 0; i < classesRelevant.length; i++) {
			System.out.print(classesRelevant[i] + "\t");
		}
		System.out.println();
		
		solr.closeConnection();
		fileUtil.closeAll();
		System.out.println("Done.");
	}
	
}
