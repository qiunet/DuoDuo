package org.qiunet.flash.handler.context.response.push;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.header.IProtocolHeaderType;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.data.ByteUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.IDataToString;
import org.qiunet.utils.string.ToString;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 对外响应的编码消息
 * @author qiunet.
 * 17/12/11
 */
public interface IChannelMessage<T> {
	/***
	 * 得到消息的内容
	 * @return
	 */
	T getContent();

	/***
	 * 转换bytes
	 * @return
	 */
	ByteBuffer byteBuffer();
	/**
	 * 得到消息id
	 * @return
	 */
	int getProtocolID();
	/**
	 * 是否需要打印输出
	 * @return
	 */
	default boolean needLogger(){
		return ! this.getContent().getClass().isAnnotationPresent(SkipDebugOut.class);
	}

	/**
	 * 转logger 格式字符串
	 * @return
	 */
	default String toStr() {
		T content = this.getContent();
		if (IDataToString.class.isAssignableFrom(content.getClass())) {
			return ((IDataToString) content)._toString();
		}
		return ToString.toString(content);
	}

	/**
	 * 获得 添加 protocol header 的 ByteBuf
	 * @param channel
	 * @return
	 */
	default ByteBuf withHeaderByteBuf(Channel channel) {
		IProtocolHeaderType adapter = ChannelUtil.getProtocolHeaderAdapter(channel);
		IProtocolHeader header = adapter.outHeader(this.getProtocolID(), this);

		ByteBuf byteBuf = Unpooled.wrappedBuffer(((ByteBuffer) header.dataBytes().rewind()), ((ByteBuffer) this.byteBuffer().rewind()));

		if (LoggerType.DUODUO_FLASH_HANDLER.isDebugEnabled()) {
			LoggerType.DUODUO_FLASH_HANDLER.debug("header: {}", Arrays.toString(ByteUtil.readBytebuffer(header.dataBytes())));
			LoggerType.DUODUO_FLASH_HANDLER.debug("body: {}", Arrays.toString(ByteUtil.readBytebuffer(this.byteBuffer())));
		}
		return byteBuf;
	}

	/**
	 * 获得不添加 protocol header 的ByteBuf
	 * @return
	 */
	default ByteBuf withoutHeaderByteBuf() {
		if (LoggerType.DUODUO_FLASH_HANDLER.isDebugEnabled()) {
			LoggerType.DUODUO_FLASH_HANDLER.debug("body: {}", Arrays.toString(ByteUtil.readBytebuffer(this.byteBuffer())));
		}
		return Unpooled.wrappedBuffer(byteBuffer());
	}
}
