package org.qiunet.flash.handler.common.player;

import org.qiunet.flash.handler.context.session.DSession;

/***
 * 跨服状态的actor
 *
 * @author qiunet
 * 2020-10-26 14:54
 */
public interface ICrossStatusActor {
	/**
	 * 是否跨服状态
	 * @return
	 */
	boolean isCrossStatus();
	/**
	 * 获得crossSession
	 * @return
	 */
	DSession crossSession();
}
