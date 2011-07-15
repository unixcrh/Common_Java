package com.orange.common.utils.similarity;

public class EDAlgorithm implements SimilarityAlgorithm {

	private int minThree(int a, int b, int c) {
		return Math.min(a, Math.min(c, b));
	}

	private int countEditDistance(String a, String b) {
		if (a == null || b == null)
			return -1;
		if (a.length() == 0 || b.length() == 0)
			return Math.max(a.length(), b.length());
		int ed[][] = new int[a.length() + 1][b.length() + 1];
		int al = a.length();
		int bl = b.length();
		for (int i = 0; i <= al; ++i) {
			ed[i][0] = i;
		}
		for (int i = 0; i <= bl; ++i) {
			ed[0][i] = i;
		}
		for (int i = 1; i <= al; ++i) {
			for (int j = 1; j <= bl; ++j) {
				int cost = 0;
				if (a.charAt(i - 1) != b.charAt(j - 1))
					cost = 1;
				ed[i][j] = minThree(ed[i][j - 1] + 1, ed[i - 1][j] + 1,
						ed[i - 1][j - 1] + cost);
			}
		}
		return ed[a.length()][b.length()];
	}

	private double getSimilarity(int alength,int blength,int distance){
		double al = alength;
		double bl = blength;
		double d = distance;
		return (1- d/Math.max(al, bl))*100;
	}
	@Override
	public double getStringSimilarity(String a, String b) {
		// TODO Auto-generated method stub
		if (a == null || b == null || a.length() < 1 || b.length() < 1)
			return 0;
		int distance = countEditDistance(a, b);
		return getSimilarity(a.length(), b.length(), distance);
	}

}
