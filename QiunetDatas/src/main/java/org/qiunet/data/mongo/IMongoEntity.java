package org.qiunet.data.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.qiunet.utils.exceptions.CustomException;

/***
 *  mongodb 对象的接口
 *  对象只会找一层. 不会找父类!
 *
 * @author qiunet
 * 2023/8/22 17:46
 */
public interface IMongoEntity<Key> {
	String ID_FIELD_NAME = "_id";
	/**
	 * 获得id
	 *
	 * @return id
	 */
	Key getId();

	static <K, T extends IMongoEntity<K>> T _find(Class<T> clz, K id) {
		if (BasicPlayerMongoEntity.class.isAssignableFrom(clz)) {
			throw new CustomException("Use IPlayerDataLoader.getEntity(Clz) instead!");
		}

		MongoCollection<T> collection = MongoDbSupport.getCollection(clz);
		return collection.find(Filters.eq(ID_FIELD_NAME, id)).first();
	}

	/**
	 * 插入或者更新
	 */
	default UpdateResult save() {
		MongoCollection collection = MongoDbSupport.getCollection(this.getClass());
		return collection.replaceOne(Filters.eq(ID_FIELD_NAME, getId()), this, new ReplaceOptions().upsert(true));
	}
	/**
	 * 删除记录
	 */
	default DeleteResult delete() {
		MongoCollection collection = MongoDbSupport.getCollection(this.getClass());
		return collection.deleteOne(Filters.eq(ID_FIELD_NAME, getId()));
	}
}
