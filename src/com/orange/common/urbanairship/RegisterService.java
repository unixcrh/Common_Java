package com.orange.common.urbanairship;

import java.net.HttpURLConnection;
import net.sf.json.JSONException;

public class RegisterService extends BasicService {

	String deviceAlias;
	String deviceToken;
	
	public static RegisterService createService(String appKey, String secret, String masterSecret,
			String deviceAlias, String deviceToken){
		RegisterService service = new RegisterService(appKey, secret, masterSecret);
		service.deviceAlias = deviceAlias;
		service.deviceToken = deviceToken;
		return service;
	}
	
	private RegisterService(String appKey, String secret, String masterSecret) {
		super(appKey, secret, masterSecret);
	}

	@Override
	String getURL() {
		// TODO Auto-generated method stub
		if (deviceToken == null)
			return null;
		
		return URBANAIRSHIP_URL + URBANAIRSHIP_URL_DEVICE + deviceToken;
	}

	@Override
	int parseResponseCode(int httpResponseCode) {
		if (httpResponseCode == HttpResponseCode.USER_CREATE_SUCCESS ||
			httpResponseCode == HttpResponseCode.USER_UPDATE_SUCCESS)
			return ErrorCode.ERROR_SUCCESS;
		
		return ErrorCode.ERROR_PUSH_REGISTER_FAILURE;
	}

	@Override
	boolean setConnectionParameters(HttpURLConnection connection) {
		return true;
	}

	@Override
	boolean setRequestJSON() throws JSONException {
		if (deviceAlias != null){
			requestJSON.put(RequestPara.PARA_ALIAS, deviceAlias);
		}

		return true;
	}

	@Override
	String getRequestMethod() {
		// TODO Auto-generated method stub
		return RequestMethod.METHOD_PUT;
	}

	@Override
	String getUserName(){
		return appKey;
	}

	@Override
	String getPassword(){
		return appSecret;
	}
	

}
