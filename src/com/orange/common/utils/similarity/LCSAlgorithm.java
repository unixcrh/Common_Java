package com.orange.common.utils.similarity;

public class LCSAlgorithm implements SimilarityAlgorithm {

	private double getSimilarity(int srcLength, int destLength, int commonLength){
		double sl = (double)srcLength;
		double dl = (double)destLength;
		double cl = (double)commonLength;
		
		double result = 70*cl/Math.min(sl,dl)+30*cl/Math.max(sl,dl);
		return result;
	}

	private int getStringLCSValue(String src, String dest) {
		if (src == null || dest == null || src.length() < 1
				|| dest.length() < 1)
			return 0;
		int[][] count = new int[src.length()][dest.length()];
		if (src.charAt(0) == dest.charAt(0))
			count[0][0] = 1;
		else
			count[0][0] = 0;
		for (int i = 1; i < src.length(); i++) {
			if (src.charAt(i) == dest.charAt(0))
				count[i][0] = 1;
			else
				count[i][0] = count[i - 1][0];
		}

		for (int i = 1; i < dest.length(); i++) {
			if (dest.charAt(i) == src.charAt(0))
				count[0][i] = 1;
			else
				count[0][i] = count[0][i - 1];
		}
		for (int i = 1; i < src.length(); i++)
			for (int j = 1; j < dest.length(); j++) {
				if (src.charAt(i) == dest.charAt(j))
					count[i][j] = count[i - 1][j - 1] + 1;
				else
					count[i][j] = Math.max(count[i - 1][j], count[i][j - 1]);
			}
		return count[src.length() - 1][dest.length() - 1];
	}

	// if maxCount < 1, the return size is srcs.length;
	
	@Override
	public double getStringSimilarity(String src, String dest) {
		int commonLength = getStringLCSValue(src, dest);
		return getSimilarity(src.length(), dest.length(), commonLength);
	}

}
