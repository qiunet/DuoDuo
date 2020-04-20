package org.qiunet.utils.http;

import com.google.common.collect.Maps;
import okhttp3.Headers;
import okhttp3.Request;

import java.util.Map;

/***
 *
 *
 * @author qiunet
 * 2020-04-20 17:47
 ***/
public class GetHttpRequest extends HttpRequest<GetHttpRequest> {

	private Map<String, String> params = Maps.newHashMap();
	GetHttpRequest(){}

	/**
	 * 如果是get模式. 我会拼接到url后面.
	 * @param params
	 * @return
	 */
	public GetHttpRequest params(Map<String, String> params) {
		this.params = params;
		return this;
	}

	private String buildUrl() {
		StringBuilder sb = new StringBuilder(url);
		if(params != null && !params.isEmpty()){
			if(sb.indexOf("?") != -1) sb.append("&");
			else sb.append("?");

			for(Map.Entry<String, String> en : params.entrySet()) {
				sb.append(en.getKey()).append('=').append(en.getValue()).append("&");
			}
			if(sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}


	@Override
	protected Request buildRequest() {
		return new Request.Builder().url(buildUrl())
			.headers(Headers.of(headerMap))
			.build();
	}
}
