package de.ur.jonbrem.pubmed.indexing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import de.ur.jonbrem.pubmed.solrconnection.SolrConnection;
import util.Const;

/**
 * the given MeSH-Headings were not used during in the previous indexing process (they are in the class, though),
 * but since I've decided to use the Headings for something else, this was created to fix that.
 */
public class MeshIndexer {

	public static void main(String[] args) {
		new MeshIndexer().run();
	}
	
	private SolrConnection solrConnection;
	
	private int meshHeadingsCounter;
	private int docCounter;
	
	public MeshIndexer() {
		solrConnection = new SolrConnection();
		solrConnection.openConnection();
		meshHeadingsCounter = 0;
		docCounter = 0;
	}
	
	public void run() {
		File rootFolder = new File(Const.TREC_XML_PATH);
		int start = 0;
		
		for(File document : rootFolder.listFiles()) {
			if(document.isDirectory()) continue;
			start++;
			if(start < 144276) continue;
			
			try {
				indexDocument(document);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		solrConnection.commit();
		solrConnection.closeConnection();
		System.out.println("Done.");
		System.out.println("Total docs: " + docCounter);
	}

	private void indexDocument(File document) throws Exception {
		docCounter++;
		Document xmlDoc = Jsoup.parse(document, "utf8");
		assignMeshheadings(xmlDoc);
	}
	
	private void assignMeshheadings(Document xmlDoc) {
		String pmid = getPmid(xmlDoc);		
		SolrQuery q = new SolrQuery();
		q.set("q", "pmid:"+pmid);
		q.set("fl", "*");
		
		QueryResponse response = solrConnection.query(q);
		if(response != null) {
			SolrDocumentList results = response.getResults();
			if(results != null && results.size() > 0) {
				SolrDocument result = results.get(0);
				SolrInputDocument resultAsInputDoc = ClientUtils.toSolrInputDocument(result);
				
				resultAsInputDoc.remove("_version_");
				try {
					List<List<String>> meshheadings = getMeshheadings(xmlDoc);
					
					resultAsInputDoc.setField("meshheading", meshheadings.get(0));
					resultAsInputDoc.setField("meshheading_minor", meshheadings.get(1));
					solrConnection.addToIndex(resultAsInputDoc);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
//		List<List<String>> meshheadings = getMeshheadings(xmlDoc);
//
//		if(meshheadings.get(0).size() > 0) {
//			meshHeadingsCounter++;
//		}
//		
//		if(meshHeadingsCounter == 66557) {
//			System.out.println("Doc counter at 66557 " + docCounter);
//		}
	}
	
	private List<List<String>> getMeshheadings(Document xmlDoc) {
		List<List<String>> keywords = new ArrayList<>();
		
		keywords.add(new ArrayList<>());
		keywords.add(new ArrayList<>());
		
		for(Element e : xmlDoc.getElementsByTag("DescriptorName")) {
			if(e.hasAttr("MajorTopicYN") && e.attr("MajorTopicYN").equals("N")) {
				keywords.get(1).add(e.text());
			} else {
				keywords.get(0).add(e.text());
			}
		}
		
		return keywords;
	}
	
	
	private String getPmid(Document xmlDoc) {
		return xmlDoc.getElementsByTag("PMID").first().text();
	}
	
}
