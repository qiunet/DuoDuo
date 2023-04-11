package org.qiunet.cfg.base;

/***
 * 配置中 需要延迟加载数据
 * 比如 Rewards Consumes
 * 注入时候, 就需要读取其它Cfg的数据.
 * 这个时候, 可能没有准备好.
 *
 * @author qiunet
 * 2023/4/12 16:26
 */
public interface ICfgDelayLoadData {
	/**
	 * 调用加载方法
	 */
	void loadData();
}
