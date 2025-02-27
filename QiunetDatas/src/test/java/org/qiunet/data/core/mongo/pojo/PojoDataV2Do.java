package org.qiunet.data.core.mongo.pojo;

import org.bson.codecs.pojo.annotations.BsonId;
import org.qiunet.data.core.mongo.IMongoEntity;
import org.qiunet.data.core.mongo.annotation.MongoDbEntity;

import java.util.Map;

/***
 *
 * @author qiunet
 * 2024/9/6 11:08
 ***/

@MongoDbEntity(comment = "测试嵌套")
public class PojoDataV2Do implements IMongoEntity<Long> {

	@BsonId
	private long playerId;
	/**
     * 测试嵌套
	 */
	private Map<String, Map<Integer, Integer>> map2;

	public static PojoDataV2Do valueOf(long playerId, Map<String, Map<Integer, Integer>> map2){
		PojoDataV2Do data = new PojoDataV2Do();
	    data.playerId = playerId;
	    data.map2 = map2;
		return data;
	}

	public Map<String, Map<Integer, Integer>> getMap() {
		return map2;
	}

	@Override
	public Long getId() {
		return playerId;
	}
}
