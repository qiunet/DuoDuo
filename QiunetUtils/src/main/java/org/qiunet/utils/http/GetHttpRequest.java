package org.qiunet.utils.http;

import com.google.common.collect.Maps;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

import java.util.Map;

/***
 *
 *
 * @author qiunet
 * 2020-04-20 17:47
 ***/
public class GetHttpRequest extends HttpRequest<GetHttpRequest> {

	private Map<String, String> params = Maps.newHashMap();
	GetHttpRequest(String urlstring){
		super(urlstring);
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
		StringBuilder sb = new StringBuilder(this.path());
		if(params != null && !params.isEmpty()){
			if(sb.indexOf("?") != -1) sb.append("&");
			else sb.append("?");

			for(Map.Entry<String, String> en : params.entrySet()) {
				sb.append(en.getKey()).append('=').append(en.getValue()).append("&");
			}
			if(!sb.isEmpty()) sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}


	@Override
	protected FullHttpRequest buildRequest() {
		return new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, buildUrl());
	}
}
