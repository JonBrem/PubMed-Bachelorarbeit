package de.ur.jonbrem.pubmed.indexing.combined_way;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

public class FindOptimalHeadings {

	public static void main(String... args) {
		new FindOptimalHeadings("files/optimal_headings4.txt").run();
	}
	
	private String outputFile;
	private FileUtil fileUtil;
	private SolrConnection solr;
	private RelevanceAssessment relevance;
	
	public FindOptimalHeadings(String outputFile) {
		this.outputFile = outputFile;
		this.solr = new SolrConnection();
		this.fileUtil = new FileUtil();
		this.relevance = new RelevanceAssessment().loadAssessments();
		
	}
	
	public void run() {
		solr.openConnection();
		try {
			findOptimalHeadings();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileUtil.closeAll();
		solr.closeConnection();
		Const.log("Done");
	}
	
	private void findOptimalHeadings() throws Exception {
		for(int trecQueryId : FindOptimalHeadingsForRelevantDocs.TREC_QUESTION_IDS) {
			if(trecQueryId != 187) continue;
			fileUtil.writeLine(outputFile, trecQueryId + "");
			Map<String, Integer> meshHeadingsOfDocsCitingRelevantDocs = getMeshHeadingsOfDocsCitingRelevantDocs(trecQueryId);

			int minimum = relevance.getNumRelevant(String.valueOf(trecQueryId)) / 10;
			if(minimum > 15) minimum = 15;
			if(minimum < 4) minimum = 4;
			
			for(String meshHeading : meshHeadingsOfDocsCitingRelevantDocs.keySet()) {
				if(meshHeadingsOfDocsCitingRelevantDocs.get(meshHeading) < minimum) continue; // performance measure
				Map<String, Integer> docsCitationCount = getDocsCitationCountForHeading(meshHeading);
				fileUtil.writeLine(outputFile, "\t" + meshHeading + ";\t" + getPositionsOfRelevantDocs(docsCitationCount, trecQueryId) + ";\ttotal: " + docsCitationCount.size());
				fileUtil.flushWriter(outputFile);
//				Const.log(Const.LEVEL_DEBUG, "Finished with mesh heading: " + meshHeading);
			}
			Const.log(Const.LEVEL_INFO, "Finished with " + trecQueryId);
		}
	}

	private String getPositionsOfRelevantDocs(Map<String, Integer> docsCitationCount, int trecId) {
		List<PMIDandCount> pmidsAndCounts = new ArrayList<>();
		for(String pmid : docsCitationCount.keySet()) {
			pmidsAndCounts.add(new PMIDandCount(pmid, docsCitationCount.get(pmid)));
		}
		Collections.sort(pmidsAndCounts);
		Collections.reverse(pmidsAndCounts);		
		
		String positionString = "";
		int currentPosition = 0;
		int currentValue = Integer.MAX_VALUE;
		int currentValueRelevantCounter = 0;
		int currentIrrelevantValueCounter = 0;
		boolean justWrote = false;
		for(int i = 0; i < pmidsAndCounts.size(); i++) {
			justWrote = false;
			try {
				if(pmidsAndCounts.get(i).count < currentValue) {
					if(currentValue != Integer.MAX_VALUE) {
						justWrote = true;
						positionString += "pos:" + currentPosition+",count:" + currentValue + ",relevant:" + currentValueRelevantCounter+",irrelevant:" + currentIrrelevantValueCounter;
						if(i != pmidsAndCounts.size() - 1) positionString += " ";
						currentValueRelevantCounter = 0;
						currentIrrelevantValueCounter = 0;
					}
					currentValue = pmidsAndCounts.get(i).count;
					currentPosition++;
				}
				if(relevance.isRelevant(String.valueOf(trecId), pmidsAndCounts.get(i).pmid)) {
					currentValueRelevantCounter++;
				} else {
					currentIrrelevantValueCounter++;
				}
			} catch (Exception e) {
			}
		}
		
		if(!justWrote) {
			positionString += "pos:" + currentPosition+",count:" + currentValue + ",relevant:" + currentValueRelevantCounter+",irrelevant:" + currentIrrelevantValueCounter;
		}
		
		positionString = positionString.trim();
		return positionString;
	}

	private Map<String, Integer> getDocsCitationCountForHeading(String meshHeading) {
		Map<String, Integer> docCitationCount = new HashMap<>();
		
		SolrQuery q = new SolrQuery();
		q.set("q", "meshheading:\"" + meshHeading +"\" OR meshheading_minor:\"" + meshHeading + "\"");
		q.set("fl", "pmid");
		
		int start = 0;
		int rows = 2000;
		q.set("rows", rows);
		
		while(true) {
			q.set("start", start);
			QueryResponse response = solr.query(q);
			if(start == 0 && getNumFound(response) >= 10000) return new HashMap<>();
			if(response == null) break;
			SolrDocumentList results = response.getResults();
			if(results == null) break;
			
			for(SolrDocument doc : results) increaseDocCitationCount((String) doc.get("pmid"), docCitationCount);
			
			if(results.size() < rows) break;
			start += rows;
		}
		
		return docCitationCount;
	}
	
	private int getNumFound(QueryResponse response) {
		NamedList<Object> responseAsList = response.getResponse();
		for(int i = 0; i < responseAsList.size(); i++) {
			if(responseAsList.getName(i).equals("response")) {
				String responseString = responseAsList.getVal(i).toString();
				String numFoundPart = responseString.substring(responseString.indexOf("numFound"));
				numFoundPart = numFoundPart.substring(0, numFoundPart.indexOf(","));
				int numFound = Integer.parseInt(numFoundPart.substring(numFoundPart.indexOf("=") + 1));
				return numFound;
			}
		}
		return -1;
	}

	private void increaseDocCitationCount(String pmid, Map<String, Integer> docCitationCount) {
		SolrQuery q = new SolrQuery();
		q.set("q", "cited_by:" + pmid);
		q.set("fl", "pmid");
		q.set("rows", 5000);
		
		QueryResponse response = solr.query(q);
		if(response == null) return;
		SolrDocumentList results = response.getResults();
		if(results == null) return;
		for(SolrDocument doc : results) {
			if(!doc.containsKey("pmid")) continue;
			String pmidOfCitedDoc = doc.get("pmid").toString();
			if(docCitationCount.containsKey(pmidOfCitedDoc)) {
				docCitationCount.put(pmidOfCitedDoc, docCitationCount.get(pmidOfCitedDoc) + 1);
			} else {
				docCitationCount.put(pmidOfCitedDoc, 1);
			}
		}
	}

	private Map<String, Integer> getMeshHeadingsOfDocsCitingRelevantDocs(int trecQueryId) throws Exception {
		Map<String, Integer> meshHeadings = new HashMap<>();
		
		for(String pmid : relevance.getRelevantPmids(String.valueOf(trecQueryId))) {
			SolrQuery q = new SolrQuery();
			q.set("q", "pmid:" + pmid);
			q.set("fl", "cited_by");
			QueryResponse response = solr.query(q);
			if(response != null) {
				SolrDocumentList results = response.getResults();
				if(results.size() == 0) continue;
				if(results.get(0).get("cited_by") == null) continue;
				if(results.size() > 0) {
					addMeshheadingsFromDocs(meshHeadings, new ArrayList<Object>(results.get(0).getFieldValues("cited_by")));
				}
			}
		}
		
		return meshHeadings;
	}

	private void addMeshheadingsFromDocs(Map<String, Integer> meshHeadings, List<Object> pmids) {
		String queryString = "";
		for(int i = 0; i < pmids.size(); i++) {
			queryString += "pmid:" + pmids.get(i).toString();
			if(i != pmids.size() - 1) queryString += " OR ";
		}
		SolrQuery q = new SolrQuery();
		q.set("q", queryString);
		q.set("fl", "meshheading,meshheading_minor");
		q.set("rows", 500);
		
		QueryResponse response = solr.query(q);
		if(response != null) {
			SolrDocumentList results = response.getResults();
			if(results != null) {
				for(SolrDocument result : results) {
					if(result.containsKey("meshheading")) {
						for(Object meshheading : result.getFieldValues("meshheading")) {
							if(meshHeadings.containsKey(meshheading)) {
								meshHeadings.put(meshheading.toString(), meshHeadings.get(meshheading) + 1);
							} else {
								meshHeadings.put(meshheading.toString(), 1);
							}
						}
					}
					if(result.containsKey("meshheading_minor")) {
						for(Object meshheading : result.getFieldValues("meshheading_minor")) {
							if(meshHeadings.containsKey(meshheading)) {
								meshHeadings.put(meshheading.toString(), meshHeadings.get(meshheading) + 1);
							} else {
								meshHeadings.put(meshheading.toString(), 1);
							}
						}
					}
				}
			}
		}
	}

	private class PMIDandCount implements Comparable<PMIDandCount> {

		private String pmid;
		private int count;
		
		public PMIDandCount(String pmid, int count) {
			this.pmid = pmid;
			this.count = count;
		}
		
		@Override
		public int compareTo(PMIDandCount another) {
			return Integer.compare(this.count, another.count);
		}
		
	}
	
}
