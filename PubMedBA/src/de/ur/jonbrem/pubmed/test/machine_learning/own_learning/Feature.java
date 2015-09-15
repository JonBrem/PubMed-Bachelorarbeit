package de.ur.jonbrem.pubmed.test.machine_learning.own_learning;

public class Feature {
	private String name;
	private double mean;
	
	private boolean multiplier;
	
	public Feature(String name) {
		this.name = name;
		this.multiplier = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public void setIsMultiplier(boolean b) {
		this.multiplier = b;
	}
	
	public boolean isMultiplier() {
		return this.multiplier;
	}
}