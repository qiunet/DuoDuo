package org.qiunet.flash.handler.context.request.http;

import org.qiunet.flash.handler.context.request.IRequest;

import java.util.List;

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
	/**
	 * 得到远程的Ip地址
	 * @return
	 */
	public String getRemoteAddress();
}
