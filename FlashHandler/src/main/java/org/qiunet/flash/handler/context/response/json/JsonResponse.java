package org.qiunet.flash.handler.context.response.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.utils.json.JsonUtil;

/**
 * Created by qiunet.
 * 18/1/29
 */
public class JsonResponse extends JSONObject {

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
		response.putAll(JSON.parseObject(jsonString));
		return response;
	}
	/**
	 * 设置状态
	 * @param status
	 * @return
	 */
	public JsonResponse setGameStatus(IGameStatus status) {
		super.put("status", status.getStatus());
		if(status != IGameStatus.SUCCESS){
			super.put("desc", status.getDesc());
		}
		return this;
	}

	public int status(){
		Integer ret = getInteger("status");
		return ret == null ? -1 : ret;
	}

	public String desc(){
		return getString("desc");
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
	@Override
	public JsonResponse put(String key, Object value) {
		if (key.equals("status") || key.equals("desc")) {
			throw new IllegalArgumentException("JsonResponse key can not named `status` or `desc`");
		}
		super.put(key, value);
		return this;
	}

	@Override
	public String toString() {
		return JsonUtil.toJsonString(this);
	}
}
