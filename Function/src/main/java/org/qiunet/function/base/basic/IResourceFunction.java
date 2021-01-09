package org.qiunet.function.base.basic;

import org.qiunet.function.base.IResourceCfg;
import org.qiunet.function.base.IResourceSubType;
import org.qiunet.function.base.IResourceType;

/***
 *
 * @Author qiunet
 * @Date 2021/1/9 22:01
 **/
public interface IResourceFunction {
	/**
	 * 获得资源配置
	 * @param cfgId
	 * @param <T>
	 * @return
	 */
	<T extends IResourceCfg> T getResById(int cfgId);
	/**
	 * 获得指定资源的subType
	 * @param cfgId 资源id
	 * @return subType
	 */
	default <T extends IResourceSubType> T getResSubType(int cfgId) {
		return getResById(cfgId).subType();
	}

	/**
	 * 获得资源类型
	 * @param cfgId
	 * @param <T>
	 * @return
	 */
	default <T extends IResourceType> T getResType(int cfgId) {
		return getResSubType(cfgId).resourceType();
	}
}
