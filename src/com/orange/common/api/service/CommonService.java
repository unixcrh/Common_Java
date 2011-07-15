package com.orange.common.api.service;


import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.orange.common.cassandra.CassandraClient;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.common.utils.StringUtil;

public abstract class CommonService {
	// response data
	int resultCode = 0;
	Object resultData = null;
	
	CassandraClient cassandraClient = null;
	MongoDBClient mongoClient = null;
	HttpServletRequest request = null;

	public MongoDBClient getMongoClient() {
		return mongoClient;
	}

	public void setMongoClient(MongoDBClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@SuppressWarnings("unchecked")
	static private Map<String, Class> methodMap = null;

	@SuppressWarnings("unchecked")
	static private void initMethodMap() {
		if (methodMap != null)
			return;
		methodMap = new HashMap<String, Class>();
	}

	public CassandraClient getCassandraClient() {
		return cassandraClient;
	}

	public void setCassandraClient(CassandraClient cassandraClient) {
		this.cassandraClient = cassandraClient;
	}

	public static final Logger log = Logger.getLogger(CommonService.class
			.getName());

	@SuppressWarnings("unchecked")
	public static CommonService createServiceObjectByMethod(String method)
			throws InstantiationException, IllegalAccessException {
		initMethodMap();
		Class classObj = methodMap.get(method);
		if (classObj == null) {
			log.warning("Cannot find service object for METHOD = " + method);
			return null;
		}

		CommonService obj = (CommonService) classObj.newInstance();
		if (obj == null) {
			log
					.warning("Cannot create service object by given class for method = "
							+ method);
		}
		return obj;
	}

	// save data from request to object fields
	public abstract boolean setDataFromRequest(HttpServletRequest request);

	// print object fields (for request data)
	public abstract void printData();

	// return false if this method doesn't need security check
	public abstract boolean needSecurityCheck();

	// handle request, business logic implementation here
	// need to set responseData and resultCode and return them as JSON string
	public abstract void handleData();

	public String getResponseString() {
		JSONObject resultObject = new JSONObject();
		if (resultData != null) {
			resultObject.put(CommonParameter.RET_DATA, resultData);
		}
		resultObject.put(CommonParameter.RET_CODE, resultCode);

		String retString = resultObject.toString();
		return retString;
	}

	public boolean check(String value, int errorCodeEmpty, int errorCodeNull) {
		if (value == null) {
			resultCode = errorCodeNull;
			return false;
		}
		if (value.length() == 0) {
			resultCode = errorCodeEmpty;
			return false;
		}
		return true;
	}

	static final String SHARE_KEY = "NetworkRequestShareKey";

	public boolean validateSecurity(HttpServletRequest request) {

		if (needSecurityCheck() == false) {
			log.warning("<validateSecurity> skip security check");
			return true;
		}

		String timeStamp = request.getParameter(CommonParameter.PARA_TIMESTAMP);
		String mac = request.getParameter(CommonParameter.PARA_MAC);

		// if (!check(userId, ErrorCode.ERROR_PARAMETER_USERID_EMPTY,
		// ErrorCode.ERROR_PARAMETER_USERID_NULL))
		// return false;

		if (!check(timeStamp, CommonErrorCode.ERROR_PARAMETER_TIMESTAMP_EMPTY,
				CommonErrorCode.ERROR_PARAMETER_TIMESTAMP_NULL))
			return false;

		if (!check(mac, CommonErrorCode.ERROR_PARAMETER_MAC_EMPTY,
				CommonErrorCode.ERROR_PARAMETER_MAC_NULL))
			return false;

		String input = timeStamp + SHARE_KEY;
		String encodeStr = StringUtil.md5base64encode(input);
		if (encodeStr == null) {
			log.warning("<validateSecurity> failure, input=" + input
					+ ",client mac=" + mac + ",server mac=null");
			return false;
		}

		if (encodeStr.equals(mac)) {
			log.info("<validateSecurity> OK, input=" + input + ",client mac="
					+ mac + ",server mac=" + encodeStr);
			return true;
		} else {
			log.warning("<validateSecurity> failure, input=" + input
					+ ",client mac=" + mac + ",server mac=" + encodeStr);
			return false;
		}
	}
}
