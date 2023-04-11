package org.qiunet.cfg.base;

/***
 * key value 类型的配置类.
 * val 会根据需要注入的类型自动注入的.
 *
 * @author qiunet
 * 2020-09-18 17:06
 */
public interface IKeyValCfg extends ISimpleMapCfg<String> {
	/**
	 * key
	 * @return key
	 */
	default String key(){
		return getId();
	}

	/**
	 * val
	 * @return val
	 */
	String val();
}
