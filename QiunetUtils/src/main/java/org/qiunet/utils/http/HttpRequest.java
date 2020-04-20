package org.qiunet.utils.http;

import okhttp3.*;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.io.IOException;
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
	protected static final Logger logger = LoggerType.DUODUO_HTTP.getLogger();
	protected static final OkHttpClient client = new OkHttpClient.Builder()
		.connectTimeout(3000, TimeUnit.MILLISECONDS)
		.readTimeout(3000, TimeUnit.MILLISECONDS)
		.callTimeout(3000, TimeUnit.MILLISECONDS)
		.build();

	protected String url;

	protected IAsyncHttpCallBack callBack;

	protected Charset charset = StandardCharsets.UTF_8;

	protected Headers.Builder headerBuilder = new Headers.Builder()
		.add("Accept-Charset", "UTF-8");

	public static PostHttpRequest post() {
		return new PostHttpRequest();
	}

	public static GetHttpRequest get() {
		return new GetHttpRequest();
	}

	public B url(String url) {
		this.url = url;
		return (B) this;
	}

	public B async(IAsyncHttpCallBack callBack) {
		this.callBack = callBack;
		return (B) this;
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
	 * 如果是异步请求. 返回null
	 * @return
	 */
	public String executor() throws Exception {
		Request request = buildRequest();
		if (callBack != null) {
			client.newCall(request).enqueue(new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {
					callBack.onFail(e);
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					callBack.onResponse(response.body().string());
				}
			});
			return null;
		}
		Response response = client.newCall(request).execute();
		if (response.isSuccessful()) {
			return response.body().string();
		}else {
			logger.error("Request: {} Fail, StatusCode {}", request, response.code());
			return null;
		}
	}

	protected abstract Request buildRequest();
}
