package com.orange.common.utils;

import java.util.Date;

public class RankUtil {
	public static double calcTopScore(int factor, Date startDate) {
	    final double GRAVITY = 1.5;
	    Date nowDate = new Date();
	    int hours = DateUtil.calcHour(startDate, nowDate);
	    if (hours != -1) {
	        double score = (double) factor / Math.pow((hours + 2), GRAVITY);    
	        return score;
	    } else {
	        return 0; 
	    }
    }
	
	public static double calcTopScore_2(int factor, Date startDate) {
	    final double T = 24 * 60 * 60 * 1000;
	    final int N = 2;
        long time = startDate.getTime();
        if (factor > 0) {
            double score = logFuntion(N, factor) +  ((double)time / (T));
            return score;
        } else if (factor == 0){
            double score =  (double)time / (T);
            return score;
        } 
        else {
            return 0;
        }
    }
	
	private static double logFuntion(int n, int bought) {
	    if (n<0 || bought<0)
	        return 0.0;
	    double base = Math.log((double) bought) / Math.log((double) n); 
	        
        return base;  
    }
}
