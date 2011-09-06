package com.orange.common.api.service;

import java.io.IOException;

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

		printRequest(request);

		String method = request.getParameter(CommonParameter.METHOD);
		CommonService obj = null;
		obj = serviceFactory.createServiceObjectByMethod(method);

		try {

			if (obj == null) {
				sendResponseByErrorCode(response,
						CommonErrorCode.ERROR_PARA_METHOD_NOT_FOUND);
				return;
			}

			obj.setCassandraClient(cassandraClient);
			obj.setMongoClient(mongoClient);
			obj.setRequest(request);
			
			if (!obj.validateSecurity(request)) {
				sendResponseByErrorCode(response,
						CommonErrorCode.ERROR_INVALID_SECURITY);
				return;
			}

			// parse request parameters
			if (!obj.setDataFromRequest(request)) {
				sendResponseByErrorCode(response, obj.resultCode);
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
		sendResponse(response, responseData,responseType);

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

	void sendResponse(HttpServletResponse response, String responseData, String responseType) {
		printResponse(response, responseData);
		response.setContentType(responseType);
		try {
			response.getWriter().write(responseData);
			response.getWriter().flush();
		} catch (IOException e) {
			log.error("sendResponse, catch exception=" + e.toString());
		}
	}

	void sendResponseByErrorCode(HttpServletResponse response, int errorCode) {
		String resultString = CommonErrorCode.getJSONByErrorCode(errorCode);
		sendResponse(response, resultString,CommonParameter.APPLICATION_JSON);
	}
}
