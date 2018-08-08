package org.qiunet.utils.string;

import org.qiunet.utils.base.BaseTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author qiunet
 *         Created on 16/11/6 13:55.
 */
public class TestStringUtil extends BaseTest{
	@Test
	public void testSplit(){
		String str = "1,2,";
		String strs [] = StringUtil.split(str, ",");
		Assert.assertEquals("",strs[2]);

		str = ",1,2";
		strs  = StringUtil.split(str, ",");
		Assert.assertTrue(strs.length == 3);
		Assert.assertEquals("", strs[0]);

		str = "1,2";
		strs  = StringUtil.split(str, ",");
		Assert.assertTrue(strs.length == 2);

		str = ",1,2,";
		strs  = StringUtil.split(str, ",");
		Assert.assertTrue(strs.length == 4);
	}
	@Test
	public void testPowerfullTrim() {
		String str = "s\ts ";
		Assert.assertEquals("s\ts", StringUtil.powerfulTrim(str));

		str = "\ts\ts\t";
		Assert.assertEquals("s\ts", StringUtil.powerfulTrim(str));

		str = "abc";
		Assert.assertEquals(str, StringUtil.powerfulTrim(str));

		str = "ㅤ";// 特殊字符
		Assert.assertEquals("", StringUtil.powerfulTrim(str));

		str = "　尹漂亮　 　";
		Assert.assertEquals("尹漂亮", StringUtil.powerfulTrim(str));

		str = "　　﹏ℳ๓二ৡৢﺴ";
		Assert.assertEquals("二", StringUtil.powerfulTrim(str));

		str = "　　ۣۣ ۣۣ ۣۣ ۣۣ ۣۣ ۣۣ ";
		Assert.assertEquals("", StringUtil.powerfulTrim(str));
	}
	@Test
	public void testIsNum(){
		Assert.assertTrue(StringUtil.isNum("123"));
		Assert.assertTrue(StringUtil.isNum("-123"));
		Assert.assertFalse(StringUtil.isNum("-1ff"));
		Assert.assertFalse(StringUtil.isNum(""));
	}
	@Test
	public void testArrayToString() {
		String [] strings = new String[]{"0", "1", "2", "3", "4", "5"};

		Assert.assertEquals("0,1,2,3,4,5", StringUtil.arraysToString(strings, ","));
		Assert.assertEquals("[0,1,2,3,4,5]", StringUtil.arraysToString(strings,"[", "]", ","));
		Assert.assertEquals("[1,2,3,4]", StringUtil.arraysToString(strings,"[", "]", 1, 4,","));

		Object [] arr = new Object[]{"obj1", "obj2", strings};
		Assert.assertEquals("obj1,obj2,[0, 1, 2, 3, 4, 5]", StringUtil.arraysToString(arr, ","));
	}
}
