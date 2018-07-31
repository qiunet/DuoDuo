package org.qiunet.flash.handler.netty.server.udp.handler;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/20 17:38
 **/
public class UdpChannel implements Channel {

	private Channel channel;

	private InetSocketAddress sender;

	UdpChannel(Channel channel, InetSocketAddress sender) {
		this.channel = channel;
		this.sender = sender;

		UdpSenderManager.getInstance().addSender(sender, this);
	}

	/***
	 * 发送消息
	 * @param message
	 */
	public void sendMessage(Object message) {
		this.sendMessage(message, true);
	}

	/***
	 * 发送消息
	 * @param message
	 * @param importantMsg false的消息, 不需要等待回应. 发送后就取消掉了.
	 */
	public void sendMessage(Object message, boolean importantMsg) {

	}

	public InetSocketAddress getSender() {
		return sender;
	}

	@Override
	public ChannelId id() {
		return new UdpChannelId(sender);
	}

	@Override
	public EventLoop eventLoop() {
		return channel.eventLoop();
	}

	@Override
	public Channel parent() {
		return channel.parent();
	}

	@Override
	public ChannelConfig config() {
		return channel.config();
	}

	@Override
	public boolean isOpen() {
		return channel.isOpen();
	}

	@Override
	public boolean isRegistered() {
		return channel.isRegistered();
	}

	@Override
	public boolean isActive() {
		return channel.isActive();
	}

	@Override
	public ChannelMetadata metadata() {
		return channel.metadata();
	}

	@Override
	public SocketAddress localAddress() {
		return channel.localAddress();
	}

	@Override
	public SocketAddress remoteAddress() {
		return sender;
	}

	@Override
	public ChannelFuture closeFuture() {
		return channel.closeFuture();
	}

	@Override
	public boolean isWritable() {
		return channel.isWritable();
	}

	@Override
	public long bytesBeforeUnwritable() {
		return channel.bytesBeforeUnwritable();
	}

	@Override
	public long bytesBeforeWritable() {
		return channel.bytesBeforeWritable();
	}

	@Override
	public Unsafe unsafe() {
		return channel.unsafe();
	}

	@Override
	public ChannelPipeline pipeline() {
		return channel.pipeline();
	}

	@Override
	public ByteBufAllocator alloc() {
		return channel.alloc();
	}

	@Override
	public ChannelFuture bind(SocketAddress localAddress) {
		return channel.bind(localAddress);
	}

	@Override
	public ChannelFuture connect(SocketAddress remoteAddress) {
		return channel.connect(remoteAddress);
	}

	@Override
	public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
		return channel.connect(remoteAddress, localAddress);
	}

	@Override
	public ChannelFuture disconnect() {
		return channel.disconnect();
	}

	@Override
	public ChannelFuture close() {
		UdpSenderManager.getInstance().removeChannel(this.sender);
		return channel.close();
	}

	@Override
	public ChannelFuture deregister() {
		return channel.deregister();
	}

	@Override
	public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
		return channel.bind(localAddress, promise);
	}

	@Override
	public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
		return channel.connect(remoteAddress, promise);
	}

	@Override
	public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
		return channel.connect(remoteAddress, localAddress, promise);
	}

	@Override
	public ChannelFuture disconnect(ChannelPromise promise) {
		return channel.disconnect(promise);
	}

	@Override
	public ChannelFuture close(ChannelPromise promise) {
		UdpSenderManager.getInstance().removeChannel(this.sender);
		return channel.close(promise);
	}

	@Override
	public ChannelFuture deregister(ChannelPromise promise) {
		return channel.deregister(promise);
	}

	@Override
	public Channel read() {
		return channel.read();
	}

	@Override
	public ChannelFuture write(Object msg) {
		return channel.write(msg);
	}

	@Override
	public ChannelFuture write(Object msg, ChannelPromise promise) {
		return channel.write(msg, promise);
	}

	@Override
	public Channel flush() {
		return channel.flush();
	}

	@Override
	public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
		return channel.writeAndFlush(msg, promise);
	}

	@Override
	public ChannelFuture writeAndFlush(Object msg) {
		return channel.writeAndFlush(msg);
	}

	@Override
	public ChannelPromise newPromise() {
		return channel.newPromise();
	}

	@Override
	public ChannelProgressivePromise newProgressivePromise() {
		return channel.newProgressivePromise();
	}

	@Override
	public ChannelFuture newSucceededFuture() {
		return channel.newSucceededFuture();
	}

	@Override
	public ChannelFuture newFailedFuture(Throwable cause) {
		return channel.newFailedFuture(cause);
	}

	@Override
	public ChannelPromise voidPromise() {
		return channel.voidPromise();
	}

	@Override
	public <T> Attribute<T> attr(AttributeKey<T> key) {
		return channel.attr(key);
	}

	@Override
	public <T> boolean hasAttr(AttributeKey<T> key) {
		return channel.hasAttr(key);
	}

	@Override
	public int compareTo(Channel o) {
		return channel.compareTo(o);
	}

	private class UdpChannelId implements ChannelId {
		private String id;
		UdpChannelId(InetSocketAddress address) {
			this.id = address.toString();
		}

		@Override
		public String asShortText() {
			return id;
		}

		@Override
		public String asLongText() {
			return id;
		}

		@Override
		public int compareTo(ChannelId o) {
			return 0;
		}
	}
}
