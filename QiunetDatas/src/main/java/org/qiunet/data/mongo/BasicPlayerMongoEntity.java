package org.qiunet.data.mongo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.qiunet.data.enums.EntityStatus;
import org.qiunet.data.mongo.annotation.MongoDbEntity;
import org.qiunet.utils.exceptions.CustomException;

import java.util.concurrent.atomic.AtomicReference;

/***
 * IMongoEntity 基类
 * 所有需要实现玩家数据cache的mongo数据都继承该类
 *
 * @author qiunet
 * 2024/2/22 16:38
 ***/
@MongoDbEntity
public abstract class BasicPlayerMongoEntity implements IMongoEntity<Long> {
	/**
	 * 当前状态
	 */
	@BsonIgnore
	@JSONField(serialize = false)
	private final AtomicReference<EntityStatus> status = new AtomicReference<>(EntityStatus.NORMAL);

	@BsonIgnore
	@JSONField(serialize = false)
	PlayerDataLoader playerDataLoader;

	@BsonId
	private long playerId;

	public BasicPlayerMongoEntity() {
	}

	public BasicPlayerMongoEntity(long playerId) {
		this.setPlayerId(playerId);
	}

	@Override
	public UpdateResult save() {
		if (playerDataLoader == null) {
			throw new CustomException("BasicPlayerMongoEntity Need call IPlayerDataLoader.save()");
		}
		playerDataLoader.save(this, false);
		return null;
	}

	@Override
	public DeleteResult delete() {
		status.set(EntityStatus.DELETE);
		// 没有插入过的,这里是null
		if (playerDataLoader != null) {
			playerDataLoader.dataCache.put(getClass(), PlayerDataLoader.NULL);
			return IMongoEntity.super.delete();
		}
		return DeleteResult.unacknowledged();
	}

	@Override
	public Long getId() {
		return playerId;
	}

	public long getPlayerId() {
		return playerId;
	}

	protected void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	boolean atomicSetEntityStatus(EntityStatus expect, EntityStatus update) {
		return status.compareAndSet(expect, update);
	}

	EntityStatus status() {
		return status.get();
	}
}
