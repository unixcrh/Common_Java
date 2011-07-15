package com.orange.common.snsrequest;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;
public abstract class SNS {
	protected BlockingQueue<SNSRequest> snsRequestQueue;
	protected String consumerKey = null;
	protected String consumerSecret = null;

	public static final Logger log = Logger.getLogger(SNS.class.getName());

	public SNS(String consumerKey, String consumerSecret,
			BlockingQueue<SNSRequest> snsRequestQueue) {
		this.snsRequestQueue = snsRequestQueue;
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
	}

	public SNS() {
		super();
	}

	public SNS(String consumerKey, String consumerSecret) {
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
	}

	public abstract void publishSNSRequest(String consumerKey,
			String consumerSecret, SNSRequest request);

	public abstract String toLogString(SNSRequest request, String reason);

	public void putSNSRequest(SNSRequest request) throws InterruptedException {
		snsRequestQueue.put(request);
	}

	public SNSRequest getSNSRequest(SNSRequest request)
			throws InterruptedException {
		return snsRequestQueue.take();
	}

	public void setSnsRequestQueue(BlockingQueue<SNSRequest> snsRequestQueue) {
		this.snsRequestQueue = snsRequestQueue;
	}

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
