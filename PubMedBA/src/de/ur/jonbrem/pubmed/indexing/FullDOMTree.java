package de.ur.jonbrem.pubmed.indexing;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.Const;

/**
 * The FullDOMTree is built when the collection is first extracted from the full pubmed open access collection.
 * It builds a xml structure that explains all possible variations of the element.
 */
public class FullDOMTree {

	private Node tree;
	
	private String root;
	
	public FullDOMTree(String root) {
		this.root = root;
		tree = new Node(root);
	}
	
	public void addDocument(Document doc) {		
		Element front = doc.getElementsByTag(root).first();
		doc = null; // nobody needs the full doc and this is slow as it is!
		
		if(front != null) tree.parseStructure(front);
	}
	

	public String buildTree() {
		return tree.buildTree();
	}
}
