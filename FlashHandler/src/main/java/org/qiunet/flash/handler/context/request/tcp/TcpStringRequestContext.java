package org.qiunet.flash.handler.context.request.tcp;

import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.InvocationTargetException;

/**
 * tcp 请求解析成字符串的.
 * Created by qiunet.
 * 17/11/21
 */
public class TcpStringRequestContext extends AbstractTcpRequestContext<String, String> {
	protected String requestData;
	public TcpStringRequestContext(MessageContent content, ChannelHandlerContext channelContext) {
		super(content, channelContext);
	}

	@Override
	public String getRequestData() {
		if (StringUtil.isEmpty(requestData)) {
			try {
				this.requestData = getHandler().parseRequestData(bytes);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return requestData;
	}

	@Override
	public void response(int protocolId, String o) {

	}

	@Override
	public boolean handler() {
		return false;
	}

	@Override
	public String toStr() {
		return null;
	}
}
