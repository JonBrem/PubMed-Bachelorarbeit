package de.ur.jonbrem.pubmed.indexing.citations;

import java.io.File;
import java.util.List;

import util.Const;
import util.FileUtil;

/**
 * Finds citations in the files, puts them in a text file.
 */
public class CitationFinder {
	
	public static void main(String[] args) {
		new CitationFinder().run();
	}
	
	private FileUtil fileUtil;
	
	public CitationFinder() {
		this.fileUtil = new FileUtil();
	}
	
	public void run() {
		File rootFolder = new File(Const.TREC_HTML_PATH);
		for(File f : rootFolder.listFiles()) {
			if(f.isDirectory()) {
				for(File publication : f.listFiles()) {
					findCitations(publication);
				}
			}
		}
		
		fileUtil.closeAll();
	}
	
	private void findCitations(File publication) {
		String contents = fileUtil.readWholeFile(publication.getAbsolutePath());
		
		List<String> citations = HighwireCitationFinder.findIDs(contents);
		
		for(String s : citations) {
			fileUtil.writeLine("files/citations.txt", publication.getName() + "\t" + s);
		}
		
		fileUtil.closeReader(publication.getAbsolutePath());
		contents = null;
	}
}
