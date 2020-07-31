package org.qiunet.cfg.base;

/***
 * 在整个配置加载后. 执行有实现该接口的cfg
 *
 * @author qiunet
 * 2020-04-24 11:18
 ***/
public interface IAfterLoad {

	void afterLoad();
}
