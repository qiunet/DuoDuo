package org.qiunet.flash.handler.util.proto;

import java.util.HashMap;
import java.util.Map;

/***
 *
 * @author qiunet
 * 2022/2/28 11:40
 */
public enum GeneratorProtoFeature {
	/**
	 * 输出默认 package
	 */
	PROTO_PACKAGE,
	/**
	 * 输出协议枚举
	 */
	OUTPUT_PROTOCOL_LIST_ENUM,
	/**
	 * 枚举 转 int
	 */
	ENUM_TO_INT,
	/**
	 * 输出protocol 映射 markdown
	 */
	OUTPUT_PROTOCOL_MAPPING_MD,
	;
	public static final Map<GeneratorProtoFeature, Object> features = new HashMap<>();

	/**
	 * 是否包含该 特性
	 * @return
	 */
	public boolean prepare() {
		return features.containsKey(this);
	}
}
