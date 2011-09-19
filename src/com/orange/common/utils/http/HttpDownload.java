package com.orange.common.utils.http;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

public class HttpDownload {

	public static final Logger log = Logger.getLogger(HttpDownload.class.getName());
	private static final int CONNECTION_TIMEOUT = 30 * 1000;	// 30 seconds		
	
    public static boolean downloadFile(String httpURL, String localFilePath)
    {
    	boolean result = true;
    	
        if (httpURL == null || httpURL.trim().equals(""))
        {
        	log.warn("<downloadFile> http URL is null");
            return false;
        }

        HttpURLConnection tHttpURLConnection = null;
        BufferedInputStream tBufferedInputStream = null;
        FileOutputStream tFileOutputStream = null;
        
        try
        {
            URL tURL = new URL(httpURL);
            tHttpURLConnection = (HttpURLConnection)tURL.openConnection();
            tHttpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            tHttpURLConnection.setReadTimeout(CONNECTION_TIMEOUT);
            tHttpURLConnection.connect();
            tBufferedInputStream = new BufferedInputStream(tHttpURLConnection.getInputStream());
            tFileOutputStream = new FileOutputStream(localFilePath);

            int nBufferSize = 1024;
            byte[] bufContent = new byte[nBufferSize];
            int nContentSize = 0;
            while ((nContentSize = tBufferedInputStream.read(bufContent)) != -1)
            {
                tFileOutputStream.write(bufContent, 0, nContentSize);
            }
        }
        catch (Exception e)
        {
        	log.error("<downloadFile> catch exception, http URL=" + httpURL + 
        			", local path=" + localFilePath + ", exception=" + e.toString(), e);
        	
        	result = false;
        }
        finally {
        	
				try {
		        	if (tHttpURLConnection != null)
		        		tHttpURLConnection.disconnect();

		        	if (tBufferedInputStream != null)
		        		tBufferedInputStream.close();
		        	
		        	if (tFileOutputStream != null)
		        		tFileOutputStream.close();
		        	
		            tHttpURLConnection = null;
		            tBufferedInputStream = null;
		            tFileOutputStream = null;

				} catch (IOException e) {
		        	log.error("<downloadFile> catch exception at finally stage, http URL=" + httpURL + 
		        			", local path=" + localFilePath + ", exception=" + e.toString(), e);        	        	
				}
        	
        }

        return result;
    }
}
