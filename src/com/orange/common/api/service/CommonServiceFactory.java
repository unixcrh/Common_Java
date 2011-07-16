package com.orange.common.api.service;

public abstract class CommonServiceFactory {
	public abstract CommonService createServiceObjectByMethod(String method);
}
