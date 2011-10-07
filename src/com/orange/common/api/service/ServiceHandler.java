package com.orange.common.api.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.sf.json.JSONException;

import me.prettyprint.hector.api.exceptions.HectorException;

import com.orange.common.cassandra.CassandraClient;
import com.orange.common.mongodb.MongoDBClient;

public class ServiceHandler {
	
	private static final Logger log = Logger.getLogger(ServiceHandler.class
			.getName());

	CassandraClient cassandraClient = null;	
	MongoDBClient mongoClient = null;
	CommonServiceFactory serviceFactory = null;

	
	public static ServiceHandler getServiceHandler(CassandraClient cassandraClient, CommonServiceFactory serviceFactory) {
		ServiceHandler handler = new ServiceHandler();
		handler.cassandraClient = cassandraClient;
		handler.serviceFactory = serviceFactory;
		return handler;
	}

	public static ServiceHandler getServiceHandler(MongoDBClient mongoClient, CommonServiceFactory serviceFactory) {
		ServiceHandler handler = new ServiceHandler();
		handler.mongoClient = mongoClient;
		handler.serviceFactory = serviceFactory;
		return handler;
	}

	
	public void handlRequest(HttpServletRequest request,
			HttpServletResponse response) {

		boolean gzip = isGzipEncoding(request); 

		printRequest(request);

		String method = request.getParameter(CommonParameter.METHOD);
		CommonService obj = null;
		obj = serviceFactory.createServiceObjectByMethod(method);

		try {

			if (obj == null) {
				sendResponseByErrorCode(response,
						CommonErrorCode.ERROR_PARA_METHOD_NOT_FOUND, gzip);
				return;
			}

			obj.setCassandraClient(cassandraClient);
			obj.setMongoClient(mongoClient);
			obj.setRequest(request);
			
			if (!obj.validateSecurity(request)) {
				sendResponseByErrorCode(response,
						CommonErrorCode.ERROR_INVALID_SECURITY, gzip);
				return;
			}

			// parse request parameters
			if (!obj.setDataFromRequest(request)) {
				sendResponseByErrorCode(response, obj.resultCode, gzip);
				return;
			}

			// print parameters
			obj.printData();

			// handle request
			obj.handleData();
		} catch (HectorException e) {
			obj.resultCode = CommonErrorCode.ERROR_CASSANDRA;
			log.error("catch DB exception=" + e.toString(), e);
		} catch (JSONException e) {
			obj.resultCode = CommonErrorCode.ERROR_JSON;
			log.error("catch JSON exception=" + e.toString(), e);
		} catch (Exception e) {
			obj.resultCode = CommonErrorCode.ERROR_SYSTEM;
			log.error("catch general exception=" + e.toString(), e);
		} finally {
		}

		String responseData = obj.getResponseString();
		String responseType = obj.resultType;

		// send back response
		sendResponse(response, responseData, responseType, gzip);

	}

	void printRequest(HttpServletRequest request) {
		log.info("[RECV] request = " + request.getQueryString());
	}

	void printResponse(HttpServletResponse reponse, String responseData) {
		String printStr = "";
		int len = responseData.length();
		final int MAX_PRINT_LEN = 500;
		if (len <= MAX_PRINT_LEN){
			printStr = responseData;
		}
		else{
			printStr = responseData.substring(0, MAX_PRINT_LEN).concat("..."); 
		}
		log.info("[SEND] response data = " + printStr);
	}
	
	private static boolean isGzipEncoding(HttpServletRequest request){ 
        boolean flag=false; 
//        String encoding = request.getHeader("Accept-Encoding");                
//        if(encoding.indexOf("gzip")!=-1){ 
//          flag=true; 
//        } 
        return flag; 
    } 

    private static final String ACCEPT_ENCODING = "Accept-Encoding";
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String CONTENT_ENCODING = "Content-Encoding";
    private static final String CONTENT_ENCODING_GZIP = "gzip";
    private static final String CONTENT_TYPE_TEXT_PLAIN_UTF8 = "text/plain; charset=utf-8";
    private static final String GENERIC_FAILURE_MSG = "The call failed on the server; see server log for details";
	
	void sendResponse(HttpServletResponse response, String responseData, String responseType, boolean gzip) {
		printResponse(response, responseData);
		response.setContentType(responseType);
		try {

			if (gzip){
				byte[] reply = responseData.getBytes(CHARSET_UTF8);
		        String contentType = CONTENT_TYPE_TEXT_PLAIN_UTF8;
				
				ByteArrayOutputStream output = null;
	            GZIPOutputStream gzipOutputStream = null;
	            try {
	                output = new ByteArrayOutputStream(reply.length);
	                gzipOutputStream = new GZIPOutputStream(output);
	                gzipOutputStream.write(reply);
	                gzipOutputStream.finish();
	                gzipOutputStream.flush();
	                response.setHeader(CONTENT_ENCODING, CONTENT_ENCODING_GZIP);
	                reply = output.toByteArray();
	            } catch (IOException e) {
	                log.error("send gzip reponse but catch exception = " + e.toString(), e);
	                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            } finally {
	                if (null != gzipOutputStream) {
	                    gzipOutputStream.close();
	                }
	                if (null != output) {
	                    output.close();
	                }
	            }
	            
	            response.setContentLength(reply.length);
	            response.setContentType(contentType);
	            response.setStatus(HttpServletResponse.SC_OK);
	            response.getOutputStream().write(reply);
	            response.getOutputStream().flush();
	        }
			else{
				response.getWriter().write(responseData);
				response.getWriter().flush();
			}
		} catch (IOException e) {
			log.error("sendResponse, catch exception=" + e.toString(), e);			
		}
	}

	void sendResponseByErrorCode(HttpServletResponse response, int errorCode, boolean gzip) {
		String resultString = CommonErrorCode.getJSONByErrorCode(errorCode);
		sendResponse(response, resultString,CommonParameter.APPLICATION_JSON, gzip);
	}
}
