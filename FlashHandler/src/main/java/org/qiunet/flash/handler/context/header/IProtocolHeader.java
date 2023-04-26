package org.qiunet.flash.handler.context.header;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.utils.pool.IRecycle;

import java.nio.ByteBuffer;

/***
 * 协议头.
 * 只有客户端到服务端的协议头可以自定义.
 * 服务端节点通讯. 跨服通讯都是使用DuoDuo默认指定的.
 *
 * @author qiunet
 * 2022/10/18 18:04
 */
public interface IProtocolHeader {
	/**
	 * 服务端 接到客户端请求消息头长度
	 * @return
	 */
	int getServerInHeadLength();

	/**
	 * 根据是否是connectReq 返回对应的值
	 * @param connectReq 是否是connect request
	 * @return head length
	 */
	default int getServerInHeadLength(boolean connectReq) {
		if (connectReq) {
			return getConnectInHeadLength();
		}
		return getServerInHeadLength();
	}
	/**
	 * 根据是否是connectReq 返回对应的header
	 * @param connectReq 是否是connect request
	 * @return header
	 */
	default ProtocolHeader getServerInHeader(boolean connectReq, ByteBuf in, Channel channel) {
		if (connectReq) {
			return serverConnectIn(in, channel);
		}
		return serverNormalIn(in, channel);
	}

	/**
	 * 根据 serverOrClient 返回对应的 ProtocolHeader
	 * @param message  消息体
	 * @param serverOrClient true server,  false client
	 * @return ProtocolHeader 实例
	 */
	default ProtocolHeader outHeader(IChannelMessage<?> message, Channel channel, boolean serverOrClient, boolean connectReq) {
		if (serverOrClient) {
			return serverNormalOut(message, channel);
		}
		if (connectReq) {
			return clientConnectOut(message, channel);
		}
		return clientNormalOut(message, channel);
	}
	/**
	 * 获得客户端请求服务器的头长度
	 * @return
	 */
	default int getClientOutHeadLength() {
		return getServerInHeadLength();
	}
	/**
	 * 服务端连接建立头长度
	 * @return
	 */
	int getConnectInHeadLength();

	/**
	 * 服务端连入 magic 长度
	 * @return
	 */
	byte[] getConnectInMagic();

	/**
	 * 客户端读取到的服务器响应的消息头长度
	 * @return
	 */
	int getClientInHeadLength();

	/**
	 * 获得服务器响应头的长度
	 * @return
	 */
	default int getServerOutHeadLength() {
		return getClientInHeadLength();
	}

	/**
	 * 连接的消息头
	 * @param in byteBuf
	 * @param channel channel
	 * @return ConnectIn
	 */
	IConnectInHeader serverConnectIn(ByteBuf in, Channel channel);
	/**
	 * 客户端发出去的连接消息头
	 * @param message 消息
	 * @return NormalClientOut
	 */
	IConnectOutHeader clientConnectOut(IChannelMessage<?> message, Channel channel);
	/**
	 * 进入服务端的消息头
	 * @param in byteBuf
	 * @param channel channel
	 * @return NormalServerIn
	 */
	IServerInHeader serverNormalIn(ByteBuf in, Channel channel);

	/**
	 * 进入客户端的消息头
	 * @param in byteBuf
	 * @param channel channel
	 * @return IProtocolHeader0.NormalClientIn
	 */
	IClientInHeader clientNormalIn(ByteBuf in, Channel channel);

	/**
	 * 客户端发出去的消息头
	 * @param message 消息
	 * @return NormalClientOut
	 */
	IClientOutHeader clientNormalOut(IChannelMessage<?> message, Channel channel);

	/**
	 * 服务端发出去的头
	 * @param message 消息
	 * @return NormalServerOut
	 */
	IServerOutHeader serverNormalOut(IChannelMessage<?> message, Channel channel);

	/**
	 * 服务端接收的连接请求头.
	 */
	interface ProtocolHeader extends IRecycle {
		/**
		 * 得到协议ID
		 * @return
		 */
		int getProtocolId();
		/**
		 * 得到body的长度
		 * @return
		 */
		int getLength();
		/***
		 * 将header对象的内容输出到ByteBuf
		 */
		ByteBuf headerByteBuf();
		/***
		 * 对剩余bytes的校验数据进行校验
		 * @param buffer 原始的byte数组
		 * @return 如果校验成功, 返回true
		 */
		boolean validEncryption(ByteBuffer buffer);

		/**
		 * 是否是有效的消息.
		 * 魔数是否对, 重放攻击序号是否正确?
		 * @return false 中断channel
		 */
		boolean isValidMessage();
	}

	interface IConnectInHeader extends ProtocolHeader {}

	/**
	 * 客户端往服务端的connect header
	 */
	interface IConnectOutHeader extends IConnectInHeader {}
	/**
	 * 服务端接收的普通的请求头
	 */
	interface IServerInHeader extends IConnectInHeader {}

	/**
	 * 客户端接收服务端的响应头
	 */
	interface IClientInHeader extends IConnectInHeader {}

	/**
	 * 服务端响应头
	 */
	interface IServerOutHeader extends IConnectInHeader {}

	/**
	 * 客户端的请求服务器的消息头
	 */
	interface IClientOutHeader extends IConnectInHeader {}
}
