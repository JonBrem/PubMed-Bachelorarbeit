package de.ur.jonbrem.pubmed.test.machine_learning;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import de.ur.jonbrem.pubmed.advanced_querying.AdvancedQuery;
import de.ur.jonbrem.pubmed.advanced_querying.QueryResults;
import de.ur.jonbrem.pubmed.solrconnection.SolrConnection;
import de.ur.jonbrem.pubmed.test.QueryStream;
import de.ur.jonbrem.pubmed.test.RelevanceAssessment;
import de.ur.jonbrem.pubmed.test.TrecQuery;
import de.ur.jonbrem.pubmed.test.advanced_querybuilders.AMachineLearningQueryBuilder;
import util.Const;
import util.FileUtil;

public class MachineLearningTest {

	public static void main(String[] args) {
		new MachineLearningTest(
				new AMachineLearningQueryBuilder(), 
				new QueryStream(), 
				new RelevanceAssessment().loadAssessments(),
				"machine_learning_with_all_remaining"
			).run();
	}

	private AMachineLearningQueryBuilder builder;
	private QueryStream stream;
	private RelevanceAssessment relevance;
	
	private FileUtil fileUtil;
	private SolrConnection solr;
	private String resultsFolder;
			
	public MachineLearningTest (AMachineLearningQueryBuilder builder, QueryStream stream, RelevanceAssessment relevance, String resultsFolder) {
		this.builder = builder;
		this.stream = stream;
		this.relevance = relevance;
		fileUtil = new FileUtil();
		this.resultsFolder = resultsFolder;
//		builder.prepareForMesh();
	}
	
	public void run() {
		TrecQuery q = null;
		solr = new SolrConnection();
		solr .openConnection();

		int offset = 0;
		while((q = stream.getNext()) != null) {
			if(offset > 0) {
				offset--;
				continue;
			}
			
			Timestamp stamp = new Timestamp(System.currentTimeMillis());
			Date date = new Date(stamp.getTime());
			Time time = new Time(stamp.getTime());
			
			Const.log(Const.LEVEL_DEBUG, "starting topic " + q.getId() + "\t" + date.toString() + "\t" + time.toString());

			processQueries(builder.getQueriesFor(q), String.valueOf(q.getId()));
			
			stamp = new Timestamp(System.currentTimeMillis());
			date = new Date(stamp.getTime());
			time = new Time(stamp.getTime());
			Const.log(Const.LEVEL_DEBUG, "finished with topic " + q.getId() + "\t" + date.toString()+ "\t" + time.toString());
		}
				
		fileUtil.closeAll();
		solr.closeConnection();
//		builder.close();
	}
	
	private void processQueries(AdvancedQuery query, String topic) {			
		MLTestResults results = new MLTestResults();
		QueryResults queryResults = query.query(solr);
				
		results.setResults(queryResults);

		try {
			for(String pmid : results.getResultPMIDs()) {
				if(relevance.isRelevant(topic, pmid)) {
					results.getDoc(pmid).put("relevant", 1);
				} else {
					results.getDoc(pmid).put("relevant", 0);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		results.writeResultsAsCSV(fileUtil, 
				"files/" + this.resultsFolder + "/results_" + topic + ".csv",
				true);
		fileUtil.closeWriter("files/" + this.resultsFolder + "/results_" + topic + ".csv");
	}	
}
