package com.orange.common.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;

import com.orange.common.mongodb.MongoDBClient;
import com.orange.common.utils.StringUtil;

public class ScheduleServer implements Runnable {
    
    Logger log = Logger.getLogger(ScheduleServer.class.getName());

    BlockingQueue<BasicProcessorRequest> queue = null;
    
    MongoDBClient mongoClient = null;    
    
    private CommonProcessor processor;
    
    private static int requestCounter = 0;

    private static long startTime = 0;
    
    private int request_frequency = 20;
    
    private int threadNum = 5;

    private int sleep_interval = 1000;
    
    List<ScheduleServerProcessor> processorList;
    
    public ScheduleServer(CommonProcessor processor) {
        this.processor = processor;
        loadParam();
        createProcessThreads(processor);
    }
    
    public void loadParam() {
        if ( !StringUtil.isEmpty(System.getProperty("scheduleserver.request_frequency"))) {
            this.request_frequency = Integer.parseInt(System.getProperty("scheduleserver.frequency"));
        }
        if ( !StringUtil.isEmpty(System.getProperty("scheduleserver.threadNum"))) {
            this.threadNum = Integer.parseInt(System.getProperty("scheduleserver.threadnum"));
        }
        if ( !StringUtil.isEmpty(System.getProperty("scheduleserver.sleep_interval"))) {
            this.sleep_interval = Integer.parseInt(System.getProperty("scheduleserver.c"));
        }
    }

    public void createProcessThreads(CommonProcessor processor) {
        processorList = new ArrayList<ScheduleServerProcessor>();
        for (int i = 0; i < threadNum; i++) {
            
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
                    log.debug("no request, sleep "+sleep_interval+" ms");
                    Thread.sleep(sleep_interval);
                } else {
                    queue.put(request);
                }

                flowControl();

            }
            catch (Exception e) {
                log.fatal("<ScheduleServer> catch Exception while running. exception=" + e.toString());
                e.printStackTrace();
            }
        }
    }

    private void flowControl() {
        try {
            requestCounter++;

            if (requestCounter == 1) {
                startTime = System.currentTimeMillis();
            }

            if (requestCounter == request_frequency) {
                long duration = System.currentTimeMillis() - startTime;
                if (duration < sleep_interval) {
                    long sleepTime = sleep_interval - duration;
                    log.info("sleep " + sleepTime + " milliseconds for flow control");
                    Thread.sleep(sleepTime);
                }
                requestCounter = 0;
            }
        }
        catch (InterruptedException e) {
            log.fatal("<ScheduleServer> catch Exception while running. exception=" + e.toString());
        }

    }

    public void setFrequency(int frequecy) {
        this.request_frequency = frequecy;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public void setInterval(int interval) {
        this.sleep_interval = interval;
    }


}
