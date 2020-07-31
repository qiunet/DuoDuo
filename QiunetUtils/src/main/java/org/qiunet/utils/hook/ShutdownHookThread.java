package org.qiunet.utils.hook;

import org.qiunet.utils.classScanner.Singleton;
import org.qiunet.utils.listener.data.ServerShutdownEventData;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/5/31 12:04
 **/
@Singleton
public class ShutdownHookThread implements ServerShutdownEventData.ServerShutdownListener {
	private LinkedList<IShutdownCloseHook> closes = new LinkedList<>();
	private Logger logger = LoggerType.DUODUO.getLogger();
	private AtomicBoolean executing = new AtomicBoolean();
	private volatile static ShutdownHookThread instance;

	private ShutdownHookThread() {
		if (instance != null) {
			throw new RuntimeException("Instance Duplication!");
		}
		Runtime.getRuntime().addShutdownHook(new Thread(this::shutdownNow));
		instance = this;
	}

	public static ShutdownHookThread getInstance() {
		if (instance == null) {
			synchronized (ShutdownHookThread.class) {
				if (instance == null)
				{
					new ShutdownHookThread();
				}
			}
		}
		return instance;
	}
	/***
	 * 添加 到开头
	 * @param closeHook
	 */
	public void addFirst(IShutdownCloseHook closeHook) {
		this.closes.addFirst(closeHook);
	}
	/***
	 * 添加 到最后
	 * @param closeHook
	 */
	public void addLast(IShutdownCloseHook closeHook) {
		this.closes.addLast(closeHook);
	}
	/***
	 * 添加
	 * @param closeHook
	 */
	public void addShutdownHook(IShutdownCloseHook closeHook) {
		this.addFirst(closeHook);
	}

	/***
	 * 提前执行.  并从钩子里面去掉.
	 */
	private void shutdownNow() {
		if (executing.compareAndSet(false, true)) {
			this.run();
		}
	}


	@Override
	public void onShutdown(ServerShutdownEventData data) {
		this.shutdownNow();
	}

	private void run() {
		logger.error("----------------Shutdown now-----------------------");
		for (IShutdownCloseHook close : closes) {
			try {
				close.close();
				logger.info("Closed ["+close.getClass().getName()+"]");
			}catch (Exception e) {
				logger.error("Closing ["+close.getClass().getName()+"], But Exception.", e);
			}
		}
		logger.error("----------------Shutdown over-----------------------");
	}
}
