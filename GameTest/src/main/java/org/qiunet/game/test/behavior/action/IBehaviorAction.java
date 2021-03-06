package org.qiunet.game.test.behavior.action;

import org.qiunet.flash.handler.context.sender.IChannelMessageSender;

/***
 * 具体的测试动作.
 * 执行一个测试动作.
 *
 * @author qiunet
 * 2021-07-05 09:57
 */
public interface IBehaviorAction extends IBehaviorNode, IChannelMessageSender {
	/**
	 * 行为类型
	 * @return
	 */
	IBehaviorType type();
}
