package org.qiunet.utils.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import org.qiunet.utils.json.JsonUtil;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.StringJoiner;

/***
 *
 *
 * @author qiunet
 * 2020-04-20 17:42
 ***/
public class PostHttpRequest extends HttpRequest<PostHttpRequest> {
	/**
	 * post 数据
	 */
	private ByteBuffer data;

	protected PostHttpRequest(String url) {
		super(url);
	}

	/**
	 * 使用form的方式提交数据
	 *
	 * @param params map 参数
	 * @return 对象本身
	 */
	public PostHttpRequest withFormData(Map<String, String> params) {
		this.header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED);
		StringJoiner joiner = new StringJoiner("&");
		params.forEach((key, val) -> {
			joiner.add(key + "=" + val);
		});
		this.data = this.charset.encode(joiner.toString());
		return this;
	}

	/**
	 * 使用json的方式提交数据
	 *
	 * @param params map 参数
	 * @return 对象本身
	 */
	public PostHttpRequest withJsonData(Map<String, Object> params) {
		String json = JsonUtil.toJsonString(params);
		return withJsonData(json);
	}

	/**
	 * 使用json的方式提交数据
	 *
	 * @param params
	 * @return 对象本身
	 */
	public PostHttpRequest withJsonData(Object params) {
		return this.withJsonData(JsonUtil.toJsonString(params));
	}

	/**
	 * 使用string的方式提交数据
	 * @param data string 数据
	 * @return 对象本身
	 */
	public PostHttpRequest withStringData(String data) {
		this.header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
		this.data = this.charset.encode(data);
		return this;
	}
	/**
	 * 使用json的方式提交数据
	 *
	 * @param json
	 * @return
	 */
	public PostHttpRequest withJsonData(String json) {
		this.header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
		this.data = this.charset.encode(json);
		return this;
	}
	/**
	 * 使用byte数组的方式提交数据
	 *
	 * @param bytes
	 * @return
	 */
	public PostHttpRequest withBytes(byte [] bytes) {
		this.header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.MULTIPART_FORM_DATA);
		this.data = ByteBuffer.wrap(bytes);
		return this;
	}
	/**
	 * 自定义body格式类型
	 *
	 * @param bodyData
	 * @return
	 */
	public PostHttpRequest customBody(ByteBuffer bodyData) {
		this.data = bodyData;
		return this;
	}


	@Override
	protected FullHttpRequest buildRequest() {
		ByteBuf byteBuf;
		if (this.data == null) {
			byteBuf = Unpooled.EMPTY_BUFFER;
		}else {
			byteBuf = Unpooled.wrappedBuffer(this.data);
		}
		this.header(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
		return new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, this.path(), byteBuf);
	}
}
