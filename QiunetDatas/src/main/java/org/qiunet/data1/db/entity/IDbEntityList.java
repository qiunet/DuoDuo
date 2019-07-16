package org.qiunet.data1.db.entity;

import org.qiunet.data1.core.entity.IEntityList;
import org.qiunet.data1.support.IEntityVo;

public interface IDbEntityList<Key, SubKey, Vo extends IEntityVo> extends IDbEntity<Key, Vo>, IEntityList<Key, SubKey, Vo> {
}
