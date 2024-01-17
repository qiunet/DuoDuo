package org.qiunet.utils.http;

import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.Promise;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/***
 * Http client
 *
 * @author qiunet
 * 2020-04-20 17:39
 ***/
public abstract class HttpRequest<B extends HttpRequest<?>> {
	protected static final Logger logger = LoggerType.DUODUO_HTTP.getLogger();
	private static final HttpClient client = HttpClient.instance;
	protected Charset charset = StandardCharsets.UTF_8;
	protected HttpHeaders headers = new DefaultHttpHeaders(true)
		.add(HttpHeaderNames.ACCEPT_CHARSET, charset)
		.add(HttpHeaderNames.ACCEPT, "*/*");
	private static final int DEFAULT_MAX_RECEIVED_CONTENT_LENGTH = 1024 * 128;
	private static final int DEFAULT_CONNECT_TIMEOUT = 10;
	private static final int DEFAULT_READ_TIMEOUT = 10;

	protected static int maxReceivedContentLength = DEFAULT_MAX_RECEIVED_CONTENT_LENGTH;
	protected static int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
	protected static int readTimeout = DEFAULT_READ_TIMEOUT;
	protected String urlstring;
	protected URL url;

	protected HttpRequest(String urlstring) {
        try {
            this.url = new URL(urlstring);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        this.urlstring = urlstring;
	}

	public static PostHttpRequest post(String urlstring) {
		return new PostHttpRequest(urlstring);
	}

	public static GetHttpRequest get(String urlstring) {
		return new GetHttpRequest(urlstring);
	}

	public B charset(Charset charset) {
		this.header(HttpHeaderNames.ACCEPT_CHARSET, charset);
		this.charset = charset;
		return (B) this;
	}

	public B header(CharSequence name, Object val) {
		this.headers.add(name, val);
		return (B) this;
	}
	/**
	 * 每次请求关闭 connect
	 * @return
	 */
	public B closeConnectAlive() {
		this.header(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
		return (B) this;
	}

	public B header(Map<String, Object> headerMap) {
		headerMap.forEach(this::header);
		return (B) this;
	}

	/**
	 * 异步执行请求
	 * @param callBack
	 */
	public Promise<HttpResponse> asyncExecutor(IHttpCallBack callBack) {
		FullHttpRequest request = this.buildRequest();
		HttpRequestData requestData = HttpRequestData.valueOf(this, request,  callBack);
		return client.request(requestData);
	}
	/**
	 * 执行请求
	 * @return
	 */
	public <T> T executor(IResultSupplier<T> supplier) {
		try {
			Promise<HttpResponse> promise = asyncExecutor(null);
			HttpResponse response = promise.get(readTimeout, TimeUnit.SECONDS);
			if (! HttpResponseStatus.OK.equals(response.getStatus())) {
				throw new CustomException("Request: {} Fail, StatusCode {}", response.getRequestUrl(), response.getStatus());
			}
			return supplier.result(response);
		} catch (Exception e) {
			throw new CustomException(e, "http client send request error!");
		}
	}
	/**
	 * 执行请求
	 * @return
	 */
	public String executor() {
		return executor(IResultSupplier.STRING_SUPPLIER);
	}

	protected String path() {
		String path = url.getFile();
		if (StringUtil.isEmpty(path)) {
			return "/";
		}
		return path;
	}

	public static void setMaxReceivedContentLength(int max_received_content_length) {
		HttpRequest.maxReceivedContentLength = max_received_content_length;
	}

	public static void setConnectTimeout(int connectTimeout) {
		HttpRequest.connectTimeout = connectTimeout;
	}

	public static void setReadTimeout(int readTimeout) {
		HttpRequest.readTimeout = readTimeout;
	}

	protected abstract FullHttpRequest buildRequest();
}
