package org.qiunet.flash.handler.acceptor;

import org.qiunet.flash.handler.context.IContext;
import org.qiunet.flash.handler.context.TcpContext;
import org.qiunet.utils.nonSyncQuene.IndexNonSyncQueueHandler;

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
	private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
	/**线程的处理队列*/
	private IndexNonSyncQueueHandler<IContext> contextProcessor;
	private volatile static Acceptor instance;

	private Acceptor(int threadCount) {
		if (instance != null ) {
			throw new RuntimeException("Duplicate called the Acceptor Instructor ");
		}

		if (threadCount < 1) throw new Error("ThreadCount can not less than 1 !");
		this.contextProcessor = new IndexNonSyncQueueHandler<>(threadCount, false);
		instance = this;
	}

	/**
	 * 创建当前cpu核数相当的线程作为 context的处理队列
	 * @return
	 */
	public static Acceptor create() {
		return create(THREAD_COUNT);
	}

	public static Acceptor create(int threadCount) {
		if (instance == null) {
			synchronized (Acceptor.class) {
				if (instance == null)
				{
					new Acceptor(threadCount);
				}
			}
		}
		return instance;
	}
	/**
	 * 停止所有的线程
	 */
	public void stop() {
		contextProcessor.stop();
	}
	/**
	 * 请求的处理
	 * @param iContext
	 */
	public void process(IContext iContext) {
		switch (iContext.getHandler().getHandlerType()) {
			case HTTP:
				iContext.handler();
				break;
			case TCP:
				contextProcessor.addElement(iContext, ((TcpContext)iContext).getQueueHandlerIndex());
				break;
		}
	}
}
