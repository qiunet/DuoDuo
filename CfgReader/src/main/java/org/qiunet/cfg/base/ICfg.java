package org.qiunet.cfg.base;

/***
 * 所有配置文件类的基类接口
 * @author qiunet
 * 2020-02-04 12:43
 **/
public interface ICfg<ID> {
	/**
	 * 得到该对象的id
	 * 可能是一个唯一key 也可能是几个字段拼的唯一key
	 * 用于标识在map中的key.
	 * @return
	 */
	ID getId();
}
