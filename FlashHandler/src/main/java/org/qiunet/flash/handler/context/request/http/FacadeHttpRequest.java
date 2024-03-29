package org.qiunet.flash.handler.context.request.http;

import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.Cookie;

import java.util.List;
import java.util.Set;

/**
 * Created by qiunet.
 * 17/11/21
 */
class FacadeHttpRequest<RequestData, ResponseData> implements IHttpRequest<RequestData> {
	private static final ThreadLocal<FacadeHttpRequest> pool = ThreadLocal.withInitial(FacadeHttpRequest::new);

	private IHttpRequestContext<RequestData, ResponseData> context;

	private FacadeHttpRequest() {}

	public static FacadeHttpRequest valueOf(IHttpRequestContext context) {
		FacadeHttpRequest request = pool.get();
		request.context = context;
		return request;
	}

	public void recycle() {
		this.context = null;
	}

	@Override
	public RequestData getRequestData() {
		return context.getRequestData();
	}

	@Override
	public Object getAttribute(String key) {
		return context.getAttribute(key);
	}

	@Override
	public void setAttribute(String key, Object val) {
		context.setAttribute(key, val);
	}

	@Override
	public boolean otherRequest() {
		return context.otherRequest();
	}

	@Override
	public String getUriPath() {
		return context.getUriPath();
	}

	@Override
	public List<String> getParametersByKey(String key) {
		return context.getParametersByKey(key);
	}

	@Override
	public String getHttpHeader(String name) {
		return context.getHttpHeader(name);
	}

	@Override
	public List<String> getHttpHeadersByName(String name) {
		return context.getHttpHeadersByName(name);
	}

	@Override
	public String getRemoteAddress() {
		return context.getRemoteAddress();
	}

	@Override
	public HttpVersion getProtocolVersion() {
		return context.getProtocolVersion();
	}

	@Override
	public Set<Cookie> getCookieSet() {
		return context.getCookieSet();
	}

	@Override
	public Cookie getCookieByName(String name) {
		return context.getCookieByName(name);
	}
}
