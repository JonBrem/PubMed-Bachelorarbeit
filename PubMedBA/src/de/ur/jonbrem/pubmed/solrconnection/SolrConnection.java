package de.ur.jonbrem.pubmed.solrconnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import util.Const;

/**
 * This class is a utility to not have the same lines of code in every file that needs to query or index Solr.
 * The individual classes still very much need to be aware of Solr objects and classes.
 */
public class SolrConnection {

	private SolrClient client;
	private List<SolrInputDocument> buffer;
	private boolean isClosed;
	
	public SolrConnection() {
		buffer = new ArrayList<>();
	}
	
	/**
	 * The server needs to be running for this to work.
	 */
	public void openConnection() {
		openConnection("http://localhost:8983/solr/new_core");
	}
	
	public void openConnection(String url) {
		client = new HttpSolrClient(url);
		isClosed = false;
	}
	
	public synchronized void addToIndex(SolrInputDocument obj) {
		buffer.add(obj);
		if(buffer.size() >= 500) {
			commit();
		}
	}
	
	public synchronized void addToIndex(SolrDocument obj) {
		this.addToIndex(ClientUtils.toSolrInputDocument(obj));
	}
	
	public QueryResponse query(SolrQuery params) {
		try {
			return client.query(params);
		} catch (Exception e) {
			Const.log(Const.LEVEL_DEBUG, "Error in query.");
		}
		return null;
	}
	
	public synchronized void commit() {
		if(buffer.size() == 0) {
			return;
		}
//		log(Const.LEVEL_INFO, "Commiting " + buffer.size() + " documents, total " + total);
		
		try {			
			 client.add(buffer);
			 client.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		
		buffer.clear();
		buffer = new ArrayList<>();
	}
	
	public synchronized void closeConnection() {
		try {
			client.close();
			isClosed = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized boolean isClosed() {
		return this.isClosed;
	}
}
