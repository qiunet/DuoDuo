package org.qiunet.utils.json;

import java.util.List;

import com.alibaba.fastjson.JSON;
/**
 * json 工具类
 * @author qiunet
 *
 */
public final class JsonUtil {
	private JsonUtil(){}
	/**
	 * 转换json
	 * @param o
	 * @return
	 */
	public static String toJsonString(Object o){
		return JSON.toJSONString(o);
	}
	/**
	 * 得到通用的列表
	 * @param json
	 * @param c
	 * @return
	 */
	public static <T> List<T> getGeneralList(String json, Class<T> c){
		return JSON.parseArray(json, c);
	}
	/**
	 * 得到通用的对象
	 * @param json
	 * @param c
	 * @return
	 */
	public static <T> T getGeneralObject(String json ,Class<T> c){
		return JSON.parseObject(json, c);
	}
}
