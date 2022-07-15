package org.qiunet.flash.handler.common.player;

import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.args.Argument;
import org.qiunet.utils.args.ArgumentKey;
import org.qiunet.utils.args.IArgsContainer;
import org.qiunet.utils.exceptions.CustomException;

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

	private String msgExecuteIndex;

	protected ISession session;

	protected AbstractMessageActor() {

	}

	public void setMsgExecuteIndex(String msgExecuteIndex) {
		if (this.msgExecuteIndex != null) {
			throw new CustomException("msgExecuteIndex setting repeated");
		}
		this.msgExecuteIndex = msgExecuteIndex;
	}

	@Override
	public String msgExecuteIndex() {
		return msgExecuteIndex;
	}

	public AbstractMessageActor(ISession session) {
		this.setSession(session);
	}

	protected void setSession(ISession session) {
		this.session = session;
	}

	@Override
	public ISession getSender() {
		return session;
	}

	@Override
	public void destroy() {
		super.destroy();
		this.container.clear();
	}

	@Override
	public <T> Argument<T> getArgument(ArgumentKey<T> key, boolean computeIfAbsent) {
		return container.getArgument(key, computeIfAbsent);
	}
}
