package org.qiunet.data1.async;

import org.qiunet.data1.async.IAsyncNode;
import org.qiunet.quartz.CronSchedule;
import org.qiunet.utils.asyncQuene.factory.DefaultThreadFactory;
import org.qiunet.utils.hook.ShutdownHookThread;
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
 class AsyncJobSupport {
	private Logger logger = LoggerType.DUODUO.getLogger();
	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
			10,
			new DefaultThreadFactory("AsyncJobSupport"));

	private volatile static AsyncJobSupport instance = new AsyncJobSupport();
	private AsyncJobSupport() {
		instance = this;
		ShutdownHookThread.getInstance().addShutdownHook(() -> {
			asyncToDb();
			executor.shutdown();
		});
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
	@CronSchedule("10 0/3 * * * ?")
	private void asyncToDb(){
		nodes.parallelStream().forEach(node -> executor.schedule(() -> {
			try {
				// 必须try catch 否则导致线程停止
				node.syncToDatabase();
			}catch (Exception e) {
				logger.error("["+getClass().getSimpleName()+"] Exception: ", e);
			}
			// 会延迟一定时间执行 免得凑一块了.
		}, MathUtil.random(0, 300), TimeUnit.MILLISECONDS));
	}
}
