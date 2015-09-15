package de.ur.jonbrem.pubmed.test;

public class TrecQuery {

	private int id;
	private int oldId;
	
	private String topic;
	private String gene;
	private String otherConcept;
	
	private String[] meshTerms;
	
	private int trecYear;
	
	public TrecQuery() {
		
	}
	
	public TrecQuery(int id, int oldId, String gene, String otherConcept, String topic, String... meshTerms) {
		this.id = id;
		this.oldId = oldId;
		this.gene = gene;
		this.otherConcept = otherConcept;
		this.topic = topic;
		this.meshTerms = meshTerms;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOldId() {
		return oldId;
	}

	public void setOldId(int oldId) {
		this.oldId = oldId;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getGene() {
		return gene;
	}

	public void setGene(String gene) {
		this.gene = gene;
	}

	public String getOtherConcept() {
		return otherConcept;
	}

	public void setOtherConcept(String otherConcept) {
		this.otherConcept = otherConcept;
	}

	public int getTrecYear() {
		return trecYear;
	}

	public void setTrecYear(int trecYear) {
		this.trecYear = trecYear;
	}
	
	public String[] getMeshTerms() {
		return this.meshTerms;
	}
	
	
}
