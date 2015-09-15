package de.ur.jonbrem.pubmed.indexing;

/**
 * Simple model class containing two field names for Solr documents.
 */
public class CopyField {
	
	private String fieldToCopy;
	private String fieldToCopyTo;
	
	/**
	 * @param fieldToCopy The value of this field will be in the other field (and also remain in this field!).
	 * @param fieldToCopyTo The value of this field will be the value of the other field.
	 */
	public CopyField(String fieldToCopy, String fieldToCopyTo) {
		this.fieldToCopy = fieldToCopy;
		this.fieldToCopyTo = fieldToCopyTo;
	}

	public String getFieldToCopy() {
		return fieldToCopy;
	}

	public void setFieldToCopy(String fieldToCopy) {
		this.fieldToCopy = fieldToCopy;
	}

	public String getFieldToCopyTo() {
		return fieldToCopyTo;
	}

	public void setFieldToCopyTo(String fieldToCopyTo) {
		this.fieldToCopyTo = fieldToCopyTo;
	}
	
	
}