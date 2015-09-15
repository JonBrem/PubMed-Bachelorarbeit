package de.ur.jonbrem.pubmed.test.machine_learning.own_learning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.ur.jonbrem.pubmed.test.RelevanceAssessment;

class CalculatorThread implements Runnable {

	public static final boolean F2 = false;
	
	public static int MAX_RESULTS = 300;
	
	private List<FeatureValues> featureValues;
	private FastOptimization parent;
	private RelevanceAssessment relevance;

	private int[] TREC_TESTING_IDS;

	private int[] TREC_TRAINING_IDS;

	public CalculatorThread(List<FeatureValues> featureValues,
			FastOptimization parent, RelevanceAssessment relevance,
			int[] TREC_TESTING_IDS, int[] TREC_TRAINING_IDS) {
		this.relevance = relevance;
		this.TREC_TESTING_IDS = TREC_TESTING_IDS;
		this.TREC_TRAINING_IDS = TREC_TRAINING_IDS;
		Collections.sort(featureValues, new Comparator<FeatureValues>() {
			@Override
			public int compare(FeatureValues o1, FeatureValues o2) {
				int compareDouble = Double.compare(o1.getMean(), o2.getMean());

				if (o1.isMultiplier()) {
					if (o2.isMultiplier()) {
						return compareDouble;
					} else {
						return -1;
					}
				} else {
					if (o2.isMultiplier()) {
						return 1;
					} else {
						return compareDouble;
					}
				}
			}
		});

		this.featureValues = featureValues;
		this.parent = parent;
	}

	@Override
	public void run() {
		String result = this.calculateHighestScore(featureValues);
		parent.onCalculatorThreadFinished(result);
	}

	private String calculateHighestScore(List<FeatureValues> featureValues) {
		double[][] weights = getWeights(featureValues);

		double highestScore = Double.NEGATIVE_INFINITY;
		double recallForHighestScore = Double.NEGATIVE_INFINITY;
		double precisionForHighestScore = Double.NEGATIVE_INFINITY;
		double[] highestWeights = null;

		int max = getPower(weights[0].length, weights.length);

		for (int weightsIndex = 0; weightsIndex < max; weightsIndex++) {
			double[] currentWeights = getWeightsForIndex(weightsIndex, weights);
			if (currentWeights == null)
				break;

			double[] scoreAndPrecisionAndRecall = calculateScoreForWeightsAndFeatures(featureValues, currentWeights, TREC_TRAINING_IDS);
			if (scoreAndPrecisionAndRecall[0] > highestScore) {
				highestWeights = currentWeights;
				highestScore = scoreAndPrecisionAndRecall[0];
				recallForHighestScore = scoreAndPrecisionAndRecall[1];
				precisionForHighestScore = scoreAndPrecisionAndRecall[2];
			}
		}

		if (highestWeights == null || highestWeights.length == 0)
			return "No highest weights found. Some error occurred or there were no results.";
		else {
			double[][] newWeights = getNewWeights(highestWeights);
			max = getPower(newWeights[0].length, newWeights.length);
			for (int weightsIndex = 0; weightsIndex < max; weightsIndex++) {
				double[] currentWeights = getWeightsForIndex(weightsIndex, newWeights);
				if (currentWeights == null)
					break;

				double[] scoreAndPrecisionAndRecall = calculateScoreForWeightsAndFeatures(featureValues, currentWeights, TREC_TRAINING_IDS);
								
				if (scoreAndPrecisionAndRecall[0] > highestScore) {
					highestWeights = currentWeights;
					highestScore = scoreAndPrecisionAndRecall[0];
					recallForHighestScore = scoreAndPrecisionAndRecall[1];
					precisionForHighestScore = scoreAndPrecisionAndRecall[2];
				}
			}
		}
		
		double[] scoreAndRecallAndPrecision = calculateScoreForWeightsAndFeatures(featureValues, highestWeights, TREC_TESTING_IDS);
		highestScore = scoreAndRecallAndPrecision[0];
		recallForHighestScore = scoreAndRecallAndPrecision[1];
		precisionForHighestScore = scoreAndRecallAndPrecision[2];
		
		String resultString = "Highest score for: ";
		for (int i = 0; i < featureValues.size(); i++) {
			resultString += featureValues.get(i).getName();
			if (featureValues.get(i).isMultiplier()) {
				resultString += ", multiplier " + highestWeights[i] + " & ";
			} else if (i < highestWeights.length) {
				resultString += ", " + highestWeights[i] + " & ";
			} else if (i != featureValues.size() - 1) {
				resultString += ", " + 1 + " & ";
			}
		}
		resultString += ", 1; precision: " + precisionForHighestScore + "; recall: " + recallForHighestScore
				+ "; f2-score: " + highestScore;
		return resultString;
	}

	private double[][] getNewWeights(double[] highestWeights) {
		double[][] newWeights = new double[highestWeights.length][8];
		
		for(int i = 0; i < highestWeights.length; i++) {
			double expo = Math.log(highestWeights[i]) / Math.log(3);
			
			newWeights[i][0] = Math.pow(3, expo - 0.8);
			newWeights[i][1] = Math.pow(3, expo - 0.6);
			newWeights[i][2] = Math.pow(3, expo - 0.4);
			newWeights[i][3] = Math.pow(3, expo - 0.2);
			newWeights[i][4] = Math.pow(3, expo + 0.2);
			newWeights[i][5] = Math.pow(3, expo + 0.4);
			newWeights[i][6] = Math.pow(3, expo + 0.6);
			newWeights[i][7] = Math.pow(3, expo + 0.8);
			
		}
		
		return newWeights;
	}

	private double[][] getWeights(List<FeatureValues> featureValues) {
		int size = featureValues.size() - 1;
		if (size < 1)
			return new double[][] { new double[] { 1 } };

		double[][] weights = new double[size][11];

		for (int i = 0; i < weights.length; i++) {
			FeatureValues values1 = featureValues.get(i);
			int otherIndex = featureValues.size() - 1;
			while (otherIndex > 0 && featureValues.get(otherIndex).isMultiplier()) {
				otherIndex--;
			}

			FeatureValues values2 = featureValues.get(otherIndex);

			int defaultPower = getDefaultPower(values1.getMean(), values2.getMean());

			if (values1.isMultiplier()) {
				for (int j = 0; j < 11; j++) {
					weights[i][j] = j / (double) 10;
				}

				double power = Math.pow(3, defaultPower);

				for (int trecId : FastOptimization.TREC_QUESTION_IDS) {
					Map<String, Double> valuesForTrecId = values1.getValuesFor(trecId);
					for (String pmid : valuesForTrecId.keySet()) {
						valuesForTrecId.put(pmid, valuesForTrecId.get(pmid) * power);
					}
				}

			} else {
				for (int j = 0; j < 11; j++) {
					weights[i][j] = Math.pow(3, defaultPower - 5 + j);
				}
			}
		}

		return weights;
	}

	private int getDefaultPower(double mean1, double mean2) {
		if (mean1 == 0)
			return 0;
		double rawRatio = mean2 / mean1;

		int currentPower = (int) Math.floor(Math.log(rawRatio) / Math.log(3));

		return currentPower;
	}

	private int getPower(int root, int pow) {
		int power = 1;
		for (int powerCount = 0; powerCount < pow; powerCount++) {
			power *= root;
		}
		return power;
	}

	private double[] getWeightsForIndex(int weightsIndex, double[][] weights) {
		double[] weightsForIndex = new double[weights.length];

		for (int i = weights.length - 1; i >= 0; i--) {
			int power = getPower(weights[0].length, i);

			weightsForIndex[i] = weights[i][weightsIndex / power];
			weightsIndex = weightsIndex - (power * (weightsIndex / power));
		}
		return weightsForIndex;
	}

	private double[] calculateScoreForWeightsAndFeatures(List<FeatureValues> featureValues, double[] currentWeights, int[] trecIDs) {
		if(F2) return calculateScoreForWeightsAndFeaturesF2(featureValues, currentWeights, trecIDs);
		else return calculateScoreForWeightsAndFeaturesMAP(featureValues, currentWeights, trecIDs);
	}
	
	private double[] calculateScoreForWeightsAndFeaturesMAP(List<FeatureValues> featureValues2, double[] currentWeights,
			int[] trecIDs) {
		double[] recallValues = new double[trecIDs.length];
		double[] precisionValues = new double[trecIDs.length];
		double[] mapValues = new double[trecIDs.length];

		for (int i = 0; i < trecIDs.length; i++) {
			int trecId = trecIDs[i];
			List<PMIDandScore> pmidsScores = getPmidsAndScores(featureValues, currentWeights, trecId);
			if (pmidsScores.size() >= MAX_RESULTS)
				pmidsScores = pmidsScores.subList(0, MAX_RESULTS);

			double[] recallAndPrecisionAndMAP = getRecallAndPrecisionAndMAP(pmidsScores, trecId);
			recallValues[i] = recallAndPrecisionAndMAP[0];
			precisionValues[i] = recallAndPrecisionAndMAP[1];
			mapValues[i] = recallAndPrecisionAndMAP[2];
		}

		double averageRecall = getAverage(recallValues);
		double averagePrecision = getAverage(precisionValues);
		double averageMap = getAverage(mapValues);

		if (averagePrecision == 0 && averageRecall == 0)
			return new double[] { 0, 0, 0 };

		return new double[] {averageMap, averageRecall, averagePrecision };
	
	}

	private double[] calculateScoreForWeightsAndFeaturesF2(List<FeatureValues> featureValues, double[] currentWeights, int[] trecIDs) {
		double[] recallValues = new double[trecIDs.length];
		double[] precisionValues = new double[trecIDs.length];

		for (int i = 0; i < trecIDs.length; i++) {
			int trecId = trecIDs[i];
			List<PMIDandScore> pmidsScores = getPmidsAndScores(featureValues, currentWeights, trecId);
			if (pmidsScores.size() >= MAX_RESULTS)
				pmidsScores = pmidsScores.subList(0, MAX_RESULTS);

			double[] recallAndPrecision = getRecallAndPrecision(pmidsScores, trecId);
			recallValues[i] = recallAndPrecision[0];
			precisionValues[i] = recallAndPrecision[1];
		}

		double averageRecall = getAverage(recallValues);
		double averagePrecision = getAverage(precisionValues);

		if (averagePrecision == 0 && averageRecall == 0)
			return new double[] { 0, 0, 0 };

		return new double[] { (5) * (averagePrecision * averageRecall) / ((4 * averagePrecision) + averageRecall),
				averageRecall, averagePrecision };
	}
	
	

	private List<PMIDandScore> getPmidsAndScores(List<FeatureValues> featureValues, double[] currentWeights,
			int trecId) {
		Map<String, PMIDandScore> pmidsAndScores = new HashMap<>();

		for (int i = 0; i < featureValues.size(); i++) {
			if (featureValues.get(i).isMultiplier())
				continue;
			Map<String, Double> values = featureValues.get(i).getValuesFor(trecId);
			if (values == null)
				continue;

			double weight = (i >= currentWeights.length) ? 1 : currentWeights[i];

			for (String pmid : values.keySet()) {
				PMIDandScore pmidAndScore;
				if (pmidsAndScores.containsKey(pmid)) {
					pmidAndScore = pmidsAndScores.get(pmid);
				} else {
					pmidAndScore = new PMIDandScore();
					pmidAndScore.pmid = pmid;
					pmidAndScore.score = 0;
				}

				pmidAndScore.score += values.get(pmid) * weight;

				pmidsAndScores.put(pmid, pmidAndScore);
			}

		}

		for (int i = 0; i < featureValues.size(); i++) {
			if (!featureValues.get(i).isMultiplier())
				continue;

			Map<String, Double> values = featureValues.get(i).getValuesFor(trecId);
			double weight = (i >= currentWeights.length) ? 1 : currentWeights[i];

			for (String pmid : values.keySet()) {
				if (pmidsAndScores.containsKey(pmid)) {
					PMIDandScore pmidAndScore = pmidsAndScores.get(pmid);
					pmidAndScore.score = weight * pmidAndScore.score + (1 - weight) * values.get(pmid);
					pmidsAndScores.put(pmid, pmidAndScore);
				}
			}
		}

		List<PMIDandScore> pmidsAndScoresAsList = new ArrayList<>(pmidsAndScores.values());
		Collections.sort(pmidsAndScoresAsList);
		Collections.reverse(pmidsAndScoresAsList);
		
		for(PMIDandScore pmidAndScore : pmidsAndScoresAsList) {
			if(pmidAndScore.score == 0) pmidsAndScoresAsList.remove(pmidAndScore);
		}
		
		return pmidsAndScoresAsList;
	}

	private double getAverage(double... values) {
		double total = 0;
		for (double d : values)
			total += d;

		return total / values.length;
	}
	
	private double[] getRecallAndPrecision(List<PMIDandScore> pmidsScores, int trecId) {
		double[] recallAndPrecision = new double[2];
		String trecIdString = ""+trecId;
		
		int relevantFound = 0;
		
		for(int i = 0; i < pmidsScores.size(); i++) {
			try {
				if(relevance.isRelevant(trecIdString, pmidsScores.get(i).pmid)) {
					relevantFound++;
				}
			} catch (Exception e) {}
		}
				
		try {
			recallAndPrecision[0] = relevantFound / (double) relevance.getNumRelevant(trecIdString);
		} catch (Exception e) {}
		
		if(recallAndPrecision[0] == Double.NaN) recallAndPrecision[0] = 0;
		
		if(pmidsScores.size() == 0) {
			recallAndPrecision[1] = 0;
		} else {
			recallAndPrecision[1] = relevantFound / (double) pmidsScores.size();
		}
		
		return recallAndPrecision;
	}

	private double[] getRecallAndPrecisionAndMAP(List<PMIDandScore> pmidsScores, int trecId) {
		double[] recallAndPrecisionAndMap = new double[3];
		int relevantFound = 0;
		List<Double> precisions = new ArrayList<>();
		String trecIdString = trecId + "";
		
		for(int i = 0; i < pmidsScores.size(); i++) {
			try {
				if(relevance.isRelevant(trecIdString, pmidsScores.get(i).pmid)) {
					relevantFound++;
					precisions.add(relevantFound / (double) (i + 1));
				}
			} catch (Exception e) {}
		}
		
		try {
			recallAndPrecisionAndMap[0] = relevantFound / (double) relevance.getNumRelevant(trecIdString);
		} catch (Exception e) {}
		
		if(recallAndPrecisionAndMap[0] == Double.NaN) recallAndPrecisionAndMap[0] = 0;
		
		if(pmidsScores.size() == 0) {
			recallAndPrecisionAndMap[1] = 0;
		} else {
			recallAndPrecisionAndMap[1] = relevantFound / (double) pmidsScores.size();
		}

		if(precisions.size() > 0) {
			double total = 0;
			for(Double d : precisions) total += d;
			try {
				double numRel = (double) relevance.getNumRelevant(trecIdString);
				double denominator = MAX_RESULTS;
				if(numRel < denominator) denominator = numRel;
				recallAndPrecisionAndMap[2] = total / denominator;
			} catch (Exception e) {
				recallAndPrecisionAndMap[2] = total / precisions.size();
			}
		} else {
			recallAndPrecisionAndMap[2] = 0;
		}
		
		return recallAndPrecisionAndMap;
	}


	private class PMIDandScore implements Comparable<PMIDandScore> {
		private String pmid;
		private double score;

		@Override
		public int compareTo(PMIDandScore another) {
			return Double.compare(score, another.score);
		}
	}

}