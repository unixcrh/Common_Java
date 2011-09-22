package com.orange.common.apnsservice;

import java.util.HashMap;
import java.util.List;

import com.notnoop.apns.APNS;
import com.notnoop.apns.PayloadBuilder;

public class ApnsService extends BasicService{
    
    int badge = 0;
    String alertMessage = "";
    String sound = "default";
    HashMap<String, Object> userInfo;   

    public ApnsService(String certificatePath, String password) {
        super(certificatePath, password);
    }
    
    public static ApnsService createService(String certificatePath, String password, String deviceToken, int badge,
                                            String alertMessage, String sound, HashMap<String, Object> userInfo) {
        
        ApnsService service = new ApnsService(certificatePath, password);
        service.deviceToken = deviceToken;
        service.badge = badge;
        service.alertMessage = alertMessage;
        service.sound = sound;
        service.userInfo = userInfo;
        return service;
    }
    
    @Override
    boolean setPayload() {
        PayloadBuilder builder = APNS.newPayload()
                .badge(badge)
                .sound(sound)
                .alertBody(alertMessage);
        
        // set customized info
        if (userInfo != null){
            builder.customFields(userInfo);
        }
        
        payload = builder.build();
        
        return true;
    }

}
