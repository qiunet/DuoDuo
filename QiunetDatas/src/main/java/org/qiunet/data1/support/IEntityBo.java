package org.qiunet.data1.support;

import org.qiunet.data1.core.entity.IEntity;

/***
 *
 * Bo( Business Object ) 的接口
 * @param <Po>
 */
public interface IEntityBo<Po extends IEntity> {
	/**
	 * 得到Bo
	 * @return
	 */
	Po getPo();

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
		getPo().update();
	}

	/**
	 * 删除
	 */
	default void delete() {
		getPo().delete();
	}
}
