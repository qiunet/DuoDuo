package org.qiunet.flash.handler.common.player;

import org.qiunet.flash.handler.context.session.DSession;

/***
 * 玩家类型的messageActor 继承该类
 *
 * @author qiunet
 * 2020-10-13 20:51
 */
public abstract class AbstractUserActor<T extends AbstractUserActor<T>> extends AbstractMessageActor<T>  {

	public AbstractUserActor(DSession session) {
		super(session);
	}

	/**
	 * 是否跨服状态
	 *
	 * @return
	 */
	public abstract boolean isCrossStatus();
}
