package com.orange.common.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;

import com.orange.common.mongodb.MongoDBClient;

public class ScheduleServer implements Runnable {
    
    Logger log = Logger.getLogger(ScheduleServer.class.getName());

    BlockingQueue<BasicProcessorRequest> queue = null;
    
    MongoDBClient mongoClient = null;    
    
    private CommonProcessor processor;
    
    private static int requestCounter = 0;

    private static long startTime = 0;
    
    private int max_request_per_second = 20;
    
    private int max_thread_num = 5;

    private int sleep_interval_for_no_request = 1000;
    
    List<ScheduleServerProcessor> processorList;
    
    public ScheduleServer(CommonProcessor processor) {
        this.processor = processor;
        createProcessThreads(processor);
    }
    
    public void createProcessThreads(CommonProcessor processor) {
        processorList = new ArrayList<ScheduleServerProcessor>();
        for (int i = 0; i < max_thread_num; i++) {
            
            ScheduleServerProcessor runnable = (ScheduleServerProcessor)processor;
            processorList.add(runnable);
            
            Thread thread = new Thread(runnable);
            thread.start();
            if (i == 0) {
                setQueue(runnable.getQueue());
                setMongoDBClient(runnable.getMongoDBClient());
            }
        }
        
        if (queue == null) {
            log.info("no queue available to use, application quit");
            return;
        }
    }    
    
    public void setMongoDBClient(MongoDBClient mongoDBClient) {
        this.mongoClient = mongoDBClient;
    }

    public void setQueue(BlockingQueue<BasicProcessorRequest> queue) {
        this.queue = queue;
    }

    
    private ScheduleServerProcessor getFirstProcessor(){
        return processorList.get(0);
    }

    public void run(){
        

        ScheduleServerProcessor processor = getFirstProcessor();
        
        log.info("reset all running message.");
        processor.resetAllRunningMessage();

        while (true) {
            try {
                
               if(!processor.canProcessRequest()) {
                   //sleep 1 minute
                   log.info("sleeping, wake up util current time match process time");
                   Thread.sleep(60000);
                   continue;
               }
                
                // get 1 record and put into queue
                BasicProcessorRequest request = processor.getProcessorRequest();
                
                // if there is no record, sleep one second
                if (request == null) {
                    log.info("no request, sleep.");
                    Thread.sleep(sleep_interval_for_no_request);
                } else {
                    queue.put(request);
                }

                flowControl();

            } catch (Exception e) {
                log.fatal("<ScheduleServer> catch Exception while running. exception="+e.toString());
            }
        }
    }

    private void flowControl() {
        try {
            requestCounter++;

            if (requestCounter == 1) {
                startTime = System.currentTimeMillis();
            }

            if (requestCounter == max_request_per_second) {
                long duration = System.currentTimeMillis() - startTime;
                if (duration < sleep_interval_for_no_request) {
                    long sleepTime = sleep_interval_for_no_request - duration;
                    log.info("sleep " + sleepTime + " milliseconds for flow control");
                    Thread.sleep(sleepTime);
                }
                requestCounter = 0;
            }
        } catch (InterruptedException e) {
            log.fatal("<ScheduleServer> catch Exception while running. exception="+e.toString());
        }

    }

    public void setMax_request_per_second(int max_request_per_second) {
        this.max_request_per_second = max_request_per_second;
    }

    public void setMax_thread_num(int max_thread_num) {
        this.max_thread_num = max_thread_num;
    }

    public void setSleep_interval_for_no_request(int sleep_interval_for_no_request) {
        this.sleep_interval_for_no_request = sleep_interval_for_no_request;
    }

    
}
