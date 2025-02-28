package org.qiunet.data.core.mongo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.mongodb.client.result.UpdateResult;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.qiunet.data.enums.EntityStatus;

import java.util.concurrent.atomic.AtomicReference;

/***
 *
 * @author qiunet
 * 2024/8/22 15:38
 ***/
public abstract class BasicPlayerMongoEntity implements IMongoEntity<Long> {
	@BsonIgnore
	@JSONField(serialize = false)
	transient PlayerDataLoader playerDataLoader;
	@BsonIgnore
	transient AtomicReference<EntityStatus> atomicStatus = new AtomicReference<>(EntityStatus.NORMAL);
	@BsonId
	protected long playerId;


	@Override
	public UpdateResult save() {
		if (playerDataLoader == null) {
			this.playerDataLoader = PlayerDataLoader.get(playerId);
		}
		playerDataLoader.save(this);
		return null;
	}

	EntityStatus entityStatus() {
		return atomicStatus.get();
	}

	public void setDeleted() {
		this.atomicStatus.set(EntityStatus.DELETE);
	}

	boolean entityUpdateToDb() {
		return atomicStatus.compareAndSet(EntityStatus.UPDATE, EntityStatus.NORMAL);
	}

	boolean entityUpdate() {
		return atomicStatus.compareAndSet(EntityStatus.NORMAL, EntityStatus.UPDATE);
	}

	public long getPlayerId() {
		return playerId;
	}

	@Override
	public Long getId() {
		return playerId;
	}
}
