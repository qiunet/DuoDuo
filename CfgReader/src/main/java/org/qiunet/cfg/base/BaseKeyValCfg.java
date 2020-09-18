package org.qiunet.cfg.base;

/***
 * key val 类型配置的基类
 *
 * @author qiunet
 * 2020-09-18 17:10
 */
public abstract class BaseKeyValCfg implements IKeyValCfg {
	private String key;
	private String val;

	@Override
	public String val() {
		return val;
	}

	@Override
	public String getId() {
		return key;
	}
}
