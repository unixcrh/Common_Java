package com.orange.common.processor;

import com.orange.common.mongodb.MongoDBClient;

public abstract class ScheduleServerProcessor extends CommonProcessor {
    
    @Override
    public MongoDBClient getMongoDBClient() {
        return null;
    }

    public abstract void resetAllRunningMessage();

    public abstract BasicProcessorRequest getProcessorRequest();
    
    public abstract boolean canProcessRequest();
    
}
