package org.qiunet.flash.handler.context.request.udp;

import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.BaseRequestContext;
import org.qiunet.flash.handler.context.response.IUdpResponse;
import org.qiunet.flash.handler.context.response.push.IMessage;
import org.qiunet.flash.handler.context.response.push.ResponseMsgUtil;
import org.qiunet.flash.handler.context.session.SessionManager;
import org.qiunet.flash.handler.netty.server.param.UdpBootstrapParams;
import org.qiunet.flash.handler.netty.server.udp.handler.UdpChannel;

import java.net.InetSocketAddress;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/30 14:53
 **/
public abstract class AbstractUdpRequestContext<RequestData, ResponseData> extends BaseRequestContext<RequestData> implements IUdpRequestContext<RequestData>, IUdpResponse {
	protected UdpBootstrapParams params;
	protected UdpChannel channel;

	protected AbstractUdpRequestContext(MessageContent content, UdpChannel channel, UdpBootstrapParams params) {
		super(content, null);
		this.channel = channel;
		this.params = params;
	}
	@Override
	public int getQueueIndex() {
		return SessionManager.getInstance().getSession(channel).getQueueIndex();
	}

	@Override
	public void udpResponse(int protocolId, Object data) {
		ResponseMsgUtil.responseUdpMessage(channel, getResponseMessage(protocolId, (ResponseData) data));
	}
	/***
	 * 得到responseData的数组数据
	 * @param responseData
	 * @return
	 */
	protected abstract IMessage getResponseMessage(int protocolId, ResponseData responseData);

	@Override
	public String getRemoteAddress() {
		String ip = "";
		if (channel().remoteAddress() != null && channel().remoteAddress() instanceof InetSocketAddress) {
			ip = ((InetSocketAddress) channel().remoteAddress()).getAddress().getHostAddress();
		}
		return ip;
	}

	@Override
	public UdpChannel channel() {
		return channel;
	}
}
