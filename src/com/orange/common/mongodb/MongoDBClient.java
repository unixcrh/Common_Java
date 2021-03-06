package com.orange.common.mongodb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoDBClient {

	public static final Logger log = Logger.getLogger(MongoDBClient.class
			.getName());

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

	public MongoDBClient(String dbName) {

		String address = System.getProperty("mongodb.address");
		String portStr = System.getProperty("mongodb.port");
		int port = 27017;

		if (address == null) {
			address = "localhost";
		}
		if (portStr != null) {
			port = Integer.parseInt(portStr);
		}

		try {
			this.mongo = new Mongo(address, port);
		} catch (UnknownHostException e) {
			e.printStackTrace(); // TODO
		} catch (MongoException e) {
			e.printStackTrace(); // TODO
		}

		this.db = mongo.getDB(dbName);
		return;
	}

	public boolean insert(String tableName, DBObject docObject) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return false;
		collection.insert(docObject);
		return true;
	}

	// upsert = false
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

	public DBObject findAndModifyUpsert(String tableName, String fieldName,
			int findValue, int modifyValue) {

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		DBObject query = new BasicDBObject();
		DBObject queryOr = new BasicDBObject();
		query.put(fieldName, findValue);
		queryOr.put(fieldName, null);

		DBObject queryCondition = new BasicDBObject();
		BasicDBList values = new BasicDBList();
		values.add(query);
		values.add(queryOr);
		queryCondition.put("$or", values);

		DBObject update = new BasicDBObject();
		DBObject updateValue = new BasicDBObject();
		updateValue.put(fieldName, modifyValue);
		update.put("$set", updateValue);
		return collection.findAndModify(queryCondition, null, null, false,
				update, true, false);

	}

	// returnNew = true
	public DBObject findAndModify(String tableName, String fieldName,
			String findValue, String modifyValue) {

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

	// returnNew = true
	public DBObject findAndModify(String tableName, DBObject query,
			DBObject update) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		// System.out.println("update db, query = " + query.toString() +
		// ", update = " + update.toString());
		return collection.findAndModify(query, null, null, false, update, true,
				false);
	}
	
	public void updateOne(String tableName, DBObject query, DBObject update) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return;

		log.info("<updateOne> query = " + query.toString() + ", update = "
				+ update.toString());
		collection.update(query, update, false, false);
	}

	public void updateAll(String tableName, DBObject query, DBObject update) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return;

		log.info("<updateAll> query = " + query.toString() + ", update = "
				+ update.toString());
		collection.update(query, update, false, true);
	}

	public void upsertAll(String tableName, DBObject query, DBObject update) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return;

		log.info("<upsertAll> query = " + query.toString() + ", update = "
				+ update.toString());
		collection.update(query, update, true, true);
	}

	// returnNew = false
	public DBObject findAndModifySet(String tableName,
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

		log.info("<findAndModify> query = " + query.toString() + ", update = "
				+ update.toString());
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

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		DBObject query = new BasicDBObject();
		query.put(fieldName, fieldValue);
		return collection.findOne(query);
	}

	public DBCursor find(String tableName, String fieldName, String fieldValue,
			int limit) {

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		DBObject query = new BasicDBObject();
		query.put(fieldName, fieldValue);
		return collection.find(query).limit(limit);
	}

	public DBObject findOne(String tableName, DBObject query) {
		if (query == null)
			return null;

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		return collection.findOne(query);
	}

	public DBObject findOne(String tableName, DBObject query, DBObject returnFields) {
		if (query == null)
			return null;

		log.info("<findOne>:query = "+query+", returnFields = "+returnFields);
		
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;
		return collection.findOne(query, returnFields);
	}

	
	public DBObject findOneWithArrayLimit(String tableName, DBObject query,
			String arrayField, int offset, int limit) {
		if (query == null)
			return null;

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		DBObject slice = new BasicDBObject();
		BasicDBList sliceList = new BasicDBList();
		sliceList.add(Integer.valueOf(offset));
		sliceList.add(Integer.valueOf(limit));
		slice.put("$slice", sliceList);
		DBObject field = new BasicDBObject();
		field.put(arrayField, slice);

		return collection.findOne(query, field);
	}

	public void pullArrayKey(String tableName, DBObject query,
			String ArrayName, String key, String keyValue) {
		if (query == null) {
			return;
		}
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return;

		BasicDBObject pull = new BasicDBObject();
		BasicDBObject pullValue = new BasicDBObject();
		pullValue.put(key, keyValue);

		pull.put(ArrayName, pullValue);

		BasicDBObject update = new BasicDBObject();
		update.put("$pull", pull);

		log.info("query=" + query + " update=" + update);
		updateAll(tableName, query, update);
	}

	public boolean removeOne(String tableName, DBObject query) {
		if (query == null)
			return false;

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return false;

		collection.findAndRemove(query);
		return true;
	}
	
	public boolean remove(String tableName, DBObject query) {
		if (query == null)
			return false;

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return false;

		collection.remove(query);
		return true;
	}

	public boolean removeByObjectId(String tableName, String objectId) {
		BasicDBObject object = new BasicDBObject();
		object.put("_id", new ObjectId(objectId));
		return removeOne(tableName, object);
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

	public static final int SORT_ASCENDING = 1;
	public static final int SORT_DESCENDING = -1;

	public DBCursor find(String tableName, DBObject query, DBObject orderBy,
			int offset, int limit) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		DBCursor cursor = null;
		if (orderBy == null) {
			cursor = collection.find(query).skip(offset).limit(limit);
		} else {
			cursor = collection.find(query).sort(orderBy).skip(offset).limit(
					limit);
		}

		return cursor;
	}
	
	public DBCursor find(String tableName, DBObject query,DBObject retureFields, DBObject orderBy,
			int offset, int limit) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		DBCursor cursor = null;
		if (orderBy == null) {
			cursor = collection.find(query,retureFields).skip(offset).limit(limit);
		} else {
			cursor = collection.find(query,retureFields).sort(orderBy).skip(offset).limit(
					limit);
		}

		return cursor;
	}
	

	public DBCursor findByIds(String tableName, String fieldName,
			List<ObjectId> valueList) {

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;
		DBObject in = new BasicDBObject();
		DBObject query = new BasicDBObject();
		if (valueList == null || valueList.size() == 0)
			return null;
		in.put("$in", valueList);
		query.put(fieldName, in);
		DBCursor result = collection.find(query);
		;

		return result;
	}

	public DBCursor findByFieldInValues(String tableName, String fieldName,
			List<String> valueList, String sortFieldName,
			boolean sortAscending, int offset, int limit) {

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		DBObject orderBy = null;
		if (sortFieldName != null) {
			orderBy = new BasicDBObject();
			if (sortAscending) {
				orderBy.put(sortFieldName, 1);
			} else {
				orderBy.put(sortFieldName, -1);
			}
		}

		DBObject in = new BasicDBObject();
		DBObject query = new BasicDBObject();
		if (fieldName != null && fieldName.trim().length() > 0
				&& valueList != null && valueList.size() > 0) {
			in.put("$in", valueList);
			query.put(fieldName, in);
		}
		DBCursor result;
		if (orderBy != null) {
			result = collection.find(query).sort(orderBy).skip(offset).limit(
					limit);
		} else {
			result = collection.find(query).skip(offset).limit(limit);
		}
		return result;
	}

	public DBCursor findNearby(String tableName, String gpsFieldName,
			double latitude, double longitude, int offset, int count) {

		if (gpsFieldName == null || gpsFieldName.trim().length() == 0) {
			return null;
		}

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		List<Double> gpsList = new ArrayList<Double>();
		gpsList.add(latitude);
		gpsList.add(longitude);

		DBObject near = new BasicDBObject();
		near.put("$near", gpsList);
		DBObject query = new BasicDBObject();
		query.put(gpsFieldName, near);

		log.info("<findNearby>" + query.toString());

		DBCursor result = collection.find(query).skip(offset).limit(count);
		return result;
	}

	public DBCursor findByFieldInValues(String tableName, String fieldName,
			List<Object> valueList, int offset, int count) {

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;
		DBObject in = new BasicDBObject();
		in.put("$in", valueList);
		DBObject query = new BasicDBObject();
		query.put(fieldName, in);
//		log.info("map search = " + query);
		return collection.find(query).skip(offset).limit(count);
	}


	public DBCursor findByFieldInValues(String tableName, String fieldName,
			List<Object> valueList,DBObject returnFields,  int offset, int count) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;
		DBObject in = new BasicDBObject();
		in.put("$in", valueList);
		DBObject query = new BasicDBObject();
		query.put(fieldName, in);
//		log.info("map search = " + query);
		return collection.find(query,returnFields).skip(offset).limit(count);
	}

	public DBCursor findByFieldInValues(String tableName, String fieldName,
			List<Object> valueList,DBObject returnFields) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;
		DBObject in = new BasicDBObject();
		in.put("$in", valueList);
		DBObject query = new BasicDBObject();
		query.put(fieldName, in);
//		log.info("map search = " + query);
		return collection.find(query,returnFields);
	}

	
	public DBCursor findByFieldsInValues(String tableName,
			Map<String, List<Object>> fieldValueMap, int offset, int limit) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null) {
			return null;
		}

		DBObject query = null;
		if (fieldValueMap != null && fieldValueMap.size() > 0) {
			query = new BasicDBObject();
			for (String field : fieldValueMap.keySet()) {
				DBObject in = new BasicDBObject();
				in.put("$in", fieldValueMap.get(field));
				query.put(field, in);
			}
		}
		if (query == null) {
			return collection.find().skip(offset).limit(limit);
		}
		return collection.find(query).skip(offset).limit(limit);
	}
	
	public boolean inc(String tableName, String keyFieldName,
			Object keyFieldValue, String counterName, int counterValue) {

		DBCollection collection = db.getCollection(tableName);
		if (collection == null) {
			return false;
		}

		if (keyFieldName == null || counterName == null)
			return false;

		BasicDBObject query = new BasicDBObject();
		query.put(keyFieldName, keyFieldValue);

		BasicDBObject inc = new BasicDBObject();
		BasicDBObject incValue = new BasicDBObject();
		incValue.put(counterName, counterValue);
		inc.put("$inc", incValue);

		collection.update(query, inc);
		return true;
	}

	public void updateOrInsert(String tableName, DBObject query, DBObject update) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return;

		collection.update(query, update, true, false);
	}

	public DBObject findOne(String tableName, String fieldName, ObjectId value) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		DBObject query = new BasicDBObject();
		query.put(fieldName, value);
		return collection.findOne(query);
	}

	public DBObject findOne(String tableName, String fieldName, Object value, DBObject returnFields) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		DBObject query = new BasicDBObject();
		query.put(fieldName, value);
		return collection.findOne(query, returnFields);
	}

	
	public DBObject findOneByObjectId(String tableName, String value) {
		return this.findOne(tableName, "_id", new ObjectId(value));
	}

	public DBObject findAndModifyInsert(String tableName, BasicDBObject query,
			BasicDBObject update) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		return collection.findAndModify(query, null, null, false, update, true,
				true);
	}

	public DBCursor findAll(String tableName) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		return collection.find();
	}

	public DBCursor find(String tableName, String fieldName, String fieldValue) {

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		DBObject query = new BasicDBObject();
		query.put(fieldName, fieldValue);
		return collection.find(query);
	}

	public Long count(String tableName, DBObject query) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		return collection.count(query);
	}

	public DBObject findOneByObjectId(String tableName, String objectId,
			DBObject fields) {
		return findOne(tableName, "_id", new ObjectId(objectId), fields);
	}
	

}
