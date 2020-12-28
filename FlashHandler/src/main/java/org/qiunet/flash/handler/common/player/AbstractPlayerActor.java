package org.qiunet.flash.handler.common.player;

import org.qiunet.flash.handler.common.player.event.BasePlayerEventData;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.listener.event.EventManager;

/***
 * 玩家playerActor 的父类
 *
 * @author qiunet
 * 2020-10-21 10:08
 */
public abstract class AbstractPlayerActor<T extends AbstractPlayerActor>
	extends AbstractUserActor<T> implements ICrossStatusActor, IPlayer {


	public AbstractPlayerActor(DSession session) {
		super(session);
	}

	/**
	 * 对事件的处理.
	 * 跨服的提交跨服那边. 本服调用自己的
	 * @param eventData
	 */
	public <D extends BasePlayerEventData> void fireEvent(D eventData){
		eventData.setPlayer(this);
		EventManager.fireEventHandler(eventData);
	}
}
