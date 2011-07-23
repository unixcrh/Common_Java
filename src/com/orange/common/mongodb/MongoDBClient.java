package com.orange.common.mongodb;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.management.Query;

import org.apache.cassandra.cli.CliParser.newColumnFamily_return;

import org.apache.cassandra.thrift.Cassandra.system_add_column_family_args;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBConnector;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

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

	public MongoDBClient(String serverAddress, String dbName, String userName,
			String password) {

		try {
			this.mongo = new Mongo(serverAddress, 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace(); // TODO
		} catch (MongoException e) {
			e.printStackTrace(); // TODO
		}
		this.db = mongo.getDB(dbName);
		boolean auth = db.authenticate(userName, password.toCharArray());
		return;
	}

	public boolean insert(String tableName, DBObject docObject) {
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

		return collection.findAndModify(query, null, null, false, update, true,
				false);
	}

	public DBObject findAndModify(String tableName,
			Map<String, Object> equalCondition, Map<String, Object> updateMap) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		DBObject query = new BasicDBObject();
		// query.put(fieldName, findValue);
		query.putAll(equalCondition);

		DBObject update = new BasicDBObject();
		DBObject updateValue = new BasicDBObject();
		// updateValue.put(fieldName, modifyValue);
		updateValue.putAll(updateMap);
		update.put("$set", updateValue);
		// collection.findan
		System.out.println("query = " + query.toString());
		System.out.println("update = " + updateValue.toString());
		return collection.findAndModify(query, update);
	}

	public void save(String tableName, DBObject docObject) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return;

		collection.save(docObject);
		return;
	}

	public DBObject findOne(String tableName, String fieldName,
			String fieldValue) {
		if (fieldValue == null)
			return null;

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		DBObject query = new BasicDBObject();
		query.put(fieldName, fieldValue);
		return collection.findOne(query);
	}

	public DBObject findOne(String tableName, Map<String, String> fieldValues) {
		if (fieldValues == null)
			return null;

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		DBObject query = new BasicDBObject();
		query.putAll(fieldValues);
		return collection.findOne(query);
	}

	public DBCursor findAll(String tableName, String rangeFieldName,
			String orField, List<String> orList, boolean range, int offset,
			int limit) {
		DBCollection collection = db.getCollection(tableName);

		DBObject orderBy = new BasicDBObject();
		if (range) {
			orderBy.put(rangeFieldName, 1);
		} else {
			orderBy.put(rangeFieldName, -1);
		}
		DBObject in = new BasicDBObject();
		DBObject query = new BasicDBObject();
		if (orField == null || orField.trim().length() < 1 || orList == null
				|| orList.size() < 1) {
		} else {
			in.put("$in", orList);
			query.put(orField, in);
		}

		DBCursor result = collection.find(query).sort(orderBy).skip(offset)
				.limit(limit);
		return result;
	}

}
