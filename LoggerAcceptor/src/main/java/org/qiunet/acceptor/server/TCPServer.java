package org.qiunet.acceptor.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.log4j.Logger;
import org.qiunet.acceptor.log.LogData;
import org.qiunet.acceptor.log.LogDataRegister;
import org.qiunet.acceptor.log.LoggerUtil;
import org.qiunet.logger.sender.MsgHeader;
import org.qiunet.utils.args.ArgsMapping;
import org.qiunet.utils.asyncQuene.factory.DefaultThreadFactory;

import java.net.InetSocketAddress;
import java.util.List;

public class TCPServer {
	private Logger logger = LoggerUtil.getOutLogger();
	private EventLoopGroup boss = new NioEventLoopGroup(1, new DefaultThreadFactory("tcp-boss-event-loop-"));
	private EventLoopGroup worker = new NioEventLoopGroup(0 , new DefaultThreadFactory("tcp-worker-event-loop-"));
	private int port;

	TCPServer (ArgsMapping mapping) {
		this.port = mapping.getInt("port", 8888);
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(boss, worker);

			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(new NettyTcpServerInitializer());

			bootstrap.option(ChannelOption.SO_BACKLOG, 256);
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);
			bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
			bootstrap.option(ChannelOption.SO_RCVBUF, 1024*1024*2);

			ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress(port));
			logger.error("[NettyTcpServer]  Tcp server is Listener on port ["+port+"]");

			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Created by qiunet.
	 * 17/8/13
	 */
	class NettyTcpServerInitializer extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast("decoder", new Decoder());
			pipeline.addLast("handler", new TcpServerHandler());
		}
	}

	class TcpServerHandler extends SimpleChannelInboundHandler<LogData> {

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			logger.info("udp server is listener at port["+TCPServer.this.port+"]");
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			logger.error("Exception : ", cause);
			ctx.close();
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, LogData msg) throws Exception {
			LogDataRegister.getInstance().addLogNode(msg);
		}
	}
	class Decoder extends ByteToMessageDecoder {
		@Override
		protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
			if (! in.isReadable(MsgHeader.MESSAGE_HEADER_LENGTH)) return;
			in.markReaderIndex();
			MsgHeader msgHeader = MsgHeader.parseFrom(in);
			in.readBytes(MsgHeader.MESSAGE_HEADER_LENGTH);
			in.resetReaderIndex();
			if (! in.isReadable(msgHeader.getLength())) {
				return;
			}

			out.add(new LogData(in.copy(in.readerIndex(), MsgHeader.MESSAGE_HEADER_LENGTH + msgHeader.getLength())));
			in.skipBytes(MsgHeader.MESSAGE_HEADER_LENGTH + msgHeader.getLength());
		}
	}

}
