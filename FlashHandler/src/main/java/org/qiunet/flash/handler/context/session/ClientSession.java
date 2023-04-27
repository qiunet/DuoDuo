package org.qiunet.flash.handler.context.session;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.header.ISequenceProtocolHeader;
import org.qiunet.flash.handler.context.header.SequenceIdProtocolHeader;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.utils.exceptions.CustomException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

/***
 * 客户端专用session
 *
 * @author qiunet
 * 2023/3/24 20:36
 */
public class ClientSession extends BaseChannelSession implements IClientSession {
	private final AtomicInteger SEQUENCE_GENERATE = new AtomicInteger();

	private final Cache<Integer, BiConsumer<StatusResult, IChannelData>> reqCache = CacheBuilder.newBuilder()
		.expireAfterWrite(15, TimeUnit.SECONDS)
		.build();

	private final boolean sequenceHeader;

	public ClientSession(Channel channel, IProtocolHeader header) {
		super.setChannel(channel);
		sequenceHeader = SequenceIdProtocolHeader.class.isAssignableFrom(header.getClass());
	}

	public boolean isSequenceHeader() {
		return sequenceHeader;
	}

	@Override
	public ChannelFuture sendMessage(IChannelMessage<?> message, boolean flush, BiConsumer<StatusResult, IChannelData> consumer) {
		if (consumer != null && !sequenceHeader) {
			throw new CustomException("Need use SequenceProtocolHeader!");
		}
		if (sequenceHeader && consumer != null) {
			int sequence = SEQUENCE_GENERATE.incrementAndGet();
			message.add(ISequenceProtocolHeader.MESSAGE_KEY, sequence);
			reqCache.put(sequence, consumer);
			if (sequence >= Short.MAX_VALUE) {
				SEQUENCE_GENERATE.set(0);
			}
		}
		return super.sendMessage(message, flush);
	}

	/**
	 * 根据序列取到请求数据
	 * @param sequence 序列id
	 * @return 是否消费成功
	 */
	public boolean rsp(int sequence, IChannelData response) {
		if (sequence <= 0) {
			return false;
		}
		BiConsumer<StatusResult, IChannelData> consumer = reqCache.getIfPresent(sequence);
		if (consumer == null) {
			return false;
		}
		boolean success = response.protocolId() != IProtocolId.System.ERROR_STATUS_TIPS_RSP;
		StatusResult result = success ? StatusResult.SUCCESS : StatusResult.FAIL;
		consumer.accept(result, response);
		return true;
	}

	@Override
	public ISession getSession() {
		return this;
	}
}
