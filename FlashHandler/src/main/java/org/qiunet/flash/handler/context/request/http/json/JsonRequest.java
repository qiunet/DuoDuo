package org.qiunet.flash.handler.context.request.http.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.qiunet.utils.json.JsonUtil;

import java.util.Map;

/**
 * Created by qiunet.
 * 18/1/29
 */
public class JsonRequest {
	private final JSONObject data = new JSONObject();

	public static JsonRequest parse(String jsonText) {
		JsonRequest request = new JsonRequest();
		request.putAll0(JSON.parseObject(jsonText));
		return request;
	}

	private void putAll0(Map<? extends String, ?> m) {
		data.putAll(m);
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

	public JsonRequest put(String key, Object value) {
		data.put(key, value);
		return this;
	}

	public Object get(String key) {
		return data.get(key);
	}

	public String getString(String key) {
		return data.getString(key);
	}

	public Integer getInteger(String key) {
		return data.getInteger(key);
	}

	public int getIntValue(String key) {
		return data.getIntValue(key);
	}

	public long getLongValue(String key) {
		return data.getLongValue(key);
	}

	public Long getLong(String key) {
		return data.getLong(key);
	}

	@Override
	public String toString() {
		return JsonUtil.toJsonString(this);
	}
}
