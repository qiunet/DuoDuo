package org.qiunet.flash.handler.context.request.http;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.utils.json.JsonUtil;

/**
 * 转Json对象的
 * Created by qiunet.
 * 17/11/21
 */
public class HttpJsonRequest extends AbstractHttpRequest<JSONObject, JSONObject> {
	private JSONObject reqeustData;
	public HttpJsonRequest(MessageContent content, ChannelHandlerContext channelContext, HttpRequest request) {
		super(content, channelContext, request);
	}

	@Override
	public JSONObject getRequestData() {
		if (reqeustData == null) {
			reqeustData = JSONObject.parseObject(new String(bytes, CharsetUtil.UTF_8));
		}
		return reqeustData;
	}

	/**
	 * json 解析成指定的对象
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public <T> T getRequestData(Class<T> clazz) {
		return JsonUtil.getGeneralObject(new String(bytes, CharsetUtil.UTF_8), clazz);
	}

	@Override
	protected byte[] getResponseDataBytes(JSONObject s) {
		return s.toJSONString().getBytes(CharsetUtil.UTF_8);
	}

	@Override
	public boolean handler() {
		return false;
	}

	@Override
	protected String contentType() {
		return "text/plain; charset=UTF-8";
	}

	@Override
	public String toStr() {
		return "request: "+ JsonUtil.toJsonString(reqeustData);
	}
}
