package org.qiunet.data1.cache.entity;


import org.qiunet.data1.support.IEntityVo;

public abstract class CacheEntityList<Key, SubKey, Vo extends IEntityVo> extends CacheEntity<Key, Vo> implements ICacheEntityList<Key, SubKey, Vo> {
}
