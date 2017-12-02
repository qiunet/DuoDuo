package org.qiunet.flash.handler.context.request.websocket;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.ReferenceCountUtil;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.flash.handler.context.header.ProtocolHeader;
import org.qiunet.flash.handler.context.request.BaseRequestContext;
import org.qiunet.flash.handler.context.response.IResponse;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.utils.encryptAndDecrypt.CrcUtil;

import java.net.InetSocketAddress;

/**
 * Created by qiunet.
 * 17/12/2
 */
public abstract class AbstractWebSocketRequestContext<RequestData, ResponseData>  extends BaseRequestContext<RequestData> implements IWebSocketRequestContext<RequestData>, IResponse<ResponseData> {
	protected HttpBootstrapParams params;
	protected AbstractWebSocketRequestContext(MessageContent content, ChannelHandlerContext ctx,HttpBootstrapParams params) {
		super(content, ctx);
		this.params = params;
	}

	@Override
	public int getQueueHandlerIndex() {
		return ctx.channel().id().asLongText().hashCode();
	}

	@Override
	public void response(int protocolId, ResponseData data) {
		this.ctx.writeAndFlush(encode(protocolId, data));
	}

	/***
	 * 序列化
	 * @param protocolId
	 * @param data
	 */
	private BinaryWebSocketFrame encode(int protocolId, ResponseData data){
		byte [] bytes = getResponseDataBytes(data);
		ProtocolHeader header = new ProtocolHeader(bytes.length, protocolId, (int) CrcUtil.getCrc32Value(bytes));
		ByteBuf byteBuf = Unpooled.buffer();
		header.writeToByteBuf(byteBuf);
		byteBuf.writeBytes(bytes);
		BinaryWebSocketFrame frame = new BinaryWebSocketFrame(byteBuf);
		ReferenceCountUtil.release(byteBuf);
		return frame;
	}
	/***
	 * 得到responseData的数组数据
	 * @param responseData
	 * @return
	 */
	protected abstract byte[] getResponseDataBytes(ResponseData responseData);
	@Override
	public String getRemoteAddress() {
		String ip = "";
		if (ctx.channel().remoteAddress() != null && ctx.channel().remoteAddress() instanceof InetSocketAddress) {
			ip = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
		}
		return ip;
	}
}
