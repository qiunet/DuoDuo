package org.qiunet.utils.common;

import org.qiunet.utils.base.BaseTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * @author qiunet
 *         Created on 16/11/5 12:24.
 */
public class TestCommonUtil extends BaseTest {

	@Test
	public void testExistInArray(){
		Integer [] arr = {1,2,3,4,5};
		Assert.assertTrue(CommonUtil.existInList(3, arr));
		Assert.assertFalse(CommonUtil.existInList(8, arr));
	}

	@Test
	public void testExistInCollection(){
		List<Integer> list = new ArrayList();
		list.add(0);
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);

		Assert.assertTrue(CommonUtil.existInList(3, list));
		Assert.assertFalse(CommonUtil.existInList(8, list));
	}
	@Test
	public void testGetMap() {
		String [] fields = new String[]{"uid" , "nick" , "regDt", "level" , "exp"};
		TestPo testPo = new TestPo();
		testPo.setExp(11);
		testPo.setLevel(22);
		testPo.setUid(33);
		testPo.setNick("qiunet");
		testPo.setRegDt(new Date());
		Map<String, String > ret = CommonUtil.getMap(testPo, fields);
		Assert.assertEquals("qiunet", ret.get("nick"));

		TestPo testPo1 = CommonUtil.getObjFromMap(ret, TestPo.class);
		Assert.assertEquals("qiunet" , testPo1.getNick());
	}
	@Test
	public void testSubList() {
		List<Integer> list = new ArrayList<>(Arrays.asList(new Integer[]{0,1,2,3,4,5,6,7,8,9}));
		// skip
		List<Integer> subList = CommonUtil.getSubListPage(list, 1 , 4);
		
		Assert.assertTrue(subList.get(0) == 1);
	}
}
