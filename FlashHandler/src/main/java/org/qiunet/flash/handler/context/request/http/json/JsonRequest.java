package org.qiunet.flash.handler.context.request.http.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.json.JsonUtil;

import java.util.Map;

/**
 * Created by qiunet.
 * 18/1/29
 */
public class JsonRequest extends JSONObject {

	public static JsonRequest parse(String jsonText) {
		JsonRequest request = new JsonRequest();
		request.putAll0(JSON.parseObject(jsonText));
		return request;
	}
	private void putAll0(Map<? extends String, ?> m) {
		super.putAll(m);
	}

	@Override
	public void putAll(Map<? extends String, ?> m) {
		throw new CustomException("can call putAll method in JsonRequest");
	}
	public JsonRequest addAttribute(String key, int val) {
		this.put(key, val);
		return this;
	}
	public JsonRequest addAttribute(String key, long val) {
		this.put(key, val);
		return this;
	}

	public JsonRequest addAttribute(String key, boolean val) {
		this.put(key, val);
		return this;
	}

	public JsonRequest addAttribute(String key, String val) {
		this.put(key, val);
		return this;
	}

	public JsonRequest addAttribute(String key, JSONArray array) {
		this.put(key, array);
		return this;
	}
	@Override
	public JsonRequest put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	@Override
	public String toString() {
		return JsonUtil.toJsonString(this);
	}
}
