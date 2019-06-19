package org.qiunet.utils.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

abstract class BaseHttpUtil {
	private static final Logger logger = LoggerType.DUODUO.getLogger();
	protected static RequestConfig REQUESTCONFIG = RequestConfig.custom()
		.setSocketTimeout(6000)
		.setConnectTimeout(6000)
		.build();

	 static String getMethodHandler(String url, Map<String, Object> params) {
		StringBuilder sb = new StringBuilder(url);
		if(params != null && !params.isEmpty()){
			if(sb.indexOf("?") != -1) sb.append("&");
			 else sb.append("?");

			for(Entry<String, Object> en : params.entrySet()) {
				sb.append(en.getKey()).append('=').append(en.getValue()).append("&");
			}
			if(sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	 static String cookieHandler(Map<String, Object> cookies, Charset charset) throws Exception {
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
	 static String encodeUrl(String input, Charset charset) throws Exception {
		try {
			return URLEncoder.encode(input, charset.toString());
		} catch (UnsupportedEncodingException e) {
			logger.error("[BaseHttpUtil] Exception: ", e);
		}
		return "";
	}

	 static HttpEntity postMethodHandler(Map<String, Object> params) {
		if(params != null){
			List<NameValuePair> paramList = new ArrayList<>(params.size());
			for(Entry<String, Object> en : params.entrySet()){
				paramList.add(new BasicNameValuePair(en.getKey(), String.valueOf(en.getValue())));
			}
			return new UrlEncodedFormEntity(paramList, StandardCharsets.UTF_8);
		}
		return null;
	}

	 static void errorHandler(Map<String, Object> params,Map<String, Object> cookies,int httpStatus , String url) {
	 	errorHandler(params, cookies, httpStatus, url, null);
	 }
	static void errorHandler(Map<String, Object> params,Map<String, Object> cookies,int httpStatus , String url, Throwable throwable) {
		logger.error("httpstatus: ["+httpStatus+"] URL ["+url+"]");
		logger.error("params : ["+JsonUtil.toJsonString(params)+"]");
		logger.error("cookies : ["+JsonUtil.toJsonString(cookies)+"]");
		if (throwable != null) logger.error("Exception", throwable);
	}
}
