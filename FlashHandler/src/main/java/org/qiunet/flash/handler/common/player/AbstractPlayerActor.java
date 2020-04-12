package org.qiunet.flash.handler.common.player;

import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.flash.handler.context.response.push.IResponseMessage;
import org.qiunet.flash.handler.context.sender.IResponseSender;
import org.qiunet.flash.handler.context.session.AbstractSession;
import org.qiunet.flash.handler.context.session.ISession;

/***
 * Player 的一个总的处理对象.
 * 继承类
 *
 * @author qiunet
 * 2020/3/1 21:45
 **/
public abstract class AbstractPlayerActor<S extends ISession, P extends AbstractPlayerActor>
	extends MessageHandler<P> implements IPlayerActor<S, P>, IResponseSender {

	protected S session;

	public AbstractPlayerActor(S session) {
		if (session instanceof AbstractSession) {
			((AbstractSession) session).setPlayerActor(this);
		}
		this.session = session;
	}

	public S getSession() {
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
