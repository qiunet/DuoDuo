package org.qiunet.log.record.logger;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.qiunet.log.record.msg.ILogRecordMsg;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.config.anno.DConfigValue;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerDeprecatedEvent;
import org.qiunet.utils.listener.event.data.ServerShutdownEvent;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;
import org.qiunet.utils.thread.ThreadPoolManager;
import org.qiunet.utils.timer.TimerManager;

import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/***
 *
 *
 * @author qiunet
 * 2020-03-25 09:51
 ***/
public enum LogRecordManager {
	instance;
	// 默认的logger
	static final String DEFAULT_LOGGER_RECORD_NAME = "logbackRecord";


	public void sendLog(ILogRecordMsg msg) {
		LogRecordManager0.instance.sendLog(msg);
	}

	private enum LogRecordManager0 implements IApplicationContextAware {
		instance;
		/**
		 * 基本的logger
		 */
		private IBasicRecordLogger basicLogger;
		/**
		 * 从配置读取 logger
		 */
		@DConfigValue(value = "server.record_log_names", defaultVal = "", configName = "server.conf")
		private static Set<String> RECORD_LOG_NAMES;

		private final AtomicInteger size = new AtomicInteger();
		/**
		 * 需要打印的日志
		 */
		private final List<IBasicRecordLogger> loggers = Lists.newLinkedList();
		/**
		 * 消息队列
		 */
		private final Queue<ILogRecordMsg> queue = new ConcurrentLinkedQueue<>();
		/**
		 * 发送日志
		 * @param log
		 */
		void sendLog(ILogRecordMsg log) {
			Preconditions.checkNotNull(log);
			if (basicLogger != null) {
				basicLogger.send(log);
			}

			if (loggers.isEmpty()) {
				return;
			}

			queue.add(log);

			if (size.incrementAndGet() == 1) {
				ThreadPoolManager.NORMAL.execute(this::consumeLog);
			}
		}
		/**
		 * 记录消费
		 */
		private void consumeLog() {
			ILogRecordMsg msg;
			while ((msg = queue.poll()) != null) {
				try {
					for (IBasicRecordLogger logger : loggers) {
						try {
							logger.send(msg);
						}catch (Exception e) {
							LoggerType.DUODUO.error("consumeLog:", e);
						}
					}
				}finally {
					size.decrementAndGet();
				}
			}
		}

		@Override
		public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
			Set<Class<? extends IRecordLogger>> subTypesOf = context.getSubTypesOf(IRecordLogger.class);
			for (Class<? extends IRecordLogger> clz : subTypesOf) {
				IRecordLogger instance = (IRecordLogger) context.getInstanceOfClass(clz);
				if (RECORD_LOG_NAMES == null
					|| ! RECORD_LOG_NAMES.contains(instance.recordLoggerName())
					|| Objects.equals(DEFAULT_LOGGER_RECORD_NAME, instance.recordLoggerName())) {
					continue;
				}
				loggers.add(instance);
			}

			try {
				// 先判断有没有jar.
				Class.forName("ch.qos.logback.classic.Logger");

				Class<?> aClass = Class.forName("org.qiunet.log.record.logger.LogBackRecordLogger");
				this.basicLogger = ((IBasicRecordLogger) context.getInstanceOfClass(aClass));
			}catch (ClassNotFoundException e) {
				LoggerType.DUODUO.error("LogRecordManager ERROR:", new CustomException("logback jar not setting!"));
			}
		}


		@EventListener
		private void shutdown(ServerShutdownEvent event) {
			this.consumeLog();
		}


		@EventListener
		private void deprecate(ServerDeprecatedEvent event) {
			this.consumeLog();
		}

		@Override
		public ScannerType scannerType() {
			return ScannerType.LOG_RECORD;
		}
	}
}
