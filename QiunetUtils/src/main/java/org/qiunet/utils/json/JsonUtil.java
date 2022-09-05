package org.qiunet.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.lang.reflect.Type;
import java.util.List;

/**
 * json 工具类
 * @author qiunet
 *
 */
public final class JsonUtil {
	public static final ParserConfig DEFAULT_PARSER_CONFIG = new ParserConfig(true);
	public static final SerializeConfig DEFAULT_SERIALIZE_CONFIG = new SerializeConfig(true);

	private JsonUtil(){}
	/**
	 * 转换json
	 * @param o
	 * @return
	 */
	public static String toJsonString(Object o, SerializeConfig config, SerializerFeature... features){
		return JSON.toJSONString(o, config, features);
	}
	/**
	 * 转换json
	 * @param o
	 * @return
	 */
	public static String toJsonString(Object o, SerializerFeature... features){
		return toJsonString(o, DEFAULT_SERIALIZE_CONFIG, features);
	}
	/**
	 * 转换json
	 * @param o
	 * @return
	 */
	public static String toJsonString(Object o){
		return toJsonString(o, DEFAULT_SERIALIZE_CONFIG, SerializerFeature.DisableCircularReferenceDetect);
	}
	/**
	 * 使用默认字段和Class反序列化对象
	 * @return
	 */
	public static <T> T getGeneralObj(String jsonText, Class<T> clz, Feature... features) {
		return getGeneralObj(jsonText, clz, DEFAULT_PARSER_CONFIG, features);
	}
	/**
	 * 使用Class反序列化
	 * @return 对象
	 */
	public static <T> T getGeneralObj(String jsonText, Class<T> clz, ParserConfig parserConfig, Feature... features) {
		return JSON.parseObject(jsonText, clz, parserConfig, features);
	}
	/**
	 * 使用TYPE反序列化
	 * @return 对象
	 */
	public static <T> T getGeneralObj(String jsonText, TypeReference<T> type,  Feature... features) {
		return getGeneralObj(jsonText, type.getType(), features);
	}
	public static <T> T getGeneralObj(String jsonText, Type type,  Feature... features) {
		return getGeneralObj(jsonText, type, DEFAULT_PARSER_CONFIG, features);
	}

	/**
	 * 使用自定义的parseConfig
	 */
	public static <T> T getGeneralObj(String jsonText, TypeReference<T> type, ParserConfig parserConfig, Feature... features) {
		return getGeneralObj(jsonText, type.getType(), parserConfig, features);
	}

	public static <T> T getGeneralObj(String jsonText, Type type, ParserConfig parserConfig, Feature... features) {
		return JSON.parseObject(jsonText, type, parserConfig, features);
	}
	/**
	 * 得到通用的列表
	 * @param json
	 * @param c
	 * @return
	 */
	public static <T> List<T> getGeneralList(String json, Class<T> c){
		return getGeneralList(json, c, DEFAULT_PARSER_CONFIG);
	}

	/**
	 * 使用parseConfig 进行反序列化
	 * @return 对象List
	 * @param <T>
	 */
	public static <T> List<T> getGeneralList(String json, Class<T> c, ParserConfig config){
		return JSON.parseArray(json, c, config);
	}

	/**
	 * 转成JsonObject
	 * @param obj
	 * @return
	 */
	public static JSONObject toJsonObject(Object obj) {
		return toJsonObject(obj, DEFAULT_SERIALIZE_CONFIG);
	}

	/**
	 * 使用config序列化对象
	 * @return JsonObject
	 */
	public static JSONObject toJsonObject(Object obj, SerializeConfig config) {
		return (JSONObject) JSON.toJSON(obj, config);
	}
}
