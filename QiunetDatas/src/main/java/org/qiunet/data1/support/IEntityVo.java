package org.qiunet.data1.support;

import org.qiunet.data1.core.entity.IEntity;

/***
 * Vo的接口
 * @param <Po>
 */
public interface IEntityVo<Po extends IEntity> {
	/**
	 * 得到vo
	 * @return
	 */
	Po getPo();

	/**
	 * 序列化数据到po
	 */
	void serialize();

	/**
	 * 从po反序列化数据到vo
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
