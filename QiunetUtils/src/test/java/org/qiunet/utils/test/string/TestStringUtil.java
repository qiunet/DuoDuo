package org.qiunet.utils.test.string;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.test.base.BaseTest;

/**
 * @author qiunet
 *         Created on 16/11/6 13:55.
 */
public class TestStringUtil extends BaseTest{
	@Test
	public void testSplit(){
		String str = "1,2,";
		String[] strs = StringUtil.split(str, ",");
		Assert.assertEquals("",strs[2]);

		str = ",1344,2";
		strs  = StringUtil.split(str, ",");
		Assert.assertEquals(3, strs.length);
		Assert.assertEquals("", strs[0]);

		str = "1,2";
		strs  = StringUtil.split(str, ",");
		Assert.assertEquals(2, strs.length);

		str = ",1,2,";
		strs  = StringUtil.split(str, ",");
		Assert.assertEquals(4, strs.length);
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
	@Test
	public void testMixedStringLength(){
		Assert.assertEquals(StringUtil.getMixedStringLength("a行b不行"), 8);
		Assert.assertEquals(StringUtil.getMixedStringLength("abcdefg"), 7);
		Assert.assertEquals(StringUtil.getMixedStringLength("嘻嘻嘻"), 6);
	}
	@Test
	public void testChineseWord(){
		Assert.assertEquals(true, StringUtil.regexChinese("我们"));
		Assert.assertEquals(false, StringUtil.regexChinese("ab"));
		Assert.assertEquals(false, StringUtil.regexChinese("126"));
		Assert.assertEquals(false, StringUtil.regexChinese("^%^%#$"));
	}

	@Test
	public void testFormat() {
		Assert.assertEquals("我叫Qiunet", StringUtil.format("{0}叫{1}", "我", "Qiunet") );
	}
}
