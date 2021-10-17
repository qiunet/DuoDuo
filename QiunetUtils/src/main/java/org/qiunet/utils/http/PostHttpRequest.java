package org.qiunet.utils.http;

import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.string.StringUtil;

import java.net.URI;
import java.util.Map;

/***
 *
 *
 * @author qiunet
 * 2020-04-20 17:42
 ***/
public class PostHttpRequest extends HttpRequest<PostHttpRequest> {

	private java.net.http.HttpRequest.BodyPublisher requestBody;

	PostHttpRequest(String url) {
		super(url);
	}

	/**
	 * 使用form方式提交
	 * @param params
	 * @return
	 */
	public PostHttpRequest withFormData(Map<String, String> params) {
		String formData = StringUtil.mapToString(params, "=", "&");
		this.withStringData(formData);
		return this;
	}
	/**
	 * 使用json的方式提交数据
	 *
	 * @param params
	 * @return
	 */
	public PostHttpRequest withJsonData(Map<String, Object> params) {
		String json = JsonUtil.toJsonString(params);
		return withStringData(json);
	}

	/**
	 * 使用string的方式提交数据
	 *
	 * @param string
	 * @return
	 */
	public PostHttpRequest withStringData(String string) {
		this.requestBody = java.net.http.HttpRequest.BodyPublishers.ofString(string);
		return this;
	}
	/**
	 * 使用byte数组的方式提交数据
	 *
	 * @param bytes
	 * @return
	 */
	public PostHttpRequest withBytes(byte [] bytes) {
		this.requestBody = java.net.http.HttpRequest.BodyPublishers.ofByteArray(bytes);
		return this;
	}
	/**
	 * 自定义body格式类型
	 *
	 * @param requestBody
	 * @return
	 */
	public PostHttpRequest customBody(java.net.http.HttpRequest.BodyPublisher requestBody) {
		this.requestBody = requestBody;
		return this;
	}


	@Override
	protected java.net.http.HttpRequest buildRequest() {
		java.net.http.HttpRequest.Builder builder = java.net.http.HttpRequest.newBuilder(URI.create(url));
		headerBuilder.forEach(builder::setHeader);
		if (requestBody != null) {
			builder.POST(requestBody);
		}
		return builder.build();
	}
}
