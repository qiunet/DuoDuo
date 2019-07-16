package org.qiunet.data1.db.entity;

import org.qiunet.data1.core.entity.IEntity;
import org.qiunet.data1.support.IEntityVo;

/***
 * 默认不使用 Cache
 * @param <Key>
 */
public interface IDbEntity<Key, Vo extends IEntityVo> extends IEntity<Key, Vo> {


}
