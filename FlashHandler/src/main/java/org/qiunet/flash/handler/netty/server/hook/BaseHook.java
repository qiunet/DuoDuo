package org.qiunet.flash.handler.netty.server.hook;

import org.qiunet.utils.asyncQuene.mutiThread.MultiAsyncQueueHandler;
import org.qiunet.utils.hook.ShutdownHookThread;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 *
 * @Author qiunet
 * @Date Create in 2019/1/4 10:47
 **/
public abstract class BaseHook implements Hook {
	protected Logger logger = LoggerType.DUODUO.getLogger();

	private MultiAsyncQueueHandler queue;

	protected BaseHook(){
		this(new MultiAsyncQueueHandler("Hook-Queue"));
	}

	protected BaseHook(MultiAsyncQueueHandler queue) {
		if (queue == null) {
			throw new NullPointerException("queue can not be null");
		}
		this.queue = queue;

		ShutdownHookThread.getInstance().addShutdownHook(() ->
			this.queue.shutdown()
		);
	}
	@Override
	public void custom(String msg, String ip) {
		this.queue.addElement(this.returnCustomTask(msg, ip));
	}
	/**
	 * 由继承类返回需要执行的任务 父类异步多线程执行.
	 * @param msg
	 * @param ip
	 * @return
	 */
	protected abstract Runnable returnCustomTask(String msg, String ip);
}
