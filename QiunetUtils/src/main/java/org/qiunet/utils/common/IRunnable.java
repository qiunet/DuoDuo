package org.qiunet.utils.common;

/***
 * 可以抛出异常的Runnable
 * 异常外面处理
 *
 * @author qiunet
 * 2021/12/28 20:30
 */
@FunctionalInterface
public interface IRunnable {

	void run() throws Exception;
}
