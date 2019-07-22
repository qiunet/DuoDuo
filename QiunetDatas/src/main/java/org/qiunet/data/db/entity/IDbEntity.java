package org.qiunet.data.db.entity;

import org.qiunet.data.core.entity.IEntity;
import org.qiunet.data.support.IEntityBo;

/***
 * 默认不使用 Cache
 * @param <Key>
 */
public interface IDbEntity<Key, Bo extends IEntityBo> extends IEntity<Key, Bo> {


}
