package org.qiunet.cfg.base;

/**
 * 嵌套map的接口
 * 得到一个 Map<Key, Map<subKey, Cfg> 结构
 * Created by qiunet.
 * 17/6/3
 */
public interface INestMapConfig<Key, SubKey> {

	public Key getKey();

	public SubKey getSubKey();
}
