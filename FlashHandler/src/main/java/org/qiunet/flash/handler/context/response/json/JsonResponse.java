package org.qiunet.flash.handler.context.response.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.utils.json.JsonUtil;

import java.util.Map;

/**
 * Created by qiunet.
 * 18/1/29
 */
public class JsonResponse {
	/**
	 * 数据
	 */
	private final JSONObject data = new JSONObject();

	public JsonResponse() {this.setGameStatus(IGameStatus.SUCCESS);}

	/***
	 * 设置错误
	 * @param status
	 * @return
	 */
	public JsonResponse setFail(IGameStatus status) {
		return this.setGameStatus(status);
	}

	public static JsonResponse parse(String jsonString) {
		JsonResponse response = new JsonResponse();
		response.data.putAll(JSON.parseObject(jsonString));
		return response;
	}
	/**
	 * 设置状态
	 * @param status
	 * @return
	 */
	public JsonResponse setGameStatus(IGameStatus status) {
		Map<String, Object> map = ImmutableMap.of("status", status.getStatus(), "desc", status.getDesc());
		data.put("status", map);
		return this;
	}

	public int status(){
		Map<String, Object> status = (Map<String, Object>) data.get("status");
		return (int) status.get("status");
	}

	public JsonResponse addAttribute(String key, int val) {
		this.put(key, val);
		return this;
	}
	public JsonResponse addAttribute(String key, long val) {
		this.put(key, val);
		return this;
	}

	public JsonResponse addAttribute(String key, boolean val) {
		this.put(key, val);
		return this;
	}

	public JsonResponse addAttribute(String key, String val) {
		this.put(key, val);
		return this;
	}

	public JsonResponse addAttribute(String key, JSONArray array) {
		this.put(key, array);
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

	public JsonResponse put(String key, Object value) {
		if (key.equals("status")) {
			throw new IllegalArgumentException("JsonResponse key can not named `status` or `desc`");
		}
		data.put(key, value);
		return this;
	}

	@Override
	public String toString() {
		return JsonUtil.toJsonString(data);
	}
}
