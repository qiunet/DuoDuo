package org.qiunet.utils.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.qiunet.utils.enums.CharsetEnum;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultHttpRequestHandler implements HttpRequestHandler<String> {
	private static final Logger logger = LoggerFactory.getLogger(LoggerType.QIUNET_UTILS);
	private String result;
	@Override
	public String getMethodHandler(String url, Map<String, Object> params) {
		StringBuilder sb = new StringBuilder(url);
		if(params != null && !params.isEmpty()){
			if(sb.indexOf("?") != -1) sb.append("&");
			else 	sb.append("?");
			for(Entry<String, Object> en : params.entrySet())
				sb.append(en.getKey()).append('=').append(en.getValue()).append("&");

			if(sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	@Override
	public String cookieHandler(Map<String, Object> cookies, CharsetEnum charset) throws Exception {
		if(cookies == null || cookies.isEmpty()) return "";

		StringBuilder sb = new StringBuilder();
		for(Entry<String, Object> en : cookies.entrySet())
			sb.append(en.getKey()).append("=").append(encodeUrl(String.valueOf(en.getValue()), charset)).append(';');

		return sb.toString();
	}

	/**
	 * URL编码 (符合FRC1738规范)
	 * @param input
	 */
	public static String encodeUrl(String input, CharsetEnum charset) throws Exception {
		try {
			return URLEncoder.encode(input, charset.toString());
		} catch (UnsupportedEncodingException e) {
			logger.error("[DefaultHttpRequestHandler] Exception: ", e);
		}
		return "";
	}

	@Override
	public HttpEntity postMethodHandler(Map<String, Object> params) throws UnsupportedEncodingException {
		if(params != null){
			List<NameValuePair> paramList = new ArrayList<NameValuePair>(params.size());
			for(Entry<String, Object> en : params.entrySet()){
				paramList.add(new BasicNameValuePair(en.getKey(), String.valueOf(en.getValue())));
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Charset.forName("UTF-8"));
			return entity;
		}
		return null;
	}

	@Override
	public void errorHandler(Map<String, Object> params,Map<String, Object> cookies,int httpStatus , String url) {
		logger.error("httpstatus: ["+httpStatus+"] URL ["+url+"]");
		logger.error("params : ["+JsonUtil.toJsonString(params)+"]");
		logger.error("cookies : ["+JsonUtil.toJsonString(cookies)+"]");
	}

	@Override
	public void handlerResult(HttpResponse response, CharsetEnum charset) {
		try {
			this.result = EntityUtils.toString(response.getEntity(), charset.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public String returnResult() {
		return result;
	}
}
