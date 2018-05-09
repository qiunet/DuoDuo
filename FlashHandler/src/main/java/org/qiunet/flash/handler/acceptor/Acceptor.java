package org.qiunet.flash.handler.acceptor;

import org.qiunet.flash.handler.context.request.IRequestContext;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequestContext;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequestContext;
import org.qiunet.utils.asyncQuene.IndexAsyncQueueHandler;

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
		this.contextProcessor = new IndexAsyncQueueHandler();
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
			case WEB_SOCKET:
				contextProcessor.addElement(iContext);
				break;
			case TCP:
				contextProcessor.addElement(iContext);
				break;
		}
	}
}
