package org.qiunet.utils.nonSyncQuene.mutiThread;

import org.apache.log4j.Logger;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class DefaultExecutorRejectHandler implements RejectedExecutionHandler {
	private Logger logger = Logger.getLogger(DefaultExecutorRejectHandler.class);
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
