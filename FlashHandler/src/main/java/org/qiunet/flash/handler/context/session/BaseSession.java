package org.qiunet.flash.handler.context.session;

import com.google.common.collect.Lists;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.context.session.future.DChannelFutureWrapper;
import org.qiunet.flash.handler.context.session.future.IDSessionFuture;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicBoolean;

/***
 *
 * @author qiunet
 * 2022/4/26 15:13
 */
public class BaseSession implements ISession {

	protected static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	protected Channel channel;
	/**
	 * 绑定的kcp session
	 */
	protected KcpSession kcpSession;

	protected void setChannel(Channel channel) {
		if (channel != null) {
			// 测试可能为null
			channel.closeFuture().addListener(f -> this.close(CloseCause.CHANNEL_CLOSE));
		}
		this.channel = channel;
	}

	/**
	 * session是否是活跃的.
	 * @return
	 */
	@Override
	public boolean isActive() {
		return channel != null && channel.isActive();
	}


	@Override
	public Channel channel() {
		return channel;
	}

	@Override
	public String getIp() {
		return ChannelUtil.getIp(channel);
	}
	@Override
	public <T> T getAttachObj(AttributeKey<T> key) {
		return channel.attr(key).get();
	}

	@Override
	public <T> void attachObj(AttributeKey<T> key, T obj) {
		channel.attr(key).set(obj);
	}

	private final AtomicBoolean closed = new AtomicBoolean();
	public void close(CloseCause cause) {
		if (! closed.compareAndSet(false, true)) {
			// 避免多次调用close. 多次调用监听.
			return;
		}

		logger.info("Session [{}] closed by cause [{}]", this, cause.getDesc());
		closeListeners.forEach(l -> l.close(this, cause));
		if (channel != null && (channel.isActive() || channel.isOpen())) {
			channel.close();
		}
	}

	@Override
	public void bindKcpSession(KcpSession kcpSession) {
		Attribute<ServerConnType> attr = this.channel.attr(ServerConstants.HANDLER_TYPE_KEY);
		if (attr.get() != ServerConnType.TCP && attr.get() != ServerConnType.WS) {
			throw new CustomException("Not support!");
		}
		this.kcpSession = kcpSession;
	}

	@Override
	public IDSessionFuture sendKcpMessage(IChannelMessage<?> message) {
		if (this.kcpSession == null) {
			throw new CustomException("Not bind kcp session");
		}
		return this.kcpSession.sendKcpMessage(message);
	}

	@Override
	public IDSessionFuture sendMessage(IChannelMessage<?> message) {
		return this.sendMessage(message, true);
	}

	@Override
	public IDSessionFuture sendMessage(IChannelMessage<?> message, boolean flush) {
		return new DChannelFutureWrapper(this.channel.writeAndFlush(message));
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(",", "[", "]");
		if (channel != null) {
			IMessageActor messageActor = getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
			if (messageActor != null) {
				sj.add(messageActor.getIdentity());
			}
			sj.add("ID = " + channel.id().asShortText());
			sj.add("Ip = " + getIp());
		}
		return sj.toString();
	}

	@Override
	public void addCloseListener(SessionCloseListener listener) {
		this.closeListeners.add(listener);
	}

	@Override
	public void clearCloseListener(){
		this.closeListeners.clear();
	}

	private final List<SessionCloseListener> closeListeners = Lists.newCopyOnWriteArrayList();

	@Override
	public IChannelMessageSender getSender() {
		return this;
	}


	@FunctionalInterface
	public interface SessionCloseListener {
		void close(ISession session, CloseCause cause);
	}
}
