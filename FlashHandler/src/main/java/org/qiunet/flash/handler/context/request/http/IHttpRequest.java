package org.qiunet.flash.handler.context.request.http;

import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.Cookie;
import org.qiunet.flash.handler.context.request.IRequest;

import java.util.List;
import java.util.Set;

/**
 * Created by qiunet.
 * 17/11/21
 */
public interface IHttpRequest<RequestData> extends IRequest<RequestData> {
	/***
	 * 得到请求的get参数
	 * @param key
	 * @return
	 */
	public String getParameter(String key);

	/**
	 * 得到请求的参数. 返回是一个数组
	 * @param key
	 * @return
	 */
	public List<String> getParametersByKey(String key);

	/**
	 * 得到指定header 的值
	 * @param name
	 * @return
	 */
	public String getHttpHeader(String name);
	/**
	 * 得到指定header 的值
	 * @param name
	 * @return
	 */
	public List<String> getHttpHeadersByName(String name);
	/***
	 * 得到协议使用的http版本
	 * @return
	 */
	public HttpVersion getProtocolVersion();

	/**
	 * 得到所有的cookie
	 * @return 没有返回一个empty set
	 */
	public Set<Cookie> getCookieSet();

	/**
	 * 得到一个cookie
	 * @param name cookie的名称
	 * @return 没有 返回null
	 */
	public Cookie getCookieByName(String name);
}
