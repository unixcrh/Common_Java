package com.orange.common.db;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.orange.common.mongodb.MongoDBClient;

// TODO not implement yet
public class MongoDBExecutor {

	public static final String DB_NAME = "game";
	
	
	MongoDBClient mongoClient = new MongoDBClient(DB_NAME);
    
    public MongoDBClient getMongoClient(){
    	return mongoClient;
    }
    
	protected static final int EXECUTOR_POOL_NUM = 5;

	CopyOnWriteArrayList<ExecutorService> executorList = new CopyOnWriteArrayList<ExecutorService>();
	
	// thread-safe singleton implementation
    private static MongoDBExecutor manager = new MongoDBExecutor();     
    private MongoDBExecutor(){		
    	for (int i=0; i<EXECUTOR_POOL_NUM; i++){
    		ExecutorService executor = Executors.newSingleThreadExecutor();
    		executorList.add(executor);
    	}
	} 	    
    public static MongoDBExecutor getInstance() { 
    	return manager; 
    }	
    
    public void executeDBRequest(final int sessionId, Runnable runnable){
    	ExecutorService executor = getExecutor(sessionId);
    	executor.execute(runnable);    	
    }
    
    private ExecutorService getExecutor(int sessionId) {
    	int index = sessionId % EXECUTOR_POOL_NUM;    	
		return executorList.get(index);
	}
    
    
}