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
	TypeReference<Set<Long>> LONG_SET = new DTypeReference<>();
	TypeReference<Set<Integer>> INT_SET = new DTypeReference<>();
	TypeReference<Set<String>> STRING_SET = new DTypeReference<>();
	TypeReference<Map<Integer, Integer>> INT_INT_MAP = new DTypeReference<>();
	TypeReference<Map<String, String>> STRING_STRING_MAP = new DTypeReference<>();
	TypeReference<Map<Integer, String>> INT_STRING_MAP = new DTypeReference<>();
	TypeReference<Map<String, Integer>> STRING_INT_MAP = new DTypeReference<>();
	TypeReference<Map<Long, String>> LONG_STRING_MAP = new DTypeReference<>();
	TypeReference<Map<String, Long>> STRING_LONG_MAP = new DTypeReference<>();
}
