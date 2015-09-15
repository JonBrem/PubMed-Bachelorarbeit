package de.ur.jonbrem.pubmed.indexing.combined_way;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.ur.jonbrem.pubmed.test.RelevanceAssessment;
import util.FileUtil;

public class OptimalHeadingsFileAnalyzer {

	private static final double[] RANK_THRESHOLDS = {0, 0.00001, 0.00005, 0.0001, 0.0005, 0.001, 0.005, 0.01, 0.05, 0.1, 0.5, 1};
	
	public static void main(String... args) {
		new OptimalHeadingsFileAnalyzer("files/optimal_headings_analysis.txt", "files/optimal_headings4.txt").run();
	}

	private String outputFile;
	private FileUtil fileUtil;
	private String inputFile;
	
	public OptimalHeadingsFileAnalyzer(String outputFile, String inputFile) {
		this.outputFile = outputFile;
		this.inputFile = inputFile;
		this.fileUtil = new FileUtil();
	}
	
	
	public void run() {
		List<Double> ranksForRelevantDocs = new ArrayList<>();
		List<Double> ranksForIrrelevantDocs = new ArrayList<>();
		
		readRanks(ranksForRelevantDocs, ranksForIrrelevantDocs);

		Collections.sort(ranksForRelevantDocs);
		Collections.sort(ranksForIrrelevantDocs);
		
		writeSummaries(ranksForRelevantDocs, ranksForIrrelevantDocs);
		fileUtil.writeLine(outputFile, "");
		writeValues(ranksForRelevantDocs, ranksForIrrelevantDocs);
		fileUtil.flushWriter(outputFile);
		fileUtil.closeAll();
	}

	private void readRanks(List<Double> ranksForRelevantDocs, List<Double> ranksForIrrelevantDocs) {
		String s;
		while((s = fileUtil.readLine(inputFile)) != null) {
			if(!s.startsWith("\t")) continue;
			String valuePart = s.substring(s.indexOf("\t")).split(";\t")[1];
			if(valuePart.length() == 0) continue;
			analyzeValues(valuePart.split(" "), ranksForRelevantDocs, ranksForIrrelevantDocs);
		}
	}

	private void analyzeValues(String[] values, List<Double> ranksForRelevantDocs, List<Double> ranksForIrrelevantDocs) {
		int total = getTotalCitations(values);
		for(String value : values) {
			int relevantCount = getValuePart("relevant", value);
			int irrelevantCount = getValuePart("irrelevant", value);
			int numberOfCitations = getValuePart("count", value);
			
			for(int i = 0; i < relevantCount; i++) ranksForRelevantDocs.add(numberOfCitations / (double) total);
			for(int i = 0; i < irrelevantCount; i++) ranksForIrrelevantDocs.add(numberOfCitations / (double) total);
		}
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

	private void writeSummaries(List<Double> ranksForRelevantDocs, List<Double> ranksForIrrelevantDocs) {
		writeSummary("relevant", ranksForRelevantDocs);
		writeSummary("irrelevant", ranksForIrrelevantDocs);
	}

	private void writeSummary(String which, List<Double> ranksForDocs) {
		for(double d : new double[]{0, 0.001, 0.005, 0.01, 0.05, 0.1, 0.25, 0.5, 0.75, 0.9, 0.95, 0.99, 0.995, 0.999, 1}) {
			int index = (int) (ranksForDocs.size() * d);
			while(index == ranksForDocs.size()) index--;
			
			fileUtil.writeLine(outputFile, which + "\t" + "Quantile:" + d + ", value:" + ranksForDocs.get(index));
		}
	}


	private void writeValues(List<Double> ranksForRelevantDocs, List<Double> ranksForIrrelevantDocs) {
		writeValuesForDocs("relevant", ranksForRelevantDocs);
		writeValuesForDocs("irrelevant", ranksForIrrelevantDocs);
	}


	private void writeValuesForDocs(String which, List<Double> ranksForDocs) {
		for(int i = 0; i < RANK_THRESHOLDS.length - 1; i++) {
			int count = getCountBetween(RANK_THRESHOLDS[i], RANK_THRESHOLDS[i + 1], ranksForDocs);
			fileUtil.writeLine(outputFile, which + "\t" + RANK_THRESHOLDS[i] + " - " + RANK_THRESHOLDS[i + 1] + "\t" + count + "\t" + (count / (double) ranksForDocs.size()));
		}
	}


	private int getCountBetween(double min, double max, List<Double> ranksForDocs) {
		int count = 0;
		for(double d : ranksForDocs) {
			if(d >= min && d < max) count++;
		}
		return count;
	}
	
}
