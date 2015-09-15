package de.ur.jonbrem.pubmed.indexing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.ur.jonbrem.pubmed.solrconnection.SolrConnection;
import util.Const;

/**
 * Parses the XML files in the collection & indexes the documents using Solr.
 */
public class Indexer {

	public static void main(String[] args) {
		new Indexer().run();
	}
	
	private SolrConnection solrConnection;
	
	public Indexer() {
		solrConnection = new SolrConnection();
		solrConnection.openConnection();
	}
	
	public void run() {
		File rootFolder = new File(Const.TREC_XML_PATH);
		for(File document : rootFolder.listFiles()) {
			if(document.isDirectory()) continue;

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
	}

	private void indexDocument(File document) throws Exception {
		Document xmlDoc = Jsoup.parse(document, "utf8");
		SolrInputDocument solrDoc = createInputDoc(xmlDoc);
		
		solrConnection.addToIndex(solrDoc);
	}
	
	private SolrInputDocument createInputDoc(Document xmlDoc) {
		SolrInputDocument solrDoc = new SolrInputDocument();
		
		String pmid = getPmid(xmlDoc);
		solrDoc.setField("id", pmid);
		solrDoc.setField("pmid", pmid);
		
		solrDoc.setField("content_type", "pubmed_article");
		
		try {putJournalInfo(xmlDoc, solrDoc);	 } catch (Exception e) {}
		try {putArticleInfo(xmlDoc, solrDoc);	 } catch (Exception e) {}
//		try {putMeshheadings(xmlDoc, solrDoc);	 } catch (Exception e) {}
		
		return solrDoc;
	}
	
	private void putAuthors(Document xmlDoc, SolrInputDocument solrDoc, String pmid) {		
		int index = 0;
		for(Element authorElement : xmlDoc.select("Author")) {
			Elements authorFirstname = authorElement.getElementsByTag("firstname");
			if(authorFirstname.size() == 0) authorFirstname = authorElement.getElementsByTag("initials");
			if(authorFirstname.size() == 0) authorFirstname = authorElement.getElementsByTag("forename");
			
			Elements authorLastname = authorElement.getElementsByTag("lastname");
			if(authorLastname.size() > 0) {
				String firstName = authorFirstname.size() > 0 ? authorFirstname.first().text() : "";
				String lastName = authorLastname.first().text();
				String fullName = firstName + " " + lastName;
				
				SolrInputDocument authorDoc = new SolrInputDocument();
				authorDoc.setField("id", pmid + "-auth-" + index);
				authorDoc.setField("_root_", pmid);
				authorDoc.setField("author_firstname", firstName);
				authorDoc.setField("author_lastname", lastName);
				authorDoc.setField("author_fullname", fullName);
				
				solrDoc.addChildDocument(authorDoc);
			}
			index++;
		}
	}

	private void putKeywords(Document xmlDoc, SolrInputDocument solrDoc) {
		List<String> keywords = new ArrayList<>();
		
		for(Element e : xmlDoc.getElementsByTag("Keyword")) {
			keywords.add(e.text());
		}
		
		solrDoc.setField("keyword", keywords);
	}

	private void putMeshheadings(Document xmlDoc, SolrInputDocument solrDoc) {
		List<String> keywords = new ArrayList<>();
		
		for(Element e : xmlDoc.getElementsByTag("DescriptorName")) {
			keywords.add(e.text());
		}
		
		solrDoc.setField("meshheading", keywords);
	}

	private void putArticleInfo(Document xmlDoc, SolrInputDocument solrDoc) {
		// article_title, article_language, abstract, pub_date
		solrDoc.setField("article_title", xmlDoc.getElementsByTag("ArticleTitle").first().text());
		solrDoc.setField("article_language", xmlDoc.getElementsByTag("Language").first().text());
		
		// ABSTRACT
		Elements abstractElements = xmlDoc.getElementsByTag("Abstract");
		if(abstractElements.size() == 0) {
			abstractElements = xmlDoc.getElementsByTag("OtherAbstract");
		}
		
		if(abstractElements.size() > 0) {
			solrDoc.setField("abstract", abstractElements.first().text());
		}
		
		// DATE
		Elements dateElements = xmlDoc.getElementsByTag("DateCreated");
		if(dateElements.size() == 0) dateElements = xmlDoc.getElementsByTag("DateCompleted");
		if(dateElements.size() == 0) dateElements = xmlDoc.getElementsByTag("PubmedPubDate");

		if(dateElements.size() > 0) {
			Element dateElement = dateElements.first();
			Elements year = dateElement.getElementsByTag("Year");
			Elements month = dateElement.getElementsByTag("Month");
			
			if(year.size() > 0 || month.size() > 0) {
				Elements day = dateElement.getElementsByTag("Day");
				
				String date = ((day.size() > 0)?
						(year.first().text() + "-" + getMonth(month.first().text()) + "-" + day.first().text()) :
						(year.first().text() + "-" + getMonth(month.first().text()) + "-01" )) + "T00:00:00Z";
				solrDoc.setField("pub_date", date);
			}
		}
		
	}
	
	private String getMonth(String monthString) {
		if(monthString.matches("[0-9]+")) return monthString;
		
		if(monthString.startsWith("Jan")) return "01";
		if(monthString.startsWith("Feb")) return "02";
		if(monthString.startsWith("Mar")) return "03";
		if(monthString.startsWith("Apr")) return "04";
		if(monthString.startsWith("May")) return "05";
		if(monthString.startsWith("Jun")) return "06";
		if(monthString.startsWith("Jul")) return "07";
		if(monthString.startsWith("Aug")) return "08";
		if(monthString.startsWith("Sep")) return "09";
		if(monthString.startsWith("Oct")) return "10";
		if(monthString.startsWith("Nov")) return "11";
		if(monthString.startsWith("Dec")) return "12";
		
		return "01";
	}

	private void putJournalInfo(Document xmlDoc, SolrInputDocument solrDoc) {
		Element journalElement = xmlDoc.getElementsByTag("Journal").first();
		solrDoc.setField("journal_issn", journalElement.getElementsByTag("ISSN").first().text());
		solrDoc.setField("journal_title", journalElement.getElementsByTag("title").first().text());
	}

//	private Object getHwid(File document) {
//		
//		return null;
//	}

	private String getPmid(Document xmlDoc) {
		return xmlDoc.getElementsByTag("PMID").first().text();
	}
	
}
