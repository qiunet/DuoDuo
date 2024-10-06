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
	private static final SerializerFeature [] DEFAULT_FEATURES = new SerializerFeature[]{SerializerFeature.DisableCircularReferenceDetect};
	private JsonUtil(){}
	/**
	 * 转换json
	 * @param o
	 * @return
	 */
	public static String toJsonString(Object o, SerializeConfig config, SerializerFeature... features){
		SerializerFeature [] finalFeatures;
		if (features == null || features.length == 0) {
			finalFeatures = DEFAULT_FEATURES;
		}else {
			finalFeatures = new SerializerFeature[features.length + DEFAULT_FEATURES.length];
			System.arraycopy(DEFAULT_FEATURES, 0, finalFeatures, 0, DEFAULT_FEATURES.length);
			System.arraycopy(features, 0, finalFeatures, DEFAULT_FEATURES.length, features.length);
		}
		return JSON.toJSONString(o, config, finalFeatures);
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
		T o = JSON.parseObject(jsonText, clz, parserConfig, features);
		if(o == null && ! clz.isInterface()){
			try {
				clz.getDeclaredConstructor();
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("getGeneralObj class ["+clz.getSimpleName()+"] Error", e);
			}
			return null;
		}
		return o;
	}
	/**
	 * 使用TYPE反序列化
	 * @return 对象
	 */
	public static <T> T getGeneralObject(String jsonText, TypeReference<T> type,  Feature... features) {
		return getGeneralObject(jsonText, type.getType(), features);
	}
	public static <T> T getGeneralObject(String jsonText, Type type,  Feature... features) {
		return getGeneralObject(jsonText, type, DEFAULT_PARSER_CONFIG, features);
	}

	/**
	 * 使用自定义的parseConfig
	 */
	public static <T> T getGeneralObject(String jsonText, TypeReference<T> type, ParserConfig parserConfig, Feature... features) {
		return getGeneralObject(jsonText, type.getType(), parserConfig, features);
	}

	public static <T> T getGeneralObject(String jsonText, Type type, ParserConfig parserConfig, Feature... features) {
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
