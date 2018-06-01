package org.qiunet.utils.hook;

import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/5/31 12:04
 **/
public class ShutdownHookThread {
	private Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);

	private volatile static ShutdownHookThread instance;

	private Hook hook;
	private ShutdownHookThread() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		this.hook = new Hook();
		this.effective();
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
	 * 添加
	 * @param closeHook
	 */
	public void addShutdownHook(IShutdownCloseHook closeHook) {
		hook.closes.add(closeHook);
	}

	/***
	 * 完事自己添加到Runtime
	 */
	private void effective(){
		Runtime.getRuntime().addShutdownHook(this.hook);
	}
	/***
	 * 提前执行.  并从钩子里面去掉.
	 */
	public void shutdownNow() {
		Runtime.getRuntime().removeShutdownHook(this.hook);
		this.hook.run();
	}

	/***
	 * Thread 对外隐藏. 不能被调用run方法了.
	 */
	private class Hook extends Thread {
		List<IShutdownCloseHook> closes = new LinkedList<>();
		@Override
		public void run() {
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
}
