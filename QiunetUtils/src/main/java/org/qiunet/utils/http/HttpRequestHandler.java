package org.qiunet.utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.qiunet.utils.enums.CharsetEnum;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 对数据的处理
 * @author qiunet
 *
 */
public interface HttpRequestHandler<T> {
	/**
	 * get 方法的处理
	 * @param params
	 * @return
	 */
	public String getMethodHandler(String url, Map<String, Object> params);
	/**
	 * cookie 的处理
	 * @param cookies
	 * @return
	 */
	public String cookieHandler(Map<String, Object> cookies, CharsetEnum charset)throws Exception;
	/**
	 *  post 方法的处理
	 * @param params
	 * @return
	 */
	public HttpEntity postMethodHandler(Map<String, Object> params)throws UnsupportedEncodingException;
	/***
	 * 错误的处理 默认打印参数
	 * @param params
	 * @param cookies
	 * @param httpStatus
	 * @param url
	 */
	public void errorHandler(Map<String, Object> params, Map<String, Object> cookies, int httpStatus, String url);
	/**
	 * 处理结果
	 * @param response
	 * @return
	 */
	public void handlerResult(HttpResponse response, CharsetEnum charset);
	/**
	 * 返回结果
	 * @return
	 */
	public T returnResult();
}
