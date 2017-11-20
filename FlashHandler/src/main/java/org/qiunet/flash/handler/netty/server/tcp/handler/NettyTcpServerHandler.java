package org.qiunet.flash.handler.netty.server.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import org.qiunet.flash.handler.acceptor.Acceptor;
import org.qiunet.flash.handler.context.IContext;
import org.qiunet.flash.handler.param.TcpBootstrapParams;


/**
 * Created by qiunet.
 * 17/8/13
 */
public class NettyTcpServerHandler extends ChannelInboundHandlerAdapter {
	private Logger logger = Logger.getLogger(getClass());
	private Acceptor acceptor = Acceptor.getInstance();
	private TcpBootstrapParams params;

	public NettyTcpServerHandler(TcpBootstrapParams params) {
		this.params = params;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		acceptor.process((IContext) msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Exception : ", cause);
		ctx.close();
	}
}
