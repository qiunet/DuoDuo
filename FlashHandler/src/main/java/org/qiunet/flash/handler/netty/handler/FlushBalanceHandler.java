package org.qiunet.flash.handler.netty.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

/***
 * 平衡flush 的一个handler.
 * write一定次数. 或者一定时间后 会flush
 *
 * @author qiunet
 * 2023/3/26 19:11
 */
public class FlushBalanceHandler extends ChannelDuplexHandler {
	/**
	 * 延迟的毫秒数
	 */
	private final int delayFlushMilliSeconds;
	/**
	 * 定时任务
	 */
	private ScheduledFuture<?> schedule;
	/**
	 * 限定的次数
	 */
	private final int limitWriteCount;
	/**
	 * 当前的次数
	 */
	private int currWriteCount;

	public FlushBalanceHandler() {
		this(50, 10);
	}

	public FlushBalanceHandler(int delayFlushMilliSeconds, int limitWriteCount) {
		this.delayFlushMilliSeconds = delayFlushMilliSeconds;
		this.limitWriteCount = limitWriteCount;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		super.write(ctx, msg, promise);
		if (this.currWriteCount >= this.limitWriteCount) {
			this.flush0(ctx, true);
		}
		this.currWriteCount ++;
		if (this.schedule == null) {
			this.schedule = ctx.executor().schedule(() -> {
				try {
					this.flush0(ctx, true);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}, this.delayFlushMilliSeconds, TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		this.flush0(ctx, false);
	}

	public void flush0(ChannelHandlerContext ctx, boolean cleanSchedule) throws Exception {
		super.flush(ctx);

		if (cleanSchedule && this.schedule != null) {
			this.schedule.cancel(false);
			this.schedule = null;
		}
		if (this.currWriteCount <= 0) {
			return;
		}
		this.currWriteCount = 0;
	}
}
