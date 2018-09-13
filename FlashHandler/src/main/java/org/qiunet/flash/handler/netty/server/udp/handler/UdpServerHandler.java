package org.qiunet.flash.handler.netty.server.udp.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.UdpBootstrapParams;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/20 14:43
 **/
public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
	private Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);
	private UdpBootstrapParams params;
	public UdpServerHandler(UdpBootstrapParams params) {
		UdpSenderManager.getInstance().params = params;
		this.params = params;
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		System.out.println("channel:["+msg.sender().hashCode()+"] "+ msg.content().toString(StringUtil.UTF8));
		ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("xxxx", StringUtil.UTF8), msg.sender()));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Acceptor is ERROR: ",cause);
		ctx.close();
	}
}
