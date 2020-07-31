package org.qiunet.cfg.base;

/***
 *
 * cfg实现该方法后,
 * 底层会再次调用该方法. 业务可以转换一些自己的处理.
 * 比如: reward对象, 可能需要当前manager给出, 但是manager没有初始化,
 * 就可以放到init里面.
 * @author qiunet
 * 2020/3/11 08:03
 **/
public interface INeedInitCfg {
	/**
	 * cfg 实现init的方法
	 */
	void init();
}
