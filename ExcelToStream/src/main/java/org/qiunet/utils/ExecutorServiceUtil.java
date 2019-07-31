package org.qiunet.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.qiunet.utils.asyncQuene.factory.DefaultThreadFactory;
import org.qiunet.utils.logger.LoggerType;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * created by wgw on 2019/7/28
 */
public class ExecutorServiceUtil {

	private static ExecutorService executor;

	private static ExecutorServiceUtil instance;

	private ExecutorServiceUtil() {
		executor = Executors.newSingleThreadExecutor(new DefaultThreadFactory("task_"));
	}

	public static ExecutorServiceUtil getInstance() {
		if (instance == null) {
			instance = new ExecutorServiceUtil();
		}
		return instance;
	}

	public void submit(Runnable task) {
		executor.submit(task);
	}

	public <T> T submit(Callable<T> task) {
		Future<T> future = executor.submit(task);
		try {
			return future.get();
		} catch (Exception e) {
			LoggerType.DUODUO.error("获取线程返回值异常\r\n"+ExceptionUtils.getStackTrace(e));
			return null;
		}

	}

	public void shutdown() {
		if (executor.isShutdown()) {
			LoggerType.DUODUO.info("关闭任务线程池...");
			executor.shutdown();
		}
	}
}
