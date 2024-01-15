package org.qiunet.utils.http;

import io.netty.channel.pool.ChannelPool;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.util.concurrent.Promise;
import org.qiunet.utils.thread.ThreadPoolManager;

import java.net.URL;

/***
 * 请求的一些数据
 * @author qiunet
 * 2024/1/15 10:29
 ***/
class HttpRequestData {
	private static final HttpChannelManager pool = HttpChannelManager.instance;

	private Promise<HttpResponse> rspPromise;

	private IHttpCallBack callback;

	private FullHttpRequest request;

	private HttpAddress address;

	private URL url;

	public static HttpRequestData valueOf(HttpRequest<?> req, FullHttpRequest request, IHttpCallBack callback1){
		HttpRequestData data = new HttpRequestData();
		request.headers().set(req.headers);
		request.headers().add(HttpHeaderNames.HOST, req.url.getHost());
		URL url = req.url;
		int port = url.getPort();
		if (port == -1) {
			// 如果URL中没有明确指定端口，则使用默认的端口
			port = url.getDefaultPort();
		}
		boolean ssl = "https".equalsIgnoreCase(url.getProtocol());
		data.rspPromise = HttpChannelManager.group.next().newPromise();
		data.address = new HttpAddress(url.getHost(), ssl, port);
		data.callback = callback1;
		data.request = request;
		data.url = url;
		return data;
	}

	/**
	 * 是否是ssl
	 * @return true 是
	 */
	public boolean isSsl() {
		return address.ssl();
	}

	public URL getUrl() {
		return url;
	}

	public String getProtocol() {
		return url.getProtocol();
	}

	public HttpAddress getAddress() {
		return address;
	}

	public void fail(Throwable throwable) {
		if (this.callback != null) {
			ThreadPoolManager.NORMAL.execute(() -> {
				this.callback.fail(request, throwable);
			});
		}
		rspPromise.tryFailure(throwable);
	}

	public void success(FullHttpResponse response) {
		HttpResponse httpResponse = new HttpResponse(request, response, url);
		if (this.callback != null) {
			ThreadPoolManager.NORMAL.execute(() -> {
				this.callback.success(httpResponse);
			});
		}
		rspPromise.trySuccess(httpResponse);
	}

	public Promise<HttpResponse> getRspPromise() {
		return rspPromise;
	}

	public ChannelPool getPool() {
		return pool.get(address);
	}

	public FullHttpRequest getRequest() {
		return request;
	}
}
