package org.qiunet.utils.http;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.qiunet.utils.json.JsonUtil;

import java.util.Collections;
import java.util.Map;

/***
 *
 *
 * @author qiunet
 * 2020-04-20 17:42
 ***/
public class PostHttpRequest extends HttpRequest<PostHttpRequest> {

	private RequestBody requestBody;

	PostHttpRequest(String url) {
		super(url);
	}

	/**
	 * 使用form的方式提交数据
	 *
	 * @param params
	 * @return
	 */
	public PostHttpRequest withFormData(Map<String, String> params) {
		FormBody.Builder builder = new FormBody.Builder();
		params.forEach(builder::add);
		this.requestBody = builder.build();
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
		this.requestBody = RequestBody.create(MediaType.parse("application/json; charset=" + charset), json);
		return this;
	}

	/**
	 * 使用json的方式提交数据
	 *
	 * @param json
	 * @return
	 */
	public PostHttpRequest withJsonData(String json) {
		this.requestBody = RequestBody.create(MediaType.parse("application/json; charset=" + charset), json);
		return this;
	}

	/**
	 * 自定义body格式类型
	 *
	 * @param requestBody
	 * @return
	 */
	public PostHttpRequest customBody(RequestBody requestBody) {
		this.requestBody = requestBody;
		return this;
	}


	@Override
	protected Request buildRequest() {
		if (requestBody == null) {
			this.withFormData(Collections.emptyMap());
		}
		return new Request.Builder()
				.headers(headerBuilder.build())
				.post(requestBody)
				.url(url)
				.build();
	}
}
