package org.qiunet.flash.handler.common.player;

import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.context.session.DSession;

/***
 * Player 的一个总的处理对象.
 * 继承类
 *
 * @author qiunet
 * 2020/3/1 21:45
 **/
public abstract class AbstractMessageActor<P extends AbstractMessageActor>
	extends MessageHandler<P> implements IMessageActor<P>, IChannelMessageSender {

	protected DSession session;

	public AbstractMessageActor(DSession session) {
		this.session = session;
	}

	public DSession getSession() {
		return session;
	}

	@Override
	public void send(IChannelMessage message) {
		session.writeMessage(message);
	}
}
