package de.ur.jonbrem.pubmed.indexing.combined_way;

import java.io.File;
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

public class ScoreAssignment {

	public static void main(String[] args) {
		new ScoreAssignment("C:/Users/Jonathan/git/barepo/PubMedBA/files/combined_approach_5_scores", "rank5_for_").run();
	}

	private String folder;
	private String scoreName;
	
	private Map<String, List<Score>> scores;
	
	public ScoreAssignment(String folder, String scoreName) {
		this.folder = folder;
		this.scoreName = scoreName;
	}
	
	public void run() {
		scores = new HashMap<>();
		readScores();
		assignScores();
	}	
	
	private void readScores() {
		File scoreFolder = new File(folder);
		FileUtil fileUtil = new FileUtil();
		for(File f : scoreFolder.listFiles()) {
			String scoreName = this.scoreName + f.getName().substring(0, f.getName().indexOf(".csv"));
			String s;
			while((s = fileUtil.readLine(f.getAbsolutePath())) != null) {
				String[] split = s.split("\t");
				
				if(split.length == 2) {
					if(!scores.containsKey(split[0])) {
						scores.put(split[0], new ArrayList<>());
					}
					
					scores.get(split[0]).add(new Score(scoreName, Double.parseDouble(split[1])));
				}
			}
		}
		fileUtil.closeAll();
	}
	
	private void assignScores() {
		SolrConnection solr = new SolrConnection();
		solr.openConnection();
		
		int count = 0;
		
		List<String> pmidsToAssign = new ArrayList<>();
		
		for(String pmid : scores.keySet()) {
			pmidsToAssign.add(pmid);
			
			count++;
			if(count % 50 == 0) {
				assignScoresForDocs(pmidsToAssign, solr);
				pmidsToAssign.clear();
			}
			
			if(count % 500 == 0) {
				System.out.println("Assigned scores for " + count + " out of " + scores.keySet().size() + " docs.");
			}
		}
		
		if(pmidsToAssign.size() > 0) assignScoresForDocs(pmidsToAssign, solr);
		
		solr.commit();
		solr.closeConnection();
	}
	
	
	
	private void assignScoresForDocs(List<String> pmidsToAssign, SolrConnection solr) {
		String queryString = "";
		for(int i = 0; i < pmidsToAssign.size(); i++) {
			queryString += "pmid:" + pmidsToAssign.get(i);
			
			if(i != pmidsToAssign.size() - 1) {
				queryString += " OR ";
			}
		}
		
		SolrQuery query = new SolrQuery();
		query.set("q", queryString);
		
		query.set("start", 0);
		query.set("rows", 50);
		
		QueryResponse response = solr.query(query);
		if(response != null) {
			SolrDocumentList results = response.getResults();
			if(results == null) return;
			
			for(SolrDocument doc : results) {
				String pmid = (String) doc.get("pmid");
				
				SolrInputDocument inputDoc = ClientUtils.toSolrInputDocument(doc);
				inputDoc.remove("_version_");
				
				for(Score score : scores.get(pmid)) {
					inputDoc.setField(score.name, score.value);
				}
				
				solr.addToIndex(inputDoc);
			}
		}
	}



	private class Score {
		public String name;
		public double value;
		
		public Score(String name, double value) {
			this.name = name;
			this.value = value;
		}
	}
}
