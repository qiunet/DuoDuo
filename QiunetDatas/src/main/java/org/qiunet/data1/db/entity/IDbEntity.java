package org.qiunet.data1.db.entity;

import org.qiunet.data1.core.entity.IEntity;
import org.qiunet.data1.support.IEntityBo;

/***
 * 默认不使用 Cache
 * @param <Key>
 */
public interface IDbEntity<Key, Bo extends IEntityBo> extends IEntity<Key, Bo> {


}
