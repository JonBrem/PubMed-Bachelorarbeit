package de.ur.jonbrem.pubmed.advanced_querying;

/** model class that only stores a name and a weight. */
public class Boost {
	
	private String name;
	private float weight;

	public Boost(String name, float weight) {
		this.name = name;
		this.weight = weight;
	}
	
	public String getName() {
		return name;
	}
	
	public float getWeight() {
		return weight;
	}
	
}