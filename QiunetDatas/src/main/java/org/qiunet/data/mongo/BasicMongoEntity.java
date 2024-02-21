package org.qiunet.data.mongo;

import org.bson.codecs.pojo.annotations.BsonId;
import org.qiunet.data.mongo.annotation.MongoDbEntity;

/***
 * IMongoEntity 基类
 * 所有实现IMongoEntity 的类都是继承该类
 *
 * @author qiunet
 * 2024/2/22 16:38
 ***/
@MongoDbEntity
public abstract class BasicMongoEntity<Key> implements IMongoEntity<Key> {
	@BsonId
	private Key id;

	public BasicMongoEntity() {
	}

	public BasicMongoEntity(Key id) {
		this.setId(id);
	}

	@Override
	public Key getId() {
		return id;
	}
	/**
	 * set id
	 * @param id id
	 */
	protected void setId(Key id) {
		this.id = id;
	}
}
