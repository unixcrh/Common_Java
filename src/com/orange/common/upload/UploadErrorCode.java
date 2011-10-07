package com.orange.common.upload;

public enum UploadErrorCode {
	
	ERROR_SUCCESS(0), 
	ERROR_UPLOAD_EXCEPTION (1600001), 
	ERROR_NO_MIME_DATA (1600002), 
	ERROR_NO_FORM_ITEM (1600003), 
	ERROR_NO_DATA (1600001);
	
	final int value;

	private UploadErrorCode(int value){
		this.value = value;
	}
}
