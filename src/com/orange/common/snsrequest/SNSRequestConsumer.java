package com.orange.common.snsrequest;

import java.util.concurrent.BlockingQueue;

import com.orange.common.utils.DateUtil;

public class SNSRequestConsumer implements Runnable {
	public BlockingQueue<SNSRequest> snsRequestQueue;
	public SNSRequestManager snsRequestManager;

	public SNSRequestConsumer(BlockingQueue<SNSRequest> snsRequestQueue,
			SNSRequestManager snsRequestManager) {
		this.snsRequestQueue = snsRequestQueue;
		this.snsRequestManager = snsRequestManager;
	}

	private void publisSNSRequest(SNSRequest request) {
		int type = request.getSnsRequestType();
		SNS sns = SNSRequestManager.SNSFactory(type, snsRequestManager);
		String consumerKey = sns.getConsumerKey();
		String consumerSecret = sns.getConsumerSecret();
		sns.publishSNSRequest(consumerKey, consumerSecret, request);
	}

	private boolean isRuestCountEnough() {
		if (SNSRequestManager.requestCount.longValue() < SNSRequestManager.MAX_REQUEST_COUNT_PER_INTERVAL) {
			return false;
		}
		return true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			if (isRuestCountEnough()) {
				long time = DateUtil.getCurrentTime();
				long interval = time - SNSRequestManager.lasttime.get();
				if (interval > SNSRequestManager.INTERVAL) {
					SNSRequestManager.lasttime.set(DateUtil.getCurrentTime());
					SNSRequestManager.requestCount.set(0);
				} else {
					try {
						Thread.sleep(SNSRequestManager.INTERVAL - interval);
						SNSRequestManager.lasttime.set(DateUtil
								.getCurrentTime());
						SNSRequestManager.requestCount.set(0);
					} catch (InterruptedException e) {
						SNSRequestManager.log.info("<SNSRequestConsumer.run>: "
								+ "thread sleep error");
					}
				}
			} else {
				SNSRequest request = null;
				try {
					if (!isRuestCountEnough())
						request = snsRequestQueue.take();
				} catch (InterruptedException e) {
					SNSRequestManager.log.info("<SNSRequestConsumer.run>: "
							+ "fail to excute snsRequestQueue.take().");
				}
				if (request != null) {
					SNSRequestManager.requestCount.incrementAndGet();
					publisSNSRequest(request);
				}
			}
		}

	}

}
