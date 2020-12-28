package org.qiunet.function.base;

import org.qiunet.utils.scanner.anno.IgnoreEmptyWired;

/***
 * 资源管理类接口
 *
 * @author qiunet
 * 2020-12-28 12:12
 */
@IgnoreEmptyWired
public interface IResourceManager {
	/**
	 * 获得指定资源的subType
	 * @param cfgId 资源id
	 * @return subType
	 */
	IResourceSubType getResSubType(int cfgId);
}
