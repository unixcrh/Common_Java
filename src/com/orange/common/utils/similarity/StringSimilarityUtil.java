package com.orange.common.utils.similarity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringSimilarityUtil {
	
	public static String[] getSortedLCSArray(String[] srcs, String dest,
			double similarity, int maxCount, SimilarityAlgorithm sa) {
		if (srcs == null || srcs.length < 1 || dest == null
				|| dest.length() < 1)
			return null;
		List<StringSimilarityComparator> lcsList = new ArrayList<StringSimilarityComparator>();
		for (int i = 0; i < srcs.length; ++i) {
			double stringSimilarity = sa.getStringSimilarity(srcs[i], dest);
			if (stringSimilarity < similarity)
				continue;
			StringSimilarityComparator temp = new StringSimilarityComparator(srcs[i], stringSimilarity);
			lcsList.add(temp);
		}
		StringSimilarityComparator stringWithLCS[] = new StringSimilarityComparator[lcsList.size()];
		lcsList.toArray(stringWithLCS);
		Arrays.sort(stringWithLCS);
		
		int count;
		if (maxCount < 1)
			count = stringWithLCS.length;
		else
			count = Math.min(stringWithLCS.length, maxCount);

		String sortedStringArray[] = new String[count];
		for (int i = 0; i < count; ++i) {
			sortedStringArray[i] = stringWithLCS[i].string;
		}
		return sortedStringArray;
	}
	
	static class StringSimilarityComparator implements Comparable<Object> {
		public String string;
		public double lcsValue;

		public StringSimilarityComparator(String string, double lcsValue) {
			this.string = string;
			this.lcsValue = lcsValue;
		}

		@Override
		public int compareTo(Object obj) {
			// TODO Auto-generated method stub
			StringSimilarityComparator temp = (StringSimilarityComparator) obj;
			if (this.lcsValue == temp.lcsValue) {
				return 0;
			}
			if (this.lcsValue < temp.lcsValue)
				return 1;
			return -1;
		}

		@Override
		public String toString() {
			return "StringWithLCS [lcsValue=" + lcsValue + ", string=" + string
					+ "]";
		}
	}

}
