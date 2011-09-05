package com.orange.common.urbanairship;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;


public class PushMessageService extends BasicService {
	
	String deviceToken;
	int badge = 0;
	String alertMessage = "";
	String alertLoc = "";
	List<String> alertArgs;
	String sound = "default";
	HashMap<String, Object> userInfo;	
	
	public static PushMessageService createService(String appKey, String secret, String masterSecret,
			String deviceToken, int badge, String alertMessage, String sound, HashMap<String, Object> userInfo ){
		PushMessageService service = new PushMessageService(appKey, secret, masterSecret);
		service.deviceToken = deviceToken;
		service.badge = badge;
		service.alertMessage = alertMessage;
		service.sound = sound;
		service.userInfo = userInfo;
		return service;
	}

	public static PushMessageService createService(String appKey, String secret, String masterSecret,
			String deviceToken, int badge, String alertLoc, List<String> alertArgs, String sound, HashMap<String, Object> userInfo ){
		PushMessageService service = new PushMessageService(appKey, secret, masterSecret);
		service.deviceToken = deviceToken;
		service.badge = badge;
		service.alertArgs = alertArgs;
		service.alertLoc = alertLoc;
		service.sound = sound;
		service.userInfo = userInfo;
		return service;
	}
	
	private PushMessageService(String appKey, String secret, String masterSecret) {
		super(appKey, secret, masterSecret);
	}

	@Override
	String getPassword() {
		// TODO Auto-generated method stub
		return this.appMasterSecret;
	}

	@Override
	String getRequestMethod() {
		// TODO Auto-generated method stub
		return RequestMethod.METHOD_POST;
	}

	@Override
	String getURL() {
		// TODO Auto-generated method stub
		return URBANAIRSHIP_URL + URBANAIRSHIP_URL_PUSH;
	}

	@Override
	int parseResponseCode(int httpResponseCode) {
		if (httpResponseCode == HttpResponseCode.SUCCESS)
			return ErrorCode.ERROR_SUCCESS;
		else if (httpResponseCode == HttpResponseCode.PUSH_MESSAGE_DATA_INCORRECT)
			return ErrorCode.ERROR_PUSH_INCORRECT_MESSAGE;
		else
			return ErrorCode.ERROR_PUSH_MESSAGE_FAILURE;
	}

	@Override
	boolean setConnectionParameters(HttpURLConnection connection) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	boolean setRequestJSON() throws JSONException {
		
		// part1: device tokens
		JSONArray deviceTokenArray = new JSONArray();
		deviceTokenArray.add(deviceToken);
		requestJSON.put(RequestPara.PARA_DEVICE_TOKENS, deviceTokenArray);
		
		// part2: message
		JSONObject aps = new JSONObject();
//		aps.put(RequestPara.PARA_BADGE, badge);		// don't set badge at this moment
		aps.put(RequestPara.PARA_SOUND, sound);	

		// set alert by alert message or alert localized string and parameters
		if (alertLoc != null && alertLoc.length() > 0){
			JSONObject alert = new JSONObject();
			alert.put(RequestPara.PARA_LOC_KEY, alertLoc);
			if (alertArgs != null && alertArgs.size() > 0){
				JSONArray args = new JSONArray();
				for (String arg : alertArgs){
					args.add(arg);
				}
				alert.put(RequestPara.PARA_LOC_ARGS, args);
			}
			aps.put(RequestPara.PARA_ALERT, alert);
		}
		else{
			aps.put(RequestPara.PARA_ALERT, alertMessage);
		}
		
		// set customized info
		if (userInfo != null){
			for(Entry<String, Object> entry : userInfo.entrySet()) {
				aps.put(entry.getKey(), entry.getValue());
			}
		}
				
		requestJSON.put(RequestPara.PARA_APS, aps);
		
		return true;
	}

}
