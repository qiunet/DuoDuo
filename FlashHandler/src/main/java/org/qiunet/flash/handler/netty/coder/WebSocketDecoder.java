package org.qiunet.flash.handler.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.header.IProtocolHeaderAdapter;
import org.qiunet.flash.handler.netty.bytebuf.PooledBytebufFactory;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.List;

/**
 * 消息的解析
 * Created by qiunet.
 * 17/8/13
 */
public class WebSocketDecoder extends ByteToMessageDecoder {
	private Logger logger = LoggerType.DUODUO.getLogger();
	private int maxReceivedLength;
	private boolean encryption;

	//协议上行数据(处理上行协议过大，超过websocket的frame大小限制 65536的情况，将多帧数据流收集起来，再读取)
	private int dataMaxLength;		//上行完整数据长度
	private int readLength;			//已读数据长度
	private IProtocolHeader header;	//协议头
	private ByteBuf byteBuf;		//把多frame的byteBuf汇总到这里

	public WebSocketDecoder(int maxReceivedLength, boolean encryption) {
		this.encryption = encryption;
		this.maxReceivedLength = maxReceivedLength;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (dataMaxLength == 0) {
			IProtocolHeaderAdapter adapter = ChannelUtil.getProtolHeaderAdapter(ctx.channel());
			header = adapter.newHeader(in);
			if (!header.isMagicValid()) {
				logger.error("Invalid message, magic is error! " + header);
				ctx.channel().close();
				return;
			}

			if (header.getLength() < 0 || header.getLength() > maxReceivedLength) {
				logger.error("Invalid message, length is error! length is : " + header.getLength());
				ctx.channel().close();
				return;
			}
			dataMaxLength = header.getLength();
			readLength = in.readerIndex();
			/*System.out.println("header.getLength():" + header.getLength()
					+ "\tin.readableBytes():" + in.readableBytes()
					+ "\tmaxReceivedLength:" + maxReceivedLength
					+ "\tin.readerIndex():" + in.readerIndex()
			);*/
			byteBuf = PooledBytebufFactory.getInstance().alloc(dataMaxLength);
		}
		readLength += in.readableBytes();
		byteBuf.writeBytes(in);

		//数据读完了
		if (readLength >= dataMaxLength) {
			byte[] dataBytes = new byte[dataMaxLength];
			byteBuf.readBytes(dataBytes);
			if (encryption && (dataBytes = header.validAndDecryptBytes(dataBytes)) == null) {
				ctx.channel().close();
				return;
			}
			MessageContent context = new MessageContent(header.getProtocolId(), dataBytes);
			out.add(context);
		}
	}
}
