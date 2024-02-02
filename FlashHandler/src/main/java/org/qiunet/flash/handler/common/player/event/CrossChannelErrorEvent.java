package org.qiunet.flash.handler.common.player.event;

import org.qiunet.flash.handler.netty.server.constants.CloseCause;

/***
 * Cross channel 错误,需要gate端断开channel
 * @author qiunet
 * 2024/2/2 10:34
 ***/
public class CrossChannelErrorEvent extends ToPlayerEvent {
	/**
	 * 异常原因
	 */
	private CloseCause cause;

	public static CrossChannelErrorEvent valueOf(CloseCause cause){
		CrossChannelErrorEvent data = new CrossChannelErrorEvent();
	    data.cause = cause;
		return data;
	}

	public CloseCause getCause() {
		return cause;
	}
}
