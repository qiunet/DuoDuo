package org.qiunet.flash.handler.common.player;

import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.flash.handler.context.sender.ISessionHolder;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.args.Argument;
import org.qiunet.utils.args.ArgumentKey;
import org.qiunet.utils.args.IArgsContainer;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/***
 * Player 的一个总的处理对象.
 * 继承类
 *
 * @author qiunet
 * 2020/3/1 21:45
 **/
public abstract class AbstractMessageActor<P extends AbstractMessageActor<P>>
	extends MessageHandler<P> implements IMessageActor<P>, ISessionHolder, IArgsContainer {
	protected static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private final ArgsContainer container = new ArgsContainer();


	protected ISession session;

	protected AbstractMessageActor(String msgExecuteIndex) {
		this(null, msgExecuteIndex);
	}

	protected AbstractMessageActor(ISession session, String msgExecuteIndex) {
		super(msgExecuteIndex);
		this.setSession(session);
	}

	protected void setSession(ISession session) {
		this.session = session;
	}

	@Override
	public void destroy() {
		super.destroy();
		this.container.clear();
	}

	@Override
	public ISession getSession() {
		return session;
	}

	@Override
	public <T> Argument<T> getArgument(ArgumentKey<T> key, boolean computeIfAbsent) {
		return container.getArgument(key, computeIfAbsent);
	}
}
