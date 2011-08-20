
package com.orange.common.processor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import com.orange.common.mongodb.MongoDBClient;

public abstract class CommonProcessor implements Runnable {
	
	public static final Logger log = Logger.getLogger(CommonProcessor.class.getName());		
	public static final BlockingQueue<BasicProcessorRequest> queue = new LinkedBlockingQueue<BasicProcessorRequest>();		
	
	public static BlockingQueue<BasicProcessorRequest> getQueue(){
		return queue;
	}
	
	@Override
	public void run() {
		log.info("Processor start running now...");
		while (true) {
			try {
				BasicProcessorRequest request = queue.take();
				printRequest(request);
				request.execute(this);
			} catch (InterruptedException e) {
				log.severe("Processor catch InterruptedException while running. exception="+e.toString());
			}
		}

	}
	
	public boolean putRequest(BasicProcessorRequest request){
		try {
			queue.put(request);
			return true;
		} catch (InterruptedException e) {
			log.severe("<putRequest> catch InterruptedException while running. exception="+e.toString());
			return false;
		}
	}
	
	//public abstract void processRequest(BasicProcessorRequest request);
	
	private void printRequest(BasicProcessorRequest request){
		log.info("Processing request = "+request.toString());
	}
	
	public void info(BasicProcessorRequest request, String logData){
		log.info(String.format("[%010d] %s", request.getRequestId(), logData));
	}

	public void warning(BasicProcessorRequest request, String logData){
		log.warning(String.format("[%010d] %s", request.getRequestId(), logData));
	}

	public void severe(BasicProcessorRequest request, String logData){
		log.severe(String.format("[%010d] %s", request.getRequestId(), logData));
	}	
	
	public abstract MongoDBClient getMongoDBClient();

}
