package org.qiunet.data.core.mongo.pojo;

import org.bson.codecs.pojo.annotations.BsonId;
import org.qiunet.data.core.mongo.IMongoEntity;
import org.qiunet.data.core.mongo.annotation.MongoDbEntity;

import java.util.List;
import java.util.Map;

/***
 *
 * @author qiunet
 * 2024/9/6 11:08
 ***/

@MongoDbEntity(comment = "pojo数据")
public class PojoDataDo implements IMongoEntity<Long> {

	@BsonId
	private long playerId;

	private Map<Integer, List<String>> map;

	public static PojoDataDo valueOf(long playerId, Map<Integer, List<String>> map){
		PojoDataDo data = new PojoDataDo();
	    data.playerId = playerId;
	    data.map = map;
		return data;
	}

	public Map<Integer, List<String>> getMap() {
		return map;
	}

	@Override
	public Long getId() {
		return playerId;
	}
}
