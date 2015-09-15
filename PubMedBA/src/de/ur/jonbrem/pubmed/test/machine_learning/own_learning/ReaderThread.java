package de.ur.jonbrem.pubmed.test.machine_learning.own_learning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.FileUtil;

class ReaderThread implements Runnable {
	
	private static final Set<String> NOT_ONLY_THIS = new HashSet<String>(Arrays.asList("citation_rank", "combined_score_1",
			"combined_score_2", "combined_score_3"));
	
	/**
	 * 
	 */
	private FastOptimization fastOptimization;
	private List<List<Feature>> features;
	private FastOptimization parent;
	private String resultsFolder;

	public ReaderThread(FastOptimization fastOptimization, List<List<Feature>> features, FastOptimization parent, String resultsFolder) {
		this.fastOptimization = fastOptimization;
		this.features = features;
		this.parent = parent;
		this.resultsFolder = resultsFolder;
	}
	
	@Override
	public void run() {
		List<Feature> featuresInSingleList = new ArrayList<>();
		for(List<Feature> subList : features) {
			for(Feature feature : subList) {
				featuresInSingleList.add(feature);
			}
		}
		
		List<FeatureValues> featureValues = this.getFeatureValues(featuresInSingleList, new FileUtil());
		
		List<List<FeatureValues>> featureValuesForCalculatorThreads = new ArrayList<>();
		
		for(int i = 0; i < features.size(); i++) {
			List<FeatureValues> val = new ArrayList<>();
			List<Feature> subList = features.get(i);
			for(int j = 0; j < subList.size(); j++) {
				for(FeatureValues values : featureValues) {
					if(subList.get(j).getName().equals(values.getName())) {
						val.add(values);
					}
				}
			}
			
			featureValuesForCalculatorThreads.add(val);
		}
		
		parent.onReadingFinished(featureValuesForCalculatorThreads);
	}
	
	private List<FeatureValues> getFeatureValues(List<Feature> features, FileUtil fileUtil) {
		List<FeatureValues> featureValues = new ArrayList<>();
		for(int i = 0; i < features.size(); i++) {
			featureValues.add(new FeatureValues(features.get(i).getName(), features.get(i).isMultiplier()));
		}
		
		for(int trecId : FastOptimization.TREC_QUESTION_IDS) {
			String s;
			String fileName = "files/" + this.resultsFolder + "/results_" + trecId + ".csv";
			s = fileUtil.readLine(fileName);
			String[] headers;
			if(s == null) {
				headers = new String[0];
			} else {
				headers = s.split(",");
			}
			for(FeatureValues featureValue : featureValues) {
				featureValue.findIndex(headers, trecId);
			}
			headers = null;
			while((s = fileUtil.readLine(fileName)) != null) {
				String[] lineParts = s.split(",");
				for(FeatureValues featureValue : featureValues) {
					featureValue.addFromFileLine(lineParts, trecId);
				}
			}
		}
		
		// remove citation rank values from stuff that is uniquely there (because that would not be used otherwhise)
		for(int i = 0; i < features.size(); i++) {			
			if(!NOT_ONLY_THIS.contains(features.get(i).getName())) continue;
			for(int trecId : FastOptimization.TREC_QUESTION_IDS) {
				List<String> toRemove = new ArrayList<>();
				for(String pmid : featureValues.get(i).getValuesFor(trecId).keySet()) {
					boolean isOk = false;
					for(int j = 0; j < features.size(); j++) {
						if(NOT_ONLY_THIS.contains(features.get(j).getName())) continue;
						if(featureValues.get(j).getValuesFor(trecId).containsKey(pmid)) {
							isOk = true;
							break;
						}
					}
					if(!isOk) toRemove.add(pmid);
				}
				for(String pmid : toRemove) featureValues.get(i).getValuesFor(trecId).remove(pmid);
			}
		}
		
		return featureValues;
	}
	
	
	
}