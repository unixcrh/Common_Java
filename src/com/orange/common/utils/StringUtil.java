package com.orange.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

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
	
	public static int intFromString(String str){
		if (str == null || str.length() == 0)
			return 0;
		
		Integer i = Integer.parseInt(str);
		return i.intValue();
	}

	public static double doubleFromString(String str){
		if (str == null || str.length() == 0)
			return 0.0;
		
		Double i = Double.parseDouble(str);
		return i.doubleValue();
	}
}
