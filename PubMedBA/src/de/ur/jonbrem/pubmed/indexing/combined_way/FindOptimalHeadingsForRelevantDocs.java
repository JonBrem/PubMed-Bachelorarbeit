package de.ur.jonbrem.pubmed.indexing.combined_way;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;

import de.ur.jonbrem.pubmed.solrconnection.SolrConnection;
import de.ur.jonbrem.pubmed.test.RelevanceAssessment;
import util.Const;
import util.FileUtil;

public class FindOptimalHeadingsForRelevantDocs {

	public static final int[] TREC_QUESTION_IDS = { 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 174,
			175, 176, 177, 178, 179,
			181, 182, 183, 184, 185, 186, 187 };
	
	public static void main(String... args) {
		new FindOptimalHeadingsForRelevantDocs("files/optimal_headings.txt").run();
	}
	
	private String outputFile;
	private RelevanceAssessment relevance;
	private FileUtil fileUtil;
	private SolrConnection solr;
	
	public FindOptimalHeadingsForRelevantDocs(String outputFile) {
		this.outputFile = outputFile;
		this.relevance = new RelevanceAssessment().loadAssessments();
		this.fileUtil = new FileUtil();
		this.solr = new SolrConnection();
	}
	
	public void run() {
		solr.openConnection();
		
		for(int trecId : TREC_QUESTION_IDS) {
			try {
				findOptimalHeadingsFor(trecId);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
			Const.log(Const.LEVEL_INFO, "Finished with topic " + trecId);
		}
		
		solr.closeConnection();
		fileUtil.closeAll();
		System.out.println("Done.");
	}

	private void findOptimalHeadingsFor(int trecId) throws Exception {
		Map<String, Integer> majorHeadingsInRelevantDocs = new HashMap<>();
		Map<String, Integer> minorHeadingsInRelevantDocs = new HashMap<>();
		
		setupHeadingMaps(trecId, majorHeadingsInRelevantDocs, minorHeadingsInRelevantDocs);
				
		fileUtil.writeLine(outputFile, "Topic: " + trecId);
		storeQualityOfValues(majorHeadingsInRelevantDocs, true);
		storeQualityOfValues(minorHeadingsInRelevantDocs, false);
		
		fileUtil.write(outputFile, "\n\n");
	}
	
	private void storeQualityOfValues(Map<String, Integer> headingsInRelevantDocs, boolean major) {
		for(String heading : headingsInRelevantDocs.keySet()) {
			storeQualityOfValue(heading, headingsInRelevantDocs.get(heading), major);
		}
	}

	private void storeQualityOfValue(String heading, int countInRelevantDocs, boolean major) {
		SolrQuery q = new SolrQuery();
		heading = "\"" + heading + "\"";
		if(major) {
			q.set("q", "meshheading:" + heading);
		} else {
			q.set("q", "meshheading_minor:" + heading);
		}
		q.set("rows", 0);
		
		QueryResponse response = solr.query(q);
		NamedList<Object> responseAsList = response.getResponse();
		for(int i = 0; i < responseAsList.size(); i++) {
			if(responseAsList.getName(i).equals("response")) {
				String responseString = responseAsList.getVal(i).toString();
				String numFoundPart = responseString.substring(responseString.indexOf("numFound"));
				numFoundPart = numFoundPart.substring(0, numFoundPart.indexOf(","));
				int numFound = Integer.parseInt(numFoundPart.substring(numFoundPart.indexOf("=") + 1));
				
				fileUtil.writeLine(outputFile, "\t" +
						heading + "\t" + (major? "major" : "minor") + "\t" +
						"rel: " + countInRelevantDocs + "\tirrel: " + (numFound - countInRelevantDocs) + "\t" +
						(Math.round((countInRelevantDocs / (double) numFound) * 100) / 100.0) + " are relevant."
						);
			}
		}
	}

	private void setupHeadingMaps(int trecId, Map<String, Integer> majorHeadingsInRelevantDocs, 
			Map<String, Integer> minorHeadingsInRelevantDocs) throws Exception {
		for(String pmid : relevance.getRelevantPmids("" + trecId)) {
			SolrQuery q = new SolrQuery();
			q.set("q", "pmid:" + pmid);
			q.set("fl", "meshheading,meshheading_minor");
			
			QueryResponse response = solr.query(q);
			SolrDocumentList results;
			if(response != null && (results = response.getResults()) != null && results.size() > 0) {
				SolrDocument result = results.get(0);
				
				putFieldValuesInMap("meshheading", result, majorHeadingsInRelevantDocs);
				putFieldValuesInMap("meshheading_minor", result, minorHeadingsInRelevantDocs);
			}
		}
	}
	
	private void putFieldValuesInMap(String fieldName, SolrDocument result, Map<String, Integer> map) {		
		if(!result.containsKey(fieldName)) return;
		for(Object meshheading : result.getFieldValues(fieldName)) {
			if(map.containsKey(meshheading)) {
				map.put((String) meshheading, map.get(meshheading) + 1);
			} else {
				map.put((String) meshheading, 1);
			}
		}
		
	}
	
}
