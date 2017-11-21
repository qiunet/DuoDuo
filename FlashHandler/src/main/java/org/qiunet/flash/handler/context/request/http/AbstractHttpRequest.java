package org.qiunet.flash.handler.context.request.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.qiunet.flash.handler.context.request.BaseRequest;
import org.qiunet.flash.handler.context.request.IRequest;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.flash.handler.handler.response.IResponse;
import org.qiunet.utils.string.StringUtil;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

/**
 * @author qiunet
 *         Created on 17/3/17 14:28.
 */
public abstract class AbstractHttpRequest<RequestData, ResponseData> extends BaseRequest<RequestData> implements IResponse<ResponseData>, IHttpRequest<RequestData> {
	private HttpRequest request;
	private QueryStringDecoder queryStringDecoder;

	public AbstractHttpRequest(MessageContent content, ChannelHandlerContext channelContext, HttpRequest request)  {
		super(content, channelContext);
		this.request = request;
	}

	private Map<String ,List<String>> parameters(){
		if (queryStringDecoder == null) queryStringDecoder = new QueryStringDecoder(request.uri());
		return queryStringDecoder.parameters();
	}
	@Override
	public List<String> getParametersByKey(String key) {
		return this.parameters().get(key);
	}

	@Override
	public String getParameter(String key) {
		List<String> ret = this.parameters().get(key);
		if (ret != null && !ret.isEmpty()) {
			return ret.get(0);
		}
		return null;
	}

	@Override
	public String getRemoteAddress() {
		String ip = getHttpHeader("HTTP_X_FORWARDED_FOR");
		if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = getHttpHeader("x-forwarded-for");
		}
		if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = getHttpHeader("x-forwarded-for-pound");
		}
		if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = getHttpHeader("Proxy-Client-IP");
		}
		if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = getHttpHeader("WL-Proxy-Client-IP");
		}
		if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = getHttpHeader("HTTP_CLIENT_IP");
		}
		if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			if (ctx.channel().remoteAddress() != null && ctx.channel().remoteAddress() instanceof InetSocketAddress) {
				ip = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
			}
		}
		return ip;
	}

	@Override
	public String getHttpHeader(String name) {
		return request.headers().get(name);
	}

	@Override
	public List<String> getHttpHeadersByName(String name) {
		return request.headers().getAll(name);
	}

	@Override
	public void response(int protocolId, ResponseData responseData) {

	}

	/***
	 * 得到responseData的数组数据
	 * @param responseData
	 * @return
	 */
	protected abstract byte[] getResponseDataBytes(ResponseData responseData);
}
