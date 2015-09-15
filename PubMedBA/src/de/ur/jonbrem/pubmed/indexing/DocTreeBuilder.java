package de.ur.jonbrem.pubmed.indexing;

import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static util.Const.log;

import util.Const;
import util.FileUtil;

/**
 * Analyzes the file structure of XML files by building a "Tree".
 * the Tree (in XML itself) contains the names of elements and attributes and some
 * other information about these.
 * <br>In other words: This doesn't really do anything, it's just here to analyze unknown
 * XML files to make it easier to work with them. 
 */
public class DocTreeBuilder {

	private static final int MAX = 60000;
	
	public static void main(String[] args) {
		new DocTreeBuilder().run();
	}
	
	private int count;
	
	private FileUtil fileUtil;
	
	private FullDOMTree tree;
	
	public DocTreeBuilder() {
		this.fileUtil = new FileUtil();
		this.tree = new FullDOMTree("PubmedArticle");
	}
	
	public void run() {
		count = 0;
		File collectionFolder = new File(Const.TREC_XML_PATH);
		for(File f : collectionFolder.listFiles()) {
			if(!f.isDirectory()) {
				parseFile(f);
			}
			
			if(count > MAX) break;
		}
		
		fileUtil.writeLine(Const.OWN_PATH + "tree.xml", tree.buildTree());
		fileUtil.closeAll();
		System.out.println("Done.");
	}
	
	private void parseFile(File publication) {
//		try {
//			if(publication.getName().substring(publication.getParentFile().getName().length() + 1).compareTo("2006") > 0) return;
//		} catch(Exception e) {
//			return;
//		}
		
		count++;
		if(count % 5000 == 0) log(Const.LEVEL_INFO, count + " documents checked.");
		
		String wholeFile = fileUtil.readWholeFile(publication.getAbsolutePath());
		Document doc = Jsoup.parse(wholeFile);
		
		tree.addDocument(doc);
		
		wholeFile = null;
	}
	
}

