package org.qiunet.data.async;

import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.hook.ShutdownHookThread;
import org.qiunet.utils.listener.EventHandlerWeight;
import org.qiunet.utils.listener.EventHandlerWeightType;
import org.qiunet.utils.listener.data.ServerShutdownEventData;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.math.MathUtil;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异步更新的公用类
 * @author qiunet
 * Created on 17/2/11 08:04.
 */
 public class AsyncJobSupport implements ServerShutdownEventData.ServerShutdownListener {
	private Logger logger = LoggerType.DUODUO.getLogger();
	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
			8,
			new DefaultThreadFactory("AsyncJobSupport"));

	private volatile static AsyncJobSupport instance = new AsyncJobSupport();
	private AsyncJobSupport() {
		instance = this;
		ShutdownHookThread.getInstance().addShutdownHook(() -> executor.shutdown());
	}

	public static AsyncJobSupport getInstance() {
		return instance;
	}

	private Set<IAsyncNode> nodes = new HashSet<>();
	void addNode(IAsyncNode node) {
		this.nodes.add(node);
	}
	/***
	 * 异步更新到db
	 */
	public void asyncToDb(int maxDelay, TimeUnit unit){
		nodes.forEach(node -> executor.schedule(() -> {
			try {
				// 必须try catch 否则导致线程停止
				node.syncToDatabase();
			}catch (Exception e) {
				logger.error("["+getClass().getSimpleName()+"] Exception: ", e);
			}
			// 会延迟一定时间执行 免得凑一块了.
		}, MathUtil.random(0, (int) unit.toMillis(maxDelay)), TimeUnit.MILLISECONDS));
	}

	@Override
	@EventHandlerWeight(EventHandlerWeightType.HIGHEST)
	public void onShutdown(ServerShutdownEventData data) {
		this.asyncToDb(100, TimeUnit.MILLISECONDS);
		logger.info("Shutdown async update success!");
	}
}
