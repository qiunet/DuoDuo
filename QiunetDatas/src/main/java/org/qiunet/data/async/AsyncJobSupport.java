package org.qiunet.data.async;

import org.qiunet.quartz.CronSchedule;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerShutdownEventData;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.math.MathUtil;
import org.qiunet.utils.timer.TimerManager;
import org.slf4j.Logger;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 异步更新的公用类
 * @author qiunet
 * Created on 17/2/11 08:04.
 */
 public enum AsyncJobSupport {
	instance;
	private final Logger logger = LoggerType.DUODUO.getLogger();
	public static AsyncJobSupport getInstance() {
		return instance;
	}

	private final Set<IAsyncNode> nodes = new HashSet<>();
	void addNode(IAsyncNode node) {
		this.nodes.add(node);
	}
	/***
	 * 异步更新到db
	 */
	@CronSchedule("0 * * * * ?")
	public void asyncToDb(){
		nodes.forEach(node -> TimerManager.executor.scheduleWithDelay(() -> {
			try {
				// 必须try catch 否则导致线程停止
				node.syncToDatabase();
			}catch (Exception e) {
				logger.error(MessageFormat.format("[{0}] Exception: ", getClass().getSimpleName()), e);
			}
			return null;
			// 会延迟一定时间执行 免得凑一块了.
		}, MathUtil.random(0, 200), TimeUnit.MILLISECONDS));
	}

	@EventListener(EventHandlerWeightType.HIGHEST)
	public void onShutdown(ServerShutdownEventData data) {
		if (! nodes.isEmpty()) {
			this.asyncToDb();
		}
		logger.info("Shutdown async update success!");
	}
}
