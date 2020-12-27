package org.qiunet.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

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
		return JSON.toJSONString(o, features);
	}
	/**
	 * 转换json
	 * @param o
	 * @return
	 */
	public static String toJsonString(Object o){
		return JSON.toJSONString(o, SerializerFeature.DisableCircularReferenceDetect);
	}

	/**
	 * 仅字段对象
	 * @param
	 * @return
	 */
	public static <T> T getGeneralObjWithField(String jsonText, Class<T> clz, Feature... features) {
		return getGeneralObjWithField(jsonText, clz, DEFAULT_PARSER_CONFIG, features);
	}
	/**
	 * 仅字段对象
	 * @param
	 * @return
	 */
	public static <T> T getGeneralObjWithField(String jsonText, Class<T> clz, ParserConfig parserConfig, Feature... features) {
		return JSON.parseObject(jsonText, clz, parserConfig, features);
	}
	/**
	 * 使用字段
	 * @param
	 * @return
	 */
	public static <T> T getGeneralObjWithField(String jsonText, TypeReference<T> type, Feature... features) {
		return getGeneralObjWithField(jsonText, type, DEFAULT_PARSER_CONFIG, features);
	}
	/**
	 * 使用字段
	 * @param
	 * @return
	 */
	public static <T> T getGeneralObjWithField(String jsonText, TypeReference<T> type, ParserConfig parserConfig, Feature... features) {
		return JSON.parseObject(jsonText, type.getType(), parserConfig, features);
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
	 * 反序列化根据get set判断字段的对象
	 * 使用TypeReference 反序列. TypeReference最好定义为常量
	 * @param json
	 * @param typeReference
	 * @param features
	 * @param <T>
	 * @return
	 */
	public static <T> T getGeneralObject(String json , TypeReference<T> typeReference, Feature ... features){
		return JSON.parseObject(json, typeReference.getType(), features);
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
