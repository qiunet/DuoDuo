package org.qiunet.utils.http;

import okhttp3.*;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/***
 *
 *
 * @author qiunet
 * 2020-04-20 17:39
 ***/
public abstract class HttpRequest<B extends HttpRequest> {
	private static final IResultSupplier<String> DEFAULT_SUPPLIER = response -> response.body().string();
	protected static final Logger logger = LoggerType.DUODUO_HTTP.getLogger();
	protected static final OkHttpClient client = new OkHttpClient.Builder()
		.connectTimeout(3000, TimeUnit.MILLISECONDS)
		.readTimeout(3000, TimeUnit.MILLISECONDS)
		.callTimeout(3000, TimeUnit.MILLISECONDS)
		.build();

	protected String url;

	protected Charset charset = StandardCharsets.UTF_8;

	protected HttpRequest(String url) {
		this.url = url;
	}

	protected Headers.Builder headerBuilder = new Headers.Builder()
		.add("Accept-Charset", "UTF-8");

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
		this.headerBuilder.add(name, val);
		return (B) this;
	}

	public B header(Map<String, String> headerMap) {
		headerMap.forEach((key, val) -> this.headerBuilder.add(key ,val));
		return (B) this;
	}

	/**
	 * 异步执行请求
	 * @param callBack
	 * @throws Exception
	 */
	public void asyncExecutor(Callback callBack) {
		Request request = buildRequest();
		client.newCall(request).enqueue(callBack);
	}
	/**
	 * 执行请求
	 * @return
	 * @throws Exception
	 */
	public <T> T executor(IResultSupplier<T> supplier) throws Exception {
		Request request = buildRequest();
		Response response = client.newCall(request).execute();
		if (response.isSuccessful()) {
			return supplier.result(response);
		}else {
			logger.error("Request: {} Fail, StatusCode {}", request, response.code());
			return null;
		}
	}
	/**
	 * 执行请求
	 * @return
	 * @throws Exception
	 */
	public String executor() throws Exception {
		return executor(DEFAULT_SUPPLIER);
	}

	protected abstract Request buildRequest();
}