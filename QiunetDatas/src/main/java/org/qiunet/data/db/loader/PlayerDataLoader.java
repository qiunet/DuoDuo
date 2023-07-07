package org.qiunet.data.db.loader;

import com.google.common.collect.Maps;
import org.qiunet.data.async.ISyncDbExecutor;
import org.qiunet.data.cache.status.EntityStatus;
import org.qiunet.data.core.entity.IEntityList;
import org.qiunet.data.db.entity.DbEntityList;
import org.qiunet.data.db.entity.IDbEntity;
import org.qiunet.data.support.DataSupportMapping;
import org.qiunet.utils.exceptions.CustomException;

import java.util.Map;
import java.util.concurrent.Future;

/***
 * 跟玩家相关的数据加载管理
 *
 * @author qiunet
 * 2021/11/18 16:20
 */
public class PlayerDataLoader implements IPlayerDataLoader {
	enum  EntityOperate {INSERT, UPDATE, DELETE}
	static final Object NULL = new Object();
	/**
	 * 玩家的所有数据
	 */
	final Map<Class<? extends DbEntityBo>, Object> dataCache = Maps.newConcurrentMap();
	/**
     * 玩家有修改的数据. 需要入库的
	 */
	final DbEntityAsyncQueue cacheAsyncToDb = new DbEntityAsyncQueue();
	/**
	 * 同步到库
	 */
	final ISyncDbExecutor sync;
	/**
     * 玩家ID
	 */
	private final long playerId;

	private PlayerDataLoader(ISyncDbExecutor sync, long playerId) {
		this.playerId = playerId;
		this.sync = sync;
	}
	/**
	 *
	 * @param sync 同步的线程对象. 是从messageHandler取到. player id不变. 则不会变
	 * @param playerId 玩家id
	 * @return 玩家的数据加载
	 */
	public static PlayerDataLoader get(ISyncDbExecutor sync, long playerId) {
		return DataLoaderManager.instance.getPlayerLoader(playerId, () -> new PlayerDataLoader(sync, playerId));
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
	public Future<?> syncToDb(){
		return this.sync.submit(cacheAsyncToDb::syncToDb);
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

	/**
	 * 插入一个Do对象
	 * @param entity
	 * @param <Do>
	 * @param <Bo>
	 * @return
	 */
	@Override
	public <Do extends IDbEntity<?>, Bo extends DbEntityBo<Do>> Bo insertDo(Do entity) {
		Bo bo = (Bo) DataSupportMapping.getMapping(entity.getClass()).convertBo(entity);
		bo.playerDataLoader = this;
		if (bo.getDo() instanceof DbEntityList aDo) {
			Map mapData = this.getMapData(bo.getClass());
			if (mapData.containsKey(((IEntityList) entity).subKey())) {
				throw new CustomException("Current cache type {} have data already, can not insert again!", bo.getClass().getSimpleName() + ".class");
			}
			mapData.put(aDo.subKey(), bo);
		}else {
			DbEntityBo data = getData(bo.getClass());
			if (data != null) {
				throw new CustomException("Current cache already have class data. getData({}) first!", bo.getClass().getSimpleName() + ".class");
			}
			dataCache.put(bo.getClass(), bo);
		}
		if (bo.atomicSetEntityStatus(EntityStatus.INIT, EntityStatus.INSERT)) {
			cacheAsyncToDb.add(EntityOperate.INSERT, bo);
		}
		return bo;
	}

	/**
	 * 获得注册的数据
	 * @param clazz
	 * @param <Data>
	 * @return
	 */
	@Override
	public <Data extends DbEntityBo<?>> Data getData(Class<Data> clazz) {
		Object data = dataCache.computeIfAbsent(clazz, key -> {
			Object obj = DataLoaderManager.instance.getData(key, playerId);
			if (obj == null) {
				obj = NULL;
			}
			return obj;
		});
		if (data == NULL) {
			return null;
		}
		return (Data) data;
	}

	/**
	 * 获取一个Map
	 * @param clazz bo的Class
	 * @param <SubKey> Do的subKey 类型
	 * @param <Bo> Bo类型
	 * @param <Do> Do类型
	 * @return
	 */
	@Override
	public <SubKey, Bo extends DbEntityBo<Do>, Do extends DbEntityList<Long, SubKey>> Map<SubKey, Bo> getMapData(Class<Bo> clazz) {
		return (Map<SubKey, Bo>) dataCache.computeIfAbsent(clazz, key -> {
			Map data = (Map) DataLoaderManager.instance.getData(key, playerId);
			data.values().forEach(val -> ((DbEntityBo) val).playerDataLoader = this);
			return data;
		});
	}
}
