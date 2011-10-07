package com.orange.common.upload;

public class UploadFileResult {

	UploadErrorCode errorCode;
	String localPathURL;
	String remotePathURL;
	
	public UploadFileResult(UploadErrorCode errorCode, String localPathURL, String remotePathURL){
		this.errorCode = errorCode;
		this.localPathURL = localPathURL;
		this.remotePathURL = remotePathURL;
	}

	public UploadErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(UploadErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public String getLocalPathURL() {
		return localPathURL;
	}

	public void setLocalPathURL(String localPathURL) {
		this.localPathURL = localPathURL;
	}

	public String getRemotePathURL() {
		return remotePathURL;
	}

	public void setRemotePathURL(String remotePathURL) {
		this.remotePathURL = remotePathURL;
	}
	
	
}
