package com.orange.common.utils.html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtils {
	
	  public static String getEncoding(String html)
	  {
		  
		  Pattern p = Pattern.compile("(?i)\bcharset=(?<charset>[-a-zA-Z_0-9]+)"); 
          Matcher m = p.matcher(html); 
          while (m.find()) { 
              String s = m.group(); 
              return s;
          } 
          
          return null;
	  }
}
