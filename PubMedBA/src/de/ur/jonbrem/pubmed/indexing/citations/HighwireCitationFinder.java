package de.ur.jonbrem.pubmed.indexing.citations;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Finds citation IDs in a String.
 */
public class HighwireCitationFinder {

	private static final Pattern p = Pattern.compile("&resid=[0-9a-zA-Z/]+\"");
	
	public static List<String> findIDs(String fullText) {
		List<String> highwireIDs = new ArrayList<>();
		
		Matcher m = p.matcher(fullText);
		while(m.find()) {
			String match = m.group();
			String id = match.substring(match.indexOf("=") + 1);
			id = id.substring(0, id.length() - 1);
			highwireIDs.add(id);
		}
		
		return highwireIDs;
	}
	
	
}
