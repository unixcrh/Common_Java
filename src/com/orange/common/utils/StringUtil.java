package com.orange.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.DataFormatException;

import org.apache.commons.codec.binary.Base64;

public class StringUtil {

	public static String md5base64encode(String input) {
		try {
			if (input == null)
				return null;

			if (input.length() == 0)
				return null;

			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(input.getBytes("UTF-8"));
			byte[] enc = md.digest();
			String base64str = Base64.encodeBase64String(enc);
			return base64str;
			
		} 
		catch (NoSuchAlgorithmException e) {
			return null;			
		} 
		catch (UnsupportedEncodingException e) {
			return null;
		}
		
	}
	
	public static String[] getStringList(String... stringList){
		return stringList;
	}
	
	public static boolean isEmpty(String str){
		return (str == null || str.trim().length() == 0);
	}
	
	public static Date dateFromIntString(String str){
		if (str == null || str.length() == 0)
			return null;
		Integer time = Integer.parseInt(str);		
		return new Date(time.longValue()*1000);
	}
	// 2011-07-19T00:00:00+08:00
	public static Date dateFromString(String str){
		
		int year   = intFromString(str.substring(0, 4));
		int month  = intFromString(str.substring(5, 7));
		int day	   = intFromString(str.substring(8, 10));
		int hour   = intFromString(str.substring(11, 13));
		int minute = intFromString(str.substring(14, 16));
		int second = intFromString(str.substring(17, 19));
		return new Date(year, month, day, hour, minute, second);
	}
	
	public static int intFromString(String str){
		if (str == null || str.length() == 0)
			return 0;
		
		Integer i = Integer.parseInt(str);
		return i.intValue();
	}

	public static double doubleFromString(String str){
		if (str == null || str.length() == 0)
			return -1;
		
		str = str.replaceAll("\\-", "");
		if (str.length() == 0){
			return -1;
		}
		
		Double i = Double.parseDouble(str);
		return i.doubleValue();
	}

	public static List<String> stringToList(String strings) {
		if (strings == null)
			return null;
		
		String[] list = strings.split(",");
		if (list == null || list.length == 0)
			return null;
		
		List<String> stringList = new ArrayList<String>();
		for (int i=0; i<list.length; i++)
			stringList.add(list[i]);

		return stringList;
	}
}
