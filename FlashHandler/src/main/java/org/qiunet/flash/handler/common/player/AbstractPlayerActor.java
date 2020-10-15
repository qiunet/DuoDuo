package org.qiunet.flash.handler.common.player;

import org.qiunet.flash.handler.common.player.event.BasePlayerEventData;
import org.qiunet.flash.handler.context.session.DSession;

/***
 * 玩家类型的messageActor 继承该类
 *
 * @author qiunet
 * 2020-10-13 20:51
 */
public abstract class AbstractPlayerActor<T extends AbstractPlayerActor> extends AbstractMessageActor<T> {

	public AbstractPlayerActor(DSession session) {
		super(session);
	}

	/**
	 * 是否跨服状态
	 *
	 * @return
	 */
	public abstract boolean isCrossStatus();
	/**
	 * 对事件的处理.
	 * 跨服的提交跨服那边. 本服调用自己的
	 * @param eventData
	 */
	protected abstract <T extends BasePlayerEventData> void submitEvent(T eventData);
}
