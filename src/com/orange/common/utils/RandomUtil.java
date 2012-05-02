package com.orange.common.utils;

import java.util.Random;

public class RandomUtil {

	public static int random(int n){
		Random random = new Random();
		random.setSeed(System.currentTimeMillis());		
		int val = random.nextInt(n);
		return val;		
	}
}
