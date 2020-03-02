package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.common.listener.SessionCloseEventData;
import org.qiunet.flash.handler.common.player.IPlayerActor;
import org.qiunet.flash.handler.context.response.push.IResponseMessage;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;

import java.net.InetSocketAddress;

/**
 * Session 的 父类
 * Created by qiunet.
 * 17/11/26
 */
public abstract class AbstractSession implements ISession {
	protected IPlayerActor playerActor;
	protected Channel channel;

	public AbstractSession(Channel channel) {
		this.channel = channel;
	}

	public void setPlayerActor(IPlayerActor playerActor) {
		this.playerActor = playerActor;
	}

	@Override
	public boolean isAuth() {
		return getUid() > 0;
	}

	@Override
	public boolean isActive() {
		return channel != null && channel.isActive();
	}

	@Override
	public Channel getChannel() {
		return channel;
	}

	@Override
	public long getUid() {
		if (this.playerActor != null){
			return this.playerActor.getPlayerId();
		}
		return 0;
	}

	@Override
	public String getOpenId() {
		if (this.playerActor != null){
			return this.playerActor.getOpenId();
		}
		return null;
	}

	@Override
	public String getIp() {
		String ip = "";
		if (channel.remoteAddress() != null && channel.remoteAddress() instanceof InetSocketAddress) {
			ip = ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
		}
		return ip;
	}

	@Override
	public ChannelFuture writeMessage(IResponseMessage message) {
		return channel.writeAndFlush(message.encode());
	}

	@Override
	public void close(CloseCause cause) {
		new SessionCloseEventData(this, cause).fireEventHandler();
	}
}
