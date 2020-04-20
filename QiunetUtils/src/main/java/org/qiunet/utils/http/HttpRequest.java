package org.qiunet.utils.http;

import com.google.common.collect.Maps;
import okhttp3.*;

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

	protected static final OkHttpClient client = new OkHttpClient.Builder()
		.connectTimeout(3000, TimeUnit.MILLISECONDS)
		.readTimeout(3000, TimeUnit.MILLISECONDS)
		.callTimeout(3000, TimeUnit.MILLISECONDS)
		.build();

	protected String url;

	protected IAsyncHttpCallBack callBack;

	protected Charset charset = StandardCharsets.UTF_8;

	protected Map<String, String> headerMap = Maps.newHashMap();

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
		this.headerMap.put("Accept-Charset", charset.toString());
		this.charset = charset;
		return (B) this;
	}

	public B headerMap(Map<String, String> headerMap) {
		this.headerMap = headerMap;
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
		return client.newCall(request).execute().body().string();
	}

	protected abstract Request buildRequest();
}
