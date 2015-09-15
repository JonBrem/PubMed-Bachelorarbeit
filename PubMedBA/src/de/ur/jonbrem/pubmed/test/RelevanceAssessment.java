package de.ur.jonbrem.pubmed.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.FileUtil;

public class RelevanceAssessment {

	private static final String RELEVANCE_FILE_2006 = "files/final.goldstd.tsv.txt";
	
	private Map<String, Set<String>> relevancies;
	private FileUtil fileUtil;
	
	public RelevanceAssessment() {
		this.relevancies = new HashMap<>();
		this.fileUtil = new FileUtil();
	}
	
	public RelevanceAssessment loadAssessments() {
		String s = null;
		String currentTopic = null;
		List<String> currentIDs = new ArrayList<>();
		while((s = fileUtil.readLine(RELEVANCE_FILE_2006)) != null) {
			String[] lineContents = s.split("\t");
			String topicID = lineContents[0];
			String pmid = lineContents[1];
			
			if(currentTopic != null && !currentTopic.equals(topicID)) {
				relevancies.put(currentTopic, new HashSet<>(currentIDs));
				currentIDs = new ArrayList<>();
			}
			
			currentTopic = topicID;
			currentIDs.add(pmid);
		}
		
		relevancies.put(currentTopic, new HashSet<>(currentIDs));
		fileUtil.closeReader(RELEVANCE_FILE_2006);
		return this;
	}
	
	public boolean isRelevant(String topic, String pmid) throws Exception {
		if(relevancies.containsKey(topic)) {
			return relevancies.get(topic).contains(pmid);
		} else {
			Exception e = new Exception("Invalid topic");
			throw e;
		}
	}	
	
	public int getNumRelevant(String topic) throws Exception {
		if(relevancies.containsKey(topic)) {
			return relevancies.get(topic).size();
		} else {
			Exception e = new Exception("Invalid topic");
			throw e;
		}
	}
	
	public List<String> getRelevantPmids(String topic) throws Exception {
		if(relevancies.containsKey(topic)) {
			return new ArrayList<>(relevancies.get(topic));
		} else {
			throw new Exception("Invalid topic");
		}
	}
}
