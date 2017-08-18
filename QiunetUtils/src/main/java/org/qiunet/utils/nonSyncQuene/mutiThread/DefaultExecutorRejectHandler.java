package org.qiunet.utils.nonSyncQuene.mutiThread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;

public class DefaultExecutorRejectHandler implements RejectedExecutionHandler {
	private Logger logger = LoggerManager.getInstance().getLogger(LoggerType.QIUNET_UTILS);
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
