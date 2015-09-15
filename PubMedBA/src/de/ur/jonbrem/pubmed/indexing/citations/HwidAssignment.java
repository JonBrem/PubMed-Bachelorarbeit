package de.ur.jonbrem.pubmed.indexing.citations;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import de.ur.jonbrem.pubmed.solrconnection.SolrConnection;
import util.FileUtil;

/**
 * Assigns HWIDs to the solr documents (that already have their PMID)
 */
public class HwidAssignment {

	public static void main(String[] args) {
		new HwidAssignment().run();
	}
	
	private SolrConnection solr;
	
	private FileUtil fileUtil;
	
	public HwidAssignment() {
		solr = new SolrConnection();
		fileUtil = new FileUtil();
	}
	
	public void run() {
		solr.openConnection();
		
		assignHWIDs();
		
		fileUtil.closeAll();
		solr.closeConnection();
		System.out.println("Done");
	}
	
	// @TODO: decompose, but there's no real need because it worked...
	private void assignHWIDs() {
		String s;
		int count = 0;  
		while((s = fileUtil.readLine("files/metadata.txt")) != null) {
			count++;
			if(count%500 == 0) System.out.println("Indexed " + count ); 
			String id = s.split("\t")[1];
			id = id.substring(0, id.indexOf('.'));

			String hwid = "";
			String hwidInUrl = s.split("\t")[0];
			hwid = hwidInUrl.substring(hwidInUrl.lastIndexOf("/"));
			hwidInUrl = hwidInUrl.substring(0, hwidInUrl.length() - hwid.length());
			hwid = hwidInUrl.substring(hwidInUrl.lastIndexOf("/")) + ":" + hwid;
			hwidInUrl = hwidInUrl.substring(0, hwidInUrl.lastIndexOf("/"));
			
			if(hwidInUrl.lastIndexOf(";") > hwidInUrl.lastIndexOf("/")) {
				hwid = hwidInUrl.substring(hwidInUrl.lastIndexOf(";")) + ":" + hwid;
			} else {
				hwid = hwidInUrl.substring(hwidInUrl.lastIndexOf("/")) + ":" + hwid;
			}
						
			hwid = hwid.replaceAll("(/|;)", "");
			
			SolrQuery q = new SolrQuery();
			q.set("q", "pmid:"+id);
			q.set("fl", "*");
			
			QueryResponse response = solr.query(q);
			SolrDocumentList results = response.getResults();
			
			SolrInputDocument toIndex = new SolrInputDocument();
			
			for(SolrDocument doc : results) {
				for(String fieldName : doc.getFieldNames()) {
					if(fieldName.equals("_version_") || fieldName.equals("hwid")) continue;
					toIndex.setField(fieldName, doc.get(fieldName));
				}	
				break;
			}
			
			toIndex.setField("hwid", hwid);
			toIndex.setField("id", id);
			
			solr.addToIndex(toIndex);
		}
		
		solr.commit();
	}
	
}
