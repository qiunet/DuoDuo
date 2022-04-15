package org.qiunet.function.base.basic;

import org.qiunet.function.base.IResourceCfg;
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
	 * @param <Cfg>
	 * @return
	 */
	<Cfg extends IResourceCfg> Cfg getResById(String cfgId);
	/**
	 * 获得指定资源的type
	 * @param cfgId 资源id
	 * @return type
	 */
	default <Type extends Enum<Type> & IResourceType> Type getResType(String cfgId) {
		return getResById(cfgId).type();
	}
}
