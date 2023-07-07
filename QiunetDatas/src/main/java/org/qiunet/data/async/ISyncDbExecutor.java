package org.qiunet.data.async;

import org.qiunet.utils.thread.IThreadSafe;

import java.util.concurrent.Future;

/***
 * 同步到数据库的接口
 * @author qiunet
 * 2022/10/26 09:25
 */
public interface ISyncDbExecutor extends IThreadSafe {
	/***
	 * 更新到db
	 * 同步异步看调用方
	 */
	Future<?> submit(Runnable runnable);
}
