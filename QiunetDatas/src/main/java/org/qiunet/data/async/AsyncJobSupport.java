package org.qiunet.data.async;

import org.qiunet.quartz.CronSchedule;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerShutdownEvent;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

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
	@CronSchedule(value = "0 * * * * ?", randRangeMillis = 200)
	public void asyncToDb(){
		nodes.forEach(node -> {
			try {
				// 必须try catch 否则导致线程停止
				node.syncToDatabase();
			}catch (Exception e) {
				logger.error(MessageFormat.format("[{0}] Exception: ", getClass().getSimpleName()), e);
			}
		});
	}

	@EventListener(EventHandlerWeightType.HIGHEST)
	private void onShutdown(ServerShutdownEvent data) {
		if (! nodes.isEmpty()) {
			nodes.forEach(IAsyncNode::syncToDatabase);
		}
		logger.info("Shutdown async update success!");
	}
}
