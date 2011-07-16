package com.orange.common.utils.http;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;


public class HttpDownload {

	public static final Logger log = Logger.getLogger(HttpDownload.class.getName());		
	
    public static boolean downloadFile(String httpURL, String localFilePath)
    {
        if (httpURL == null || httpURL.trim().equals(""))
        {
        	log.warning("<downloadFile> http URL is null");
            return false;
        }

        try
        {
            URL tURL = new URL(httpURL);
            HttpURLConnection tHttpURLConnection = (HttpURLConnection)tURL.openConnection();
            tHttpURLConnection.connect();
            BufferedInputStream tBufferedInputStream = new BufferedInputStream(tHttpURLConnection.getInputStream());
            FileOutputStream tFileOutputStream = new FileOutputStream(localFilePath);

            int nBufferSize = 1024 * 5;
            byte[] bufContent = new byte[nBufferSize];
            int nContentSize = 0;
            while ((nContentSize = tBufferedInputStream.read(bufContent)) != -1)
            {
                tFileOutputStream.write(bufContent, 0, nContentSize);
            }

            tFileOutputStream.close();
            tBufferedInputStream.close();
            tHttpURLConnection.disconnect();

            tURL = null;
            tHttpURLConnection = null;
            tBufferedInputStream = null;
            tFileOutputStream = null;
        }
        catch (Exception e)
        {
        	log.severe("<downloadFile> catch exception, http URL="+httpURL+", local path="+localFilePath+", exception="+e.toString());
            return false;
        }

        return true;
    }
}
