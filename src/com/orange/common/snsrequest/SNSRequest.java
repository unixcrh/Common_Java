package com.orange.common.snsrequest;

import java.io.File;

public class SNSRequest {

	public static final int REQUEST_Type_QQ_WEIBO = 1;
	public static final int REQUEST_Type_SINA_WEIBO = 2;
	public static final int REQUEST_Type_RENREN = 3;

	private String tokenKey = null;
	private String tokenSecret = null;
	private String text = null;
	private File imageFile = null;
	private int snsRequestType;

	public SNSRequest(int snsRequestType, String text, File imageFile,
			String tokenKey, String tokenSecret) {
		this.tokenKey = tokenKey;
		this.tokenSecret = tokenSecret;
		this.text = text;
		this.imageFile = imageFile;
		this.snsRequestType = snsRequestType;
	}

	public String getTokenKey() {
		return tokenKey;
	}

	public void setTokenKey(String tokenKey) {
		this.tokenKey = tokenKey;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public File getImageFile() {
		return imageFile;
	}

	public void setImageFile(File iamgeFile) {
		this.imageFile = iamgeFile;
	}

	public int getSnsRequestType() {
		return snsRequestType;
	}

	public void setSnsRequestType(int snsRequestType) {
		this.snsRequestType = snsRequestType;
	}

	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

	private final String split = "$_$";

	@Override
	public String toString() {
		return "[imageFile=" + imageFile + split + "snsRequestType="
				+ snsRequestType + split + "text=" + text + split + "tokenKey="
				+ tokenKey + split + "tokenSecret=" + tokenSecret + "]";
	}
}
