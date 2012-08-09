package com.orange.common.utils;

import java.util.Random;

public class RandomUtil {

	public static int random(int n){
		if (n <= 0){
			return 0;
		}
		
		Random random = new Random();
		int val = random.nextInt(n);
		return val;		
	}
}
