package de.ur.jonbrem.pubmed.indexing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

/**
 * A Node in the FullDOMTree (XML Tree)
 */
public class Node {
	private static final int TEXT_NEVER = 0;
	private static final int TEXT_SOMETIMES = 1;
	private static final int TEXT_ALWAYS = 2;
	
	private int count = 0;
	
	private int text;
	private String name;
	private Set<String> attributes;
	private boolean siblingsWithSameName;
	private boolean emptyAtTimes;		
	private List<Node> children;
	
	public Node(String name) {
		this.name = name;
		this.children = new ArrayList<>();
		this.attributes = new HashSet<>();
		
		this.siblingsWithSameName = false;
		this.emptyAtTimes = false;
		this.text = TEXT_NEVER;
	}
	
	public Node getChild(String nodeName) {
		for(Node n : this.children) if(n.name.equals(nodeName)) return n;
		return null;
	}
	
	public String buildTree() {
		StringBuilder b = new StringBuilder()
			.append("<").append(this.name).append(">")
			.append("<attributes>").append(attributes.toString()).append("</attributes>")
			.append("<count>").append(count).append("</count>")
			.append("<text>").append(text).append("</text>")
			.append("<multiple>").append(siblingsWithSameName).append("</multiple>")
			.append("<possiblyempty>").append(emptyAtTimes).append("</possiblyempty>").append("<children>");
		
		for (Node n : children) {
			b.append(n.buildTree());
		}
		
		b.append("</children>").append("</").append(this.name).append(">");
		
		return b.toString();
	}
	
	public void parseStructure(Element element) {
		
		for (Attribute attr : element.attributes()) {
			this.attributes.add(attr.getKey());
		}
		
		if(!this.siblingsWithSameName) {
			for (Element e : element.siblingElements()) {
				if(e.tagName().equals(element.tagName())) {
					this.siblingsWithSameName = true;
					break;
				}
			}
		}
		
		if(this.text != Node.TEXT_SOMETIMES) {
			if(this.text == Node.TEXT_NEVER && element.ownText() != null && element.ownText().length() > 0) {
				this.text = Node.TEXT_ALWAYS;
			} else if (this.text == Node.TEXT_ALWAYS && (element.ownText() == null || element.ownText().length() == 0)) {
				this.text = Node.TEXT_SOMETIMES;
			}
		}
		
		for(Element e : element.children()) {			
			Node child = this.getChild(e.tagName());
			if(child == null) {
				child = new Node(e.tagName());
				this.children.add(child);
			}
			child.parseStructure(e);
		}
	}
}