package org.qiunet.flash.handler.netty.protocol;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.log4j.Logger;
import org.qiunet.flash.handler.context.header.ProtocolHeader;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.RequestHandlerMapping;

import java.util.List;

/**
 * 消息的解析
 * Created by qiunet.
 * 17/8/13
 */
public class ProtobufDecoder extends ByteToMessageDecoder {

	private Logger logger = Logger.getLogger(ProtoBufEncoder.class);
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (! in.isReadable(ProtocolHeader.REQUEST_HEADER_LENGTH)) return;
		in.markReaderIndex();

		ProtocolHeader header = new ProtocolHeader(in);

		if (! in.isReadable(header.getLength())) {
			in.resetReaderIndex();
			return;
		}
		byte [] bytes = new byte[header.getLength()];
		in.readBytes(bytes);

		IHandler<GeneratedMessageV3> handler = RequestHandlerMapping.getInstance().getHandler(header.getProtocolId());
		try {
			GeneratedMessageV3 msg = handler.parseRequestData(bytes);
			logger.info("[decode] :"+msg.toString());
			out.add(msg);
		}catch (Exception e) {
			e.printStackTrace();
			ctx.close();
		}
	}
}
