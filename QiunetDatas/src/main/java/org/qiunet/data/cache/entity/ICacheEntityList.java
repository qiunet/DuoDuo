package org.qiunet.data.cache.entity;

import org.qiunet.data.core.entity.IEntityList;
import org.qiunet.data.support.IEntityBo;

public interface ICacheEntityList<Key, SubKey, Bo extends IEntityBo> extends ICacheEntity<Key, Bo>, IEntityList<Key, SubKey, Bo> {
}
