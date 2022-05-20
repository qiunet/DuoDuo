package org.qiunet.flash.handler.netty.server.kcp.observer;

import org.qiunet.flash.handler.common.observer.IObserver;

/***
 * kcp可用性变动
 *
 * @author qiunet
 * 2022/5/19 15:18
 */
public interface IKcpUsabilityChange extends IObserver {
	/**
	 *
	 * @param prepare
	 */
	void ability(boolean prepare);
}
