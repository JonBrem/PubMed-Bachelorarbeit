package de.ur.jonbrem.pubmed.test.machine_learning.own_learning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Score {
	List<Feature> features;
	
	Map<Feature, List<Double>> powers;
	
	public List<FeatureWeightCombination> combinations;
	
	public Score(Feature feature) {
		this.features = new ArrayList<>();
		this.combinations = new ArrayList<>();
		powers = new HashMap<>();
		features.add(feature);
	}
	
	public Score(Score score, Feature feature) {
		this(feature);
		features.addAll(score.features);
	}
	
	public void assignPowers(Feature feature, List<Double> powers) {
		this.powers.put(feature, powers);
	}

	public void setCombinations(List<FeatureWeightCombination> combinations) {
		this.combinations = combinations;
	}
	
	public List<FeatureWeightCombination> getCombinations() {
		return this.combinations;
	}
	
	public FeatureWeightCombination getBest() {
		if(this.combinations.size() == 0) return null;
		
		FeatureWeightCombination best = this.combinations.get(0);
		double bestScore = best.calculateScore();
		for(int i = 0; i < this.combinations.size(); i++) {
			double score = this.combinations.get(i).calculateScore();
			
			if(score > bestScore) {
				bestScore = score;
				best = this.combinations.get(i);
			}
		}
		
		return best;
	}

}