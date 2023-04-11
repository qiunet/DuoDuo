package org.qiunet.function.base;


import org.qiunet.cfg.resource.IResourceCfg0;

/***
 * 资源类型接口
 * @author qiunet
 * 2020-04-25 20:48
 **/
public interface IResourceCfg extends IResourceCfg0 {

	/**
	 * 获得资源的子类型
	 * @return
	 */
	<Type extends Enum<Type> & IResourceType> Type type();
}
