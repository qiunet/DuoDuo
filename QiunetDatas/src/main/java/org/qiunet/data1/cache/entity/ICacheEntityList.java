package org.qiunet.data1.cache.entity;

import org.qiunet.data1.core.entity.IEntityList;
import org.qiunet.data1.support.IEntityVo;

public interface ICacheEntityList<Key, SubKey, Vo extends IEntityVo> extends ICacheEntity<Key, Vo>, IEntityList<Key, SubKey, Vo> {
}
