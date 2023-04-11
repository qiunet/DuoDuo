package org.qiunet.cfg.base;

import org.qiunet.cfg.manager.base.ILoadSandbox;

/***
 * Cfg 实现该方法后,
 * 可以实现一些自定义的初始化内容.
 *
 * @author qiunet
 * 2020/3/11 08:03
 **/
public interface ICfgCustomInit {
	/**
	 * cfg 实现init的方法
	 */
	void init(ILoadSandbox sandBox);
}
