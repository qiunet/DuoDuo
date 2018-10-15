package org.qiunet.flash.handler.netty.server.udp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.ReferenceCountUtil;
import org.qiunet.flash.handler.acceptor.Acceptor;
import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.ProtocolHeader;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequestContext;
import org.qiunet.flash.handler.context.request.udp.IUdpRequestContext;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.UdpBootstrapParams;
import org.qiunet.utils.encryptAndDecrypt.CrcUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/20 14:43
 **/
public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
	private Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);
	private Acceptor acceptor = Acceptor.getInstance();
	private UdpBootstrapParams params;
	public UdpServerHandler(UdpBootstrapParams params) {
		UdpSenderManager.getInstance().params = params;
		this.params = params;
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		UdpChannel channel = UdpSenderManager.getInstance().getUdpChannel(msg.sender());
		if (channel == null) {
			// 也会写入sessionManager
			channel = new UdpChannel(ctx.channel(), msg.sender(), this.params.isCrc(), true);
		}

		MessageContent content = channel.decodeMessage(msg);
		if (content == null) return;

		IHandler handler = params.getAdapter().getHandler(content);
		if (handler == null) {
			channel.writeAndFlush(params.getErrorMessage().getHandlerNotFound().encode().encodeToByteBuf());
			return;
		}

		IUdpRequestContext context = params.getAdapter().createUdpRequestContext(content, channel, handler, params);
		params.getSessionEvent().sessionReceived(channel, HandlerType.UDP, context);
		acceptor.process(context);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("UdpServerHandler is ERROR: ",cause);
		// 没有状态. 不能关闭. 不能使用ctx.close()
	}

	/***
	 * 反序列化
	 * @param msg
	 * @return
	 */
	private MessageContent decode(ByteBuf msg) {

		ProtocolHeader header = new ProtocolHeader(msg);
		if (! header.isMagicValid()) {
			logger.error("Invalid message, magic is error! "+ Arrays.toString(header.getMagic()));
			return null;
		}

		if (header.getLength() < 0 || header.getLength() > params.getMaxReceivedLength()) {
			logger.error("Invalid message, length is error! length is : "+ header.getLength());
			return null;
		}

		byte [] bytes = new byte[header.getLength()];
		msg.readBytes(bytes);

		if (params.isCrc() && ! header.crcIsValid(CrcUtil.getCrc32Value(bytes))) {
			logger.error("Invalid message crc! server is : "+ CrcUtil.getCrc32Value(bytes) +" client is "+header.getCrc());
			return null;
		}

		MessageContent context = new MessageContent(header.getProtocolId(), bytes);
		return context;
	}
}
