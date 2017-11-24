package org.qiunet.flash.handler.context.request.http;

import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.Cookie;

import java.util.List;
import java.util.Set;

/**
 * Created by qiunet.
 * 17/11/21
 */
public class FacadeHttpRequest<RequestData> implements IHttpRequest<RequestData> {
	private IHttpRequestContext<RequestData> context;
	public FacadeHttpRequest(IHttpRequestContext context) {
		this.context = context;
	}

	@Override
	public RequestData getRequestData() {
		return context.getRequestData();
	}

	@Override
	public int getSequence() {
		return context.getSequence();
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
	public String getParameter(String key) {
		return context.getParameter(key);
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
