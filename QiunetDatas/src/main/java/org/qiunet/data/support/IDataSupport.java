package org.qiunet.data.support;

import org.qiunet.data.core.entity.IEntity;

/**
 * 数据操作类的接口
 * @param <Key> 数据主键
 * @param <Do> Database object (数据库对象)
 * @param <Bo> Business object (业务对象)
 */
public interface IDataSupport<Key, Do extends IEntity<Key>, Bo extends IEntityBo<Do>> {

	Bo insert(Do aDo);

	void delete(Bo bo);

	void update(Bo bo);

	Bo convertBo(Do aDo);
}
