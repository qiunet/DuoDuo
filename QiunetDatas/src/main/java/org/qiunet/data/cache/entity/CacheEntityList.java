package org.qiunet.data.cache.entity;


import org.qiunet.data.support.IEntityBo;

public abstract class CacheEntityList<Key, SubKey, Bo extends IEntityBo<? extends CacheEntityList>> extends CacheEntity<Key, Bo> implements ICacheEntityList<Key, SubKey, Bo> {
}
