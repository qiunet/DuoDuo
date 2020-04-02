package org.qiunet.event.log.enums;

import org.qiunet.event.log.logger.ILogger;

/***
 *
 *
 * @author qiunet
 * 2020-03-25 09:53
 ***/
public enum RecordModel {
	/**
	 * 本地保存
	 */
	LOCAL{
		@Override
		public ILogger getLogger() {
			return null;
		}
	},
	/**
	 * tcp 发到事件日志保存平台.
	 * 重要日志一般使用tcp
	 */
	TCP {
		@Override
		public ILogger getLogger() {
			return null;
		}
	},
	/***
	 * udp发到事件日志保存平台
	 * 普通日志使用udp, 有一定丢失可能.
	 */
	UDP {
		@Override
		public ILogger getLogger() {
			return null;
		}
	},
	;
	public abstract ILogger getLogger();
}
