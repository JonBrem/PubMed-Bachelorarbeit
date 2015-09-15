package de.ur.jonbrem.pubmed.test.machine_learning.own_learning;

import java.util.ArrayList;
import java.util.List;

public class FeatureWeightCombination {
	private List<Feature> features;
	private List<Double> weights;
	
	private List<Double> partialScores;
	
	public FeatureWeightCombination(Feature feature, Double weight) {
		this.features = new ArrayList<>();
		this.weights = new ArrayList<>();
		this.partialScores = new ArrayList<>();
		
		features.add(feature);
		weights.add(weight);
	}
	
	public FeatureWeightCombination(FeatureWeightCombination existing, Feature feature, Double weight) {
		this(feature, weight);
		if(existing != null) {
			features.addAll(existing.features);
			weights.addAll(existing.weights);
		}
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	public List<Double> getWeights() {
		return weights;
	}

	public void setWeights(List<Double> weights) {
		this.weights = weights;
	}
	
	public double calculateScore() {
		double score = 0;
		
		for(int i = 0; i < partialScores.size(); i++) {
			score += partialScores.get(i);
		}
		
		score = score / partialScores.size();
		
		return score;
	}

	public void addPartialScore(double score) {
		this.partialScores.add(score);
	}
}