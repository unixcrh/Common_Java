package com.orange.common.snsrequest;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;
public abstract class SNS {

	protected String consumerKey = null;
	protected String consumerSecret = null;

	public static final Logger log = Logger.getLogger(SNS.class.getName());

	private SNS() {
		super();
	}

	public SNS(String consumerKey, String consumerSecret) {
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
	}

	public abstract void publishWeibo(String consumerKey,
			String consumerSecret, SNSRequest request);

	public abstract String toLogString(SNSRequest request, String reason);

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

}
