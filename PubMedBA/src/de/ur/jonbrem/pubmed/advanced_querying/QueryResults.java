package de.ur.jonbrem.pubmed.advanced_querying;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The results of an advanced Query.
 * (All scores for all fields for the retrieved docs)
 */
public class QueryResults {

	protected Map<String, Map<String, Object>> results;
	
	private List<String> queryStrings;
	
	public QueryResults() {
		results = new HashMap<>();
		queryStrings = new ArrayList<>();
	}
	
	/**
	 * @param queryStrings
	 * The results will have fields for all of these strings.
	 */
	public void setQueryString(List<String> queryStrings) {
		this.queryStrings = queryStrings;
	}
	
	/**
	 * @param queryString
	 * The results will have a field for this string.
	 */
	public void addQueryString(String queryString) {
		this.queryStrings.add(queryString);
	}
	
	/**
	 * <strong>! You might be looking for "add" !</strong>
	 * <br>put stores the value, overriding any other value. Can be any Type.
	 * <br>add stores the value, adding it to any pre-existing value. Can only be Float or Double.
	 * @param pmid PMID of a doc in the results (does not need to be there already)
	 * @param field Which field score to put the <em>value</em> in.
	 * @param value
	 */
	public void put(String pmid, String field, Object value) {		
		if(results.containsKey(pmid)) {
			results.get(pmid).put(field, value);
		} else {
			createDoc(pmid, field, value);			
		}
	}
	
	/**
	 * <strong>! You might be looking for "put" !</strong>
	 * <br>put stores the value, overriding any other value. Can be any Type.
	 * <br>add stores the value, adding it to any pre-existing value. Can only be Float or Double.
	 * @param pmid PMID of a doc in the results (does not need to be in there already)
	 * @param field Which field score to add the <em>value</em> to.
	 * @param value The value (the score of a certain field)
	 */
	public void add(String pmid, String field, Object value) {
		if(results.containsKey(pmid)) {
			Object val1 = results.get(pmid).get(field);
			
			if(val1 instanceof Double) {
				if(value instanceof Double) 
					 	results.get(pmid).put(field, (Double) results.get(pmid).get(field) + (Double) value);
				else	results.get(pmid).put(field, (Float) results.get(pmid).get(field) + (Float) value);
			} else {
				if(value instanceof Double) 
						results.get(pmid).put(field, (Float) results.get(pmid).get(field) + (Double) value);
				else  	results.get(pmid).put(field, (Float) results.get(pmid).get(field) + (Float) value);
			}
			
		} else {
			createDoc(pmid, field, value);			
		}
	}

	protected void createDoc(String pmid, String field, Object value) {
		Map<String, Object> doc = new HashMap<>();
		for(String s : queryStrings) {
			doc.put(s, 0.0f);
		}
		doc.put(field, value);
		results.put(pmid, doc);
	}
	
	/**
	 * @return
	 * A list of all the PMIDs; the individual docs can be retrieved with <strong>getDoc</strong>.
	 */
	public Set<String> getResultPMIDs() {
		return results.keySet();
	}
	
	/**
	 * @param pmid a doc PMID (probably from <strong>getResultPMIDS</strong>)
	 * @return all values for the objects in a HashMap
	 */
	public Map<String, Object> getDoc(String pmid) {
		return results.get(pmid);
	}
}
