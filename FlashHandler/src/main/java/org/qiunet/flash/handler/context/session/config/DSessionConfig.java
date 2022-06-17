package org.qiunet.flash.handler.context.session.config;

import com.google.common.base.Preconditions;

/***
 * Session 的一些配置
 *
 * qiunet
 * 2021/6/28 21:53
 **/
public final class DSessionConfig {
	/**
	 * 默认配置
	 */
	public static final DSessionConfig DEFAULT_CONFIG = DSessionConfig.newBuilder()
			.setDefault_flush(false)
			.setFlush_delay_ms(100).build();
	/**
	 * 是否默认flush. 是. 每个message都flush, 否 则需要设置下面的参数
	 */
	private final boolean default_flush;
	/**
	 * 默认flush延时毫秒时间
	 */
	private final int flush_delay_ms;

	private DSessionConfig(DSessionConfigBuilder builder) {
		this.default_flush = builder.default_flush;
		this.flush_delay_ms = builder.flush_delay_ms;
	}

	public boolean isDefault_flush() {
		return default_flush;
	}


	public int getFlush_delay_ms() {
		return flush_delay_ms;
	}

	public static DSessionConfigBuilder newBuilder(){
		return new DSessionConfigBuilder();
	}

	public static class DSessionConfigBuilder {
		/**
		 * 是否默认flush
		 */
		private boolean default_flush;
		/**
		 * 默认flush延时毫秒时间
		 */
		private int flush_delay_ms;

		public DSessionConfigBuilder setDefault_flush(boolean default_flush) {
			this.default_flush = default_flush;
			return this;
		}

		public DSessionConfigBuilder setFlush_delay_ms(int flush_delay_ms) {
			Preconditions.checkState(flush_delay_ms > 10 && flush_delay_ms < 1000, "flush delay must grant than 10 ms and less than 1000 ms!");
			this.flush_delay_ms = flush_delay_ms;
			return this;
		}

		public DSessionConfig build() {
			return new DSessionConfig(this);
		}
	}
}
