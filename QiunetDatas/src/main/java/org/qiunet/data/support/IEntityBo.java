package org.qiunet.data.support;

import org.qiunet.data.core.entity.IEntity;

/***
 *
 * Bo( Business Object ) 的接口
 * @param <Do>
 */
public interface IEntityBo<Do extends IEntity> {
	/**
	 * 得到Do
	 * @return
	 */
	Do getDo();

	/**
	 * 序列化数据到Do
	 */
	void serialize();

	/**
	 * 从po反序列化数据到Bo
	 */
	void deserialize();
	/**
	 * 更新
	 */
	default void update() {
		this.serialize();
		IDataSupport dataSupport = DataSupportMapping.getMapping(getDo().getClass());
		dataSupport.update(this);
	}

	/**
	 * 删除
	 */
	default void delete() {
		IDataSupport dataSupport = DataSupportMapping.getMapping(getDo().getClass());
		dataSupport.delete(this);
	}
}
