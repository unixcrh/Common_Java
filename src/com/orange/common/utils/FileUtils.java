package com.orange.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {

	public static String stringFromFile(File file){
        
		if (file == null || !file.exists()){ 
			return null;
        }

        try 
        {
            BufferedReader in = new BufferedReader( new FileReader(file) );
            StringBuilder builder = new StringBuilder();
            String str = null;
            while ((str = in.readLine()) != null) {
            	builder.append(str);
            }
            in.close();
            return builder.toString();
        } 
        catch (Exception e) {
        	return null;
        }		
	}
	
	  public static void delFileOrDir(File f) throws IOException {

	        if ((f == null)) {
	            throw new IllegalArgumentException("Argument " + f + " is not a file or directory. ");
	        }
	        if (f.exists() && f.isDirectory()) {
	            if (f.listFiles().length == 0) {
	                f.delete();
	            } else {
	                File delFiles[] = f.listFiles();
	                for (File subf : delFiles) {
	                    if (subf.isDirectory()) {
	                        delFileOrDir(new File(subf.getAbsolutePath()));// Recursive
	                    }
	                    subf.delete();
	                }
	                f.delete();
	            }
	        }
	    }
}
