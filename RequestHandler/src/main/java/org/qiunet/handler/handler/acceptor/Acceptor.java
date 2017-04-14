package org.qiunet.handler.handler.acceptor;

import org.qiunet.handler.context.ITcpUdpContext;
import org.qiunet.handler.exception.DuplicateInstanceException;
import org.qiunet.handler.handler.intecepter.Intercepter;
import org.qiunet.handler.context.IContext;
import org.qiunet.utils.nonSyncQuene.IndexNonSyncQueueHandler;
import org.qiunet.utils.threadLocal.ThreadContextData;

/**
 * 接受线程请求的地方. 
 * @author qiunet
 *         Created on 17/3/6 18:44.
 */
public class Acceptor {
	private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
	private Intercepter intercepter;
	private IndexNonSyncQueueHandler<IContext> processorHandler;
	private volatile static Acceptor instance;

	private Acceptor(Intercepter intercepter,  int threadCount) {
		if (instance != null ) {
			throw new DuplicateInstanceException("Duplicate called the Acceptor Instructor ");
		}

		if (threadCount < 1) throw new Error("ThreadCount can not less than 1 !");

		instance = this;
		this.intercepter = intercepter;
		this.processorHandler = new IndexNonSyncQueueHandler<>(threadCount, false);
	}
	
	public static Acceptor create(Intercepter intercepter) {
		return create(intercepter, THREAD_COUNT);
	}
	
	public static Acceptor create(Intercepter intercepter,  int threadCount) {
		if (instance == null) {
			synchronized (Acceptor.class) {
				if (instance == null)
				{
					new Acceptor(intercepter, threadCount);
				}
			}
		}
		return instance;
	}
	/**
	 * 停止所有的线程
	 */
	public void stop() {
		processorHandler.stop();
	}

	/**
	 * 请求的处理
	 * @param iContext
	 */
	public void process(IContext iContext) {
		switch (iContext.getHandler().getHandlerType()) {
			case HTTP:
				this.handler(iContext);
				break;
			case TCP_UDP:
				iContext.setAcceptor(this);
				processorHandler.addElement(iContext, ((ITcpUdpContext)iContext).getQueueHandlerIndex());
				break;
		}
	}
	/**
	 * 处理请求
	 */
	public void handler (IContext context){
		if (intercepter == null) {
			throw new NullPointerException("Need  set intercepter");
		}
		try{
			intercepter.preHandler(context);
			intercepter.handler(context);
			intercepter.postHandler(context);
		}catch (Exception e) {
			// 将异常交给 intercepter 处理
			intercepter.throwCause(context, e);
		}finally {
			/**清理所有的本地线程数据*/
			ThreadContextData.removeAll();
		}
	}
}
