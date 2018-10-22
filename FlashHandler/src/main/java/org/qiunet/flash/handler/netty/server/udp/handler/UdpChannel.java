package org.qiunet.flash.handler.netty.server.udp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.ProtocolHeader;
import org.qiunet.utils.encryptAndDecrypt.CrcUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/20 17:38
 **/
public class UdpChannel implements Channel {
	protected Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);
	private boolean crc;
	private boolean server;
	private Channel channel;
	private AtomicInteger senderIdCreator; 	// 对每个udp包进行序号编号.
	private AtomicInteger receiveIdCreator; 	// 接收的包id自增
	private InetSocketAddress sender;
	// 发送的包 缓冲区
	private ConcurrentLinkedQueue<UdpPackages> sendPackages;
	// 当前正接受的包
	private AtomicReference<UdpPackages> currReceivePackage;
	// 当前正发送的包
	private AtomicReference<UdpPackages> currSendPackage;

	/**
	 * 构造一个udpChannel
	 * @param channel
	 * @param sender
	 * @param crc
	 * @param server 是否服务端. 服务端构造需要添加到sessionManager
	 */
	public UdpChannel(Channel channel, InetSocketAddress sender, boolean crc, boolean server) {
		this.currReceivePackage = new AtomicReference<>();
		this.sendPackages = new ConcurrentLinkedQueue();
		this.currSendPackage = new AtomicReference<>();
		this.senderIdCreator = new AtomicInteger();
		this.channel = channel;
		this.sender = sender;
		this.server = server;
		this.crc = crc;

		if (server) {
			UdpSenderManager.getInstance().addSender(sender, this);
		}
	}
	/***
	 * 发送消息
	 * @param message 必须是bytebuf
	 */
	public void sendMessage(ByteBuf message) {
		UdpPackages udpPackages = new UdpPackages(senderIdCreator.incrementAndGet(), message);
		this.sendPackages.add(udpPackages);

		this.triggerSendMessage();
	}

	/**
	 * 发送最终的消息.
	 * 不再加任何封装
	 * @param byteBuf
	 */
	private void sendRealMessage(ByteBuf byteBuf) {
		this.sendRealMessage(byteBuf, 1);
	}
	/**
	 * 发送最终的消息. 发送多次. udp如果网络不好. 发送多次有利于客户端收取.
	 * 不再加任何封装
	 * @param byteBuf
	 */
	private void sendRealMessage(ByteBuf byteBuf, int count) {
		if (count > 1) byteBuf.retain(count - 1);
		for (int i = 1; i <= count; i++) {
			this.channel.writeAndFlush(new DatagramPacket(byteBuf, this.sender));
		}
	}

	/***
	 * 触发发送消息
	 */
	private synchronized void triggerSendMessage(){
		// 当前有传送的. 不触发
		if (this.currSendPackage.get() != null) return;

		// 重新比较 set. 如果还是空. 说明队列为空.
		if( (! this.currSendPackage.compareAndSet(null, this.sendPackages.poll()))
		 || this.currSendPackage.get() == null 	// 队里为空, 可能弹出的是null
		){
			return;
		}

		// 延迟计算的时间 从这里开始.
		currSendPackage.get().dt = System.currentTimeMillis();
		for (int index = 0; index < currSendPackage.get().byteArrs.size(); index++) {
			this.sendRealMessage(currSendPackage.get().composeRealMessage(index));
		}
	}
	/**
	 * 会对接收到的udp包进行排序黏包 如果return null说明还不是完整的包.
	 * @return
	 */
	public MessageContent decodeMessage(DatagramPacket msg) {
		UdpPackageHeader header = UdpPackageHeader.readUdpHeader(msg.content());
		if (! header.magicValid()) {
			logger.error("Udp package magic is error!");
			return null;
		}
		switch (header.getType()) {
			case ACK:
				this.handlerAck(header);
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
		UdpPackages packages = this.currSendPackage.get();
		if (packages != null && header.getSubId() < packages.byteArrs.size()) {
			packages.byteArrs.set(header.getSubId(), null);

			for (byte[] bytes : packages.byteArrs) {
				if (bytes != null) return;
			}

			this.currSendPackage.compareAndSet(packages, null);
			this.triggerSendMessage();
		}
	}
	/***
	 * 解析消息内容
	 * @param header
	 * @param content
	 * @return
	 */
	private MessageContent decodeNormalMessage(UdpPackageHeader header, ByteBuf content) {
		if (this.receiveIdCreator != null && header.getId() != this.receiveIdCreator.get()) {
			logger.debug("Decode id error, curr id["+this.receiveIdCreator.get()+"] header Id ["+header.getId()+"]");
			return null;
		}

		if (this.currReceivePackage.get() == null) {
			synchronized (this) { // 同时到达的包 可能产生冲突. 会跳过这里, 先执行到下面的内容. 这里需要同步下.
				if (this.currReceivePackage.compareAndSet(null, new UdpPackages(header.getId(), header.getSubCount()))) {
					if (this.receiveIdCreator == null) this.receiveIdCreator = new AtomicInteger(header.getId());
				}
			}
		}

		ByteBuf in = null;
		UdpPackages receivePackage = this.currReceivePackage.get();
		if (receivePackage.byteArrs.get(header.getSubId()) == null) {
			byte [] bytes = new byte[content.readableBytes()];
			content.readBytes(bytes);

			in = receivePackage.addNewPartMessage(header, bytes);
		}
		MessageContent messageContent = null;
		if (in != null) {
			try {
				messageContent = this.decodeToMessageContent(in);
			}finally {
				ReferenceCountUtil.release(in);
			}

			if (messageContent == null) {
				logger.info("UdpPackages id ["+header.getId()+"] Message is error . remove from packages!");
				return null;
			}
			// 准备接受下一个包
			if (this.currReceivePackage.compareAndSet(receivePackage,null)) {
				this.receiveIdCreator.incrementAndGet();
			}
		}
		// 等处理完上面的事情, 再回复消息 免得并发
		this.sendRealMessage(UdpMessageType.ACK.getMessage(header.getId(), header.getSubId()));
		return messageContent;
	}

	private MessageContent decodeToMessageContent(ByteBuf in) {
		if (! in.isReadable(ProtocolHeader.REQUEST_HEADER_LENGTH))  {
			return null;
		}

		ProtocolHeader protocolHeader = new ProtocolHeader(in);
		if (! protocolHeader.isMagicValid()) {
			logger.error("Invalid message, magic is error! "+ Arrays.toString(protocolHeader.getMagic()));
			return null;
		}

		if (protocolHeader.getLength() < 0  || ! in.isReadable(protocolHeader.getLength())) {
			logger.error("Invalid message, length is error! length is : "+ protocolHeader.getLength());
			return null;
		}

		byte [] bytes = new byte[protocolHeader.getLength()];
		in.readBytes(bytes);

		if (crc && ! protocolHeader.crcIsValid(CrcUtil.getCrc32Value(bytes))) {
			logger.error("Invalid message crc! server is : "+ CrcUtil.getCrc32Value(bytes) +" client is "+protocolHeader.getCrc());
			return null;
		}
		return new MessageContent(protocolHeader.getProtocolId(), bytes);
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
		if (this.server)UdpSenderManager.getInstance().removeChannel(this.sender);
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
		return null;
	}

	@Override
	public ChannelFuture close(ChannelPromise promise) {
		if (this.server) UdpSenderManager.getInstance().removeChannel(this.sender);
		return null;
	}

	@Override
	public ChannelFuture deregister(ChannelPromise promise) {
		return null;
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
		long now = System.currentTimeMillis();
		UdpPackages sendPackage = this.currSendPackage.get();
		if (sendPackage != null && now - sendPackage.getDt() > 200) {
			sendPackage.retainSendCount();
			if (logger.isDebugEnabled()) {
				logger.debug("udp package id ["+sendPackage.getId()+"] was resend!");
			}
			for (int i = 0; i < sendPackage.byteArrs.size(); i++) {
				if (sendPackage.byteArrs.get(i) == null) continue;
				// 随着次数增加. 发送次数也增多. 保证到达可能性
				this.sendRealMessage(sendPackage.composeRealMessage(i), Math.max(1, sendPackage.getResendCount()/2));
			}
			// 5次后. 删除. 免得一直有问题.
			if(sendPackage.getResendCount() >= 10) {
				logger.error("Socket send package timeout");
				this.currSendPackage.compareAndSet(sendPackage, null);
				this.triggerSendMessage();
			}
		}

		UdpPackages receivePackage = this.currReceivePackage.get();
		if (receivePackage != null && now - receivePackage.getDt() >= 1500) {
			logger.error("Socket receive package Timeout!");
			// 重置状态. 重新开始.
			boolean ret = this.currReceivePackage.compareAndSet(receivePackage, null);
			if (ret) {
				this.receiveIdCreator = null;
			}
		}
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
