package org.qiunet.utils.json;

import com.alibaba.fastjson.TypeReference;

import java.util.Map;
import java.util.Set;

/***
 * 常用的TypeReference
 *
 * @author qiunet
 * 2020-09-18 11:52
 */
public interface TypeReferences {
	TypeReference<Set<Long>> LONG_SET = new TypeReference<Set<Long>>(){};
	TypeReference<Set<Integer>> INT_SET = new TypeReference<Set<Integer>>(){};
	TypeReference<Set<String>> STRING_SET = new TypeReference<Set<String>>(){};
	TypeReference<Map<Integer, Integer>> INT_INT_MAP = new TypeReference<Map<Integer, Integer>>(){};
	TypeReference<Map<String, String>> STRING_STRING_MAP = new TypeReference<Map<String, String>>(){};
	TypeReference<Map<Integer, String>> INT_STRING_MAP = new TypeReference<Map<Integer, String>>(){};
	TypeReference<Map<String, Integer>> STRING_INT_MAP = new TypeReference<Map<String, Integer>>(){};
	TypeReference<Map<Long, String>> LONG_STRING_MAP = new TypeReference<Map<Long, String>>(){};
	TypeReference<Map<String, Long>> STRING_LONG_MAP = new TypeReference<Map<String, Long>>(){};
}
