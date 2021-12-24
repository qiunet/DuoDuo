package org.qiunet.data.db.loader;

import org.qiunet.data.db.entity.DbEntityList;
import org.qiunet.data.db.entity.IDbEntity;

import java.util.Map;

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
	 * @param <Bo>  DbEntityBo
	 * @return
	 */
	default <Do extends IDbEntity<?>, Bo extends DbEntityBo<Do>> Bo insertDo(Do entity) {
		return dataLoader().insertDo(entity);
	}

	/**
	 * 获得注册的数据
	 * @param clazz
	 * @param <Data>
	 * @return
	 */
	default <Data extends DbEntityBo<?>> Data getData(Class<Data> clazz){
		return dataLoader().getData(clazz);
	}
	/**
	 * 获取一个Map
	 * @param clazz bo的Class
	 * @param <SubKey> Do的subKey 类型
	 * @param <Bo> Bo类型
	 * @param <Do> Do类型
	 * @return
	 */
	default <SubKey, Bo extends DbEntityBo<Do>, Do extends DbEntityList<Long, SubKey>> Map<SubKey, Bo> getMapData(Class<Bo> clazz){
		return dataLoader().getMapData(clazz);
	}
}
