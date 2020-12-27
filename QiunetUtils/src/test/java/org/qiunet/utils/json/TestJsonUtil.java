package org.qiunet.utils.json;

import com.alibaba.fastjson.TypeReference;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.base.BaseTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qiunet
 *         Created on 16/11/5 20:45.
 */
public class TestJsonUtil extends BaseTest{
	@Test
	public void testGetGeneralObject(){
		Map<String,Integer> map = new HashMap<>();
		map.put("qiunet", 111);

		String json = JsonUtil.toJsonString(map);
		logger.info(json);

		Map<String,Integer> ret = JsonUtil.getGeneralObject(json, new TypeReference<Map<String,Integer>>(){});
		Assert.assertEquals(111, ret.get("qiunet").intValue());
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
		Assert.assertEquals(3, ret.size());
		Assert.assertTrue(ret.contains(3));
		Assert.assertFalse(ret.contains(5));
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
		Assert.assertEquals("{1:{\"DEF\":\"bb\",\"ATK\":\"aa\"},2:{\"DEF\":\"bb\",\"ATK\":\"aa\"},3:{\"DEF\":\"bb\",\"ATK\":\"aa\"}}", json);
		map = JsonUtil.getGeneralObject(json, new TypeReference<Map<Integer, Map<String, String>>>(){});
		subMap = map.get(2);
		Assert.assertNotNull(subMap);
		Assert.assertEquals("bb", subMap.get("DEF"));
	}

	@Test
	public void testBaseField(){
		Map<String, Object> map = new HashMap<>();
		map.put("userId", 123456);
		map.put("name", "qiunet");
		String jsonString = JsonUtil.toJsonString(map);

		User user = JsonUtil.getGeneralObjWithField(jsonString, User.class);
		Assert.assertEquals(123456, user.getUserId());
	}
}
