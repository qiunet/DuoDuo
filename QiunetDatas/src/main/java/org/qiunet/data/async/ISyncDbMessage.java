package org.qiunet.data.async;

/***
 * 同步到数据库的接口
 * @author qiunet
 * 2022/10/26 09:25
 */
public interface ISyncDbMessage {
	/***
	 * 更新到db
	 * 同步异步看调用方
	 */
	void syncBbMessage(Runnable runnable);
}
