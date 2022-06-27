package org.qiunet.log.record.logger;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.qiunet.log.record.enums.ILogRecordType;
import org.qiunet.log.record.msg.LogRecordMsg;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.config.anno.DConfigValue;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;
import org.qiunet.utils.thread.ThreadPoolManager;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/***
 *
 *
 * @author qiunet
 * 2020-03-25 09:51
 ***/
public enum LogRecordManager {
	instance;


	public <LogType extends Enum<LogType> & ILogRecordType<LogType>> void sendLog(LogRecordMsg<LogType> msg) {
		LogRecordManager0.instance.sendLog(msg);
	}

	private enum LogRecordManager0 implements IApplicationContextAware {
		instance;
		/**
		 * 从配置读取 logger
		 */
		@DConfigValue(value = "server.record_log_names", defaultVal = "", configName = "server.conf")
		private static Set<String> RECORD_LOG_NAMES;

		private final AtomicInteger size = new AtomicInteger();
		/**
		 * 需要打印的日志
		 */
		private final List<IRecordLogger> loggers = Lists.newLinkedList();
		/**
		 * 消息队列
		 */
		private final Queue<LogRecordMsg<?>> queue = new ConcurrentLinkedQueue<>();
		/**
		 * 发送日志
		 * @param log
		 */
		<LogType extends Enum<LogType> & ILogRecordType<LogType>> void sendLog(LogRecordMsg<LogType> log) {
			Preconditions.checkNotNull(log);
			queue.add(log);

			if (size.incrementAndGet() == 1) {
				ThreadPoolManager.NORMAL.execute(this::consumeLog);
			}
		}
		/**
		 * 记录消费
		 */
		private void consumeLog() {
			LogRecordMsg msg;
			while ((msg = queue.poll()) != null) {
				for (IRecordLogger logger : loggers) {
					logger.send(msg);
				}
				size.decrementAndGet();
			}
		}

		@Override
		public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
			Set<Class<? extends IRecordLogger>> subTypesOf = context.getSubTypesOf(IRecordLogger.class);
			for (Class<? extends IRecordLogger> clz : subTypesOf) {
				IRecordLogger instance = (IRecordLogger) context.getInstanceOfClass(clz);
				if (RECORD_LOG_NAMES == null || ! RECORD_LOG_NAMES.contains(instance.recordLoggerName())) {
					continue;
				}
				loggers.add(instance);
			}

			if (RECORD_LOG_NAMES == null || RECORD_LOG_NAMES.isEmpty()) {
				try {
					Class<?> aClass = Class.forName("org.qiunet.log.record.logger.LogBackRecordLogger");
					loggers.add(((IRecordLogger) context.getInstanceOfClass(aClass)));
				}catch (ClassNotFoundException e) {
					LoggerType.DUODUO.error("logback jar not setting!");
				}
			}
		}

		@Override
		public ScannerType scannerType() {
			return ScannerType.LOG_RECORD;
		}
	}
}
