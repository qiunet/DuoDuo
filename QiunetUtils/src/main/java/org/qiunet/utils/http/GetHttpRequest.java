package org.qiunet.utils.http;

import com.google.common.collect.Maps;

import java.net.URI;
import java.time.Duration;
import java.util.Map;

/***
 *
 *
 * @author qiunet
 * 2020-04-20 17:47
 ***/
public class GetHttpRequest extends HttpRequest<GetHttpRequest> {

	private Map<String, String> params = Maps.newHashMap();
	GetHttpRequest(String url){
		super(url);
	}

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
	protected java.net.http.HttpRequest buildRequest() {
		java.net.http.HttpRequest.Builder builder = java.net.http.HttpRequest.newBuilder(URI.create(buildUrl()));
		builder.timeout(Duration.ofMillis(READ_TIMEOUT_MILLIS));
		headerBuilder.forEach(builder::header);
		return builder.build();
	}
}
