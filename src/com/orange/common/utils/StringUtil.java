package com.orange.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	public static String base64encode(String input) {
		if (input == null)
			return null;

		if (input.length() == 0)
			return null;

		String base64str = Base64.encodeBase64String(input.getBytes());
		return base64str;
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
		try {
			SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+08:00'");
			Date date = myFormatter.parse(str);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public static int intFromString(String str){
		if (str == null || str.trim().length() == 0)
			return 0;

		if (!str.matches("[0-9]*"))
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

	public static boolean booleanFromString(String isPostString) {
		if (isPostString == null)
			return false;
		
		if (isPostString.equalsIgnoreCase("no"))
			return false;
		else
			return true;
	}

	public static boolean isValidMail(String mail){
		if(null == mail) return false;
    	
		int length = mail.length();
		if (length<10) {
			return false;
		}
		String retMail = "^[a-zA-Z0-9_.\\-]{1,}@[a-zA-Z0-9_.\\-]{1,}\\.[a-zA-Z0-9_\\-.]{1,}$";

		if ( Pattern.matches(retMail, mail)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String randomUUID(){
		return UUID.randomUUID().toString();
	}
}
