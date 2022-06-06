package org.qiunet.utils.listener.hook;

import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerShutdownEventData;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/***
 * 非单例但是需要关闭. 如RedisDataUtils#redisPool, DbLoader 数据源
 *
 * 可以使用该方式关闭资源.
 *
 * @Author qiunet
 * @Date Create in 2018/5/31 12:04
 **/
public class ShutdownHookUtil {
	private final LinkedList<IShutdownCloseHook> closes = new LinkedList<>();
	private final Logger logger = LoggerType.DUODUO.getLogger();
	private final AtomicBoolean executing = new AtomicBoolean();
	private static ShutdownHookUtil instance;

	private ShutdownHookUtil() {
		if (instance != null) {
			throw new CustomException("Instance Duplication!");
		}
		instance = this;
	}

	public static ShutdownHookUtil getInstance() {
		if (instance == null) {
			synchronized (ShutdownHookUtil.class) {
				if (instance == null)
				{
					new ShutdownHookUtil();
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
		this.addLast(closeHook);
	}

	/***
	 * 提前执行.  并从钩子里面去掉.
	 */
	private void shutdownNow() {
		if (executing.compareAndSet(false, true)) {
			this.run();
		}
	}


	@EventListener(EventHandlerWeightType.LESS)
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
