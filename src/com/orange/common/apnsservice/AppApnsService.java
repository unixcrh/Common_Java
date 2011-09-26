package com.orange.common.apnsservice;

import java.util.HashMap;
import java.util.List;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.PayloadBuilder;

public class AppApnsService extends BasicService{
    
    int badge = 0;
    String alertMessage = "";
    String sound = "default";
    HashMap<String, Object> userInfo;   

    public AppApnsService(String certificatePath, String password) {
        super(certificatePath, password);
    }
    
    public AppApnsService(ApnsService service) {
        super(service);
    }
    
    public static AppApnsService createService(String certificatePath, String password, String deviceToken, int badge,
                                            String alertMessage, String sound, HashMap<String, Object> userInfo) {
        
        AppApnsService service = new AppApnsService(certificatePath, password);
        service.deviceToken = deviceToken;
        service.badge = badge;
        service.alertMessage = alertMessage;
        service.sound = sound;
        service.userInfo = userInfo;
        return service;
    }
    
    public static AppApnsService createService(ApnsService service, String deviceToken, int badge,
            String alertMessage, String sound, HashMap<String, Object> userInfo) {

        AppApnsService Appservice = new AppApnsService(service);
        Appservice.deviceToken = deviceToken;
        Appservice.badge = badge;
        Appservice.alertMessage = alertMessage;
        Appservice.sound = sound;
        Appservice.userInfo = userInfo;
        return Appservice;
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
