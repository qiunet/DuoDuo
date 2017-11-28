package org.qiunet.utils.http;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.qiunet.utils.enums.CharsetEnum;
import org.qiunet.utils.enums.HttpMethodEnums;
import org.qiunet.utils.exceptions.HttpMethodNotSupportException;
import org.qiunet.utils.exceptions.UrlEmptyException;
import org.qiunet.utils.string.StringUtil;

/**
 * 需要子类指定 httpClient pool的配置信息等
 *
 * 该类最好做成单例模式
 */
public abstract class AbstractHttpUtil {
	private  HttpsClientPool httpsClientPool;
	/* httpclient */
	protected RequestConfig REQUESTCONFIG;// 设置请求和传输超时时间

	/****
	 *
	 * @param httpsClientPool 默认的https pool
	 * @param requestConfig 配置
	 */
	protected AbstractHttpUtil(HttpsClientPool httpsClientPool, RequestConfig requestConfig){
		this.httpsClientPool = httpsClientPool;
		this.REQUESTCONFIG = requestConfig;
	}

	public String httpRequest(String url, Map<String, Object> params){
		return httpRequest(url, params, null);
	}

	public String httpRequest(String url, Map<String, Object> params, Map<String, Object> cookies){
		return httpRequest(url, HttpMethodEnums.POST, params, cookies);
	}

	public String httpRequest(String url, HttpMethodEnums method, Map<String, Object> params, Map<String, Object> cookies){
		DefaultHttpRequestHandler handler = new DefaultHttpRequestHandler();
		httpRequest(url, method, params, cookies, CharsetEnum.UTF_8, handler);
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
	public <T> void  httpRequest(String url,HttpMethodEnums method,Map<String, Object> params, Map<String, Object> cookies, CharsetEnum charset, HttpRequestHandler<T> handler){

		if(StringUtil.isEmpty(url)) throw new UrlEmptyException();

		HttpRequestBase request = null;
		HttpResponse response;
		HttpClient client = null;
		try {
			switch (method) {
			case GET:
				request = new HttpGet(handler.getMethodHandler(url, params));
				break;
			case POST:
				request = new HttpPost(url);
				if(params != null && !params.isEmpty())
					((HttpPost)request).setEntity(handler.postMethodHandler(params));
				break;
			default:
				throw new HttpMethodNotSupportException(method == null ? "null" : method.toString());
			}
			if(cookies != null && !cookies.isEmpty()){
				request.setHeader("Cookie", handler.cookieHandler(cookies, charset));
			}

			request.setConfig(REQUESTCONFIG);
			client = httpsClientPool.getFromPool();
			response = client.execute(request);
			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
				handler.errorHandler(params, cookies, response.getStatusLine().getStatusCode(), url);
				return;
			}
			handler.handlerResult(response, charset);
		} catch (Exception e) {
			e.printStackTrace();
			if (request != null) {
				request.abort();
			}
		}finally{
			if(client != null){
				httpsClientPool.recycle(client);
			}
		}
	}
}
