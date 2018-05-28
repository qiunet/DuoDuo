package org.qiunet.flash.handler.handler.http;

import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.Cookie;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.context.request.http.json.JsonRequest;
import org.qiunet.flash.handler.context.response.json.JsonResponse;
import org.qiunet.utils.string.StringUtil;

import java.util.List;
import java.util.Set;

/**
 * json的方式 http
 * Created by qiunet.
 * 18/1/29
 */
public abstract class HttpJsonHandler extends HttpStringHandler {

	@Override
	public String handler(IHttpRequest<String> request) throws Exception {
		JsonResponse response = handler1(new HttpJsonFacaderRequest(request));
		return response.toString();
	}
	/***
	 * json handler
	 * @param request
	 * @return
	 */
	protected abstract JsonResponse handler1(IHttpRequest<JsonRequest> request);

	private class HttpJsonFacaderRequest implements IHttpRequest<JsonRequest>{
		private IHttpRequest<String> request;
		private JsonRequest requestData;
		HttpJsonFacaderRequest(IHttpRequest<String> request) {
			this.request = request;
			String data = request.getRequestData();
			if (!StringUtil.isEmpty(data)) {
				this.requestData = JsonRequest.parse(data);
			}else {
				this.requestData = new JsonRequest();
			}
		}
		@Override
		public boolean otherRequest() {
			return request.otherRequest();
		}

		@Override
		public String getUriPath() {
			return request.getUriPath();
		}

		@Override
		public String getParameter(String key) {
			return request.getParameter(key);
		}

		@Override
		public List<String> getParametersByKey(String key) {
			return request.getParametersByKey(key);
		}

		@Override
		public String getHttpHeader(String name) {
			return request.getHttpHeader(name);
		}

		@Override
		public List<String> getHttpHeadersByName(String name) {
			return request.getHttpHeadersByName(name);
		}

		@Override
		public HttpVersion getProtocolVersion() {
			return request.getProtocolVersion();
		}

		@Override
		public Set<Cookie> getCookieSet() {
			return request.getCookieSet();
		}

		@Override
		public Cookie getCookieByName(String name) {
			return request.getCookieByName(name);
		}

		@Override
		public JsonRequest getRequestData() {
			return requestData;
		}

		@Override
		public String getRemoteAddress() {
			return request.getRemoteAddress();
		}

		@Override
		public Object getAttribute(String key) {
			return request.getAttribute(key);
		}

		@Override
		public void setAttribute(String key, Object val) {
			request.setAttribute(key, val);
		}
	}
}
