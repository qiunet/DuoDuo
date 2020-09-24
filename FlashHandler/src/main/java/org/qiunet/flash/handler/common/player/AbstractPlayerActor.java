package org.qiunet.flash.handler.common.player;

import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.flash.handler.context.response.push.IResponseMessage;
import org.qiunet.flash.handler.context.sender.IResponseSender;
import org.qiunet.flash.handler.context.session.DSession;

/***
 * Player 的一个总的处理对象.
 * 继承类
 *
 * @author qiunet
 * 2020/3/1 21:45
 **/
public abstract class AbstractPlayerActor<P extends AbstractPlayerActor>
	extends MessageHandler<P> implements IPlayerActor<P>, IResponseSender {

	protected DSession session;

	public AbstractPlayerActor(DSession session) {
		this.session = session;
	}

	public DSession getSession() {
		return session;
	}

	@Override
	public boolean isAuth() {
		return getPlayerId() > 0;
	}

	@Override
	public void send(IResponseMessage message) {
		session.writeMessage(message);
	}
}
