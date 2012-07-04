package com.orange.common.apnsservice;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.PayloadBuilder;
import com.notnoop.exceptions.NetworkIOException;
import com.orange.common.urbanairship.ErrorCode;

public class AppApnsService extends BasicService {

	final int badge;
	final String alertMessage = null;
	final String sound;	
	final String localizeKey;
	final Collection<String>localizeValues;	
	final HashMap<String, Object> userInfo;
    final String deviceToken;
//    String payload;	

	public AppApnsService(ApnsService service, String deviceToken, int badge,
			String sound, HashMap<String, Object> userInfo, 
			String localizeKey, List<String>localizeValues) {
		
		super(service);
		
		this.deviceToken = deviceToken;
		this.badge = badge;
//		this.alertMessage = alertMessage;
		this.sound = sound;
		this.userInfo = userInfo;		
		this.localizeKey = localizeKey;
		this.localizeValues = localizeValues;
	}
//	
//	public  void updateService(String deviceToken, int badge,
//			String localizeKey, List<String>localizeValues, String sound, HashMap<String, Object> userInfo){
//		this.deviceToken = deviceToken;
//		this.badge = badge;
//		this.localizeKey = localizeKey;
//		this.localizeValues = localizeValues;
//		this.sound = sound;
//		this.userInfo = userInfo;
//	}
	
//	public static AppApnsService createService(String certificatePath,
//			String password, String deviceToken, int badge,
//			String localizeKey, List<String>localizeValues, String sound, 
//			HashMap<String, Object> userInfo) {
//
//		AppApnsService service = new AppApnsService(certificatePath, password);
//		service.deviceToken = deviceToken;
//		service.badge = badge;
//		service.localizeKey = localizeKey;
//		service.localizeValues = localizeValues;
//		service.sound = sound;
//		service.userInfo = userInfo;
//		return service;
//	}

//	public static AppApnsService createService(ApnsService service,
//			String deviceToken, int badge, String alertMessage, String sound,
//			HashMap<String, Object> userInfo) {
//
//		AppApnsService Appservice = new AppApnsService(service);
//		Appservice.deviceToken = deviceToken;
//		Appservice.badge = badge;
//		Appservice.alertMessage = alertMessage;
//		Appservice.sound = sound;
//		Appservice.userInfo = userInfo;
//		return Appservice;
//	}

	@Override
	String getPayload() {
		PayloadBuilder builder = null;
		if (alertMessage != null && alertMessage.length() != 0) {
			builder = APNS.newPayload().badge(badge).sound(sound)
			.alertBody(alertMessage);			
		}else if(localizeKey != null && localizeKey.length() != 0)
		{
			builder = APNS.newPayload().badge(badge).sound(sound)
			.localizedKey(localizeKey).localizedArguments(localizeValues);						
		}else{
			return null;
		}
		// set customized info
		if (userInfo != null) {
			builder.customFields(userInfo);
		}

		String payload = builder.build();
		return payload;
	}

	@Override
    public int handleServiceRequest(){
        int result = ErrorCode.ERROR_SUCCESS;   
        String payload = getPayload();
        if (payload == null){
        	log.warn("APNS send push = ".concat(payload).concat(", to deviceToken=".concat(deviceToken)).concat(" but payload is null"));
        	return ErrorCode.ERROR_PUSH_GENERAL_EXCEPTION;
        }
        
        try{
            apnsService.push(deviceToken, payload);
            log.info("APNS send push = ".concat(payload).concat(", to deviceToken=".concat(deviceToken)));
        }
        catch (NetworkIOException e) {
            log.error("send message to apn, catch NetworkIOException="+e.toString(), e);
            result = ErrorCode.ERROR_PUSH_NETWORK_IOEXCETION;
        }
        catch (Exception e) {
            log.error("send message to apn, catch Exception="+e.toString(), e);
            result = ErrorCode.ERROR_PUSH_GENERAL_EXCEPTION;
        }
        return result;
    }	
}
