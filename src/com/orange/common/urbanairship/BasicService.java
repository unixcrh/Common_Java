package com.orange.common.urbanairship;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public abstract class BasicService {

	private static final Logger log = Logger.getLogger(BasicService.class.getName());

	static final String URBANAIRSHIP_URL = "https://go.urbanairship.com";
	static final String URBANAIRSHIP_URL_DEVICE = "/api/device_tokens/";
	static final String URBANAIRSHIP_URL_PUSH = "/api/push/";
	
	JSONObject requestJSON = new JSONObject();
	JSONObject responseJSON = new JSONObject();
	
	String appKey;
	String appSecret;
	String appMasterSecret;
	
	abstract String getURL();
	abstract boolean setConnectionParameters(HttpURLConnection connection);
	abstract boolean setRequestJSON() throws JSONException;
	abstract String getPassword();
	abstract String getRequestMethod();
	abstract int parseResponseCode(int httpResponseCode);
	
	public BasicService(String appKey, String secret, String masterSecret){
		this.appKey = appKey;
		this.appSecret = secret;
		this.appMasterSecret = masterSecret;
	}
	
	private BasicService(){	
	}
	
	String getUserName() {
		return this.appKey;
	}
	
	public void setBasicAuth(HttpURLConnection connection,
            String username, String password) throws java.io.UnsupportedEncodingException {
		
		if (connection == null || username == null | password == null){
			return;
		}
		
	    StringBuilder buf = new StringBuilder(username);
	    buf.append(':');
	    buf.append(password);
	    byte[] bytes = null;
	    try {
	            bytes = buf.toString().getBytes("ISO-8859-1");
	    } catch (java.io.UnsupportedEncodingException uee) {
	            throw uee;
	    }
	
	    String header = "Basic " + Base64.encodeBase64String(bytes);
	    connection.setRequestProperty("Authorization", header);
	}	
		
	public int handleServiceRequest(){
		
		String username = getUserName();
		String password = getPassword();
		String requestMethod = getRequestMethod();
		
		if (username == null)
			return ErrorCode.ERROR_PUSH_NO_USERNAME;
		
		if (password == null)
			return ErrorCode.ERROR_PUSH_NO_PASSWORD;
				
		if (requestMethod == null)
			return ErrorCode.ERROR_PUSH_NO_REQUESTMETHOD;
		
		URL url = null;		
		String urlString = getURL();
		HttpURLConnection connection = null;
		int result = ErrorCode.ERROR_SUCCESS;		
		try{			
			
			url = new URL(urlString);
			log.info("try to open connection on url ="+urlString);
			connection = (HttpURLConnection) url.openConnection();
			
			// set connection information
			setBasicAuth(connection, username, password);
	        connection.setDoOutput(true);
	        connection.setRequestMethod(requestMethod);
	        connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
	
	        // set request JSON body
	        setRequestJSON();
	        log.info("send request to urbanairship, URL="+urlString+", JSON data="+requestJSON.toString());
	
	        // send request
	        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
	        writer.write(requestJSON.toString());
	        writer.flush();
	        writer.close();
	
	        log.info("receive response code="+String.valueOf(connection.getResponseCode())+", response message="+connection.getResponseMessage());
	        result = parseResponseCode(connection.getResponseCode());
                       
		} catch (MalformedURLException e) {
			log.error("send urbanairship request, url="+urlString+", catch MalformedURLException="+e.toString(), e);
			return ErrorCode.ERROR_PUSH_URL_NULL;
		} catch (IOException e) {
			log.error("send urbanairship request, catch IOException="+e.toString(), e);
			result = ErrorCode.ERROR_PUSH_IOEXCETION;			
		} catch (JSONException e) {
			log.error("send urbanairship request, catch JSONException="+e.toString(), e);
			result = ErrorCode.ERROR_PUSH_JSON_EXCEPTION;		
		} catch (Exception e){
			log.error("send urbanairship request, catch Exception="+e.toString(), e);
			result = ErrorCode.ERROR_PUSH_GENERAL_EXCEPTION;
		} finally {
			if (connection != null){
				connection.disconnect();
			}
		}		
	
		return result;
		
	}
}
