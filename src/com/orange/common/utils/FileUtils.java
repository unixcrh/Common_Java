package com.orange.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

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
}
