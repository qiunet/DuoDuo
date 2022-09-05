package org.qiunet.utils.test.json;

import com.alibaba.fastjson.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.test.base.BaseTest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qiunet
 *         Created on 16/11/5 20:45.
 */
public class TestJsonUtil extends BaseTest{
	private static Map<Integer, Map<String, String>> testField;
	@Test
	public void testGetGeneralObject(){
		Map<String,Integer> map = new HashMap<>();
		map.put("qiunet", 111);

		String json = JsonUtil.toJsonString(map);
		logger.info(json);

		Map<String,Integer> ret = JsonUtil.getGeneralObj(json, new TypeReference<Map<String,Integer>>(){});
		Assertions.assertEquals(111, ret.get("qiunet").intValue());
	}
	@Test
	public void testGetGeneralList(){
		List<Integer> ls = new ArrayList<>();
		ls.add(1);
		ls.add(2);
		ls.add(3);

		String json = JsonUtil.toJsonString(ls);
		logger.info(json);
		List<Integer> ret = JsonUtil.getGeneralList(json, Integer.class);
		Assertions.assertEquals(3, ret.size());
		Assertions.assertTrue(ret.contains(3));
		Assertions.assertFalse(ret.contains(5));
	}
	@Test
	public void testRefJson(){
		Map<Integer, Map<String, String>> map = new HashMap<>();
		Map<String, String> subMap = new HashMap<>();
		subMap.put("ATK", "aa");
		subMap.put("DEF", "bb");
		map.put(1, subMap);
		map.put(2, subMap);
		map.put(3, subMap);

		String json = JsonUtil.toJsonString(map);
		Assertions.assertEquals("{1:{\"DEF\":\"bb\",\"ATK\":\"aa\"},2:{\"DEF\":\"bb\",\"ATK\":\"aa\"},3:{\"DEF\":\"bb\",\"ATK\":\"aa\"}}", json);
		map = JsonUtil.getGeneralObj(json, new TypeReference<Map<Integer, Map<String, String>>>(){});
		subMap = map.get(2);
		Assertions.assertNotNull(subMap);
		Assertions.assertEquals("bb", subMap.get("DEF"));
	}

	@Test
	public void testBaseField(){
		Map<String, Object> map = new HashMap<>();
		map.put("userId", 123456);
		map.put("name", "qiunet");
		String jsonString = JsonUtil.toJsonString(map);

		User user = JsonUtil.getGeneralObj(jsonString, User.class);
		Assertions.assertEquals(123456, user.getUserId());
	}

	@Test
	public void testField() throws NoSuchFieldException {
		Field testField = TestJsonUtil.class.getDeclaredField("testField");
		String value = "{1:{\"DEF\":\"bb\",\"ATK\":\"aa\"},2:{\"DEF\":\"bb\",\"ATK\":\"aa\"},3:{\"DEF\":\"bb\",\"ATK\":\"aa\"}}";
		Map<Integer, Map<String, String>>  map = (Map<Integer, Map<String, String>>) JsonUtil.getGeneralObj(value, testField.getType());
		Map<String, String> subMap = map.get(2);
		Assertions.assertNotNull(subMap);
		Assertions.assertEquals("bb", subMap.get("DEF"));
	}
}
