package de.ur.jonbrem.pubmed.indexing.pagerank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

import de.ur.jonbrem.pubmed.solrconnection.SolrConnection;
import util.Const;
import util.FileUtil;

public class CitationRankBuilder {

	private static final String MATRIX_FILE = "files/citation_rank_matrix.csv";	
	private static final String MATRIX_TRANSPOSED_FILE = "files/citation_rank_matrixp.csv";

	private static final String SCORES_FILE = "files/scores.txt";
	
	private static final float ALPHA = 0.5f;
	
	public static void main(String... args) {
		new CitationRankBuilder().run();
	}
	
	private FileUtil fileUtil;
	private SolrConnection solr;
	
	private List<Double> scores;
	private List<List<Double>> matrix;
	
	private Map<String, Integer> pmidToIndex;
	private List<Integer> pmids;
	
	private double noValue;
	
	public CitationRankBuilder() {		
		fileUtil = new FileUtil();
		solr = new SolrConnection();
		
		pmidToIndex = new HashMap<>();
		pmids = new ArrayList<>();
		initPmidToIndexMap();
		
	}
	
	public void initialize() {
		solr.openConnection();
		
		initializeScoresFile();
		writeRegularMatrixFile();
		transposeMatrix();
		
		solr.closeConnection();
		fileUtil.closeAll();
		System.out.println("Finished intializing. Can run now.");
	}

	public void run() {
		noValue = 6.2316167306E-6; // maybe @TODO: export to file
		for(int i = 0; i < 10; i++) {
			iteration();
			System.out.println(i + " iterations done.");
			fileUtil.closeAll();
			fileUtil = new FileUtil();
		}
	}	
	
	private void iteration() {
		
		double[] pageRankScoreOld = readOldPageRankScores();
		double[] pageRankScoreNew = new double[pmids.size()];
		
		for(int i = 0; i < pmids.size(); i++) {
			String s = fileUtil.readLine(MATRIX_TRANSPOSED_FILE);
			if(s != null) {
				pageRankScoreNew[i] = computeNewValue(pageRankScoreOld, toFullArray(s));
			} else {
				pageRankScoreNew[i] = pageRankScoreOld[i];
			}
		}
		
		normalizeValues(pageRankScoreNew);
		
		for(int i = 0; i < pageRankScoreNew.length; i++) {
			fileUtil.write(SCORES_FILE, String.valueOf(pageRankScoreNew[i]));
			if(i != pageRankScoreNew.length - 1) fileUtil.write(SCORES_FILE, "\t");
		}
		fileUtil.write(SCORES_FILE, "\n");
		fileUtil.closeWriter(SCORES_FILE);
	}
	
	// probably (!) neccessary because of how doubles might be rounded up and there are lots of them here.
	private void normalizeValues(double[] pageRankScoreNew) {
		double total = 0;
		for(double d : pageRankScoreNew) total += d;
		double normalize = 1 / total;
		for(int i = 0; i < pageRankScoreNew.length; i++) {
			pageRankScoreNew[i] = pageRankScoreNew[i] * normalize;
		}
	}

	private double[] toFullArray(String line) {
		double[] fullArray = new double[pmids.size()];
		
		String[] lineParts = line.split("\t");
		int currentIndexPosition = 0;
		for(int i = 0; i < lineParts.length; i++) {
			String s = lineParts[i];
			
			if(s.startsWith("!!")) {
				int count = Integer.parseInt(s.substring(2));
				for(int j = 0; j < count; j++) {
					fullArray[currentIndexPosition + j] = noValue;
				}
				currentIndexPosition += count;
			} else {
				fullArray[currentIndexPosition] = Double.parseDouble(s);
				currentIndexPosition++;
			}
		}
		
		return fullArray;
	}

	private double computeNewValue(double[] oldScores, double[] column) {
		double newValue = 0;
				
		for(int i = 0; i < oldScores.length; i++) {
			newValue += oldScores[i] * column[i];
		}
		
		return newValue;
	}
	
	private void initializeScoresFile() {
		Const.log(Const.LEVEL_INFO, "initializing scores");
		fileUtil.write(SCORES_FILE, 1.0 + "");
		int max = pmids.size();
		for(int i = 1; i < max; i++) {
			fileUtil.write(SCORES_FILE, "\t" + 0.0);
		}
		fileUtil.closeWriter(SCORES_FILE);
	}
	
	private void writeRegularMatrixFile() {
		Const.log(Const.LEVEL_INFO, "writing regular matrix");
		int totalDocs = getTotalNumberOfDocs();
		double noValue = 1 / (double) totalDocs * (1 - ALPHA);
		double alphaThroughN = ALPHA / totalDocs;
		
		fileUtil.writeLine(MATRIX_FILE, "!!no-value: " + (noValue + alphaThroughN) + "\n");
		for(int i = 0; i < pmids.size(); i++) {
			Set<Integer> citingDocs = getCitingDocuments(pmids.get(i));
			
			if(citingDocs == null) citingDocs = new HashSet<>();
			boolean notCitedByAnybody = citingDocs.size() == 0;
			int citingDocCount = citingDocs.size();

			double fieldValue = 1 / (double) citingDocCount * (1 - ALPHA);
			
			int countNoValues = 0;
			
			for(int j = 0; j < pmids.size(); j++) {
				if(notCitedByAnybody) {
					fileUtil.write(MATRIX_FILE, "!!" + pmids.size());
					break;
				} else {
					if(citingDocs.contains(pmids.get(j))) {
						if(countNoValues > 0) {
							fileUtil.write(MATRIX_FILE, "!!" + countNoValues + "\t");
							countNoValues = 0;
						}
						fileUtil.write(MATRIX_FILE, String.valueOf(fieldValue + alphaThroughN));
						if(j != pmids.size() - 1) fileUtil.write(MATRIX_FILE, "\t");
					}
					else {
						countNoValues++;
					}
				}
			}
			
			if(countNoValues > 0) {
				fileUtil.write(MATRIX_FILE, "!!" + countNoValues);
			}
			
			fileUtil.write(MATRIX_FILE, "\n");
		}
		
		fileUtil.closeWriter(MATRIX_FILE);
	}
	
	private Set<Integer> getCitingDocuments(Integer integer) {
		List<Integer> citingDocs = new ArrayList<>();
		
		SolrQuery q = new SolrQuery();
		q.set("q", "pmid:" + integer);
		q.set("fl", "cited_by");
		
		QueryResponse response = solr.query(q);
		if(response == null) return null;
		
		for(SolrDocument result : response.getResults()) {
			Collection<Object> citingDocIDs = result.getFieldValues("cited_by");
			if(citingDocIDs == null || citingDocIDs.size() == 0) return null;
			for(Object o : citingDocIDs) {
				citingDocs.add(Integer.parseInt((String) o));
			}
		}
		
		return new HashSet<>(citingDocs);
	}

	private void transposeMatrix() {
		List<List<String>> regularMatrix = readRegularMatrixFile(); // noValue is set after this
		
		
		for(int transpRowIndex = 0; transpRowIndex < regularMatrix.size(); transpRowIndex++) {
			int noValueCount = 0;

			for(int transpColIndex = 0; transpColIndex < regularMatrix.size(); transpColIndex++) {
				
				double value = getRegularMatrixValueAt(regularMatrix, transpColIndex, transpRowIndex);
				if(value == noValue) noValueCount++;
				else {
					if(noValueCount > 0) {
						fileUtil.write(MATRIX_TRANSPOSED_FILE, "!!" + noValueCount + "\t");
						noValueCount = 0;
					}
					
					if(transpColIndex != regularMatrix.size() - 1) {
						fileUtil.write(MATRIX_TRANSPOSED_FILE, value + "\t");
					} else {
						fileUtil.write(MATRIX_TRANSPOSED_FILE, String.valueOf(value));
					}
				}
			}
			
			if(noValueCount > 0) {
				fileUtil.write(MATRIX_TRANSPOSED_FILE, "!!" + noValueCount);
			}
			
			if(transpRowIndex % 1000 == 0) Const.log(Const.LEVEL_DEBUG, transpRowIndex + " lines done.");
			
			fileUtil.write(MATRIX_TRANSPOSED_FILE, "\n");
		}
		
	}

	private double getRegularMatrixValueAt(List<List<String>> matrix, int row, int column) {
		int currentIndex = 0;
		
		for(int i = 0; i < matrix.get(row).size(); i++) {
			String s = matrix.get(row).get(i);
			if(s.startsWith("!!")) {
				currentIndex += Integer.parseInt(s.substring(2));
				if(currentIndex > column) return noValue;
			} else {
				if(currentIndex == column) return Double.parseDouble(s);
				currentIndex++;
			}
		}
		
		return noValue;
	}

	private List<List<String>> readRegularMatrixFile() {
		String s = fileUtil.readLine(MATRIX_FILE);
		noValue = Double.parseDouble(s.substring("!!no-value: ".length()));
		s = fileUtil.readLine(MATRIX_FILE); // empty line
		
		List<List<String>> lines = new ArrayList<>();
		
		while((s = fileUtil.readLine(MATRIX_FILE)) != null) {
			lines.add(Arrays.asList(s.split("\t")));
		}
		
		return lines;
	}

	private int getTotalNumberOfDocs() {
		return 160472; // fewer in solr, though.
	}
	
	private double[] readOldPageRankScores() {
		String s1, s2 = null;
		while((s1 = fileUtil.readLine(SCORES_FILE)) != null) {
			s2 = s1;
		}
		fileUtil.closeReader(SCORES_FILE);
		// s1 is null, s2 is the last line in the file!
		
		String[] allValuesAsStrings = s2.split("\t");
		double[] allValues = new double[allValuesAsStrings.length];
		for(int i  = 0; i < allValuesAsStrings.length; i++) {
			allValues[i] = Double.parseDouble(allValuesAsStrings[i]);
		}
		return allValues;
	}
	
	private void initPmidToIndexMap() {
		String s;
		int i = 0;
		while((s = fileUtil.readLine("files/pmids.txt")) != null) {
			pmidToIndex.put(s, i);
			pmids.add(Integer.parseInt(s));
			i++;
		}
		fileUtil.closeReader("files/pmids.txt");
	}
	
}
