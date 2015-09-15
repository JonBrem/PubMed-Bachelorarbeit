package de.ur.jonbrem.pubmed.test.machine_learning.result_analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.FileUtil;

public class LangAnalysisSummary {

	private static Pattern TITLE_PATTERN = Pattern.compile("pubmed_language_(worddelimiter|simple|remove_nonnumeric|stemming)_rank_(tfidf|lm_[1-4]|bm25_[1-5])_title");
	private static Pattern ABSTRACT_PATTERN = Pattern.compile("pubmed_language_(worddelimiter|simple|remove_nonnumeric|stemming)_rank_(tfidf|lm_[1-4]|bm25_[1-5])_abstract");

	public static void main(String[] args) {
		FileUtil fileUtil = new FileUtil();
		Map<String, Map<String, Map<String, Map<String, Map<String, Double>>>>> values = new HashMap<>();
		//  abstr/title,lang,		rank,		title/abstr,ml		value		
		
		for(String fileName : new String[] {"var_02_language_01_020.txt",
				"var_02_language_02_020.txt",
				"var_02_language_03_020.txt",
				"var_02_language_04_020.txt"}) {
			putValuesInMap("files/final_tests_map/" + fileName, values, fileUtil);			
		}		
		
		printValues(values, fileUtil,
				"files/final_tests_map/lang_analysis_summary.txt");
		fileUtil.closeAll();
	}

	private static void putValuesInMap(String fileName,
			Map<String, Map<String, Map<String, Map<String, Map<String, Double>>>>> values,
			FileUtil fileUtil) {

		String line;
		while((line = fileUtil.readLine(fileName)) != null) {
			if(line.length() == 0) break;
			String title = getTitle(line);
			String abstr = getAbstract(line);
			Double score = getScore(line);
			
			String[] abstrParts = getParts(abstr);
			String[] titleParts = getParts(title);
			
			putValueInMap("title", titleParts, abstr, score, fileName, values);
			putValueInMap("abstract", abstrParts, title, score, fileName, values);
		}		
	}

	private static void printValues(Map<String, Map<String, Map<String, Map<String, Map<String, Double>>>>> values, FileUtil fileUtil,
			String outputFile) {
		for(String abstrOrTitle : values.keySet()) {
			for(String indexing : values.get(abstrOrTitle).keySet()) {
				for(String ranking : values.get(abstrOrTitle).get(indexing).keySet()) {
					String bestField = "";
					double bestValue = Double.NEGATIVE_INFINITY;
					for(String otherField : values.get(abstrOrTitle).get(indexing).get(ranking).keySet()) {
						double avg = 0;
						for(String testSet : values.get(abstrOrTitle).get(indexing).get(ranking).get(otherField).keySet()) {
							avg += values.get(abstrOrTitle).get(indexing).get(ranking).get(otherField).get(testSet);
						}
						avg /= values.get(abstrOrTitle).get(indexing).get(ranking).get(otherField).size();
						
						if(bestValue < avg) {
							bestField = otherField;
							bestValue = avg;
						}
					} // otherField

					fileUtil.writeLine(outputFile, 
							abstrOrTitle + "\t" + indexing + "\t" + ranking + "\t" + bestField + "\t" + bestValue);
					
				} // ranking
			} // indexing

			fileUtil.writeLine(outputFile, "");
		} // abstrOrTitle
	}

	private static void putValueInMap(String abstrOrTitle, String[] parts, String otherField, Double score, String fileName, 
			Map<String, Map<String, Map<String, Map<String, Map<String, Double>>>>> values) {
		if(!values.containsKey(abstrOrTitle)) {
			values.put(abstrOrTitle, new HashMap<>());
		}
		
		Map<String, Map<String, Map<String, Map<String, Double>>>> abstrOrTitleMap = values.get(abstrOrTitle);
		
		if(!abstrOrTitleMap.containsKey(parts[0])) {
			abstrOrTitleMap.put(parts[0], new HashMap<>());
		}
		
		Map<String, Map<String, Map<String, Double>>> rankMap = abstrOrTitleMap.get(parts[0]);
		
		if(!rankMap.containsKey(parts[1])) {
			rankMap.put(parts[1], new HashMap<>());
		}
		
		Map<String, Map<String, Double>> otherFieldMap = rankMap.get(parts[1]);
		
		if(!otherFieldMap.containsKey(otherField)) {
			otherFieldMap.put(otherField, new HashMap<>());
		}
		
		Map<String, Double> valueForFileMap = otherFieldMap.get(otherField);
		
		valueForFileMap.put(fileName, score);
	}

	private static Double getScore(String line) {
		String[] split = line.split("; ");
		String number = split[split.length - 1].substring(split[split.length - 1].indexOf(":") + 2);
		return Double.parseDouble(number);		
	}

	private static String getAbstract(String line) { 
		Matcher m = ABSTRACT_PATTERN.matcher(line);
		m.find();		
		return m.group();
	}

	private static String getTitle(String line) {
		Matcher m = TITLE_PATTERN.matcher(line);
		m.find();		
		return m.group();
	}

	private static String[] getParts(String abstr) {
		String languagePart = abstr.substring(abstr.indexOf("language") + "language_".length(), abstr.indexOf("_rank"));
		String rankPart = abstr.substring(abstr.indexOf("rank_") + "rank_".length(), abstr.lastIndexOf("_"));
		
		return new String[]{languagePart, rankPart};
	}
	
}
