package org.qiunet.data1.cache.entity;

import org.qiunet.data1.core.entity.IEntityList;
import org.qiunet.data1.support.IEntityBo;

public interface ICacheEntityList<Key, SubKey, Bo extends IEntityBo> extends ICacheEntity<Key, Bo>, IEntityList<Key, SubKey, Bo> {
}
