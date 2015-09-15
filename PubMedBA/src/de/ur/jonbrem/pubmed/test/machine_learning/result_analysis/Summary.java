package de.ur.jonbrem.pubmed.test.machine_learning.result_analysis;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import util.FileUtil;

public class Summary {
	
	// var_01_simple_train_01_050.txt
	
	private static String var = "var_01";
	
	public static void main(String... args) {
		File folder = new File("files/final_tests_map_new");
		Map<String, Map<String, Double>> values = new HashMap<>();
		
		FileUtil fileUtil = new FileUtil();
		
		for(File file : folder.listFiles()) {
			String fileName = file.getName();
			if(!fileName.contains(var)) continue;
			
			String[] split = fileName.split("_");
			String trainingSet = split[split.length - 2];
			String x = split[split.length - 1];
			
			String firstLine = fileUtil.readLine(file.getAbsolutePath());
			
			if(!values.containsKey(x)) {
				values.put(x, new HashMap<>());
			}
			values.get(x).put(trainingSet, readF2Score(firstLine));
		}
		
		for(String x : values.keySet()) {
			Map<String, Double> trainingSets = values.get(x);
			double min = Double.POSITIVE_INFINITY;
			double max = Double.NEGATIVE_INFINITY;
			double avg = 0;
			
			for(String trainingSet : trainingSets.keySet()) {
				double value = trainingSets.get(trainingSet);
				if(value < min) min = value;
				if(value > max) max = value;
				avg += value;
			}
			avg /= trainingSets.size();
			System.out.println(x + "\t" + roundAndComma(min) + "\t" + roundAndComma(max) + "\t" + roundAndComma(avg));
		}
		
		fileUtil.closeAll();
	}
	
	private static String roundAndComma(double value) {
		String asString = value + "";
		asString = asString.replaceAll("\\.", ",");
		asString = asString.substring(0, asString.length() > 8? 8 : asString.length());
		return asString;
	}

	private static Double readF2Score(String firstLine) {
		String[] split = firstLine.split("; ");
		String number = split[split.length - 1].substring(split[split.length - 1].indexOf(":") + 2);
		return Double.parseDouble(number);
	}
	
}
