package com.orange.common.apnsservice;

import org.apache.log4j.Logger;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.exceptions.NetworkIOException;
import com.orange.common.urbanairship.ErrorCode;

public abstract class BasicService {
    
    private static final Logger log = Logger.getLogger(BasicService.class.getName());

    String certificatePath;
    String password;
    String deviceToken;
    String payload;
    ApnsService apnsService;
    
    public BasicService(ApnsService apnsService) {
        super();
        this.apnsService = apnsService;
    }

    @Deprecated
	public BasicService(String certificatePath, String password) {
        this.certificatePath = certificatePath;
        this.password = password;
        apnsService = APNS.newService()
                .withCert(certificatePath, password)
                .withSandboxDestination()
                .build();
    }
    
    abstract boolean setPayload();

    public int handleServiceRequest(){
        int result = ErrorCode.ERROR_SUCCESS;   
        setPayload();
        try{
            apnsService.push(deviceToken, payload);
            log.info("APNS send push = " + payload);
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

