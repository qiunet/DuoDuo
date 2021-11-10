package org.qiunet.function.base;


/***
 * 资源类型接口
 * @author qiunet
 * 2020-04-25 20:48
 **/
public interface IResourceCfg {
	/**
	 * 获得资源的类型
	 * @return
	 */
	default < T extends IResourceType> T type() {
		return subType().resourceType();
	}

	/**
	 * 获得资源的子类型
	 * @return
	 */
	<T extends IResourceSubType> T subType();
}
