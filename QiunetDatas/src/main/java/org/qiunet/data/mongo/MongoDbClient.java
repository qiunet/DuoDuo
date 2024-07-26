package org.qiunet.data.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

/***
 * mongo client 封装
 * @author qiunet
 * 2023/8/23 13:55
 */
class MongoDbClient {
	private final ConnectionString connectionString;
	/**
	 * mongo db client
	 */
	private final MongoClient client;


	MongoDbClient(MongoClientSettings settings, ConnectionString connectionString) {
		this.client = MongoClients.create(settings);
		this.connectionString = connectionString;
	}

	public String getDefaultDbName() {
		return this.connectionString.getDatabase();
	}

	<E extends IMongoEntity<?>> MongoCollection<E> getCollection(EntityDbInfo dbInfo) {
		return (MongoCollection<E>) client.getDatabase(dbInfo.getDb()).getCollection(dbInfo.getCollectionName(), dbInfo.getClz());
	}

	<E extends IMongoEntity<?>> MongoCollection<Document> getDocCollection(EntityDbInfo dbInfo) {
		return client.getDatabase(dbInfo.getDb()).getCollection(dbInfo.getCollectionName());
	}

	MongoClient client() {
		return client;
	}

	void close() {
		this.client.close();
	}
}
