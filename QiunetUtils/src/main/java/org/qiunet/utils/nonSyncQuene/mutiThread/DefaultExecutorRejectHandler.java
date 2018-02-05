package org.qiunet.utils.nonSyncQuene.mutiThread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultExecutorRejectHandler implements RejectedExecutionHandler {
	private Logger logger = LoggerFactory.getLogger(LoggerType.QIUNET_UTILS);
	private String threadName;
	public DefaultExecutorRejectHandler(String name){
		this.threadName = name;
	}

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		if(! executor.isShutdown()){
			r.run();
		}
		logger.error(threadName + "thread reject server!");
	}

}
