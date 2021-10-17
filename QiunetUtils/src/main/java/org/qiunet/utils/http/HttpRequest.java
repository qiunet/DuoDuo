package org.qiunet.utils.http;

import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/***
 *
 *
 * @author qiunet
 * 2020-04-20 17:39
 ***/
public abstract class HttpRequest<B extends HttpRequest<B>> {
	private static final HttpResponse.BodyHandler<String> DEFAULT_SUPPLIER = HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8);
	protected static final Logger logger = LoggerType.DUODUO_HTTP.getLogger();

	//Once built, an HttpClient can be used to send multiple requests.
	protected static final HttpClient client = HttpClient.newBuilder()
			.connectTimeout(Duration.ofMillis(3000))
			.build();

	protected String url;

	protected Charset charset = StandardCharsets.UTF_8;

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
		this.asyncExecutor(HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8), callBack);
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
	public String executor() throws IOException, InterruptedException {
		return client.send(buildRequest(), DEFAULT_SUPPLIER).body();
	}

	protected abstract java.net.http.HttpRequest buildRequest();
}
