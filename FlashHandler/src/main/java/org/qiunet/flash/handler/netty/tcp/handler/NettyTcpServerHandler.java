package org.qiunet.flash.handler.netty.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;


/**
 * Created by qiunet.
 * 17/8/13
 */
public class NettyTcpServerHandler extends ChannelInboundHandlerAdapter {
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("ID["+ctx.channel().id()+"] msg: "+msg);
//            ByteBuf buf = ((ByteBuf) msg);
//            try {
//                 while (buf.isReadable()) {
//                     System.out.print(((char) buf.readByte()));
//                     System.out.flush();
//                 }
//           }finally {
//               ReferenceCountUtil.release(msg);
//           }
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Exception : ", cause);
		ctx.close();
	}
}
