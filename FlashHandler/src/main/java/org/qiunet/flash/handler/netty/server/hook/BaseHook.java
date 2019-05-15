package org.qiunet.flash.handler.netty.server.hook;

import org.qiunet.utils.asyncQuene.factory.DefaultThreadFactory;
import org.qiunet.utils.hook.ShutdownHookThread;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/***
 *
 * @Author qiunet
 * @Date Create in 2019/1/4 10:47
 **/
public abstract class BaseHook implements Hook {
	protected Logger logger = LoggerType.DUODUO.getLogger();

	private ExecutorService executorService;

	protected BaseHook(){
		this(Executors.newCachedThreadPool(new DefaultThreadFactory("DefaultHook")));
	}

	protected BaseHook(ExecutorService queue) {
		if (queue == null) {
			throw new NullPointerException("executorService can not be null");
		}
		this.executorService = queue;

		ShutdownHookThread.getInstance().addShutdownHook(() ->
			this.executorService.shutdown()
		);
	}
	@Override
	public void custom(String msg, String ip) {
		this.executorService.submit(this.returnCustomTask(msg, ip));
	}
	/**
	 * 由继承类返回需要执行的任务 父类异步多线程执行.
	 * @param msg
	 * @param ip
	 * @return
	 */
	protected abstract Runnable returnCustomTask(String msg, String ip);
}
