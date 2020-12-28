package org.qiunet.function.base;

/***
 * 主体对象
 *
 * @author qiunet
 * 2020-12-28 11:28
 */
public interface IMainObject {
	/**
	 * 是否在安全线程
	 * @return true or  false
	 */
	boolean inSafeThread();
}
