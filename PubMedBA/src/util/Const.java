package util;

/**
 * <p>The Const class contains constants and a log method; just things that are generally useful to have in a project.</p>
 * 
 * <p>All <strong>Paths</strong> for files and libraries for the project also go in here.</p>
 */
public final class Const {
	public static final boolean LOG_ERROR = true;
	public static final boolean LOG_WARNING = true;
	public static final boolean LOG_INFO = true;
	public static final boolean LOG_DEBUG = true;
	
	public static final int LEVEL_ERROR = 0;
	public static final int LEVEL_WARNING = 1;
	public static final int LEVEL_INFO = 2;
	public static final int LEVEL_DEBUG = 3;
	
	private static final String[] NOTICES = {"error", "warning", "info", "debug"};
	
	public static void log(int level, String message) {
		if((level == LEVEL_ERROR && LOG_ERROR) ||
				(level == LEVEL_WARNING && LOG_WARNING) ||
				(level == LEVEL_INFO && LOG_INFO) ||
				(level == LEVEL_DEBUG && LOG_DEBUG)) {
			System.out.println(NOTICES[level] + "\t" + message);
		}	
	}
	
	public static void log(String message) {
		System.out.println(message);
	}

	
	public static final String TREC_PATH = "E:\\trec_genomics\\data\\";
	
	public static final String TREC_PUBMED_FILE = "E:\\trec_genomics\\MEDLINE_XML\\Cits_XML_ASCII.xml";
	public static final String OWN_PATH = "E:\\collection\\own\\";
	
	public static final String TREC_XML_PATH = "E:\\collection\\trec_xml\\";
	public static final String TREC_HTML_PATH = "E:\\collection\\trec_html\\";
	public static final String TREC_SINGLE_HTML_PATH = "E:\\collection\\trec_html_one_folder\\";
	
	public static final String TREC_CITATIONS_PATH = "E:\\collection\\trec_citations\\";
}
