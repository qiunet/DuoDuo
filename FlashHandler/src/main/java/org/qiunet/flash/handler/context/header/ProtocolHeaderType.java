package org.qiunet.flash.handler.context.header;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.nio.ByteBuffer;

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
		public IProtocolHeader outHeader(int protocolId, ByteBuffer bytes) {
			return new ServerProtocolHeader(protocolId, bytes);
		}

		@Override
		public IProtocolHeader inHeader(ByteBuf in, Channel channel) {
			return new ServerProtocolHeader(in, channel);
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
		public IProtocolHeader outHeader(int protocolId, ByteBuffer bytes) {
			return new NodeProtocolHeader(protocolId, bytes);
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
	 * 测试的客户端
	 */
	client{
		@Override
		public IProtocolHeader outHeader(int protocolId, ByteBuffer bytes) {
			return new ClientProtocolHeader(protocolId, bytes);
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
