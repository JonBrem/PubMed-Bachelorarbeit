package de.ur.jonbrem.pubmed.advanced_querying;

/**
 * Simple model class. Stores the scores for documents.
 * Can be sorted.
 */
public class DocScore implements Comparable<DocScore> {

	private double score;
	private String pmid;
	
	public DocScore(String pmid) {
		this.pmid = pmid;
		score = 0;
	}
	
	public void addToScore(double d) {
		this.score += d;
	}
	
	public double getScore() {
		return score;
	}
	
	public String getPmid() {
		return pmid;
	}
	
	@Override
	public int compareTo(DocScore another) {
		return Double.compare(score, another.getScore());
	}
	
}