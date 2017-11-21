package org.qiunet.flash.handler.context.request.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import org.qiunet.flash.handler.context.request.BaseRequest;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.flash.handler.context.response.IResponse;
import org.qiunet.utils.string.StringUtil;

import java.net.InetSocketAddress;
import java.util.*;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

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
	public HttpVersion getProtocolVersion() {
		return request.protocolVersion();
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
		boolean keepAlive = HttpUtil.isKeepAlive(request);
		// 不能使用pooled的对象. 因为不清楚什么时候release
		ByteBuf content = Unpooled.wrappedBuffer(getResponseDataBytes(responseData));
		FullHttpResponse response = new DefaultFullHttpResponse(
				HTTP_1_1, OK,  content);
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType());
		if (keepAlive) {
			response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		}
		// 下面的 `writeAndFlush(Unpooled.EMPTY_BUFFER)` 会flush
		ctx.write(response);

		if (! keepAlive) {
			ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		}
	}

	/***
	 * 得到contentType
	 * @return
	 */
	protected abstract String contentType();
	/***
	 * 得到responseData的数组数据
	 * @param responseData
	 * @return
	 */
	protected abstract byte[] getResponseDataBytes(ResponseData responseData);

	private Map<String, Cookie> cookieMap;
	private Map<String, Cookie> cookies(){
		if (cookieMap != null) return cookieMap;
		String cookieString = request.headers().get(HttpHeaderNames.COOKIE);
		if (StringUtil.isEmpty(cookieString)) {
			cookieMap = Collections.emptyMap();
		}else {
			Set<Cookie> cookies = ServerCookieDecoder.STRICT.decode(cookieString);
			cookieMap = new HashMap<>();
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.name(), cookie);
			}
		}
		return cookieMap;
	}
	@Override
	public Set<Cookie> getCookieSet() {
		return new HashSet<>(cookies().values());
	}

	@Override
	public Cookie getCookieByName(String name) {
		return cookies().get(name);
	}
}
