package org.qiunet.data.db.loader;

import com.google.common.collect.Maps;
import org.qiunet.data.cache.status.EntityStatus;
import org.qiunet.data.db.entity.DbEntityList;
import org.qiunet.data.db.entity.IDbEntity;
import org.qiunet.data.support.DataSupportMapping;
import org.qiunet.utils.exceptions.CustomException;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/***
 * 跟玩家相关的数据加载管理
 *
 * @author qiunet
 * 2021/11/18 16:20
 */
public class PlayerDataLoader implements IPlayerDataLoader {
	enum  EntityOperate {INSERT, UPDATE, DELETE}
	private static final AtomicBoolean destroy = new AtomicBoolean();
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
	 * 只读
	 */
	final boolean readOnly;
	/**
     * 玩家ID
	 */
	private long playerId;

	public PlayerDataLoader(long playerId) {
		this(playerId, false);
	}

	public PlayerDataLoader(long playerId, boolean readOnly) {
		this.playerId = playerId;
		this.readOnly = readOnly;
		DataLoaderManager.instance.registerPlayerLoader(playerId, this);
	}

	/**
	 * 销毁. 顺便要更新入库
	 */
	public void destroy(){
		if (destroy.compareAndSet(false, true)) {
			DataLoaderManager.instance.unRegisterPlayerLoader(playerId);
			this.syncToDb();
		}
	}

	/**
	 * 同步数据到db
	 */
	public void syncToDb(){
		cacheAsyncToDb.syncToDb();
	}

	/**
	 * 是否已经销毁
	 * @return
	 */
	public boolean isDestroy(){
		return destroy.get();
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
	public <Do extends IDbEntity, Bo extends DbEntityBo<Do>> Bo insertDo(Do entity) {
		if (isDestroy()) {
			throw new CustomException("PlayerDataLoader id[{}] is destroy!!!!", getPlayerId());
		}

		if (readOnly) {
			throw new CustomException("Data loader read only!");
		}

		Bo bo = (Bo) DataSupportMapping.getMapping(entity.getClass()).convertBo(entity);
		bo.playerDataLoader = this;
		if (bo.getDo() instanceof DbEntityList) {
			DbEntityList aDo = (DbEntityList) bo.getDo();
			Map mapData = this.getMapData(bo.getClass());
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
	public <Data extends DbEntityBo> Data getData(Class<Data> clazz) {
		if (isDestroy()) {
			throw new CustomException("PlayerDataLoader id[{}] is destroy!!!!", getPlayerId());
		}

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
	public <SubKey, Bo extends DbEntityBo<Do>, Do extends DbEntityList<Long, SubKey, Bo>> Map<SubKey, Bo> getMapData(Class<Bo> clazz) {
		if (isDestroy()) {
			throw new CustomException("PlayerDataLoader id[{}] is destroy!!!!", getPlayerId());
		}

		return (Map<SubKey, Bo>) dataCache.computeIfAbsent(clazz, key -> {
			Map data = (Map) DataLoaderManager.instance.getData(key, playerId);
			data.values().forEach(val -> ((DbEntityBo) val).playerDataLoader = this);
			return data;
		});
	}
}
