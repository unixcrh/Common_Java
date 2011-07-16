package com.orange.common.processor;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class BasicProcessorRequest {
	
	int requestCode;
	int requestId;
	
	static AtomicInteger requestIdCounter = new AtomicInteger();
	
	public BasicProcessorRequest(){
		requestId = requestIdCounter.incrementAndGet();
	}
	
	public int getRequestCode() {
		return requestCode;
	}
	
	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}
	
	public int getRequestId() {
		return requestId;
	}	
	
	abstract public void execute(CommonProcessor mainProcessor);

	public String getRequestStringId() {		
		return String.format("%10d", requestId);
	}
}
