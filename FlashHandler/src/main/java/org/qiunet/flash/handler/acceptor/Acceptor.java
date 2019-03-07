package org.qiunet.flash.handler.acceptor;

import org.qiunet.flash.handler.context.request.IRequestContext;
import org.qiunet.utils.asyncQuene.IndexAsyncQueueHandler;
import org.qiunet.utils.hook.ShutdownHookThread;
import org.qiunet.utils.system.OSUtil;

/**
 * 整个的游戏入口, 处理TCP 和 HTTP过来的请求
 * 所有的请求封装成IContext. 然后各自分别处理
 *
 * Http是实时返回响应. TCP是使用多个异步队列处理请求. 根据session固定请求的处理队列.
 * Created by qiunet.
 * 17/7/21
 */
public class Acceptor {
	/**使用几个线程来处理*/
	private static final int THREAD_COUNT = OSUtil.availableProcessors() *2;
	/**线程的处理队列*/
	private IndexAsyncQueueHandler<IRequestContext> contextProcessor;

	/**
	 * 创建当前cpu核数相当的线程作为 context的处理队列
	 * @return
	 */
	private Acceptor(int threadCount) {
		if (instance != null ) {
			throw new RuntimeException("Duplicate called the Acceptor Instructor ");
		}

		if (threadCount < 1) throw new Error("ThreadCount can not less than 1 !");
		this.contextProcessor = new IndexAsyncQueueHandler("flash_handler_acceptor_", threadCount);

		ShutdownHookThread.getInstance().addShutdownHook(() -> {
			this.shutdown();
		});
		instance = this;
	}

	private volatile static Acceptor instance;


	public static Acceptor getInstance() {
		if (instance == null) {
			synchronized (Acceptor.class) {
				if (instance == null)
				{
					new Acceptor(THREAD_COUNT);
				}
			}
		}
		return instance;
	}

	/**
	 * 停止所有的线程
	 */
	public void shutdown() {
		contextProcessor.stop();
	}
	/**
	 * 请求的处理
	 * @param iContext
	 */
	public void process(IRequestContext iContext) {
		switch (iContext.getHandler().getHandlerType()) {
			case HTTP:
				iContext.handler();
				break;
			default:
				contextProcessor.addElement(iContext);
				break;
		}
	}
}
