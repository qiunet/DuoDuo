package org.qiunet.data.db.loader;

import com.google.common.collect.Maps;
import org.qiunet.data.cache.status.EntityStatus;
import org.qiunet.data.core.select.DbParamMap;
import org.qiunet.data.core.support.db.DbSourceDatabaseSupport;
import org.qiunet.data.core.support.db.IDatabaseSupport;
import org.qiunet.data.core.support.db.Table;
import org.qiunet.data.db.entity.IDbEntityList;
import org.qiunet.data.redis.util.DbUtil;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/***
 * Cache 类型的异步入库
 *
 * @author qiunet
 * 2021/11/18 16:43
 */
class DbEntityAsyncQueue {
	protected static final Logger logger = LoggerType.DUODUO.getLogger();

	private static final Map<Class<? extends DbEntityBo>, SyncDoInfo> dbSourceCache = Maps.newConcurrentMap();

	private final ConcurrentLinkedQueue<SyncEntityElement> syncKeyQueue = new ConcurrentLinkedQueue<>();

	void add(PlayerDataLoader.EntityOperate operate, DbEntityBo entity) {
		this.syncKeyQueue.add(new SyncEntityElement(operate, entity));
	}

    /**
     * 数据入库
	 */
	void syncToDb(){
		SyncEntityElement element;
		while ((element = syncKeyQueue.poll()) != null) {
			SyncDoInfo<DbEntityBo> syncDoInfo = dbSourceCache.computeIfAbsent(element.entity.getClass(), SyncDoInfo::new);
			syncDoInfo.syncToDb(element);
		}
	}

	/**
	 * 队列的对象
	 */
	private static class SyncEntityElement<Entity extends DbEntityBo> {
		private PlayerDataLoader.EntityOperate operate;
		private Entity entity;

		SyncEntityElement(PlayerDataLoader.EntityOperate operate, Entity entity) {
			this.operate = operate;
			this.entity = entity;
		}
	}

    /**
     * db 同步需要的信息
	 */
	private static class SyncDoInfo<Entity extends DbEntityBo> {
		private IDatabaseSupport databaseSupport;

		private String insertStatement;
		private String deleteStatement;
		private String updateStatement;
		private Table table;

		public SyncDoInfo(Class<Entity> clazz) {
			Class<?> doClass = null;
			try {
				Method method = clazz.getMethod("getDo");
				doClass = method.getReturnType();
				this.table = doClass.getAnnotation(Table.class);
			} catch (NoSuchMethodException e) {
				throw new CustomException(e, "");
			}
			this.databaseSupport = DbSourceDatabaseSupport.getInstance(table.dbSource());
			this.insertStatement = DbUtil.getInsertStatement(doClass.getSimpleName());
			this.deleteStatement = DbUtil.getDeleteStatement(doClass.getSimpleName());
			this.updateStatement = DbUtil.getUpdateStatement(doClass.getSimpleName());

		}

		public void syncToDb(SyncEntityElement<Entity> element) {
			Entity entity = element.entity;
			switch (element.operate) {
				case INSERT:
					if (entity.atomicSetEntityStatus(EntityStatus.INSERT, EntityStatus.NORMAL)) {
						databaseSupport.insert(insertStatement, entity.getDo());
					}else {
						logger.error("Entity status [{}] is error, can not insert to db.", entity.entityStatus());
					}
					break;
				case UPDATE:
					if (entity.atomicSetEntityStatus(EntityStatus.UPDATE, EntityStatus.NORMAL)) {
						entity.serialize();
						databaseSupport.update(updateStatement, entity.getDo());
					}else {
						logger.error("Entity status [{}] is error, can not update to db.", entity.entityStatus());
					}
					break;
				case DELETE:
					entity.updateEntityStatus(EntityStatus.DELETE);
					DbParamMap map;
					if (IDbEntityList.class.isAssignableFrom(entity.getDo().getClass())) {
						IDbEntityList entityList = (IDbEntityList) entity.getDo();;
						map = DbParamMap.create(table, entityList.keyFieldName(), entityList.key())
								.put(entityList.subKeyFieldName(), entityList.subKey());
					}else {
						map = DbParamMap.create(table, entity.getDo().keyFieldName(), entity.getDo().key());
					}
					databaseSupport.delete(deleteStatement, map);
					break;
				default:
					throw new CustomException("Db Sync Not Support status: [" + entity.entityStatus() + "]");
			}
		}
	}
}
