package org.qiunet.utils.http;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.google.common.base.Preconditions;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.qiunet.utils.string.StringUtil;

/**
 * 需要子类指定 httpClient pool的配置信息等
 *
 * 该类最好做成单例模式
 */
public class HttpUtil {
	private static HttpClientBuilder builder = HttpClientBuilder.create();
	private static RequestConfig REQUESTCONFIG = RequestConfig.custom()
		.setSocketTimeout(6000)
		.setConnectTimeout(6000)
		.build();


	public static String httpRequest(String url, Map<String, Object> params){
		return httpRequest(url, params, null);
	}

	public static String httpRequest(String url, Map<String, Object> params, Map<String, Object> cookies){
		return httpRequest(url, HttpMethodEnum.POST, params, cookies);
	}

	public static String httpRequest(String url, HttpMethodEnum method, Map<String, Object> params, Map<String, Object> cookies){
		DefaultHttpRequestHandler handler = new DefaultHttpRequestHandler();
		httpRequest(url, method, params, cookies, StandardCharsets.UTF_8, handler);
		return handler.returnResult();
	}
	/**
	 * 实现一个http 请求的方法
	 * @param url url
	 * @param method get or post
	 * @param params 参数
	 * @param cookies cookies
	 * @param charset charset utf8 default
	 * @param handler request handler
	 */
	public static <T> void  httpRequest(String url, HttpMethodEnum method, Map<String, Object> params, Map<String, Object> cookies, Charset charset, HttpRequestHandler<T> handler){
		Preconditions.checkArgument(! StringUtil.isEmpty(url), "url is empty");

		HttpUriRequest request = null;
		HttpResponse httpResponse = null;
		HttpClient client = builder.build();
		try {
			request = method.createRequest(url, params, handler);
			if(cookies != null && !cookies.isEmpty()){
				request.setHeader("Cookie", handler.cookieHandler(cookies, charset));
			}

			((HttpRequestBase) request).setConfig(REQUESTCONFIG);
			httpResponse = client.execute(request);
			if(httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
				handler.errorHandler(params, cookies, httpResponse.getStatusLine().getStatusCode(), url);
				return;
			}
			handler.handlerResult(httpResponse, charset);
		} catch (Exception e) {
			e.printStackTrace();
			if (request != null) {
				request.abort();
			}
		}finally {
			HttpClientUtils.closeQuietly(httpResponse);
			HttpClientUtils.closeQuietly(client);
		}
	}
}
