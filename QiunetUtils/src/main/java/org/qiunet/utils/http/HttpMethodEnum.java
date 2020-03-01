package org.qiunet.utils.http;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author qiunet
 *         Created on 16/12/20 22:08.
 */
public enum HttpMethodEnum {
	GET {
		@Override
		public HttpUriRequest createRequest(String url, Map<String, Object> params, Map<String, Object> cookies) {
			HttpUriRequest request = new HttpGet(BaseHttpUtil.getMethodHandler(url, params));
			if(cookies != null && !cookies.isEmpty()){
				try {
					request.setHeader("Cookie", BaseHttpUtil.cookieHandler(cookies, StandardCharsets.UTF_8));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return request;
		}
	},
	POST{
		@Override
		public HttpUriRequest createRequest(String url, Map<String, Object> params, Map<String, Object> cookies) {
			HttpPost request = new HttpPost(url);
			if(params != null && !params.isEmpty()){
				request.setEntity(BaseHttpUtil.postMethodHandler(params));
			}
			if(cookies != null && !cookies.isEmpty()){
				try {
					request.setHeader("Cookie", BaseHttpUtil.cookieHandler(cookies, StandardCharsets.UTF_8));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return request;
		}
	};

	public abstract HttpUriRequest createRequest(String url, Map<String, Object> params, Map<String, Object> cookies);
}
