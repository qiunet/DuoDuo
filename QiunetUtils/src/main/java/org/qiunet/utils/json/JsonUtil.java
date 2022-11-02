package org.qiunet.utils.json;

import com.alibaba.fastjson2.*;

import java.lang.reflect.Type;
import java.util.List;

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
	public static String toJsonString(Object o, JSONWriter.Feature... features){
		return JSON.toJSONString(o, features);
	}
	/**
	 * 转换json
	 * @param o
	 * @return
	 */
	public static String toJsonString(Object o){
		return toJsonString(o, JSONWriter.Feature.FieldBased);
	}
	/**
	 * 使用默认字段和Class反序列化对象
	 * @return
	 */
	public static <T> T getGeneralObj(String jsonText, Class<T> clz) {
		return getGeneralObj(jsonText, clz, JSONReader.Feature.FieldBased);
	}
	/**
	 * 使用Class反序列化
	 * @return 对象
	 */
	public static <T> T getGeneralObj(String jsonText, Class<T> clz, JSONReader.Feature... features) {
		return getGeneralObj(jsonText, (Type)clz, features);
	}
	/**
	 * 使用TYPE反序列化
	 * @return 对象
	 */
	public static <T> T getGeneralObj(String jsonText, TypeReference<T> type) {
		return getGeneralObj(jsonText, type.getType(), JSONReader.Feature.FieldBased);
	}
	/**
	 * 使用自定义的parseConfig
	 */
	public static <T> T getGeneralObj(String jsonText, TypeReference<T> type, JSONReader.Feature... features) {
		return getGeneralObj(jsonText, type.getType(), features);
	}

	public static <T> T getGeneralObj(String jsonText, Type type, JSONReader.Feature... features) {
		return JSON.parseObject(jsonText, type, features);
	}
	/**
	 * 得到通用的列表
	 * @param json
	 * @param c
	 * @return
	 */
	public static <T> List<T> getGeneralList(String json, Class<T> c){
		return getGeneralList(json, c, JSONReader.Feature.FieldBased);
	}

	/**
	 * 使用parseConfig 进行反序列化
	 * @return 对象List
	 * @param <T>
	 */
	public static <T> List<T> getGeneralList(String json, Class<T> c, JSONReader.Feature... features){
		return JSON.parseArray(json, c, features);
	}

	/**
	 * 转成JsonObject
	 * @param obj
	 * @return
	 */
	public static JSONObject toJsonObject(Object obj) {
		return toJsonObject(obj, JSONWriter.Feature.FieldBased);
	}

	/**
	 * 使用config序列化对象
	 * @return JsonObject
	 */
	public static JSONObject toJsonObject(Object obj, JSONWriter.Feature... features) {
		return (JSONObject) JSON.toJSON(obj, features);
	}
}
