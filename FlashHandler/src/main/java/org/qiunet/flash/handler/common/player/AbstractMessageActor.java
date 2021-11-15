package org.qiunet.flash.handler.common.player;

import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.args.Argument;
import org.qiunet.utils.args.ArgumentKey;
import org.qiunet.utils.args.IArgsContainer;

/***
 * Player 的一个总的处理对象.
 * 继承类
 *
 * @author qiunet
 * 2020/3/1 21:45
 **/
public abstract class AbstractMessageActor<P extends AbstractMessageActor<P>>
	extends MessageHandler<P> implements IMessageActor<P>, IChannelMessageSender, IArgsContainer {
	private final ArgsContainer container = new ArgsContainer();

	protected DSession session;

	protected AbstractMessageActor() {

	}

	public AbstractMessageActor(DSession session) {
		this.setSession(session);
	}

	protected void setSession(DSession session) {
		session.addCloseListener(cause -> this.destroy());
		this.session = session;
	}

	@Override
	public DSession getSender() {
		return session;
	}

	@Override
	public <T> Argument<T> getArgument(ArgumentKey<T> key, boolean computeIfAbsent) {
		return container.getArgument(key, computeIfAbsent);
	}
}
