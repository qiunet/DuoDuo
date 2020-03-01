package org.qiunet.utils.http;

import com.google.common.base.Preconditions;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.qiunet.utils.string.StringUtil;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 */
public class HttpUtil extends BaseHttpUtil {
	private static HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
		.setDefaultRequestConfig(REQUESTCONFIG);

	public static String httpRequest(String url, Map<String, Object> params){
		return httpRequest(url, params, null);
	}

	public static String httpRequest(String url, Map<String, Object> params, Map<String, Object> cookies){
		return httpRequest(url, HttpMethodEnum.POST, params, cookies);
	}

	public static String httpRequest(String url, HttpMethodEnum method, Map<String, Object> params, Map<String, Object> cookies){
		return httpRequest(url, method, params, cookies, StandardCharsets.UTF_8);
	}
	/**
	 * 实现一个http 请求的方法
	 * @param url url
	 * @param method get or post
	 * @param params 参数
	 * @param cookies cookies
	 * @param charset charset utf8 default
	 */
	public static String  httpRequest(String url, HttpMethodEnum method, Map<String, Object> params, Map<String, Object> cookies, Charset charset){
		Preconditions.checkArgument(! StringUtil.isEmpty(url), "url is empty");

		HttpUriRequest request = null;
		HttpResponse httpResponse = null;
		HttpClient client = httpClientBuilder.build();
		try {
			request = method.createRequest(url, params, cookies);
			httpResponse = client.execute(request);
			if(httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
				errorHandler(params, cookies, httpResponse.getStatusLine().getStatusCode(), url);
			}else {
				return EntityUtils.toString(httpResponse.getEntity(), charset.toString());
			}
		} catch (Exception e) {
			if (request != null) request.abort();
			e.printStackTrace();
		}finally {
			HttpClientUtils.closeQuietly(httpResponse);
			HttpClientUtils.closeQuietly(client);
		}
		return "";
	}
}
