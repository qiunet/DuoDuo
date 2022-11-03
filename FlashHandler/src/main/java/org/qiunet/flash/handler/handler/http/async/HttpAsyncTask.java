package org.qiunet.flash.handler.handler.http.async;

import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.Cookie;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.context.request.http.IHttpRequestContext;
import org.qiunet.flash.handler.context.response.IHttpResponse;
import org.qiunet.utils.exceptions.CustomException;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/***
 * 异步http的处理
 *
 * @author qiunet
 * 2021/12/15 15:50
 */
public class HttpAsyncTask<Req, Rsp> implements IHttpRequest<Req>, IHttpResponse<Rsp> {
	/**
	 * request context
	 */
	private final IHttpRequest<Req> context;
	/**
	 * complete 调用
	 */
	private final Consumer<Rsp> completedConsumer;
	/**
	 * 允许调用一次.
	 */
	private final AtomicBoolean completed = new AtomicBoolean(false);

	/**
	 * 开始时间
	 */
	private final long startTime;
	public HttpAsyncTask(IHttpRequest<Req> context, Consumer<Rsp> completedConsumer) {
		this.completedConsumer =  completedConsumer;
		this.startTime = System.currentTimeMillis();
		this.context = context;
	}

	public Req getRequest() {
		return context.getRequestData();
	}

	/**
	 * 回调结果获取
	 * @param response
	 */
	@Override
	public void response(Rsp response) {
		if (response == null) {
			throw new NullPointerException("Response data can not be null!");
		}

		if (! completed.compareAndSet(false, true)) {
			throw new CustomException("Task already completed!");
		}

		long useTime = System.currentTimeMillis() - startTime;
		((IHttpRequestContext) this.context).getHandler().recordUseTime(useTime);
		this.completedConsumer.accept(response);
	}

	@Override
	public Req getRequestData() {
		return context.getRequestData();
	}

	@Override
	public String getRemoteAddress() {
		return context.getRemoteAddress();
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
