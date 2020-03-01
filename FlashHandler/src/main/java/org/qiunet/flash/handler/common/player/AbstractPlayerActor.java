package org.qiunet.flash.handler.common.player;

import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.flash.handler.context.response.push.IResponseMessage;
import org.qiunet.flash.handler.context.sender.IMessageSender;
import org.qiunet.flash.handler.context.session.ISession;

/***
 * Player 的一个总的处理对象.
 * 继承类
 *
 * @author qiunet
 * 2020/3/1 21:45
 **/
public abstract class AbstractPlayerActor<T extends AbstractPlayerActor, Session extends ISession> extends MessageHandler<T>
		implements IPlayerActor<Session>, IMessageSender {
	protected Session session;

	public AbstractPlayerActor(Session session) {
		this.session = session;
	}

	@Override
	public long getPlayerId() {
		return session.getUid();
	}

	public Session getSession() {
		return session;
	}

	@Override
	public boolean isAuth() {
		return session.isAuth();
	}

	@Override
	public void send(IResponseMessage message) {
		session.writeMessage(message);
	}
}
