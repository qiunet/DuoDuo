package org.qiunet.utils.json;

import java.util.Map;
import java.util.Set;

/***
 * 常用的TypeReference
 *
 * @author qiunet
 * 2020-09-18 11:52
 */
public interface TypeReferences {
	DTypeReference<Set<Long>> LONG_SET = new DTypeReference<>();
	DTypeReference<Set<Integer>> INT_SET = new DTypeReference<>();
	DTypeReference<Set<String>> STRING_SET = new DTypeReference<>();
	DTypeReference<Map<Integer, Integer>> INT_INT_MAP = new DTypeReference<>();
	DTypeReference<Map<String, String>> STRING_STRING_MAP = new DTypeReference<>();
	DTypeReference<Map<Integer, String>> INT_STRING_MAP = new DTypeReference<>();
	DTypeReference<Map<String, Integer>> STRING_INT_MAP = new DTypeReference<>();
	DTypeReference<Map<Long, String>> LONG_STRING_MAP = new DTypeReference<>();
	DTypeReference<Map<String, Long>> STRING_LONG_MAP = new DTypeReference<>();
}
