package org.qiunet.flash.handler.context.response.push;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.string.ToString;

import java.nio.ByteBuffer;

/**
 * 对外响应的编码消息
 * @author qiunet.
 * 17/12/11
 */
public interface IChannelMessage<T> extends IExtraInfo {
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
	default boolean debugOut(){
		return ! this.getContent().getClass().isAnnotationPresent(SkipDebugOut.class);
	}

	/**
	 * 格式字符串
	 * @return
	 */
	default String _toString() {
		return ToString.toString(getContent());
	}

	/**
	 * 获得 添加 protocol header 的 ByteBuf
	 * @param channel
	 * @return
	 */
	default ByteBuf withHeaderByteBuf(Channel channel) {
		IProtocolHeader protocolHeader = ChannelUtil.getProtocolHeader(channel);
		IProtocolHeader.ProtocolHeader header = protocolHeader.outHeader(this, channel);
		ByteBuf byteBuf = Unpooled.wrappedBuffer(header.headerByteBuf(), _content());

		header.recycle();
		this.recycle();
		return byteBuf;
	}

	/**
	 * 获得不添加 protocol header 的ByteBuf
	 * @return
	 */
	default ByteBuf withoutHeaderByteBuf() {
		try {
			return _content();
		}finally {
			this.recycle();
		}
	}

	/**
	 * 内容的byteBuf
	 * 除非了解自己用的那个数据. 否则不建议调用
	 * @return ByteBuf
	 */
	private ByteBuf _content() {
		if (this instanceof BaseByteBufMessage<T>) {
			return ((BaseByteBufMessage<T>) this).getByteBuf();
		}
		return  Unpooled.wrappedBuffer(this.byteBuffer().rewind());
	}
}
