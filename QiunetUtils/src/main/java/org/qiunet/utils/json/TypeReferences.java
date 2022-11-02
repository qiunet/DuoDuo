package org.qiunet.utils.json;


import com.alibaba.fastjson2.TypeReference;

import java.util.List;
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
	TypeReference<List<Long>> LONG_LIST = new TypeReference<List<Long>>(){};
	TypeReference<List<Integer>> INT_LIST = new TypeReference<List<Integer>>(){};
	TypeReference<List<String>> STRING_LIST = new TypeReference<List<String>>(){};
	TypeReference<Map<Integer, Integer>> INT_INT_MAP = new TypeReference<Map<Integer, Integer>>(){};
	TypeReference<Map<Integer, Long>> INT_LONG_MAP = new TypeReference<Map<Integer, Long>>(){};
	TypeReference<Map<String, String>> STRING_STRING_MAP = new TypeReference<Map<String, String>>(){};
	TypeReference<Map<Integer, String>> INT_STRING_MAP = new TypeReference<Map<Integer, String>>(){};
	TypeReference<Map<String, Integer>> STRING_INT_MAP = new TypeReference<Map<String, Integer>>(){};
	TypeReference<Map<Long, Long>> LONG_LONG_MAP = new TypeReference<Map<Long, Long>>(){};
	TypeReference<Map<Long, String>> LONG_STRING_MAP = new TypeReference<Map<Long, String>>(){};
	TypeReference<Map<String, Long>> STRING_LONG_MAP = new TypeReference<Map<String, Long>>(){};
}
