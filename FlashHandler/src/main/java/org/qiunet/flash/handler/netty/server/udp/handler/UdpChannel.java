package org.qiunet.flash.handler.netty.server.udp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.qiunet.flash.handler.netty.bytebuf.PooledBytebufFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/20 17:38
 **/
public class UdpChannel implements Channel {

	private Channel channel;
	private AtomicInteger idCreator; 	// 对每个udp包进行序号编号.
	private InetSocketAddress sender;
	private ConcurrentHashMap<Integer, UdpPackages> sendPackages;
	private ConcurrentHashMap<Integer, UdpPackages> receivedPackages;

	UdpChannel(Channel channel, InetSocketAddress sender) {
		this.receivedPackages = new ConcurrentHashMap<>();
		this.sendPackages = new ConcurrentHashMap<>();
		this.idCreator = new AtomicInteger();
		this.channel = channel;
		this.sender = sender;

		UdpSenderManager.getInstance().addSender(sender, this);
	}

	/***
	 * 发送消息
	 * @param message
	 */
	public void sendMessage(ByteBuf message) {
		this.sendMessage(message, true);
	}

	/***
	 * 发送消息
	 * @param message 必须是bytebuf
	 * @param importantMsg false的消息, 不需要等待回应. 发送后就取消掉了.
	 */
	public void sendMessage(ByteBuf message, boolean importantMsg) {
		UdpPackages udpPackages = new UdpPackages(idCreator.incrementAndGet(), importantMsg, message);

		for (int index = 0; index < udpPackages.byteArrs.size(); index++) {
			this.sendRealMessage(udpPackages.composeRealMessage(index));
		}
	}

	/**
	 * 发送最终的消息.
	 * 不再加任何封装
	 * @param byteBuf
	 */
	private void sendRealMessage(ByteBuf byteBuf) {
		this.channel.writeAndFlush(new DatagramPacket(byteBuf, this.sender));
	}

	/**
	 * 会对接收到的udp包进行排序黏包 如果return null说明还不是完整的包.
	 * @return
	 */
	public ByteBuf decodeMessage(DatagramPacket msg) {
		UdpPackageHeader header = UdpPackageHeader.readUdpHeader(msg.content());
		switch (header.getType()) {
			case ACK:
				this.handlerAck(header);
				break;
			case ASK:
				this.handlerAsk(header);
				break;
			case NORMAL:
				return this.decodeNormalMessage(header, msg.content());
		}
		return null;
	}


	/***
	 * 处理对方的确认消息
	 * @param header
	 */
	private void handlerAck(UdpPackageHeader header) {
		UdpPackages packages = this.sendPackages.get(header.getId());
		if (packages != null && header.getSubId() < packages.byteArrs.size()) {
			packages.byteArrs.set(header.getSubId(), null);

			for (byte[] bytes : packages.byteArrs) {
				if (bytes != null) return;
			}

			this.sendPackages.remove(header.getId());
		}
	}
	/***
	 * 处理对方的索要消息要求
	 * @param header
	 */
	private void handlerAsk(UdpPackageHeader header) {
		UdpPackages packages = this.sendPackages.get(header.getId());
		if (packages != null && header.getSubId() < packages.byteArrs.size()) {
			this.sendRealMessage(packages.composeRealMessage(header.getSubId()));
		}
	}
	/***
	 * 解析消息内容
	 * @param header
	 * @param content
	 * @return
	 */
	private ByteBuf decodeNormalMessage(UdpPackageHeader header, ByteBuf content) {
		if (! this.receivedPackages.containsKey(header.getId())) {
			synchronized (this) {
				if (! this.receivedPackages.containsKey(header.getId())) {
					this.receivedPackages.put(header.getId(), new UdpPackages(header.getSubCount()));
				}
			}
		}
		UdpPackages packages = this.receivedPackages.get(header.getId());
		byte [] bytes = new byte[content.readableBytes()];
		content.readBytes(bytes);

		if (header.getNeedAck() == 1){
			// 回复消息
			this.sendRealMessage(UdpMessageType.ACK.getMessage(header.getId(), header.getSubId()));
		}
		return packages.addNewPartMessage(header, bytes);
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
		return null;
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
		return this.writeAndFlush(msg);
	}

	@Override
	public ChannelFuture write(Object msg, ChannelPromise promise) {
		return this.writeAndFlush(msg, promise);
	}

	@Override
	public Channel flush() {
		return channel.flush();
	}

	@Override
	public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
		this.sendMessage(((ByteBuf) msg));
		return null;
	}

	@Override
	public ChannelFuture writeAndFlush(Object msg) {
		return this.writeAndFlush(msg, null);
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

	/**
	 * 包的超时处理.
	 * 如果是发送包.再次发送该包内容.
	 * 如果是接收包. 要求对方重新传送指定的子包.
	 */
	void timeoutHandler(){

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
			return o.asLongText().compareTo(this.asLongText());
		}
	}
}
