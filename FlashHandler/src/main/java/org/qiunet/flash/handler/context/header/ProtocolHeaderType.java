package org.qiunet.flash.handler.context.header;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;

/***
 * 协议头类型
 *
 * qiunet
 * 2021/9/23 18:11
 **/
public enum ProtocolHeaderType implements IProtocolHeaderType {
	/**
	 * 对外服务
	 */
	server {
		@Override
		public IProtocolHeader outHeader(int protocolId, IChannelMessage<?> message) {
			return ServerProtocolHeader.valueOf(protocolId, message);
		}

		@Override
		public IProtocolHeader inHeader(ByteBuf in, Channel channel) {
			return ServerProtocolHeader.valueOf(in, channel);
		}

		@Override
		public int getReqHeaderLength() {
			return ServerProtocolHeader.REQUEST_HEADER_LENGTH;
		}

		@Override
		public int getRspHeaderLength() {
			return ServerProtocolHeader.RESPONSE_HEADER_LENGTH;
		}
	},
	/**
	 * 服务器节点间通讯
	 */
	node{
		@Override
		public IProtocolHeader outHeader(int protocolId, IChannelMessage<?> message) {
			return new NodeProtocolHeader(protocolId, message);
		}

		@Override
		public IProtocolHeader inHeader(ByteBuf in, Channel channel) {
			return new NodeProtocolHeader(in, channel);
		}

		@Override
		public int getReqHeaderLength() {
			return NodeProtocolHeader.REQUEST_HEADER_LENGTH;
		}

		@Override
		public int getRspHeaderLength() {
			return NodeProtocolHeader.RESPONSE_HEADER_LENGTH;
		}
	},
	/**
	 * Cross服务, 正常请求, 响应需要告知:
	 * 1. 是否发送给玩家.
	 * 2. 是否flush
	 * 3. 是否使用kcp发送客户端
	 */
	cross{
		@Override
		public IProtocolHeader outHeader(int protocolId, IChannelMessage<?> message) {
			return CrossProtocolHeader.valueOf(protocolId, message);
		}

		@Override
		public IProtocolHeader inHeader(ByteBuf in, Channel channel) {
			return CrossProtocolHeader.valueOf(in, channel);
		}

		@Override
		public int getReqHeaderLength() {
			return CrossProtocolHeader.REQUEST_HEADER_LENGTH;
		}

		@Override
		public int getRspHeaderLength() {
			return CrossProtocolHeader.RESPONSE_HEADER_LENGTH;
		}
	},
	/**
	 * 测试的客户端
	 */
	client{
		@Override
		public IProtocolHeader outHeader(int protocolId, IChannelMessage<?> message) {
			return new ClientProtocolHeader(protocolId, message);
		}

		@Override
		public IProtocolHeader inHeader(ByteBuf in, Channel channel) {
			return new ClientProtocolHeader(in, channel);
		}

		@Override
		public int getReqHeaderLength() {
			return ClientProtocolHeader.REQUEST_HEADER_LENGTH;
		}

		@Override
		public int getRspHeaderLength() {
			return ClientProtocolHeader.RESPONSE_HEADER_LENGTH;
		}
	},
}
