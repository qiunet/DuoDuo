package org.qiunet.utils.http;

import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/***
 * Http 请求工具类.
 *
 * @author qiunet
 * 2020-04-20 17:39
 ***/
public abstract class HttpRequest<B extends HttpRequest<B>> {
	public static final HttpResponse.BodyHandler<String> STRING_SUPPLIER = HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8);

	public static final HttpResponse.BodyHandler<InputStream> INPUT_STREAM_SUPPLIER = HttpResponse.BodyHandlers.ofInputStream();

	public static final HttpResponse.BodyHandler<byte[]> BYTE_ARRAY_SUPPLIER = HttpResponse.BodyHandlers.ofByteArray();

	protected static final Logger logger = LoggerType.DUODUO_HTTP.getLogger();

	//Once built, an HttpClient can be used to send multiple requests.
	protected static final HttpClient client = HttpClient.newBuilder()
			.connectTimeout(Duration.ofMillis(6000))
			.build();

	protected Charset charset = StandardCharsets.UTF_8;

	protected String url;

	protected HttpRequest(String url) {
		this.url = url;
	}

	protected Map<String, String> headerBuilder = new HashMap<>() {{put("Accept-Charset", "UTF-8");}};

	public static PostHttpRequest post(String url) {
		return new PostHttpRequest(url);
	}

	public static GetHttpRequest get(String url) {
		return new GetHttpRequest(url);
	}

	public B charset(Charset charset) {
		this.header("Accept-Charset", charset.toString());
		this.charset = charset;
		return (B) this;
	}

	public B header(String name, String val) {
		this.headerBuilder.put(name, val);
		return (B) this;
	}

	public B header(Map<String, String> headerMap) {
		this.headerBuilder.putAll(headerMap);
		return (B) this;
	}

	/**
	 * 异步执行请求
	 * @param callBack
	 */
	public  void asyncExecutor(IHttpCallBack<String> callBack) {
		this.asyncExecutor(STRING_SUPPLIER, callBack);
	}

	public <T> void asyncExecutor(HttpResponse.BodyHandler<T> bodyHandler, IHttpCallBack<T> callBack) {
		java.net.http.HttpRequest request = buildRequest();
		CompletableFuture<HttpResponse<T>> future = client.sendAsync(request, bodyHandler);
		future.thenAccept(callBack::response);
	}
	/**
	 * 执行请求
	 * @return
	 */
	public <T> T executor(HttpResponse.BodyHandler<T> supplier) {
		java.net.http.HttpRequest request = buildRequest();
		try {
			HttpResponse<T> response = client.send(request, supplier);
			if (response.statusCode() != 200) {
				throw new CustomException("Request: {} Fail, StatusCode {}", request, response.statusCode());
			}
			return response.body();
		} catch (Exception e) {
			throw new CustomException(e, "http client send request error!");
		}
	}
	/**
	 * 执行请求
	 * @return
	 */
	public String executor() {
		return executor(STRING_SUPPLIER);
	}

	protected abstract java.net.http.HttpRequest buildRequest();
}
