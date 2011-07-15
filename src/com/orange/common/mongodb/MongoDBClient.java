package com.orange.common.mongodb;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoDBClient {
	
	public static String ID = "_id";
	
	Mongo mongo;
	DB db;
	
	public Mongo getMongo() {
		return mongo;
	}

	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}

	public DB getDb() {
		return db;
	}

	public void setDb(DB db) {
		this.db = db;
	}

	public MongoDBClient(String serverAddress, String dbName, String userName, String password) {
		
		try {
			this.mongo = new Mongo(serverAddress, 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();	// TODO 
		} catch (MongoException e) {
			e.printStackTrace();	// TODO
		}
		this.db = mongo.getDB(dbName);
		boolean auth = db.authenticate(userName, password.toCharArray());
		return;
	}


	
		
}
