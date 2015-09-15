package de.ur.jonbrem.pubmed.test.machine_learning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.ur.jonbrem.pubmed.advanced_querying.QueryResults;
import util.FileUtil;

public class MLTestResults extends QueryResults {
	
	private double[] weightsForTopics;
	
	public void setResults(QueryResults results) {
		for(String pmid : results.getResultPMIDs()) {
			this.results.put(pmid, results.getDoc(pmid));
		}
	}
	
	public void writeResultsAsCSV(FileUtil fileUtil, String file, boolean header) {
		
		boolean first = true;
		
		for(String pmid : results.keySet()) {
			Map<String, Object> doc = results.get(pmid);
			
			List<String> keys = new ArrayList<String>(doc.keySet());
			Collections.sort(keys);
			
			if(header && first) {	
				String headerString = "";
				headerString += "pmid,relevant,";
				for(String field : keys) {
					if(field.equals("pmid") || field.equals("relevant")) continue;
					headerString += field + ",";
				}
				headerString = headerString.substring(0, headerString.length() - 1);
				first = false;
				fileUtil.writeLine(file, headerString);
			}
			
			fileUtil.write(file, pmid + "," + doc.get("relevant") + ",");
			for(int i = 0; i < keys.size(); i++) {
				String field = keys.get(i);
				if(field.equals("pmid") || field.equals("relevant")) continue;
				fileUtil.write(file, doc.get(field).toString());
				if(i != keys.size() - 1) {
					fileUtil.write(file, ",");
				}
			}
			
			fileUtil.write(file, "\n");
		}
	}

	public void readFromCSV(FileUtil fileUtil, String file) {
		String s = fileUtil.readLine(file);
		String[] headings = s.split(",");
		while((s = fileUtil.readLine(file)) != null && s.length() > 0) {
			String[] values = s.split(",");
			String pmid = values[0];
			for(int i = 1; i < headings.length; i++) {
				put(pmid, headings[i], values[i]);
			}
		}
	}
	
	public void readFromMultipleFiles(FileUtil fileUtil, String[] files) {
		weightsForTopics = new double[files.length];
		for(int fileIndex = 0; fileIndex < files.length; fileIndex++) {
			String file = files[fileIndex];
			String s = fileUtil.readLine(file);
			String[] headings = s.split(",");
			
			List<String> pmidsToUpdate = new ArrayList<>();
			int linesInFile = 0;
			
			while((s = fileUtil.readLine(file)) != null && s.length() > 0) {
				String[] values = s.split(",");
				String pmid = file + ":" + values[0];
				pmidsToUpdate.add(pmid);
				for(int i = 1; i < headings.length; i++) {
					put(pmid, headings[i], values[i]);
				}
				linesInFile++;
			}
			weightsForTopics[fileIndex] = 1 / (double) linesInFile;
		
			
			for(String pmid : pmidsToUpdate) {
				getDoc(pmid).put("weight_for_topic", weightsForTopics[fileIndex]);
			}
		}
	}
	
}
