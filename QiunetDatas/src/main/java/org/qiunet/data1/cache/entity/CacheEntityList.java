package org.qiunet.data1.cache.entity;


import org.qiunet.data1.support.IEntityBo;

public abstract class CacheEntityList<Key, SubKey, Bo extends IEntityBo<? extends CacheEntityList>> extends CacheEntity<Key, Bo> implements ICacheEntityList<Key, SubKey, Bo> {
}
