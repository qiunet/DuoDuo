package org.qiunet.data.db.entity;

import org.qiunet.data.core.entity.IEntityList;
import org.qiunet.data.support.IEntityBo;

public interface IDbEntityList<Key, SubKey, Bo extends IEntityBo> extends IDbEntity<Key, Bo>, IEntityList<Key, SubKey, Bo> {
}
