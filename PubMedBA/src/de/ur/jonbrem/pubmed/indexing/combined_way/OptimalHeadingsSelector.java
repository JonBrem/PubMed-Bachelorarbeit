package de.ur.jonbrem.pubmed.indexing.combined_way;

import util.FileUtil;

public class OptimalHeadingsSelector {

	private static final double RANK_THRESHOLDS= 0.01;
	
	public static void main(String... args) {
		new OptimalHeadingsSelector("files/optimal_headings_selector.txt", "files/optimal_headings4.txt").run();
	}

	private String outputFile;
	private FileUtil fileUtil;
	private String inputFile;
	
	public OptimalHeadingsSelector(String outputFile, String inputFile) {
		this.outputFile = outputFile;
		this.inputFile = inputFile;
		this.fileUtil = new FileUtil();
	}
	
	
	public void run() {
		readRanks();
		fileUtil.closeAll();
	}

	private void readRanks() {
		String s;
		while((s = fileUtil.readLine(inputFile)) != null) {
			if(!s.startsWith("\t")) {
				fileUtil.writeLine(outputFile, s);
				continue;
			}
			String valuePart = s.substring(s.indexOf("\t")).split(";\t")[1];
			if(valuePart.length() == 0) continue;
			if(analyzeValues(valuePart.split(" "))) {
				fileUtil.writeLine(outputFile, s);
			}
		}
	}

	private boolean analyzeValues(String[] values) {
		int total = getTotalCitations(values);
		
		int relevant = 0;
		int irrelevant = 0;
		
		for(String value : values) {
			int relevantCount = getValuePart("relevant", value);
			int irrelevantCount = getValuePart("irrelevant", value);
			int numberOfCitations = getValuePart("count", value);
			
			if((numberOfCitations / (double) total) > RANK_THRESHOLDS) {
				relevant += relevantCount;
				irrelevant += irrelevantCount;
			}
		}
				
		return relevant > irrelevant;
	}

	private int getTotalCitations(String[] values) {
		int total = 0;
		for(String value : values) {
			int count = getValuePart("count", value);
			total += count * getValuePart("relevant", value) + count * getValuePart("irrelevant", value);
		}
		return total;
	}

	private int getValuePart(String str, String value) {
		value = value.substring(value.indexOf(str));
		value = value.substring(value.indexOf(":") + 1);
		if(value.contains(",")) {
			value = value.substring(0, value.indexOf(","));
		}
		return Integer.parseInt(value);
	}
	
}
