package org.qiunet.flash.handler.common.player;

import org.qiunet.flash.handler.context.session.DSession;

/***
 * 玩家playerActor 的父类
 *
 * @author qiunet
 * 2020-10-21 10:08
 */
public abstract class AbstractPlayerActor<T extends AbstractPlayerActor<T>>
	extends AbstractUserActor<T> implements ICrossStatusActor, IPlayer {

	public AbstractPlayerActor(DSession session) {
		super(session);
	}
}
