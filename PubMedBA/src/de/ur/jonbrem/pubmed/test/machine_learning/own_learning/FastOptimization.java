package de.ur.jonbrem.pubmed.test.machine_learning.own_learning;

import java.util.ArrayList;
import java.util.List;

import de.ur.jonbrem.pubmed.test.RelevanceAssessment;
import util.Const;
import util.FileUtil;

public class FastOptimization {
		
	public static final int[] TREC_QUESTION_IDS = { 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 174,
			176, 177, 178, 179,
			181, 182, 184, 185, 186, 187 };
	
	public static final int[][] TREC_TRAINING_IDS = {
		{}, // so the filenames work out & "real" indices can start at 1
		{161, 162, 163, 164, 166, 167, 169, 170, 171, 172, 176, 178, 181, 182, 185, 187},
		{160, 162, 163, 165, 166, 167, 168, 169, 170, 174, 176, 178, 181, 184, 185, 186},
		{161, 162, 164, 165, 166, 168, 170, 171, 174, 177, 178, 181, 182, 184, 186, 187},
		{160, 161, 162, 164, 165, 167, 168, 169, 171, 172, 178, 179, 184, 185, 186, 187}
	};
	
	public static final int[][] TREC_TESTING_IDS = {
		{}, // so the filenames work out & "real" indices can start at 1
		{160, 165, 168, 174, 177, 179, 184, 186},
		{161, 164, 171, 172, 177, 179, 182, 187},
		{160, 163, 167, 169, 172, 176, 179, 185},
		{163, 166, 170, 174, 176, 177, 181, 182}
	};
	
	private static String nDigits(int i, int n) {
		String iAsStr = i+"";
		while(iAsStr.length() < n) iAsStr = "0" + iAsStr;
		return iAsStr;
	}
		
	public static void main(String... args) {
		int i = 20;
		CalculatorThread.MAX_RESULTS = i;
		int trainingSet = 2;
		new FastOptimization(481, 
				"files/final_tests_map_new/var_02_language_" + nDigits(trainingSet, 2) + "_" + nDigits(i, 3) + ".txt", 
				FeatureLists.getDefaultLists(), 
				"machine_learning_with_all_but_own_scores",
				//TREC_TRAINING_IDS[trainingSet], TREC_TESTING_IDS[trainingSet]).run();
				TREC_TRAINING_IDS[trainingSet], TREC_TESTING_IDS[trainingSet]).run();
	}
	
	private FileUtil fileUtil;
	private List<FeatureList> featureLists;
	private RelevanceAssessment relevance;
	
	private String resultsFile;
	
	private int numCalculatorThreads;
	private int waitingForCalculatorThreads;
	private int[] featureIndizes;
	
	private boolean reading;
	private boolean breakNextTime;
	
	private boolean noMoreFeatures;

	private List<List<FeatureValues>> valuesWaiting;
	private boolean isWaitingForCalculation;

	private int offset;

	private String resultsFolder;

	private int[] trecTrainingIds;

	private int[] trecTestingIds;
	
	public FastOptimization(String resultsFile, List<FeatureList> featureLists, String resultsFolder, int[] trecTrainingIds, int[] trecTestingIds) {
		this(0, resultsFile, featureLists, resultsFolder, trecTrainingIds, trecTestingIds);
	}
	
	public FastOptimization(int offset, String resultsFile, List<FeatureList> featureLists, String resultsFolder, int[] trecTrainingIds, int[] trecTestingIds) {
		this.resultsFile = resultsFile;
		this.featureLists = featureLists;
		this.relevance = new RelevanceAssessment();
		this.resultsFolder = resultsFolder;
		relevance.loadAssessments();
		fileUtil = new FileUtil();
		this.offset = offset;
		this.trecTrainingIds = trecTrainingIds;
		this.trecTestingIds = trecTestingIds;
		this.noMoreFeatures = false;
		
		numCalculatorThreads = 1;
	}
	
	public void run() {
		featureIndizes = initFeatureIndizes();
		waitingForCalculatorThreads = 0;		
		this.breakNextTime = false;
		this.isWaitingForCalculation = false;
		startNewReader();
	}
	
	private synchronized void startNewReader() {
		if(breakNextTime && waitingForCalculatorThreads == 0) {
			stop();
			return;
		}
		Const.log(Const.LEVEL_INFO, "starting new reader.");
		
		setReading(true);
		List<List<Feature>> featureLists = new ArrayList<>();
		
		for(int i = 0; i < numCalculatorThreads; i++) {
			List<Feature> features;
			while(!isViable(features = getNextFeatures(featureIndizes)));
								
			if(features == null && i == 0 && waitingForCalculatorThreads == 0) {
				stop();
				return;
			} else if (features == null) {
				breakNextTime = true;
				break;
			}
			featureLists.add(features);
		}
		
		Thread t = new Thread(new ReaderThread(this, featureLists, this, this.resultsFolder));
		t.start();
	}
	
	private boolean isViable(List<Feature> list) {
		if(list == null) return true;
		for(int i = 0; i < list.size(); i++) {
			Feature f = list.get(i);
			if(f.getName().startsWith("mesh_")) {
				boolean found = false;
				for(int j = 0; j < list.size(); j++) {
					Feature f2 = list.get(j);
					if(f2.getName().startsWith("mesh_")) continue;
					if(f.getName().substring("mesh_".length()).equals(f2.getName())) {
						found = true;
						break;
					}
				}

				if(found == false) return false;
			}
		}
				
		return true;
	}

	private void stop() {
		System.out.println("Done.");
		fileUtil.closeAll();
	}

	synchronized void onCalculatorThreadFinished(String result) {
		fileUtil.writeLine(resultsFile, result);
		fileUtil.flushWriter(resultsFile);

		waitingForCalculatorThreads--;
		Const.log(Const.LEVEL_INFO, "Calculator finished, waiting for: " + waitingForCalculatorThreads + " calculators.");
		
		if(waitingForCalculatorThreads == 0 && !isReading()) {
			if(isWaitingForCalculation) {
				Const.log(Const.LEVEL_INFO, "Starting new calculator from onCalculatorThreadFinished, reader has been waiting for that.");

				for(List<FeatureValues> featureValues : valuesWaiting) {
					startNewCalculator(featureValues);
				}
				isWaitingForCalculation = false;
			}
			startNewReader();
		}
	}
	
	synchronized void onReadingFinished(List<List<FeatureValues>> values) {
		setReading(false);
		Const.log(Const.LEVEL_INFO, "Reader has finished reading.");

		if(waitingForCalculatorThreads == 0) {
			Const.log(Const.LEVEL_INFO, "Reader was not waiting for any calculator threads. Starting new calculators.");
			for(int i = 0; i < values.size(); i++) {
				startNewCalculator(values.get(i));
			}
			startNewReader();
		} else {			
			Const.log(Const.LEVEL_INFO, "Reader is waiting for calculator threads. Storing lists.");

			this.isWaitingForCalculation = true;
			this.valuesWaiting = values;
		}
	}
	
	private synchronized void startNewCalculator(List<FeatureValues> list) {
		waitingForCalculatorThreads++;
		Thread t = new Thread(new CalculatorThread(list, this, this.relevance, this.trecTestingIds, this.trecTrainingIds));
		t.start();
	}

	private synchronized void setReading(boolean reading) {
		this.reading = reading;
	}
	
	private synchronized boolean isReading() {
		return this.reading;
	}

	private List<Feature> getNextFeatures(int[] featureIndizes) {
		if(noMoreFeatures) return null;
		List<Feature> features = new ArrayList<>();
		
		for(int index = 0; index < featureIndizes.length; index++) {
			features.add(featureLists.get(index).getFeature(featureIndizes[index]));
		}
		
		if(offset > 0) {
			for(int i = 0; i < offset; i++) {
				int index = 0;
				while(true) {
					if(index >= featureIndizes.length) break;
					featureIndizes[index]++;
					if(featureIndizes[index] >= featureLists.get(index).size()) {
						featureIndizes[index] = 0;
						index++;
					} else {
						break;
					}
				}
			}
			offset = 0;
			
			features.clear();
			
			for(int index = 0; index < featureIndizes.length; index++) {
				features.add(featureLists.get(index).getFeature(featureIndizes[index]));
			}
		}
		
		int index = 0;
		
		while(true) {
			featureIndizes[index]++;
					
			if(featureIndizes[index] >= featureLists.get(index).size()) {
				featureIndizes[index] = 0;
				index++;
				if(index >= featureIndizes.length) {
					this.noMoreFeatures = true;
					return features;
				}
			} else {
				break;
			}
		}
		
		return features;
	}

	private int[] initFeatureIndizes() {
		int[] featureIndizes = new int[featureLists.size()];
		
		for(int i = 0; i < featureIndizes.length; i++) {
			featureIndizes[i] = 0;
		}
		
		return featureIndizes;
	}
}
