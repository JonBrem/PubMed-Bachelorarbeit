package de.ur.jonbrem.pubmed.test.machine_learning.own_learning;

import java.util.HashMap;
import java.util.Map;

public class FeatureValues {

	private Map<Integer, Map<String, Double>> values;
	private String featureName;
	private int currentFileHeaderIndex;
	
	private double mean;
	private int total;
	private boolean multiply;
	private boolean noIndex;
	
	public FeatureValues(String featureName, boolean multiply) {
		this.values = new HashMap<>();
		this.featureName = featureName;
		this.mean = 0;
		this.total = 0;
		this.multiply = multiply;
	}
	
	public void findIndex(String[] headers, int trecId) {
		boolean found = false;
		currentFileHeaderIndex = 0;
		for(String name : headers) {
			if(featureName.equals(name)) {
				noIndex = false;
				found = true;
				break;
			}
			currentFileHeaderIndex++;
		}
		if(!found) {
			noIndex = true;
		}
				
		values.put(trecId, new HashMap<>());
	}
	
	public boolean addFromFileLine(String[] lineParts, int trecId) {
		if(noIndex) return false;
		if(lineParts.length > currentFileHeaderIndex) {
			double value = Double.parseDouble(lineParts[currentFileHeaderIndex]);
			if(value > 0) {
				mean += value;
				total++;
				values.get(trecId).put(lineParts[0], value);
				return true;
			}
		}
		return false;
	}
	
	public double getMean() {
		if(total == 0) return 0;
		return mean / total;
	}
	
	public Map<String, Double> getValuesFor(int trecId) {
		return values.get(trecId);
	}

	public String getName() {
		return featureName;
	}
	
	public boolean isMultiplier() {
		return this.multiply;
	}
	
}
