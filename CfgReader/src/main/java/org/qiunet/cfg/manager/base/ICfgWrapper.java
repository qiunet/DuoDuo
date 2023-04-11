package org.qiunet.cfg.manager.base;

import org.qiunet.cfg.base.ICfg;

import java.util.List;

/***
 * 配置文件的容器类.
 * 实际是包含一个manager. 通过manager调用其API
 *
 * @author qiunet
 * 2020-04-23 10:53
 ***/
public interface ICfgWrapper<ID, Cfg extends ICfg<ID>> {
	/**
	 * 得到该类加载的cfg 类class
	 * @return cfg class
	 */
	Class<Cfg> getCfgClass();
	/**
	 * 配置数量
	 * @return 总数量
	 */
	default int size(){
		return list().size();
	}
	/**
	 * 所有的配置
	 * @return list
	 */
	List<Cfg> list();
}
