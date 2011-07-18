package com.orange.common.mongodb;

import java.net.UnknownHostException;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

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

	public boolean insert(String tableName, BasicDBObject docObject) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return false;
		
		collection.insert(docObject);		
		return true;
	}

	public DBObject findAndModify(String tableName, String fieldName,
			int findValue, int modifyValue) {
		
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		DBObject query = new BasicDBObject();
		query.put(fieldName, findValue);
		
		DBObject update = new BasicDBObject();
		DBObject updateValue = new BasicDBObject();
		updateValue.put(fieldName, modifyValue);
		update.put("$set", updateValue);
		
		return collection.findAndModify(query, null, null, false, update, true, false);
	}

	public void save(String tableName, DBObject docObject) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return;
		
		collection.save(docObject);
		return;
	}

	public DBObject findOne(String tableName, String fieldName, String fieldValue) {
		if (fieldValue == null)
			return null;
		
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;
		
		DBObject query = new BasicDBObject();
		query.put(fieldName, fieldValue);
		return collection.findOne(query);		
	}

	public Object findOne(String tableName, Map<String, String> fieldValues) {
		if (fieldValues == null)
			return null;
		
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;
		
		DBObject query = new BasicDBObject();
		query.putAll(fieldValues);
		return collection.findOne(query);		
	}


	
		
}
