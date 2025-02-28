package org.qiunet.data.core.mongo;

import com.google.common.collect.Maps;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.qiunet.data.db.loader.SyncImmediately;
import org.qiunet.data.enums.EntityStatus;
import org.qiunet.utils.exceptions.CustomException;

import java.util.Map;

/***
 * 跟玩家相关的数据加载管理
 *
 * @author qiunet
 * 2021/11/18 16:20
 */
public class PlayerDataLoader implements IPlayerDataLoader {

	private static final BasicPlayerMongoEntity NULL = new BasicPlayerMongoEntity() {
		@Override
		public UpdateResult save() {
			throw new ClassCastException("Not Support in NULL Object");
		}
		@Override
		public DeleteResult delete() {
			throw new ClassCastException("Not Support in NULL Object");
		}
	};
	/**
	 * 玩家的所有数据
	 */
	final Map<Class<? extends BasicPlayerMongoEntity>, BasicPlayerMongoEntity> dataCache = Maps.newConcurrentMap();
	/**
     * 玩家有修改的数据. 需要入库的
	 */
	final DbEntityAsyncQueue cacheAsyncToDb = new DbEntityAsyncQueue();
	/**
     * 玩家ID
	 */
	private final long playerId;
	/**
	 * 是否离线用户.
	 * 离线用户直接落地数据库
	 */
	boolean offline;

	PlayerDataLoader(long playerId) {
		this.playerId = playerId;
	}

	public void setOffline(boolean offline) {
		this.offline = offline;
	}

	/**
	 *
	 * @param playerId 玩家id
	 * @return 玩家的数据加载
	 */
	public static PlayerDataLoader get(long playerId) {
		return DataLoaderManager.instance.getPlayerLoader(playerId);
	}

	@Override
	public long getId() {
		return playerId;
	}

	/**
	 * 销毁. 顺便要更新入库
	 */
	public void unregister(){
		// 同步 并 移除数据
		this.remove();
		DataLoaderManager.instance.unRegisterPlayerLoader(playerId);
	}

	/**
	 * 被移除
	 */
	void remove() {
		this.syncToDb();
		// 清除数据. 下次上线重新获取新数据
		dataCache.clear();
	}

	/**
	 * 同步数据到db
	 */
	public void syncToDb(){
		cacheAsyncToDb.syncToDb();
	}
	/**
	 * 获得玩家ID
	 * @return
	 */
	public long getPlayerId() {
		return playerId;
	}

	@Override
	public PlayerDataLoader dataLoader() {
		return this;
	}

	@Override
	public <Do extends BasicPlayerMongoEntity> void save(Do entity, boolean persistenceImmediately) {
		if (offline || entity.getClass().isAnnotationPresent(SyncImmediately.class)) {
			persistenceImmediately = true;
		}

		if (entity.entityStatus() == EntityStatus.DELETE) {
			throw new CustomException("Entity already deleted!!");
		}

		this.dataCache.put(entity.getClass(), entity);
		entity.playerDataLoader = this;
		if (entity.entityUpdate()) {
			cacheAsyncToDb.add(entity, persistenceImmediately);
		}
	}

	@Override
	public <Entity extends BasicPlayerMongoEntity> Entity getEntity(Class<Entity> clazz) {
		Object data = dataCache.computeIfAbsent(clazz, key -> {
			MongoCollection<Entity> collection = MongoDbSupport.getCollection(clazz);
			BasicPlayerMongoEntity obj = collection.find(Filters.eq("_id", playerId)).first();
			if (obj != null) {
				obj.playerDataLoader = this;
			}else {
				obj = NULL;
			}
			return obj;
		});

		if (data == NULL) {
			return null;
		}
		return (Entity) data;
	}
}
