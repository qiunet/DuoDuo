package org.qiunet.data.core.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentLinkedQueue;

/***
 * Cache 类型的异步入库
 *
 * @author qiunet
 * 2021/11/18 16:43
 */
class DbEntityAsyncQueue {
	protected static final Logger logger = LoggerType.DUODUO.getLogger();

	private final ConcurrentLinkedQueue<BasicPlayerMongoEntity> syncKeyQueue = new ConcurrentLinkedQueue<>();

	void add(BasicPlayerMongoEntity entity, boolean insertImmediately) {
		if (insertImmediately) {
			this.syncToDb0(entity);
			return;
		}
		this.syncKeyQueue.add(entity);
	}

    /**
     * 数据入库
	 */
	void syncToDb(){
		BasicPlayerMongoEntity element;
		while ((element = syncKeyQueue.poll()) != null) {
			this.syncToDb0(element);
		}
	}

	private void syncToDb0(BasicPlayerMongoEntity entity) {
		if (entity.entityUpdateToDb()) {
			MongoCollection collection = MongoDbSupport.getCollection(entity.getClass());
			collection.replaceOne(Filters.eq(IMongoEntity.ID_FIELD_NAME, entity.getId()), entity, new ReplaceOptions().upsert(true));
		}
	}
}
