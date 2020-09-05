package org.qiunet.listener.event.hook;

/***
 * hook 时候.需要关闭
 * @Author qiunet
 * @Date Create in 2018/5/31 12:05
 **/
public interface IShutdownCloseHook {
	/***
	 * 自行把关闭代码贴入
	 */
	void close();
}
