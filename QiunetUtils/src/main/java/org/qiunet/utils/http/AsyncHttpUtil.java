package org.qiunet.utils.http;


import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncByteConsumer;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.hook.ShutdownHookThread;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Future;

public class AsyncHttpUtil extends BaseHttpUtil {
	private static final CloseableHttpAsyncClient httpAsyncClient;
	static {
		httpAsyncClient = HttpAsyncClients.custom()
			.setThreadFactory(new DefaultThreadFactory("AsyncHttpUtil"))
			.setDefaultRequestConfig(REQUESTCONFIG)
			.disableConnectionState()
			.setMaxConnPerRoute(50)
			.disableAuthCaching()
			.build();
		httpAsyncClient.start();

		ShutdownHookThread.getInstance().addShutdownHook(() -> {
			try {
				httpAsyncClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static Future<String> post(String uri, Callback callback) {
		return post(uri, null, callback);
	}

	public static Future<String> post(String uri, Map<String, Object> params, Callback callback) {
		return post(uri, params, null, callback);
	}

	public static Future<String> post(String uri, Map<String, Object> params, Map<String, Object> cookies, Callback callback) {
		callback.setData(uri, params, cookies);
		final HttpUriRequest post = HttpMethodEnum.POST.createRequest(uri, params, cookies);
		return httpAsyncClient.execute(HttpAsyncMethods.create(determineTarget(post), post), new StringResponseComsumer(), callback);
	}

	private static HttpHost determineTarget(final HttpUriRequest request) {
		Args.notNull(request, "HTTP request");
		HttpHost target = null;

		final URI requestURI = request.getURI();
		if (requestURI.isAbsolute()) {
			target = URIUtils.extractHost(requestURI);
		}
		return target;
	}

	private static class StringResponseComsumer extends AsyncByteConsumer<String> {
		private String result;
		@Override
		protected void onByteReceived(ByteBuffer buf, IOControl ioctrl) throws IOException {
			this.result = StandardCharsets.UTF_8.decode(buf).toString();
		}

		@Override
		protected void onResponseReceived(HttpResponse response) throws HttpException, IOException {

		}

		@Override
		protected String buildResult(HttpContext context) throws Exception {
			return this.result;
		}
	}
}
