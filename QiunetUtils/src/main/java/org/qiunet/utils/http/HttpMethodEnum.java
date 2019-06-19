package org.qiunet.utils.http;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.Map;

/**
 * @author qiunet
 *         Created on 16/12/20 22:08.
 */
public enum HttpMethodEnum {
	GET {
		@Override
		public HttpUriRequest createRequest(String url, Map<String, Object> params, HttpRequestHandler handler) {
			return new HttpGet(handler.getMethodHandler(url, params));
		}
	},
	POST{
		@Override
		public HttpUriRequest createRequest(String url, Map<String, Object> params, HttpRequestHandler handler) {
			HttpPost request = new HttpPost(url);
			if(params != null && !params.isEmpty()){
				request.setEntity(handler.postMethodHandler(params));
			}
			return request;
		}
	};

	public abstract HttpUriRequest createRequest(String url, Map<String, Object> params, HttpRequestHandler requestHandler);
}
