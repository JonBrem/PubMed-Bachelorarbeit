package de.ur.jonbrem.pubmed.test.machine_learning.own_learning;

import java.util.ArrayList;
import java.util.List;

public class FeatureList {
		
	public List<Feature> features;
	private boolean multiplyToo;
	
	public FeatureList() {
		this.features = new ArrayList<Feature>();
	}

	public Feature getFeature(int i) {
		return features.get(i);
	}

	public int size() {
		return features.size();
	}
	
}