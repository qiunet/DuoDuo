package org.qiunet.data.async;

import org.qiunet.utils.hook.ShutdownHookThread;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.asyncQuene.factory.DefaultThreadFactory;
import org.qiunet.utils.asyncQuene.mutiThread.DefaultExecutorRejectHandler;
import org.qiunet.utils.math.MathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 异步更新的公用类
 * @author qiunet
 *         Created on 17/2/11 08:04.
 */
public class AsyncJobSupport {
	private Logger logger = LoggerType.DUODUO.getLogger();
	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
			10,
			new DefaultThreadFactory("AsyncJobSupport"),
			new DefaultExecutorRejectHandler("AsyncJobSupport"));

	private volatile static AsyncJobSupport instance;

	private AsyncJobSupport() {
		instance = this;
	}

	public static AsyncJobSupport getInstance() {
		if (instance == null) {
			synchronized (AsyncJobSupport.class) {
				if (instance == null)
				{
					new AsyncJobSupport();
				}
			}
		}
		return instance;
	}

	private Set<AsyncNode> nodes = new HashSet<>();
	public void addNode(AsyncNode node) {
		if (nodes.isEmpty()) {
			ShutdownHookThread.getInstance().addShutdownHook(() -> executor.shutdown());
		}
		this.nodes.add(node);
	}

	/***
	 * 异步更新到db
	 */
	public void asyncToDb(){
		nodes.parallelStream().forEach(node -> executor.schedule(() -> {
			try {
				// 必须try catch 否则导致线程停止
				node.updateRedisDataToDatabase();
			}catch (Exception e) {
				logger.error("["+getClass().getSimpleName()+"] Exception: ", e);
			}
			// 会延迟一定时间执行 免得凑一块了.
		}, MathUtil.random(0, 500), TimeUnit.MILLISECONDS));
	}
}
