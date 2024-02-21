package org.qiunet.data.mongo;

/***
 * PlayerDataLoader Owner
 * @author qiunet
 * 2021/11/19 10:28
 */
public interface IPlayerDataLoader {
	/**
	 * dataLoader
	 * @return
	 */
	IPlayerDataLoader dataLoader();

	default long getId() {
		return dataLoader().getId();
	}
	/**
	 * 插入一个Do对象
	 * @param entity IDbEntity
	 * @param <Do> DbEntityDo
	 * @return
	 */
	default <Do extends BasicPlayerMongoEntity> void save(Do entity) {
		this.save(entity, false);
	}
	/**
	 * 插入一个Do对象
	 * @param entity IDbEntity
	 * @param <Do> DbEntityDo
	 * @param persistenceImmediately 立即入库
	 * @return BO object
	 */
	default <Do extends BasicPlayerMongoEntity> void save(Do entity, boolean persistenceImmediately) {
		dataLoader().save(entity, persistenceImmediately);
	}

	/**
	 * 获得Entity数据
	 * @param clazz
	 * @param <Entity>
	 * @return
	 */
	default <Entity extends BasicPlayerMongoEntity> Entity getEntity(Class<Entity> clazz){
		return dataLoader().getEntity(clazz);
	}
}
