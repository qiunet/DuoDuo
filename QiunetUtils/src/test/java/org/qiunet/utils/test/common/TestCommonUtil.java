package org.qiunet.utils.test.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.common.CommonUtil;
import org.qiunet.utils.test.base.BaseTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author qiunet
 *         Created on 16/11/5 12:24.
 */
public class TestCommonUtil extends BaseTest {

	@Test
	public void testExistInArray(){
		Integer [] arr = {1,2,3,4,5};
		Assertions.assertTrue(CommonUtil.existInList(3, arr));
		Assertions.assertFalse(CommonUtil.existInList(8, arr));
	}

	@Test
	public void testExistInCollection(){
		List<Integer> list = new ArrayList();
		list.add(0);
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);

		Assertions.assertTrue(CommonUtil.existInList(3, list));
		Assertions.assertFalse(CommonUtil.existInList(8, list));
	}

	@Test
	public void testSubList() {
		List<Integer> list = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7,8,9));
		// skip
		List<Integer> subList = CommonUtil.getSubListPage(list, 1 , 4);

		Assertions.assertEquals(subList.size(), 4);
        Assertions.assertEquals(1, (int) subList.get(0));
        Assertions.assertEquals(4, (int) subList.get(subList.size() - 1));
	}
	@Test
	public void testReverse(){
		byte [] arr1 = new byte[]{1,2,3,4};
		byte [] arr2 = new byte[]{1,2,3,4,5};

		CommonUtil.reverse(arr1);
		CommonUtil.reverse(arr2);

		Assertions.assertArrayEquals(new byte[]{4,3,2,1}, arr1);
		Assertions.assertArrayEquals(new byte[]{5, 4,3,2,1}, arr2);
	}
}
