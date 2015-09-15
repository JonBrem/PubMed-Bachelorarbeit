package de.ur.jonbrem.pubmed.test.machine_learning.result_analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.FileUtil;

public class ResultAnalysis {

	public static void main(String[] args) {
		new ResultAnalysis().run();
	}
	
	private FileUtil fileUtil;
	
	private Map<String, List<Double>> titlePartsScores;
	private Map<String, List<Double>> abstractPartsScores;
	
	public ResultAnalysis() {
		this.fileUtil = new FileUtil();
		titlePartsScores = new HashMap<>();
		abstractPartsScores = new HashMap<>();
	}
	
	public void run() {
		loadDataIntoMaps();
		printResults(titlePartsScores, "title");
		printResults(abstractPartsScores, "abstract");
	}

	private void printResults(Map<String, List<Double>> scores, String name) {
		for(String key : scores.keySet()) {
			System.out.println(name + "_" + key + "\t" + getAverage(scores.get(key)));
		}
	}
	
	private double getAverage(List<Double> values) {
		double total = 0;
		for(double d : values) total += d;
		return total / values.size();
	}

	private void loadDataIntoMaps() {
		String s;
		while((s = fileUtil.readLine("files/final_tests/var_04_language_analysis_100.txt")) != null) {
			if(!s.startsWith("Highest")) continue;
			
			String[] parts = s.split(" ");

			String titlePart = null;
			String abstractPart = null;
			
			for(String part : parts) {
				if(part.startsWith("pubmed")) {
					if(part.endsWith("abstract,")) {
						abstractPart = part;
					} else {
						titlePart = part;
					}
				}
			}
			
			double score = Double.parseDouble(parts[parts.length - 1]);
			addScoreToParts(titlePart, abstractPart, score);
			
		}
	}
	
	private void addScoreToParts(String titlePart, String abstractPart, double score) {
		String[] titleSubparts = getSubParts(titlePart);
		String[] abstractSubparts = getSubParts(abstractPart);
		
		addScore(titlePartsScores, titleSubparts[0], score);
		addScore(titlePartsScores, titleSubparts[1], score);
		addScore(abstractPartsScores, abstractSubparts[0], score);
		addScore(abstractPartsScores, abstractSubparts[1], score);
	}
	
	private String[] getSubParts(String part) {
		String[] subParts = new String[2];
		
		part = part.substring(16);
		subParts[0] = part.substring(0, part.indexOf("_rank_"));
		part = part.substring(subParts[0].length() + "_rank_".length());
		subParts[1] = part.substring(0, part.lastIndexOf("_"));
		
		return subParts;
	}

	private void addScore(Map<String, List<Double>> scores, String part, double score) {
		if(scores.containsKey(part)) {
			scores.get(part).add(score);
		} else {
			List<Double> scoreValues = new ArrayList<>();
			scoreValues.add(score);
			scores.put(part, scoreValues);
		}		
	}
	
}
