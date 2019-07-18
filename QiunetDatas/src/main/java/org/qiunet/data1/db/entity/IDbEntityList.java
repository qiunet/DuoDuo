package org.qiunet.data1.db.entity;

import org.qiunet.data1.core.entity.IEntityList;
import org.qiunet.data1.support.IEntityBo;

public interface IDbEntityList<Key, SubKey, Bo extends IEntityBo> extends IDbEntity<Key, Bo>, IEntityList<Key, SubKey, Bo> {
}
