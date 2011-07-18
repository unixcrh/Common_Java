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
}
