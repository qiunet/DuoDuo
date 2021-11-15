package org.qiunet.excel2cfgs.common.utils;

import com.alibaba.fastjson.JSON;

/***
 *
 * @author qiunet
 * 2021/11/15 14:01
 */
public class JsonUtil {

	public static String toJsonString(Object obj) {
		return JSON.toJSONString(obj);
	}

	public static <T> T getGeneralObject(String json ,Class<T> c){
		return JSON.parseObject(json, c);
	}
}
