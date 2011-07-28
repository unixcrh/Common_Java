package com.orange.common.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

	public static List<String> stringsToList(String...strings){
		if (strings == null)
			return null;
		
		List<String> list = new ArrayList<String>();
		for (int i=0; i<strings.length; i++){
			list.add(strings[i]);
		}
		
		return list;
	}

	public static List<Integer> stringsToIntList(String...strings){
		if (strings == null)
			return null;
		
		List<Integer> list = new ArrayList<Integer>();
		for (int i=0; i<strings.length; i++){			
			list.add(Integer.parseInt(strings[i]));
		}
		
		return list;
	}

}
