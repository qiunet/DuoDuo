package org.qiunet.cfg.base;

import org.qiunet.cfg.manager.base.ILoadSandbox;

/***
 * 检查. 如果检查失败.
 * 需要抛出异常. 会导致启动或者热更新失败.
 *
 * @author qiunet
 * 2021-01-27 20:22
 */
public interface ICfgCheck {
	/**
	 * 检查配置
	 */
	void check(ILoadSandbox sandbox);
}
