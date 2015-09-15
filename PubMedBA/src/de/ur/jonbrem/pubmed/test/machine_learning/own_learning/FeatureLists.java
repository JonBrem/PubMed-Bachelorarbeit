package de.ur.jonbrem.pubmed.test.machine_learning.own_learning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeatureLists {

	public static List<FeatureList> getDefaultLists() {
		List<FeatureList> featureLists = new ArrayList<>();
		FeatureList abstractList = new FeatureList();
		FeatureList titleList = new FeatureList();
		
		abstractList.features = Arrays.asList(ABSTRACTS);
		titleList.features = Arrays.asList(TITLES);
		
		featureLists.add(titleList);
		featureLists.add(abstractList);
		
		return featureLists;
	}
	
	public static List<FeatureList> getAdvancedLists() {
		List<FeatureList> featureLists = new ArrayList<>();
		FeatureList abstractList = new FeatureList();
		FeatureList titleList = new FeatureList();
		
		abstractList.features = Arrays.asList(ABSTRACTS_ADVANCED_LM);
		titleList.features = Arrays.asList(TITLES_ADVANCED_LM);
		
		featureLists.add(titleList);
		featureLists.add(abstractList);
		
		return featureLists;
	}
	
	public static List<FeatureList> getCitationRankLists() {
		List<FeatureList> featureLists = new ArrayList<>();
		FeatureList abstractList = new FeatureList();
		FeatureList titleList = new FeatureList();
		FeatureList citationRankList = new FeatureList();
		
		abstractList.features = Arrays.asList(ABSTRACTS_AFTER_LM);
		titleList.features = Arrays.asList(TITLES_AFTER_LM);
		
		Feature citationRank = new Feature("citation_rank");
		Feature citationRankMultiply = new Feature("citation_rank");
		citationRankMultiply.setIsMultiplier(true);
		citationRankList.features.add(citationRankMultiply);
		citationRankList.features.add(citationRank);
		
		featureLists.add(titleList);
		featureLists.add(abstractList);
		featureLists.add(citationRankList);
		
		return featureLists;
	}
	
	public static List<FeatureList> getMeshLists() {
		List<FeatureList> featureLists = new ArrayList<>();
		
		FeatureList abstractList = new FeatureList();
		FeatureList titleList = new FeatureList();
		
		FeatureList abstractMeshList = new FeatureList();
		FeatureList titleMeshList = new FeatureList();
		
		abstractList.features = Arrays.asList(ABSTRACTS_AFTER_LM);
		titleList.features = Arrays.asList(TITLES_AFTER_LM);
		
		abstractMeshList.features = Arrays.asList(ABSTRACTS_AFTER_LM_MESH);
		titleMeshList.features = Arrays.asList(TITLES_AFTER_LM_MESH);		
		
		featureLists.add(titleList);
		featureLists.add(abstractList);
		featureLists.add(titleMeshList);
		featureLists.add(abstractMeshList);
		
		return featureLists;
	}
	
	public static List<FeatureList> getOwnApproach1Lists() {
		List<FeatureList> featureLists = new ArrayList<>();
		FeatureList abstractList = new FeatureList();
		FeatureList titleList = new FeatureList();
		FeatureList citationRankList = new FeatureList();
		
		abstractList.features = Arrays.asList(ABSTRACTS_AFTER_LM);
		titleList.features = Arrays.asList(TITLES_AFTER_LM);
		
		Feature ownRank = new Feature("combined_score_1");
		Feature ownRankMultiply = new Feature("combined_score_1");
		ownRankMultiply.setIsMultiplier(true);
		citationRankList.features.add(ownRankMultiply);
		citationRankList.features.add(ownRank);
		
		featureLists.add(titleList);
		featureLists.add(abstractList);
		featureLists.add(citationRankList);
		
		return featureLists;
		
	}
	
	public static List<FeatureList> getOwnApproach2Lists() {
		List<FeatureList> featureLists = new ArrayList<>();
		FeatureList abstractList = new FeatureList();
		FeatureList titleList = new FeatureList();
		FeatureList citationRankList = new FeatureList();
		
		abstractList.features = Arrays.asList(ABSTRACTS_AFTER_LM);
		titleList.features = Arrays.asList(TITLES_AFTER_LM);
		
		Feature ownRank = new Feature("combined_score_2");
		Feature ownRankMultiply = new Feature("combined_score_2");
		ownRankMultiply.setIsMultiplier(true);
		citationRankList.features.add(ownRankMultiply);
		citationRankList.features.add(ownRank);
		
		featureLists.add(titleList);
		featureLists.add(abstractList);
		featureLists.add(citationRankList);
		
		return featureLists;
	}
	
	public static List<FeatureList> getMissingLists() {
		List<FeatureList> featureLists = new ArrayList<>();
		
		FeatureList f1 = new FeatureList();
		FeatureList f2 = new FeatureList();
		
		f1.features.add(new Feature("pubmed_language_worddelimiter_rank_tfidf_title"));
		for(Feature f : ABSTRACTS) {
			f2.features.add(f);
		}
		
		featureLists.add(f1);
		featureLists.add(f2);
		
		return featureLists;
	}
	
	private static final Feature[] ABSTRACTS = {
			new Feature("pubmed_language_worddelimiter_rank_tfidf_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_bm25_1_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_bm25_2_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_bm25_3_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_bm25_4_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_bm25_5_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_lm_1_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_lm_2_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_lm_3_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_lm_4_abstract"),
			new Feature("pubmed_language_remove_nonnumeric_rank_tfidf_abstract"),
			new Feature("pubmed_language_remove_nonnumeric_rank_bm25_1_abstract"),
			new Feature("pubmed_language_remove_nonnumeric_rank_bm25_2_abstract"),
			new Feature("pubmed_language_remove_nonnumeric_rank_bm25_3_abstract"),
			new Feature("pubmed_language_remove_nonnumeric_rank_bm25_4_abstract"),
			new Feature("pubmed_language_remove_nonnumeric_rank_bm25_5_abstract"),
			new Feature("pubmed_language_remove_nonnumeric_rank_lm_1_abstract"),
			new Feature("pubmed_language_remove_nonnumeric_rank_lm_2_abstract"),
			new Feature("pubmed_language_remove_nonnumeric_rank_lm_3_abstract"),
			new Feature("pubmed_language_remove_nonnumeric_rank_lm_4_abstract"),
			new Feature("pubmed_language_simple_rank_tfidf_abstract"),
			new Feature("pubmed_language_simple_rank_bm25_1_abstract"),
			new Feature("pubmed_language_simple_rank_bm25_2_abstract"),
			new Feature("pubmed_language_simple_rank_bm25_3_abstract"),
			new Feature("pubmed_language_simple_rank_bm25_4_abstract"),
			new Feature("pubmed_language_simple_rank_bm25_5_abstract"),
			new Feature("pubmed_language_simple_rank_lm_1_abstract"),
			new Feature("pubmed_language_simple_rank_lm_2_abstract"),
			new Feature("pubmed_language_simple_rank_lm_3_abstract"),
			new Feature("pubmed_language_simple_rank_lm_4_abstract"),
			new Feature("pubmed_language_stemming_rank_tfidf_abstract"),
			new Feature("pubmed_language_stemming_rank_bm25_1_abstract"),
			new Feature("pubmed_language_stemming_rank_bm25_2_abstract"),
			new Feature("pubmed_language_stemming_rank_bm25_3_abstract"),
			new Feature("pubmed_language_stemming_rank_bm25_4_abstract"),
			new Feature("pubmed_language_stemming_rank_bm25_5_abstract"),
			new Feature("pubmed_language_stemming_rank_lm_1_abstract"),
			new Feature("pubmed_language_stemming_rank_lm_2_abstract"),
			new Feature("pubmed_language_stemming_rank_lm_3_abstract"),
			new Feature("pubmed_language_stemming_rank_lm_4_abstract")	
	};
	
	private static final Feature[] TITLES = {
			new Feature("pubmed_language_worddelimiter_rank_tfidf_title"),
			new Feature("pubmed_language_worddelimiter_rank_bm25_1_title"),
			new Feature("pubmed_language_worddelimiter_rank_bm25_2_title"),
			new Feature("pubmed_language_worddelimiter_rank_bm25_3_title"),
			new Feature("pubmed_language_worddelimiter_rank_bm25_4_title"),
			new Feature("pubmed_language_worddelimiter_rank_bm25_5_title"),
			new Feature("pubmed_language_worddelimiter_rank_lm_1_title"),
			new Feature("pubmed_language_worddelimiter_rank_lm_2_title"),
			new Feature("pubmed_language_worddelimiter_rank_lm_3_title"),
			new Feature("pubmed_language_worddelimiter_rank_lm_4_title"),
			new Feature("pubmed_language_remove_nonnumeric_rank_tfidf_title"),
			new Feature("pubmed_language_remove_nonnumeric_rank_bm25_1_title"),
			new Feature("pubmed_language_remove_nonnumeric_rank_bm25_2_title"),
			new Feature("pubmed_language_remove_nonnumeric_rank_bm25_3_title"),
			new Feature("pubmed_language_remove_nonnumeric_rank_bm25_4_title"),
			new Feature("pubmed_language_remove_nonnumeric_rank_bm25_5_title"),
			new Feature("pubmed_language_remove_nonnumeric_rank_lm_1_title"),
			new Feature("pubmed_language_remove_nonnumeric_rank_lm_2_title"),
			new Feature("pubmed_language_remove_nonnumeric_rank_lm_3_title"),
			new Feature("pubmed_language_remove_nonnumeric_rank_lm_4_title"),
			new Feature("pubmed_language_simple_rank_tfidf_title"),
			new Feature("pubmed_language_simple_rank_bm25_1_title"),
			new Feature("pubmed_language_simple_rank_bm25_2_title"),
			new Feature("pubmed_language_simple_rank_bm25_3_title"),
			new Feature("pubmed_language_simple_rank_bm25_4_title"),
			new Feature("pubmed_language_simple_rank_bm25_5_title"),
			new Feature("pubmed_language_simple_rank_lm_1_title"),
			new Feature("pubmed_language_simple_rank_lm_2_title"),
			new Feature("pubmed_language_simple_rank_lm_3_title"),
			new Feature("pubmed_language_simple_rank_lm_4_title"),
			new Feature("pubmed_language_stemming_rank_tfidf_title"),
			new Feature("pubmed_language_stemming_rank_bm25_1_title"),
			new Feature("pubmed_language_stemming_rank_bm25_2_title"),
			new Feature("pubmed_language_stemming_rank_bm25_3_title"),
			new Feature("pubmed_language_stemming_rank_bm25_4_title"),
			new Feature("pubmed_language_stemming_rank_bm25_5_title"),
			new Feature("pubmed_language_stemming_rank_lm_1_title"),
			new Feature("pubmed_language_stemming_rank_lm_2_title"),
			new Feature("pubmed_language_stemming_rank_lm_3_title"),
			new Feature("pubmed_language_stemming_rank_lm_4_title")
	};
	
	private static final Feature[] ABSTRACTS_ADVANCED_LM = {
			new Feature("pubmed_language_worddelimiter_rank_bm25_2_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_bm25_3_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_bm25_5_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_lm_3_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_lm_4_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_lm_5_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_lm_6_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_lm_7_abstract"),

			new Feature("pubmed_language_stemming_rank_bm25_2_abstract"),
			new Feature("pubmed_language_stemming_rank_bm25_3_abstract"),
			new Feature("pubmed_language_stemming_rank_bm25_5_abstract"),
			new Feature("pubmed_language_stemming_rank_lm_3_abstract"),
			new Feature("pubmed_language_stemming_rank_lm_4_abstract"),
			new Feature("pubmed_language_stemming_rank_lm_5_abstract"),
			new Feature("pubmed_language_stemming_rank_lm_6_abstract"),
			new Feature("pubmed_language_stemming_rank_lm_7_abstract"),
			  
			new Feature("pubmed_language_stemming_worddelimiter_rank_bm25_2_abstract"),
			new Feature("pubmed_language_stemming_worddelimiter_rank_bm25_3_abstract"),
			new Feature("pubmed_language_stemming_worddelimiter_rank_bm25_5_abstract"),
			new Feature("pubmed_language_stemming_worddelimiter_rank_lm_3_abstract"),
			new Feature("pubmed_language_stemming_worddelimiter_rank_lm_4_abstract"),
			new Feature("pubmed_language_stemming_worddelimiter_rank_lm_5_abstract"),
			new Feature("pubmed_language_stemming_worddelimiter_rank_lm_6_abstract"),
			new Feature("pubmed_language_stemming_worddelimiter_rank_lm_7_abstract")	
	};

	private static final Feature[] TITLES_ADVANCED_LM = {
			new Feature("pubmed_language_worddelimiter_rank_bm25_2_title"),
			new Feature("pubmed_language_worddelimiter_rank_bm25_3_title"),
			new Feature("pubmed_language_worddelimiter_rank_bm25_5_title"),
			new Feature("pubmed_language_worddelimiter_rank_lm_3_title"),
			new Feature("pubmed_language_worddelimiter_rank_lm_4_title"),
			new Feature("pubmed_language_worddelimiter_rank_lm_5_title"),
			new Feature("pubmed_language_worddelimiter_rank_lm_6_title"),
			new Feature("pubmed_language_worddelimiter_rank_lm_7_title"),

			new Feature("pubmed_language_stemming_rank_bm25_2_title"),
			new Feature("pubmed_language_stemming_rank_bm25_3_title"),
			new Feature("pubmed_language_stemming_rank_bm25_5_title"),
			new Feature("pubmed_language_stemming_rank_lm_3_title"),
			new Feature("pubmed_language_stemming_rank_lm_4_title"),
			new Feature("pubmed_language_stemming_rank_lm_5_title"),
			new Feature("pubmed_language_stemming_rank_lm_6_title"),
			new Feature("pubmed_language_stemming_rank_lm_7_title"),
			  
			new Feature("pubmed_language_stemming_worddelimiter_rank_bm25_2_title"),
			new Feature("pubmed_language_stemming_worddelimiter_rank_bm25_3_title"),
			new Feature("pubmed_language_stemming_worddelimiter_rank_bm25_5_title"),
			new Feature("pubmed_language_stemming_worddelimiter_rank_lm_3_title"),
			new Feature("pubmed_language_stemming_worddelimiter_rank_lm_4_title"),
			new Feature("pubmed_language_stemming_worddelimiter_rank_lm_5_title"),
			new Feature("pubmed_language_stemming_worddelimiter_rank_lm_6_title"),
			new Feature("pubmed_language_stemming_worddelimiter_rank_lm_7_title")
	};

	private static final Feature[] ABSTRACTS_AFTER_LM = {
			new Feature("pubmed_language_stemming_rank_lm_4_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_bm25_2_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_bm25_3_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_bm25_5_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_lm_3_abstract"),
			new Feature("pubmed_language_worddelimiter_rank_lm_4_abstract"),

			new Feature("pubmed_language_stemming_rank_bm25_2_abstract"),
			new Feature("pubmed_language_stemming_rank_bm25_3_abstract"),
			new Feature("pubmed_language_stemming_rank_bm25_5_abstract"),
			new Feature("pubmed_language_stemming_rank_lm_3_abstract"),
	};
	
	private static final Feature[] TITLES_AFTER_LM = {
			new Feature("pubmed_language_worddelimiter_rank_bm25_2_title"),
			new Feature("pubmed_language_worddelimiter_rank_bm25_3_title"),
			new Feature("pubmed_language_worddelimiter_rank_bm25_5_title"),
			new Feature("pubmed_language_worddelimiter_rank_lm_3_title"),
			new Feature("pubmed_language_worddelimiter_rank_lm_4_title"),

			new Feature("pubmed_language_stemming_rank_bm25_2_title"),
			new Feature("pubmed_language_stemming_rank_bm25_3_title"),
			new Feature("pubmed_language_stemming_rank_bm25_5_title"),
			new Feature("pubmed_language_stemming_rank_lm_3_title"),
			new Feature("pubmed_language_stemming_rank_lm_4_title")
	};
	
	private static final Feature[] ABSTRACTS_AFTER_LM_MESH = {
			new Feature("mesh_pubmed_language_worddelimiter_rank_bm25_2_abstract"),
			new Feature("mesh_pubmed_language_worddelimiter_rank_bm25_3_abstract"),
			new Feature("mesh_pubmed_language_worddelimiter_rank_bm25_5_abstract"),
			new Feature("mesh_pubmed_language_worddelimiter_rank_lm_3_abstract"),
			new Feature("mesh_pubmed_language_worddelimiter_rank_lm_4_abstract"),

			new Feature("mesh_pubmed_language_stemming_rank_bm25_2_abstract"),
			new Feature("mesh_pubmed_language_stemming_rank_bm25_3_abstract"),
			new Feature("mesh_pubmed_language_stemming_rank_bm25_5_abstract"),
			new Feature("mesh_pubmed_language_stemming_rank_lm_3_abstract"),
			new Feature("mesh_pubmed_language_stemming_rank_lm_4_abstract"),
	};
	
	private static final Feature[] TITLES_AFTER_LM_MESH = {
			new Feature("mesh_pubmed_language_worddelimiter_rank_bm25_2_title"),
			new Feature("mesh_pubmed_language_worddelimiter_rank_bm25_3_title"),
			new Feature("mesh_pubmed_language_worddelimiter_rank_bm25_5_title"),
			new Feature("mesh_pubmed_language_worddelimiter_rank_lm_3_title"),
			new Feature("mesh_pubmed_language_worddelimiter_rank_lm_4_title"),

			new Feature("mesh_pubmed_language_stemming_rank_bm25_2_title"),
			new Feature("mesh_pubmed_language_stemming_rank_bm25_3_title"),
			new Feature("mesh_pubmed_language_stemming_rank_bm25_5_title"),
			new Feature("mesh_pubmed_language_stemming_rank_lm_3_title"),
			new Feature("mesh_pubmed_language_stemming_rank_lm_4_title"),
	};

	public static List<FeatureList> getSimpleList() {
		List<FeatureList> featureList = new ArrayList<FeatureList>();
		featureList.add(new FeatureList());
		featureList.add(new FeatureList());
		
		featureList.get(0).features.add(new Feature("pubmed_language_simple_rank_tfidf_abstract"));
		featureList.get(1).features.add(new Feature("pubmed_language_simple_rank_tfidf_title"));
		
		return featureList;
	}

	public static List<FeatureList> getSimpleListWithCitationRank() {
		List<FeatureList> featureLists = getSimpleList();
		
		Feature citationRank = new Feature("citation_rank");

		featureLists.add(new FeatureList());
		featureLists.get(2).features.add(citationRank);
		
		return featureLists;
	}
	
	public static List<FeatureList> getSimpleListWithMesh() {
		List<FeatureList> featureLists = getSimpleList();
		
		featureLists.add(new FeatureList());
		featureLists.get(2).features.add(new Feature("mesh_" + featureLists.get(0).features.get(0).getName()));

		featureLists.add(new FeatureList());
		featureLists.get(3).features.add(new Feature("mesh_" + featureLists.get(1).features.get(0).getName()));
		
		return featureLists;
	}

	public static List<FeatureList> getSimpleListWithOwnRanks123() {
		List<FeatureList> featureLists = getSimpleList();
		Feature ownRank11 = new Feature("combined_score_1");
		Feature ownRank12 = new Feature("combined_score_1");
		Feature ownRank21 = new Feature("combined_score_2");
		Feature ownRank22 = new Feature("combined_score_2");
		Feature ownRank31 = new Feature("combined_score_3");
		Feature ownRank32 = new Feature("combined_score_3");
		
		ownRank12.setIsMultiplier(true);
		ownRank22.setIsMultiplier(true);
		ownRank32.setIsMultiplier(true);

		featureLists.add(new FeatureList());
		featureLists.get(2).features.add(ownRank31);
		featureLists.get(2).features.add(ownRank32);
		featureLists.get(2).features.add(ownRank21);
		featureLists.get(2).features.add(ownRank22);
		featureLists.get(2).features.add(ownRank11);
		featureLists.get(2).features.add(ownRank12);
		
		return featureLists;
	}

	public static List<FeatureList> getSimpleListWithOwnRanks45() {
		List<FeatureList> featureLists = getSimpleList();
		Feature ownRank41 = new Feature("combined_score_4");
		Feature ownRank42 = new Feature("combined_score_4");
		Feature ownRank51 = new Feature("combined_score_5");
		Feature ownRank52 = new Feature("combined_score_5");
		
		ownRank42.setIsMultiplier(true);
		ownRank52.setIsMultiplier(true);

		featureLists.add(new FeatureList());
		featureLists.get(2).features.add(ownRank41);
		featureLists.get(2).features.add(ownRank42);
		featureLists.get(2).features.add(ownRank51);
		featureLists.get(2).features.add(ownRank52);
		
		return featureLists;
	}

	public static List<FeatureList> getSimpleListWithOwnRank(int i) {
		List<FeatureList> featureLists = getSimpleList();
		Feature ownRank = new Feature("combined_score_" + i);
		
		featureLists.add(new FeatureList());
		featureLists.get(2).features.add(ownRank);
		
		return featureLists;
	}
	
	public static List<FeatureList> getMeshAndFields() {
		List<FeatureList> featureLists = new ArrayList<>();
		
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		
		for(String feature : new String[] {
			"pubmed_language_worddelimiter_rank_lm_2"
		}) {
			featureLists.get(0).features.add(new Feature(feature + "_abstract"));
			featureLists.get(1).features.add(new Feature(feature + "_title"));
			featureLists.get(2).features.add(new Feature("mesh_" + feature + "_abstract"));
			featureLists.get(3).features.add(new Feature("mesh_" + feature + "_title"));
		}
		
		
		return featureLists;
	}
	
	public static List<FeatureList> getFieldsAndPR() {
		List<FeatureList> featureLists = new ArrayList<>();
		
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		
		featureLists.get(0).features.add(new Feature("pubmed_language_worddelimiter_rank_lm_2_abstract"));
		featureLists.get(1).features.add(new Feature("pubmed_language_worddelimiter_rank_lm_2_title"));
		featureLists.get(2).features.add(new Feature("citation_rank"));
		
		
		return featureLists;		
	}
	
	public static List<FeatureList> getFieldsAndOwn() {
		List<FeatureList> featureLists = new ArrayList<>();
		
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		
		featureLists.get(0).features.add(new Feature("pubmed_language_worddelimiter_rank_lm_2_abstract"));
		featureLists.get(1).features.add(new Feature("pubmed_language_worddelimiter_rank_lm_2_title"));
		featureLists.get(2).features.add(new Feature("combined_score_2"));
		
		
		return featureLists;		
	}
	
	public static List<FeatureList> getMeSHAndPR() {
		List<FeatureList> featureLists = new ArrayList<>();
		
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		
		featureLists.get(0).features.add(new Feature("pubmed_language_simple_rank_tfidf_abstract"));
		featureLists.get(1).features.add(new Feature("pubmed_language_simple_rank_tfidf_title"));		
		featureLists.get(2).features.add(new Feature("mesh_pubmed_language_simple_rank_tfidf_abstract"));
		featureLists.get(3).features.add(new Feature("mesh_pubmed_language_simple_rank_tfidf_title"));
		featureLists.get(4).features.add(new Feature("citation_rank"));
		
		
		return featureLists;		
	}
	
	public static List<FeatureList> getMeSHAndOwn() {
		List<FeatureList> featureLists = new ArrayList<>();
		
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		
		featureLists.get(0).features.add(new Feature("pubmed_language_simple_rank_tfidf_abstract"));
		featureLists.get(1).features.add(new Feature("pubmed_language_simple_rank_tfidf_title"));		
		featureLists.get(2).features.add(new Feature("mesh_pubmed_language_simple_rank_tfidf_abstract"));
		featureLists.get(3).features.add(new Feature("mesh_pubmed_language_simple_rank_tfidf_title"));
		featureLists.get(4).features.add(new Feature("combined_score_2"));
		
		
		return featureLists;		
	}

	public static List<FeatureList> getPRAndOwn() {
		List<FeatureList> featureLists = new ArrayList<>();
		
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		
		featureLists.get(0).features.add(new Feature("pubmed_language_simple_rank_tfidf_abstract"));
		featureLists.get(1).features.add(new Feature("pubmed_language_simple_rank_tfidf_title"));
		featureLists.get(2).features.add(new Feature("citation_rank"));
		featureLists.get(3).features.add(new Feature("combined_score_2"));
		
		
		return featureLists;		
	}	
	
	public static List<FeatureList> getAllAtOnce() {
		List<FeatureList> featureLists = new ArrayList<>();
		
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());

		featureLists.get(0).features.add(new Feature("pubmed_language_worddelimiter_rank_lm_2_abstract"));
		featureLists.get(1).features.add(new Feature("pubmed_language_worddelimiter_rank_lm_2_title"));
		featureLists.get(2).features.add(new Feature("citation_rank"));
		featureLists.get(3).features.add(new Feature("combined_score_2"));

		featureLists.get(4).features.add(new Feature("mesh_pubmed_language_worddelimiter_rank_lm_2_abstract"));
		featureLists.get(5).features.add(new Feature("mesh_pubmed_language_worddelimiter_rank_lm_2_title"));
		
		
		return featureLists;		
	}
	
	public static List<FeatureList> getBestLanguage() {
		List<FeatureList> featureLists = new ArrayList<>();
		
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());

		featureLists.get(0).features.add(new Feature("pubmed_language_worddelimiter_rank_lm_2_abstract"));
		featureLists.get(1).features.add(new Feature("pubmed_language_worddelimiter_rank_lm_2_title"));
		
		return featureLists;			
	}

	public static List<FeatureList> getFTandMeshAndPR() {
		List<FeatureList> featureLists = new ArrayList<>();
		
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		
		featureLists.get(0).features.add(new Feature("pubmed_language_worddelimiter_rank_lm_2_abstract"));
		featureLists.get(1).features.add(new Feature("pubmed_language_worddelimiter_rank_lm_2_title"));		
		featureLists.get(2).features.add(new Feature("mesh_pubmed_language_worddelimiter_rank_lm_2_abstract"));
		featureLists.get(3).features.add(new Feature("mesh_pubmed_language_worddelimiter_rank_lm_2_title"));
		featureLists.get(4).features.add(new Feature("citation_rank"));
		
		return featureLists;	
	}

	public static List<FeatureList> getFTandMeshAndOwn() {
		List<FeatureList> featureLists = new ArrayList<>();
		
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		featureLists.add(new FeatureList());
		
		featureLists.get(0).features.add(new Feature("pubmed_language_worddelimiter_rank_lm_2_abstract"));
		featureLists.get(1).features.add(new Feature("pubmed_language_worddelimiter_rank_lm_2_title"));		
		featureLists.get(2).features.add(new Feature("mesh_pubmed_language_worddelimiter_rank_lm_2_abstract"));
		featureLists.get(3).features.add(new Feature("mesh_pubmed_language_worddelimiter_rank_lm_2_title"));
		featureLists.get(4).features.add(new Feature("combined_score_2"));
		
		return featureLists;	
	}
	
}
