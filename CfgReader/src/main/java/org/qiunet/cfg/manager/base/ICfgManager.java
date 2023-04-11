package org.qiunet.cfg.manager.base;

import org.qiunet.cfg.base.ICfg;

/**
 * 游戏设定数据处理类实现接口
 * @author qiunet
 * Created on 17/2/9 12:18.
 */
public interface ICfgManager<ID, Cfg extends ICfg<ID>> extends ICfgWrapper<ID, Cfg> {
	/**
	 * 设定加载
	 */
	void loadCfg() throws Exception;

	/**
	 * 得到加载的文件名 或者 Pattern
	 * @return 文件名
	 */
	String getLoadFileName();

	/**
	 * 得到该类加载的cfg 类class
	 * @return class
	 */
	Class<Cfg> getCfgClass();
	/**
	 * 加载顺序
	 * @return 加载顺序
	 */
	int order();
}
